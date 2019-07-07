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

package com.perl5.lang.mojolicious.elementTypes;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.perl5.lang.mojolicious.psi.impl.MojoHelperWrapper;
import com.perl5.lang.mojolicious.psi.stubs.MojoHelperWrapperStub;
import com.perl5.lang.perl.psi.stubs.PerlPolyNamedElementType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class MojoHelperWrapperElementType extends PerlPolyNamedElementType<MojoHelperWrapperStub, MojoHelperWrapper> {
  public MojoHelperWrapperElementType(@NotNull String debugName) {
    super(debugName);
  }

  @Override
  public MojoHelperWrapper createPsi(@NotNull MojoHelperWrapperStub stub) {
    return new MojoHelperWrapper(stub, this);
  }

  @NotNull
  @Override
  protected MojoHelperWrapperStub createStub(@NotNull MojoHelperWrapper wrapper,
                                             StubElement parentStub,
                                             @NotNull List<StubElement> lightElementsStubs) {
    return new MojoHelperWrapperStub(parentStub, this, lightElementsStubs);
  }

  @NotNull
  @Override
  protected MojoHelperWrapperStub deserialize(@NotNull StubInputStream dataStream,
                                              StubElement parentStub,
                                              @NotNull List<StubElement> lightElementsStubs) throws IOException {
    return new MojoHelperWrapperStub(parentStub, this, lightElementsStubs);
  }

  @NotNull
  @Override
  public PsiElement getPsiElement(@NotNull ASTNode node) {
    return new MojoHelperWrapper(node);
  }
}
