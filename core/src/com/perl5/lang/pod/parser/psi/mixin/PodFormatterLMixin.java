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

package com.perl5.lang.pod.parser.psi.mixin;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.perl5.lang.pod.parser.psi.PodFormatterL;
import com.perl5.lang.pod.parser.psi.PodLinkDescriptor;
import com.perl5.lang.pod.parser.psi.PodLinkTarget;
import com.perl5.lang.pod.parser.psi.PodRenderingContext;
import com.perl5.lang.pod.parser.psi.references.PodLinkToFileReference;
import com.perl5.lang.pod.parser.psi.references.PodLinkToSectionReference;
import com.perl5.lang.pod.parser.psi.util.PodRenderUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hurricup on 26.03.2016.
 */
public class PodFormatterLMixin extends PodSectionMixin implements PodFormatterL {
  public PodFormatterLMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void renderElementContentAsHTML(StringBuilder builder, PodRenderingContext context) {
    String contentText = PodRenderUtil.renderPsiElementAsText(getContentBlock());
    if (StringUtil.isNotEmpty(contentText)) {
      PodLinkDescriptor descriptor = getLinkDescriptor();

      if (descriptor != null) {
        if (descriptor.getFileId() == null) {
          PsiFile psiFile = getContainingFile();
          if (psiFile instanceof PodLinkTarget) {
            descriptor.setEnforcedFileId(((PodLinkTarget)psiFile).getPodLink());
          }
        }

        builder.append(PodRenderUtil.getHTMLLink(descriptor, !isResolvable()));
      }
      else    // fallback
      {
        builder.append(PodRenderUtil.getHTMLPsiLink(contentText));
      }
    }
  }

  private boolean isResolvable() {
    for (PsiReference reference : getReferences()) {
      if (reference.resolve() == null) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean hasReferences() {
    return true;
  }

  @Override
  public PsiReference[] computeReferences() {
    List<PsiReference> references = new ArrayList<>();
    final PodLinkDescriptor descriptor = getLinkDescriptor();

    if (descriptor != null && !descriptor.isUrl()) {
      PsiElement contentBlock = getContentBlock();
      if (contentBlock != null) {
        int rangeOffset = contentBlock.getStartOffsetInParent();

        // file reference
        TextRange fileRange = descriptor.getFileIdTextRangeInLink();
        if (fileRange != null && !fileRange.isEmpty()) {
          references.add(new PodLinkToFileReference(this, fileRange.shiftRight(rangeOffset)));
        }

        // section reference
        TextRange sectionRange = descriptor.getSectionTextRangeInLink();
        if (sectionRange != null && !sectionRange.isEmpty()) {
          references.add(new PodLinkToSectionReference(this, sectionRange.shiftRight(rangeOffset)));
        }
      }
    }


    references.addAll(Arrays.asList(ReferenceProvidersRegistry.getReferencesFromProviders(this)));

    return references.toArray(PsiReference.EMPTY_ARRAY);
  }

  @Nullable
  @Override
  public PodLinkDescriptor getLinkDescriptor() {
    return CachedValuesManager.getCachedValue(this, () -> {
      PsiElement contentBlock = getContentBlock();
      PodLinkDescriptor result = null;
      if (contentBlock != null) {
        String contentText = contentBlock.getText();
        if (StringUtil.isNotEmpty(contentText)) {
          result = PodLinkDescriptor.getDescriptor(contentText);
        }
      }
      return CachedValueProvider.Result.create(result, PodFormatterLMixin.this);
    });
  }

  @Nullable
  @Override
  public PsiFile getTargetFile() {
    PsiReference[] references = getReferences();

    for (PsiReference reference : references) {
      if (reference instanceof PodLinkToFileReference) {
        return (PsiFile)reference.resolve();
      }
    }

    return getContainingFile();
  }
}
