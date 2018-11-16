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

package com.perl5.lang.perl.parser.moose.psi.elementTypes;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.perl5.lang.perl.parser.moose.psi.impl.PerlMooseAttributeWrapper;
import com.perl5.lang.perl.parser.moose.stubs.PerlMooseAttributeWrapperStub;
import com.perl5.lang.perl.psi.stubs.PerlPolyNamedElementType;
import com.perl5.lang.perl.psi.stubs.PerlStubSerializationUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class PerlMooseAttributeWrapperElementType
  extends PerlPolyNamedElementType<PerlMooseAttributeWrapperStub, PerlMooseAttributeWrapper> {
  public PerlMooseAttributeWrapperElementType(@NotNull String debugName) {
    super(debugName);
  }

  @Override
  public PerlMooseAttributeWrapper createPsi(@NotNull PerlMooseAttributeWrapperStub stub) {
    return new PerlMooseAttributeWrapper(stub, this);
  }

  @NotNull
  @Override
  public PsiElement getPsiElement(@NotNull ASTNode node) {
    return new PerlMooseAttributeWrapper(node);
  }

  @NotNull
  @Override
  protected PerlMooseAttributeWrapperStub createStub(@NotNull PerlMooseAttributeWrapper perlMooseAttributeWrapper,
                                                     StubElement parentStub,
                                                     @NotNull List<StubElement> lightStubs) {
    return new PerlMooseAttributeWrapperStub(parentStub, this, perlMooseAttributeWrapper.getAttributesNames(), lightStubs);
  }

  @Override
  protected void serializeStub(@NotNull PerlMooseAttributeWrapperStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    PerlStubSerializationUtil.writeStringsList(dataStream, stub.getAttributesNames());
  }

  @NotNull
  @Override
  protected PerlMooseAttributeWrapperStub deserialize(@NotNull StubInputStream dataStream,
                                                      StubElement parentStub,
                                                      @NotNull List<StubElement> childStubs) throws IOException {
    List<String> attributesNames = PerlStubSerializationUtil.readStringsList(dataStream);
    assert attributesNames != null;
    return new PerlMooseAttributeWrapperStub(parentStub, this, attributesNames, childStubs);
  }
}
