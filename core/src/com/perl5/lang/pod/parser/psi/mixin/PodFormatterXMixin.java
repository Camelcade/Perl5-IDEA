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

package com.perl5.lang.pod.parser.psi.mixin;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.pod.parser.psi.PodFormatterX;
import com.perl5.lang.pod.parser.psi.PodRenderingContext;
import com.perl5.lang.pod.parser.psi.stubs.PodSectionStub;
import com.perl5.lang.pod.parser.psi.util.PodRenderUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 26.03.2016.
 */
public class PodFormatterXMixin extends PodStubBasedSectionMixin implements PodFormatterX {
  public PodFormatterXMixin(@NotNull PodSectionStub stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  public PodFormatterXMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void renderElementAsHTML(StringBuilder builder, PodRenderingContext context) {

  }

  @Override
  public void renderElementAsText(StringBuilder builder, PodRenderingContext context) {

  }

  @Nullable
  @Override
  public PsiElement getTitleBlock() {
    return getContentBlock();
  }

  @Nullable
  @Override
  public String getPodLink() {
    return PodRenderUtil.getPodLinkForElement(this);
  }


  @Override
  public String getName() {
    PsiElement nameIdentifier = getNameIdentifier();
    if (nameIdentifier != null) {
      return nameIdentifier.getText();
    }
    return super.getName();
  }

  @Nullable
  @Override
  public PsiElement getNameIdentifier() {
    return getTitleBlock();
  }

  @Override
  public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
    return this;
  }

  @Override
  public int getTextOffset() {
    PsiElement nameIdentifier = getNameIdentifier();

    return nameIdentifier == null
           ? super.getTextOffset()
           : nameIdentifier.getTextOffset();
  }
}
