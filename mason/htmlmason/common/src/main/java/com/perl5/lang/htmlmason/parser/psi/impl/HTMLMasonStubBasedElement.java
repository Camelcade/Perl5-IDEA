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

package com.perl5.lang.htmlmason.parser.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonCompositeElement;
import com.perl5.lang.perl.psi.PerlStubBasedPsiElementBase;
import org.jetbrains.annotations.NotNull;


public abstract class HTMLMasonStubBasedElement<T extends StubElement<?>> extends PerlStubBasedPsiElementBase<T>
  implements HTMLMasonCompositeElement {
  public HTMLMasonStubBasedElement(@NotNull T stub, @NotNull IElementType nodeType) {
    super(stub, nodeType);
  }

  public HTMLMasonStubBasedElement(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public boolean processDeclarations(@NotNull PsiScopeProcessor processor,
                                     @NotNull ResolveState state,
                                     PsiElement lastParent,
                                     @NotNull PsiElement place) {
    return lastParent == null || processDeclarationsForReal(processor, state, lastParent, place);
  }

  @Override
  public boolean processDeclarationsForReal(@NotNull PsiScopeProcessor processor,
                                            @NotNull ResolveState state,
                                            PsiElement lastParent,
                                            @NotNull PsiElement place) {
    return super.processDeclarations(processor, state, lastParent, place);
  }
}
