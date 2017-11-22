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

package com.perl5.lang.pod.parser.psi.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.documentation.PerlDocUtil;
import com.perl5.lang.perl.psi.references.PerlCachingReference;
import com.perl5.lang.pod.parser.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 10.04.2016.
 */
public class PodLinkToSectionReference extends PerlCachingReference<PodFormatterL> {
  public PodLinkToSectionReference(PodFormatterL element, TextRange range) {
    super(element, range);
  }


  @Override
  public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
    return super.handleElementRename(PodElementFactory.getHeaderText(myElement.getProject(), newElementName));
  }

  @NotNull
  @Override
  protected ResolveResult[] resolveInner(boolean incompleteCode) {
    PodFormatterL podLink = getElement();
    PodLinkDescriptor descriptor = podLink.getLinkDescriptor();

    if (descriptor != null && !descriptor.isUrl() && descriptor.getSection() != null) {
      List<PsiFile> targetFiles = new ArrayList<>();

      if (descriptor.getFileId() != null) {
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
        PodDocumentPattern searchPattern = PodDocumentPattern.headingAndItemPattern(descriptor.getSection()).withExactMatch();
        for (PsiFile file : targetFiles) {
          PodCompositeElement podCompositeElement = PerlDocUtil.searchPodElement(file, searchPattern);
          if (podCompositeElement != null) {
            results.add(new PsiElementResolveResult(podCompositeElement));
          }
        }
        return results.toArray(new ResolveResult[results.size()]);
      }
    }

    return ResolveResult.EMPTY_ARRAY;
  }
}
