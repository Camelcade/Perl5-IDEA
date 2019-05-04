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

package com.perl5.lang.pod.elementTypes;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.StubElement;
import com.perl5.lang.pod.parser.psi.mixin.PodSectionItem;
import com.perl5.lang.pod.parser.psi.stubs.PodSectionStub;
import com.perl5.lang.pod.psi.impl.PsiItemSectionImpl;
import org.jetbrains.annotations.NotNull;

public class PodSectionItemElementType extends PodStubBasedTitledSectionElementType<PodSectionItem> {
  public PodSectionItemElementType(@NotNull String debugName) {
    super(debugName);
  }

  @Override
  public PodSectionItem createPsi(@NotNull PodSectionStub stub) {
    return new PsiItemSectionImpl(stub, this);
  }

  @NotNull
  @Override
  public PsiElement getPsiElement(@NotNull ASTNode node) {
    return new PsiItemSectionImpl(node);
  }

  @NotNull
  @Override
  public PodSectionStub createStub(@NotNull PodSectionItem psi, StubElement parentStub) {
    char prefix = psi.isTargetable() ? '+' : '-';
    return new PodSectionStub(parentStub, this, "" + prefix + psi.getPresentableText());
  }

  @Override
  protected boolean shouldCreateStub(@NotNull PodSectionItem item) {
    return item.isIndexed() && StringUtil.isNotEmpty(item.getPresentableText()) ||
           item.isTargetable() && super.shouldCreateStub(item);
  }
}
