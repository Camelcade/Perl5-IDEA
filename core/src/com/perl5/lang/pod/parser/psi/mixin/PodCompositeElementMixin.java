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

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiReference;
import com.perl5.lang.pod.parser.psi.PodCompositeElement;
import org.jetbrains.annotations.NotNull;

public class PodCompositeElementMixin extends ASTWrapperPsiElement implements PodCompositeElement {
  public PodCompositeElementMixin(@NotNull ASTNode node) {
    super(node);
  }

  @NotNull
  @Override
  public final PsiReference[] getReferences() {
    return getReferencesWithCache();
  }

  @Override
  public final PsiReference getReference() {
    PsiReference[] references = getReferences();
    return references.length == 0 ? null : references[0];
  }

  @Override
  public ItemPresentation getPresentation() {
    return this;
  }
}
