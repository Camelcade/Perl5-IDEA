/*
 * Copyright 2015-2017 Alexandr Evstigneev
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
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.completion.util.PerlPackageCompletionUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.pod.filetypes.PodFileType;
import com.perl5.lang.pod.lexer.PodElementTypes;
import com.perl5.lang.pod.parser.psi.PodRecursiveVisitor;
import com.perl5.lang.pod.parser.psi.PodSectionItem;
import com.perl5.lang.pod.parser.psi.PodTitledSection;
import com.perl5.lang.pod.parser.psi.references.PodLinkToFileReference;
import com.perl5.lang.pod.parser.psi.references.PodLinkToSectionReference;
import com.perl5.lang.pod.parser.psi.util.PodFileUtil;
import com.perl5.lang.pod.psi.PsiPodFormatLink;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Created by hurricup on 16.04.2016.
 */
public class PodLinkCompletionProvider extends CompletionProvider<CompletionParameters> implements PodElementTypes {
  @Override
  protected void addCompletions(@NotNull CompletionParameters parameters,
                                @NotNull ProcessingContext context,
                                @NotNull CompletionResultSet result) {
    PsiElement element = parameters.getOriginalPosition();
    if (element == null) {
      return;
    }

    PsiPodFormatLink psiPodFormatLink = PsiTreeUtil.getParentOfType(element, PsiPodFormatLink.class);
    if (psiPodFormatLink == null) {
      return;
    }

    TextRange elementRange = element.getTextRange().shiftRight(-psiPodFormatLink.getTextOffset());
    PsiReference[] references = psiPodFormatLink.getReferences();
    for (PsiReference reference : references) {
      TextRange referenceRange = reference.getRangeInElement();
      if (referenceRange.contains(elementRange)) {
        if (reference instanceof PodLinkToFileReference) {
          addFilesCompletions(psiPodFormatLink, result);
          return;
        }
        else if (reference instanceof PodLinkToSectionReference) {
          addSectionsCompletions(result, psiPodFormatLink.getTargetFile());
          return;
        }
      }
    }

    // checking for an empty section
    if (atSectionPosition(element)) {
      addSectionsCompletions(result, psiPodFormatLink.getTargetFile());
    }
  }

  protected static void addFilesCompletions(@NotNull PsiPodFormatLink link, @NotNull final CompletionResultSet result) {
    final Project project = link.getProject();
    final Set<String> foundPods = new THashSet<>();

    PerlPackageUtil.processIncFilesForPsiElement(link, (file, classRoot) -> {
      String className = PodFileUtil.getPackageNameFromVirtualFile(file, classRoot);
      if (StringUtil.isNotEmpty(className)) {
        boolean isBuiltIn = false;
        if (StringUtil.startsWith(className, "pods::")) {
          isBuiltIn = true;
          className = className.substring(6);
        }
        if (!foundPods.contains(className)) {
          result.addElement(LookupElementBuilder.create(className).withIcon(PerlIcons.POD_FILE).withBoldness(isBuiltIn));
          foundPods.add(className);
        }
      }
      return true;
    }, PodFileType.INSTANCE);

    PerlPackageUtil.processPackageFilesForPsiElement(link, s -> {
      if (StringUtil.isNotEmpty(s)) {
        if (!foundPods.contains(s)) {
          result.addElement(PerlPackageCompletionUtil.getPackageLookupElement(null, s, null));
          foundPods.add(s);
        }
      }
      return true;
    });
  }

  private static boolean atSectionPosition(PsiElement element) {
    if (element == null) {
      return false;
    }

    IElementType elementType = element.getNode().getElementType();

    PsiElement prevElement = element.getPrevSibling();

    if (elementType == POD_ANGLE_RIGHT && prevElement != null) {
      prevElement = prevElement.getLastChild();
    }

    IElementType prevElementType = prevElement == null ? null : prevElement.getNode().getElementType();

    if (elementType == POD_ANGLE_RIGHT && prevElementType == POD_DIV) {
      return true;
    }

    PsiElement prevPrevElement = prevElement == null ? null : prevElement.getPrevSibling();
    IElementType prevPrevElementType = prevPrevElement == null ? null : prevPrevElement.getNode().getElementType();

    return elementType == POD_QUOTE_DOUBLE && prevElementType == POD_QUOTE_DOUBLE && prevPrevElementType == POD_DIV;
  }

  protected static void addSectionsCompletions(@NotNull final CompletionResultSet result, PsiFile targetFile) {
    if (targetFile != null) {
      targetFile.accept(new PodRecursiveVisitor() {
        @Override
        public void visitTargetableSection(PodTitledSection o) {
          String title = o.getTitleText();
          if (StringUtil.isNotEmpty(title)) {
            if (!(o instanceof PodSectionItem)) {
              result.addElement(LookupElementBuilder.create(title).withIcon(PerlIcons.POD_FILE));
            }
          }
          super.visitTargetableSection(o);
        }
      });
    }
  }
}
