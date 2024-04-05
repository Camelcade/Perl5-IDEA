/*
 * Copyright 2015-2021 Alexandr Evstigneev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.perl5.lang.perl.idea.copyright;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.maddyhome.idea.copyright.CopyrightProfile;
import com.maddyhome.idea.copyright.options.LanguageOptions;
import com.maddyhome.idea.copyright.psi.UpdatePsiFileCopyright;
import com.perl5.lang.perl.parser.PerlElementTypesGenerated;
import com.perl5.lang.pod.PodLanguage;
import com.perl5.lang.pod.idea.completion.PodTitleCompletionProvider;
import com.perl5.lang.pod.parser.psi.PodRecursiveVisitor;
import com.perl5.lang.pod.parser.psi.PodTitledSection;
import com.perl5.lang.pod.psi.PsiCutSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

import static com.perl5.lang.pod.lexer.PodElementTypes.POD_OUTER;
import static com.perl5.lang.pod.parser.PodElementTypesGenerated.CUT_SECTION;

class UpdatePerlPackageFileCopyright extends UpdatePsiFileCopyright {
  private final @NotNull CopyrightProfile myOptions;

  public UpdatePerlPackageFileCopyright(Project project,
                                        Module module,
                                        VirtualFile root,
                                        @NotNull CopyrightProfile options) {
    super(project, module, root, options);
    myOptions = options;
  }

  @Override
  protected void scanFile() {
    TextRange rangeInLineComments = getCopyrightRangeInLineComments();
    Pair<TextRange, TextRange> rangesInPod = getCopyrightRangesInPod();
    LanguageOptions languageOptions = getLanguageOptions();
    if (languageOptions.isBlock()) {
      if (rangesInPod != null) {
        // replace
        addAction(new CommentAction(CommentAction.ACTION_REPLACE, rangesInPod.second.getStartOffset(), rangesInPod.second.getEndOffset()));
      }
      else {
        // remove lines and add block
        if (!rangeInLineComments.isEmpty()) {
          addAction(new CommentAction(
            CommentAction.ACTION_DELETE, rangeInLineComments.getStartOffset(), rangeInLineComments.getEndOffset()));
        }
        Pair<Integer, String> insertionOffsetAndSuffix = getPodInsertionOffsetAndSuffix();
        Integer insertionOffset = insertionOffsetAndSuffix.first;
        String fileText = getFile().getText();
        String titlePrefix = "";
        if (insertionOffset > 1 && fileText.charAt(insertionOffset - 1) != '\n') {
          titlePrefix = "\n\n";
        }
        else if (insertionOffset > 2 && fileText.charAt(insertionOffset - 2) != '\n') {
          titlePrefix = "\n";
        }

        addAction(new CommentAction(insertionOffset,
                                    titlePrefix + "=head1 " + PodTitleCompletionProvider.COPYRIGHT_AND_LICENSE + "\n\n",
                                    insertionOffsetAndSuffix.second));
      }
    }
    else {
      if (!rangeInLineComments.isEmpty()) {
        // replace
        addAction(
          new CommentAction(CommentAction.ACTION_REPLACE, rangeInLineComments.getStartOffset(), rangeInLineComments.getEndOffset()));
      }
      else {
        // remove block and add lines
        if (rangesInPod != null) {
          addAction(new CommentAction(CommentAction.ACTION_DELETE, rangesInPod.first.getStartOffset(), rangesInPod.first.getEndOffset()));
        }
        addAction(new CommentAction(0, "", ""));
      }
    }
  }

  @Override
  protected String getCommentText(String prefix, String suffix, String oldComment) {
    LanguageOptions languageOptions = getLanguageOptions();
    String defaultText = super.getCommentText("", "", oldComment);
    if (languageOptions.isBlock()) {
      return prefix + StringUtil.trimStart(defaultText, "# \n") + suffix;
    }
    return languageOptions.addBlankAfter ? defaultText + "\n\n" : defaultText + "\n";
  }

  /**
   * @return a text range of line comments in the beginning of the file, starting with comment matching user specified pattern and ending
   * with last trailing space. Or empty range if could not find anything.
   */
  private @NotNull TextRange getCopyrightRangeInLineComments() {
    PsiFile psiFile = getFile();
    PsiElement run = psiFile.getFirstChild();
    if (run == null) {
      return TextRange.EMPTY_RANGE;
    }

    Pattern pattern = getSearchPattern();
    if (pattern == null) {
      return TextRange.EMPTY_RANGE;
    }

    int startOffset = -1;
    int endOffset = -1;
    while (run != null) {
      IElementType elementType = PsiUtilCore.getElementType(run);
      if (elementType == PerlElementTypesGenerated.COMMENT_LINE) {
        if (pattern.matcher(run.getText()).find()) {
          startOffset = run.getTextRange().getStartOffset();
        }
      }
      else if (elementType != TokenType.WHITE_SPACE) {
        break;
      }
      if (startOffset > -1) {
        endOffset = run.getTextRange().getEndOffset();
      }
      run = run.getNextSibling();
    }
    return endOffset > -1 ? TextRange.create(startOffset, endOffset) : TextRange.EMPTY_RANGE;
  }

  /**
   * @return pattern to search existing copyright in line comments or pod section titles
   */
  private @Nullable Pattern getSearchPattern() {
    String keyword = myOptions.getKeyword();
    return StringUtil.isEmpty(keyword) ? null : Pattern.compile(keyword, Pattern.CASE_INSENSITIVE);
  }

  /**
   * @return text ranges to remove block and text range to replace block
   */
  private @Nullable Pair<TextRange, TextRange> getCopyrightRangesInPod() {
    Pattern pattern = getSearchPattern();
    if (pattern == null) {
      return null;
    }

    PsiFile podPsiFile = getPodFile();
    if (podPsiFile == null) {
      return null;
    }

    Ref<PodTitledSection> copyrightSectionRef = Ref.create();

    podPsiFile.accept(new PodRecursiveVisitor() {
      @Override
      public void visitElement(@NotNull PsiElement element) {
        if (copyrightSectionRef.isNull()) {
          super.visitElement(element);
        }
      }

      @Override
      public void visitTargetableSection(PodTitledSection o) {
        String titleText = o.getTitleText();
        if (StringUtil.isNotEmpty(titleText) && pattern.matcher(titleText).find()) {
          copyrightSectionRef.set(o);
          return;
        }
        super.visitTargetableSection(o);
      }
    });

    PodTitledSection copyrightSection = copyrightSectionRef.get();

    if (copyrightSection == null) {
      return null;
    }

    int[] offsetAdjustments = new int[]{-1, -1};
    copyrightSection.accept(new PodRecursiveVisitor() {
      @Override
      public void visitElement(@NotNull PsiElement element) {
        if (offsetAdjustments[1] > -1) {
          return;
        }
        IElementType elementType = PsiUtilCore.getElementType(element);
        if (elementType == CUT_SECTION) {
          offsetAdjustments[1] = element.getTextOffset() - 1;
          if (PsiUtilCore.getElementType(PsiTreeUtil.nextLeaf(element)) == POD_OUTER) {
            offsetAdjustments[0] = element.getTextRange().getEndOffset() + 1;
          }
          else {
            offsetAdjustments[0] = offsetAdjustments[1];
          }
          return;
        }
        super.visitElement(element);
      }
    });

    PsiElement sectionNextSibling = copyrightSection.getNextSibling();
    while (sectionNextSibling instanceof PsiWhiteSpace) {
      sectionNextSibling = sectionNextSibling.getNextSibling();
    }

    TextRange copyrightSectionTextRange = copyrightSection.getTextRange();
    TextRange copyrightSectionRange =
      sectionNextSibling == null ? copyrightSectionTextRange :
      TextRange.create(copyrightSectionTextRange.getStartOffset(), sectionNextSibling.getTextRange().getStartOffset());

    PsiElement copyrightBlock = copyrightSection.getContentBlock();
    TextRange copyrightBlockRange = copyrightBlock == null ? TextRange.EMPTY_RANGE : copyrightBlock.getTextRange();

    if (offsetAdjustments[0] < 0) {
      return Pair.create(copyrightSectionRange, copyrightBlockRange);
    }

    return Pair.create(
      TextRange.create(copyrightSectionRange.getStartOffset(), offsetAdjustments[0]),
      TextRange.create(copyrightBlockRange.getStartOffset(), offsetAdjustments[1])
    );
  }

  private @Nullable PsiFile getPodFile() {
    return getFile().getViewProvider().getPsi(PodLanguage.INSTANCE);
  }

  /**
   * @return proper offset and optional suffix for the pod section to insert
   */
  private @NotNull Pair<Integer, String> getPodInsertionOffsetAndSuffix() {
    PsiFile podFile = getPodFile();
    if (podFile == null) {
      return getDefaultPodOffsetAndSuffix();
    }
    PsiElement lastChild = podFile.getLastChild();
    if (lastChild == podFile.getFirstChild()) {
      return getDefaultPodOffsetAndSuffix();
    }

    while (PsiUtilCore.getElementType(lastChild) == POD_OUTER) {
      lastChild = lastChild.getPrevSibling();
    }
    if (lastChild == null) {
      return getDefaultPodOffsetAndSuffix();
    }

    PsiElement deepestLast = PsiTreeUtil.getDeepestLast(lastChild);
    while (PsiUtilCore.getElementType(deepestLast) == TokenType.WHITE_SPACE) {
      deepestLast = deepestLast.getPrevSibling();
    }

    PsiCutSection lastCutSection = PsiTreeUtil.getParentOfType(deepestLast, PsiCutSection.class, false);

    if (lastCutSection != null) {
      return Pair.create(lastCutSection.getTextRange().getStartOffset() - 1, "");
    }
    return Pair.create(lastChild.getTextRange().getEndOffset(), "");
  }

  private @NotNull Pair<Integer, String> getDefaultPodOffsetAndSuffix() {
    return Pair.create(0, getLanguageOptions().addBlankAfter ? "\n=cut\n\n" : "\n=cut\n");
  }
}
