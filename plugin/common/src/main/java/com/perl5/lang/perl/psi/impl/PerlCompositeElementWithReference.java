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

package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import com.intellij.util.ArrayUtil;
import com.perl5.lang.perl.psi.PerlReferenceOwner;
import org.jetbrains.annotations.NotNull;


public abstract class PerlCompositeElementWithReference extends PerlCompositeElementImpl implements PerlReferenceOwner {
  public PerlCompositeElementWithReference(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public final PsiReference @NotNull [] getReferences() {
    return PerlReferenceOwner.getReferencesWithCache(this);
  }

  @Override
  public final PsiReference getReference() {
    return ArrayUtil.getFirstElement(getReferences());
  }

  @Override
  public boolean hasReferences() {
    return true;
  }
}
