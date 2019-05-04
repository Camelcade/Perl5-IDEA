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
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.pod.parser.psi.*;
import com.perl5.lang.pod.parser.psi.stubs.PodSectionStub;
import com.perl5.lang.pod.parser.psi.util.PodRenderUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PodFormatterX extends PodStubBasedTitledSection implements PodFormatter {
  public PodFormatterX(@NotNull PodSectionStub stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  public PodFormatterX(@NotNull ASTNode node) {
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
  public PsiElement getTitleElement() {
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
    return getTitleElement();
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

  /**
   * @return a real target of this index. Outer item, heading or file.
   */
  @Nullable
  public PsiElement getIndexTarget() {
    PodSectionStub stub = getStub();
    if (stub != null) {
      StubElement parentStub = stub.getParentStub();
      return parentStub == null ? null : parentStub.getPsi();
    }
    PsiElement parent = getParent();
    if (parent instanceof PodSectionTitle || parent instanceof PodSectionContent) {
      return parent.getParent();
    }
    return parent;
  }

  /**
   * @return true iff this index makes sense.
   * @implNote for some reason perldoc may have duplicate indexes. E.g. <code>=item TEXT X<TEXT></code>. In such case there is no
   * sense to use index
   */
  public boolean isMeaningful() {
    PsiElement indexTarget = getIndexTarget();
    if (indexTarget == null) {
      Logger.getInstance(PodFormatterX.class).warn("Can't find a target element for " + getText());
    }
    if (indexTarget instanceof PodTitledSection) {
      return !StringUtil.equals(((PodTitledSection)indexTarget).getTitleText(), getTitleText());
    }
    return true;
  }
}
