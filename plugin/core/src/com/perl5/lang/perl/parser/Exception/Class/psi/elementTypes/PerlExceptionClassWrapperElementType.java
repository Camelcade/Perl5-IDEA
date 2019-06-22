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

package com.perl5.lang.perl.parser.Exception.Class.psi.elementTypes;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.perl5.lang.perl.parser.Exception.Class.psi.impl.PerlExceptionClassWrapper;
import com.perl5.lang.perl.parser.Exception.Class.psi.stubs.PerlExceptionClassWrapperStub;
import com.perl5.lang.perl.psi.stubs.PerlPolyNamedElementType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PerlExceptionClassWrapperElementType
  extends PerlPolyNamedElementType<PerlExceptionClassWrapperStub, PerlExceptionClassWrapper> {
  public static final IStubElementType EXCEPTION_CLASS_WRAPPER = new PerlExceptionClassWrapperElementType("Class::Exception wrapper");

  private PerlExceptionClassWrapperElementType(@NotNull String debugName) {
    super(debugName);
  }

  @Override
  public PerlExceptionClassWrapper createPsi(@NotNull PerlExceptionClassWrapperStub stub) {
    return new PerlExceptionClassWrapper(stub, this);
  }

  @NotNull
  @Override
  protected PerlExceptionClassWrapperStub createStub(@NotNull PerlExceptionClassWrapper wrapper,
                                                     StubElement parentStub,
                                                     @NotNull List<StubElement> lightElementsStubs) {
    return new PerlExceptionClassWrapperStub(parentStub, this, lightElementsStubs);
  }

  @NotNull
  @Override
  protected PerlExceptionClassWrapperStub deserialize(@NotNull StubInputStream dataStream,
                                                      StubElement parentStub,
                                                      @NotNull List<StubElement> lightElementsStubs) {
    return new PerlExceptionClassWrapperStub(parentStub, this, lightElementsStubs);
  }

  @NotNull
  @Override
  public PsiElement getPsiElement(@NotNull ASTNode node) {
    return new PerlExceptionClassWrapper(node);
  }
}
