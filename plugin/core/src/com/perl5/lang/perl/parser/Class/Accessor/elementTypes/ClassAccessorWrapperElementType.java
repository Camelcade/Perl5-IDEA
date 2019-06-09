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

package com.perl5.lang.perl.parser.Class.Accessor.elementTypes;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.perl5.lang.perl.parser.Class.Accessor.psi.impl.PerlClassAccessorWrapper;
import com.perl5.lang.perl.parser.Class.Accessor.psi.stubs.PerlClassAccessorWrapperStub;
import com.perl5.lang.perl.psi.stubs.PerlPolyNamedElementType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class ClassAccessorWrapperElementType extends PerlPolyNamedElementType<PerlClassAccessorWrapperStub, PerlClassAccessorWrapper> {
  public ClassAccessorWrapperElementType(@NotNull String debugName) {
    super(debugName);
  }

  @Override
  public PerlClassAccessorWrapper createPsi(@NotNull PerlClassAccessorWrapperStub stub) {
    return new PerlClassAccessorWrapper(stub, this);
  }

  @NotNull
  @Override
  public PsiElement getPsiElement(@NotNull ASTNode node) {
    return new PerlClassAccessorWrapper(node);
  }

  @NotNull
  @Override
  protected PerlClassAccessorWrapperStub createStub(@NotNull PerlClassAccessorWrapper psi,
                                                    StubElement parentStub,
                                                    @NotNull List<StubElement> lightStubs) {
    return new PerlClassAccessorWrapperStub(parentStub, this, lightStubs, psi.isFollowBestPractice());
  }

  @Override
  protected void serializeStub(@NotNull PerlClassAccessorWrapperStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeBoolean(stub.isFollowBestPractice());
  }

  @NotNull
  @Override
  protected PerlClassAccessorWrapperStub deserialize(@NotNull StubInputStream dataStream,
                                                     StubElement parentStub,
                                                     @NotNull List<StubElement> childStubs) throws IOException {
    return new PerlClassAccessorWrapperStub(parentStub, this, childStubs, dataStream.readBoolean());
  }
}
