/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package com.perl5.lang.tt2.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import com.perl5.lang.perl.psi.PerlReferenceOwner;
import com.perl5.lang.tt2.psi.TemplateToolkitCompositeElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class TemplateToolkitCompositeElementImpl extends ASTWrapperPsiElement implements TemplateToolkitCompositeElement {
  public TemplateToolkitCompositeElementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public final PsiReference @NotNull [] getReferences() {
    return PerlReferenceOwner.getReferencesWithCache(this);
  }

  @Override
  public final @Nullable PsiReference getReference() {
    PsiReference[] references = getReferences();
    return references.length == 0 ? null : references[0];
  }
}
