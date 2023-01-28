/*
 * Copyright 2015-2023 Alexandr Evstigneev
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
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.pod.parser.psi.PodRecursiveVisitor;
import com.perl5.lang.pod.parser.psi.stubs.PodSectionStub;
import com.perl5.lang.pod.parser.psi.util.PodRenderUtil;
import com.perl5.lang.pod.psi.PsiPodFormatIndex;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PodTitledSectionMixin extends PodStubBasedTitledSection {
  public PodTitledSectionMixin(@NotNull ASTNode node) {
    super(node);
  }

  public PodTitledSectionMixin(@NotNull PodSectionStub stub,
                               @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public @Nullable String getPodLink() {
    return PodRenderUtil.getPodLinkForElement(this);
  }

  @Override
  public @Nullable PsiElement getNameIdentifier() {
    return getTitleElement();
  }

  @Override
  public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
    PsiElement nameIdentifier = getNameIdentifier();
    if (nameIdentifier == null) {
      return this;
    }
    StringBuilder indexes = new StringBuilder();
    nameIdentifier.accept(new PodRecursiveVisitor() {
      @Override
      public void visitPodFormatIndex(@NotNull PsiPodFormatIndex o) {
        indexes.append(o.getText());
      }
    });
    if (indexes.length() > 0) {
      name = name + "\n" + indexes;
    }

    ElementManipulators.getManipulator(nameIdentifier).handleContentChange(nameIdentifier, name);
    return this;
  }

  @Override
  public int getTextOffset() {
    PsiElement nameIdentifier = getNameIdentifier();

    return nameIdentifier == null
           ? super.getTextOffset()
           : nameIdentifier.getTextOffset();
  }

  @Override
  public String getName() {
    PsiElement nameIdentifier = getNameIdentifier();
    return nameIdentifier == null ? null : nameIdentifier.getText();
  }
}
