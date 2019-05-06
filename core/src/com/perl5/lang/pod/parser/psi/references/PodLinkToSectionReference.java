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
import com.perl5.lang.perl.documentation.PerlDocUtil;
import com.perl5.lang.perl.psi.references.PerlCachingReference;
import com.perl5.lang.pod.parser.psi.*;
import com.perl5.lang.pod.parser.psi.mixin.PodFormatterL;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class PodLinkToSectionReference extends PerlCachingReference<PodFormatterL> {
  public PodLinkToSectionReference(PodFormatterL element, TextRange range) {
    super(element, range);
  }


  @Override
  public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
    return super.handleElementRename(PodElementFactory.getHeaderText(myElement.getProject(), newElementName));
  }

  @NotNull
  @Override
  protected ResolveResult[] resolveInner(boolean incompleteCode) {
    PodFormatterL podLink = getElement();
    PodLinkDescriptor descriptor = podLink.getLinkDescriptor();

    if (descriptor != null && !descriptor.isUrl() && descriptor.getSection() != null) {
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

      if (!targetFiles.isEmpty()) {
        List<ResolveResult> results = new ArrayList<>();
        PodDocumentPattern searchPattern = PodDocumentPattern.exactAnythingPattern(descriptor.getSection());
        for (PsiFile file : targetFiles) {
          PodCompositeElement podCompositeElement = PerlDocUtil.searchPodElement(file, searchPattern);
          if (podCompositeElement != null) {
            results.add(new PsiElementResolveResult(podCompositeElement));
          }
        }
        return results.toArray(ResolveResult.EMPTY_ARRAY);
      }
    }

    return ResolveResult.EMPTY_ARRAY;
  }

  /**
   * @return list of all section synonymous to {@link titledSection}
   */
  @NotNull
  public static List<PsiElement> getAllSynonymousSections(@NotNull PodTitledSection titledSection) {
    List<PsiElement> result = new ArrayList<>();
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
