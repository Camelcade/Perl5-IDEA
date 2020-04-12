/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package com.perl5.lang.perl.psi.stubs.calls;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.perl5.lang.perl.psi.PerlSubCallHandler;
import com.perl5.lang.perl.psi.impl.PerlSubCallElement;
import com.perl5.lang.perl.psi.impl.PsiPerlSubCallImpl;
import com.perl5.lang.perl.psi.stubs.PerlPolyNamedElementType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class PerlSubCallElementType extends PerlPolyNamedElementType<PerlSubCallElementStub, PerlSubCallElement> {
  public PerlSubCallElementType(@NotNull String debugName) {
    super(debugName);
  }

  @NotNull
  @Override
  protected PerlSubCallElementStub createStub(@NotNull PerlSubCallElement element,
                                              StubElement<?> parentStub,
                                              @NotNull List<StubElement<?>> lightElementsStubs) {
    return new PerlSubCallElementStub(parentStub,
                                      this,
                                      lightElementsStubs,
                                      Objects.requireNonNull(element.getSubName()),
                                      Objects.requireNonNull(element.getCallData()));
  }

  @NotNull
  @Override
  protected PerlSubCallElementStub deserialize(@NotNull StubInputStream dataStream,
                                               StubElement<?> parentStub,
                                               @NotNull List<StubElement<?>> lightElementsStubs) throws IOException {
    String subName = Objects.requireNonNull(dataStream.readNameString());
    PerlSubCallHandler<?> provider = getProvider(subName);
    return new PerlSubCallElementStub(parentStub, this, lightElementsStubs, subName, provider.deserialize(dataStream));
  }

  @NotNull
  private PerlSubCallHandler<?> getProvider(String subName) {
    return Objects.requireNonNull(PerlSubCallHandler.getHandler(subName),
                                  "Unable to find call handler for " + subName);
  }

  @Override
  protected void serializeStub(@NotNull PerlSubCallElementStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(stub.getSubName());
    getProvider(stub.getSubName()).serialize(stub.getCallData(), dataStream);
  }

  @Override
  public PerlSubCallElement createPsi(@NotNull PerlSubCallElementStub stub) {
    return new PsiPerlSubCallImpl(stub, this);
  }

  @NotNull
  @Override
  public PsiElement getPsiElement(@NotNull ASTNode node) {
    return new PsiPerlSubCallImpl(node);
  }
}
