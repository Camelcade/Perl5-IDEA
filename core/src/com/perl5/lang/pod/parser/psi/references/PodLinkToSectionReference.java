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

package com.perl5.lang.pod.parser.psi.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.psi.references.PerlCachingReference;
import com.perl5.lang.pod.idea.completion.PodLinkCompletionProvider;
import com.perl5.lang.pod.parser.psi.PodElementFactory;
import com.perl5.lang.pod.parser.psi.PodLinkDescriptor;
import com.perl5.lang.pod.parser.psi.PodStubsAwareRecursiveVisitor;
import com.perl5.lang.pod.parser.psi.PodTitledSection;
import com.perl5.lang.pod.parser.psi.mixin.PodFormatterL;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class PodLinkToSectionReference extends PerlCachingReference<PodFormatterL> {
  public PodLinkToSectionReference(PodFormatterL element, TextRange range) {
    super(element, range);
  }


  @Override
  public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
    return super.handleElementRename(
      PodLinkCompletionProvider.escapeTitle(PodElementFactory.getHeaderText(myElement.getProject(), newElementName)));
  }

  @NotNull
  @Override
  protected ResolveResult[] resolveInner(boolean incompleteCode) {
    PodFormatterL podLink = getElement();
    PodLinkDescriptor descriptor = podLink.getLinkDescriptor();

    String sectionTitle = descriptor == null ? null : descriptor.getSection();
    if (sectionTitle == null || descriptor.isUrl()) {
      return ResolveResult.EMPTY_ARRAY;
    }
    List<PsiFile> targetFiles = new ArrayList<>();

    if (descriptor.getName() != null && !descriptor.isSameFile()) {
      for (PsiReference reference : podLink.getReferences()) {
        if (reference instanceof PodLinkToFileReference) {
          for (ResolveResult resolveResult : ((PodLinkToFileReference)reference).multiResolve(false)) {
            targetFiles.add((PsiFile)resolveResult.getElement());
          }
        }
      }
    }
    else {
      targetFiles.add(podLink.getContainingFile());
    }

    if (targetFiles.isEmpty()) {
      return ResolveResult.EMPTY_ARRAY;
    }

    List<PsiElement> results = new ArrayList<>();
    for (PsiFile file : targetFiles) {
      file.accept(new PodStubsAwareRecursiveVisitor() {
        @Override
        public void visitTargetableSection(PodTitledSection o) {
          if (StringUtil.equals(sectionTitle, o.getTitleText())) {
            results.add(o);
          }
          super.visitTargetableSection(o);
        }
      });
      if (!results.isEmpty()) {
        break;
      }
    }
    return PsiElementResolveResult.createResults(results);
  }

  @Nullable
  @Override
  public PsiElement resolve() {
    ResolveResult[] results = multiResolve(false);
    return results.length > 0 ? results[0].getElement() : null;
  }

  /**
   * @return list of all section synonymous to {@link titledSection}
   */
  @NotNull
  public static List<PodTitledSection> getAllSynonymousSections(@NotNull PodTitledSection titledSection) {
    List<PodTitledSection> result = new ArrayList<>();
    String titleText = titledSection.getTitleText();
    titledSection.getContainingFile().accept(new PodStubsAwareRecursiveVisitor() {
      @Override
      public void visitTargetableSection(PodTitledSection o) {
        if (StringUtil.equals(titleText, o.getTitleText())) {
          result.add(o);
        }
        super.visitTargetableSection(o);
      }
    });
    return result;
  }
}
