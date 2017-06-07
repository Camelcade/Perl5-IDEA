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

package com.perl5.lang.perl.psi.impl;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.idea.stubs.PerlPolyNamedElementStub;
import com.perl5.lang.perl.psi.PerlPolyNamedElement;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class PerlPolyNamedElementImpl extends StubBasedPsiElementBase<PerlPolyNamedElementStub> implements PerlPolyNamedElement {
  public PerlPolyNamedElementImpl(@NotNull PerlPolyNamedElementStub stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  public PerlPolyNamedElementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public PerlPolyNamedElementImpl(PerlPolyNamedElementStub stub, IElementType nodeType, ASTNode node) {
    super(stub, nodeType, node);
  }

  @NotNull
  @Override
  public Map<String, PsiElement> collectNameIdentifiersMap() {
    return null;
  }
}
