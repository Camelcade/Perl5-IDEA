/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package com.perl5.lang.pod.idea.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.usageView.UsageViewUtil;
import com.intellij.util.ProcessingContext;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.completion.util.PerlPackageCompletionUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.pod.filetypes.PodFileType;
import com.perl5.lang.pod.lexer.PodElementTypes;
import com.perl5.lang.pod.parser.psi.*;
import com.perl5.lang.pod.parser.psi.util.PodFileUtil;
import com.perl5.lang.pod.psi.PsiPodFormatIndex;
import org.apache.commons.lang.math.NumberUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PodLinkCompletionProvider extends CompletionProvider<CompletionParameters> implements PodElementTypes {
  private static final Logger LOG = Logger.getInstance(PodLinkCompletionProvider.class);
  private static final List<String> TO_ESCAPE = Collections.unmodifiableList(Arrays.asList("<", ">", "/", "|"));
  private static final List<String> ESCAPE_TO = Collections.unmodifiableList(Arrays.asList("E<lt>", "E<gt>", "E<sol>", "E<verbar>"));

  @Override
  protected void addCompletions(@NotNull CompletionParameters parameters,
                                @NotNull ProcessingContext context,
                                @NotNull CompletionResultSet result) {
    PsiElement element = parameters.getPosition();
    PsiElement linkPart = element.getParent();
    PodFormatterL linkElement = PsiTreeUtil.getParentOfType(element, PodFormatterL.class);
    if (linkElement == null) {
      return;
    }
    IElementType parentType = PsiUtilCore.getElementType(linkPart);

    if (parentType == LINK_TEXT && element.getPrevSibling() == null) {
      addFilesCompletions(linkElement, result);
    }
    if (parentType == LINK_NAME) {
      addFilesCompletions(linkElement, result);
    }
    if (parentType == LINK_SECTION) {
      addSectionsCompletions(result, linkElement.getTargetFile());
    }
  }

  protected static void addFilesCompletions(@NotNull PodFormatterL link, @NotNull final CompletionResultSet result) {
    final Set<String> foundPods = new HashSet<>();
    PerlPackageUtil.processIncFilesForPsiElement(link, (file, classRoot) -> {
      String className = PodFileUtil.getPackageNameFromVirtualFile(file, classRoot);
      if (StringUtil.isNotEmpty(className)) {
        boolean isBuiltIn = false;
        if (StringUtil.startsWith(className, "pod::")) {
          isBuiltIn = true;
          className = className.substring(5);
        }
        if (!foundPods.contains(className)) {
          result.addElement(LookupElementBuilder.create(file, className).withIcon(PerlIcons.POD_FILE).withBoldness(isBuiltIn));
          foundPods.add(className);
        }
      }
      return true;
    }, PodFileType.INSTANCE);

    PerlPackageUtil.processPackageFilesForPsiElement(link, (packageName, file) -> {
      if (StringUtil.isNotEmpty(packageName)) {
        if (!foundPods.contains(packageName)) {
          result.addElement(PerlPackageCompletionUtil.getPackageLookupElement(file, packageName, null));
          foundPods.add(packageName);
        }
      }
      return true;
    });
  }

  /**
   * @return section title with escaped {@code < > | /} chars
   */
  @NotNull
  public static String escapeTitle(@NotNull String title) {
    return StringUtil.replace(title, TO_ESCAPE, ESCAPE_TO);
  }

  protected static void addSectionsCompletions(@NotNull final CompletionResultSet result, PsiFile targetFile) {
    if (targetFile != null) {
      targetFile.accept(new PodRecursiveVisitor() {
        @Override
        public void visitTargetableSection(PodTitledSection o) {
          String title = cleanItemText(o.getTitleText());
          if (title != null) {
            result.addElement(LookupElementBuilder.create(o, escapeTitle(title))
                                .withLookupString(title)
                                .withPresentableText(title)
                                .withIcon(PerlIcons.POD_FILE)
                                .withTypeText(UsageViewUtil.getType(o)));
          }
          super.visitTargetableSection(o);
        }

        @Contract("null->null")
        @Nullable
        private String cleanItemText(@Nullable String itemText) {
          if (itemText == null) {
            return null;
          }
          String trimmed = StringUtil.trimLeading(itemText.trim(), '*');
          if (NumberUtils.isNumber(trimmed)) {
            return null;
          }
          return StringUtil.nullize(StringUtil.shortenTextWithEllipsis(trimmed, 140, 0));
        }

        @Override
        public void visitPodFormatIndex(@NotNull PsiPodFormatIndex o) {
          if (!o.isMeaningful()) {
            return;
          }
          String lookupText = o.getTitleText();
          if (StringUtil.isEmpty(lookupText)) {
            return;
          }
          PsiElement indexTarget = o.getIndexTarget();
          String presentableText;
          if (indexTarget instanceof PodFile) {
            presentableText = cleanItemText(((PodFile)indexTarget).getPodLinkText());
          }
          else if (indexTarget instanceof PodTitledSection) {
            presentableText = cleanItemText(((PodTitledSection)indexTarget).getTitleText());
          }
          else if (indexTarget instanceof PodRenderableElement) {
            presentableText = cleanItemText(((PodRenderableElement)indexTarget).getAsText());
          }
          else {
            LOG.warn("Unhandled index target: " + indexTarget);
            return;
          }
          String tailText;
          if (presentableText == null) {
            presentableText = lookupText;
            tailText = null;
          }
          else {
            tailText = "(" + lookupText + ")";
          }
          result.addElement(
            LookupElementBuilder.create(o, escapeTitle(lookupText))
              .withLookupString(lookupText)
              .withPresentableText(presentableText)
              .withTailText(tailText)
              .withTypeText(UsageViewUtil.getType(o))
              .withIcon(PerlIcons.POD_FILE)
          );
        }
      });
    }
  }
}
