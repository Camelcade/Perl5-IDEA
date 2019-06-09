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

package com.perl5.lang.perl.parser.constant.psi.elementTypes;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.perl.parser.constant.psi.impl.PerlConstantsWrapper;
import com.perl5.lang.perl.psi.PerlPolyNamedElement;
import com.perl5.lang.perl.psi.stubs.PerlPolyNamedElementStub;
import com.perl5.lang.perl.psi.stubs.PerlPolyNamedElementType;
import org.jetbrains.annotations.NotNull;

public class PerlConstantsWrapperElementType extends PerlPolyNamedElementType<PerlPolyNamedElementStub, PerlPolyNamedElement> {
  public static final IStubElementType CONSTANT_WRAPPER = new PerlConstantsWrapperElementType("CONSTANT_WRAPPER");

  private PerlConstantsWrapperElementType(@NotNull String debugName) {
    super(debugName);
  }

  @Override
  public PerlPolyNamedElement createPsi(@NotNull PerlPolyNamedElementStub stub) {
    return new PerlConstantsWrapper(stub, this);
  }

  @NotNull
  @Override
  public PsiElement getPsiElement(@NotNull ASTNode node) {
    return new PerlConstantsWrapper(node);
  }
}
