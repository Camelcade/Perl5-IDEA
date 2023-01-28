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
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.pod.parser.psi.PodCompositeElement;
import com.perl5.lang.pod.parser.psi.PodTitledSection;
import com.perl5.lang.pod.parser.psi.stubs.PodSectionStub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public abstract class PodStubBasedTitledSection extends PodStubBasedSection implements StubBasedPsiElement<PodSectionStub>,
                                                                                       PodTitledSection {
  public PodStubBasedTitledSection(@NotNull PodSectionStub stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  public PodStubBasedTitledSection(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public @Nullable String getTitleText() {
    PodSectionStub stub = getGreenStub();
    if (stub != null) {
      return stub.getContent();
    }
    return PodTitledSection.super.getTitleText();
  }

  @Override
  public int getListLevel() {
    PsiElement parent = getParent();
    return parent instanceof PodCompositeElement ? ((PodCompositeElement)parent).getListLevel() : 0;
  }

  @Override
  public ItemPresentation getPresentation() {
    return this;
  }

  @Override
  public @Nullable String getPresentableText() {
    return getTitleText();
  }

  @Override
  public @Nullable Icon getIcon(boolean unused) {
    return getContainingFile().getIcon(0);
  }

  @Override
  public @Nullable String getPodLinkText() {
    return getTitleText();
  }
}
