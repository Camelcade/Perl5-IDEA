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

package com.perl5.lang.perl.psi.stubs;

import com.intellij.psi.stubs.*;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.psi.PerlPolyNamedElement;
import com.perl5.lang.perl.psi.impl.PerlPolyNamedElementImpl;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class PerlPolyNamedElementType extends IStubElementType<PerlPolyNamedElementStub, PerlPolyNamedElement> {
  public PerlPolyNamedElementType(@NotNull String debugName) {
    super(debugName, PerlLanguage.INSTANCE);
  }

  @Override
  public PerlPolyNamedElement createPsi(@NotNull PerlPolyNamedElementStub stub) {
    return new PerlPolyNamedElementImpl(stub, this);
  }

  @NotNull
  @Override
  public PerlPolyNamedElementStub createStub(@NotNull PerlPolyNamedElement psi, StubElement parentStub) {
    return new PerlPolyNamedElementStub(parentStub, this);
  }

  @NotNull
  @Override
  public String getExternalId() {
    return "perl." + super.toString();
  }

  @Override
  public void serialize(@NotNull PerlPolyNamedElementStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    // fixme NYI
  }

  @NotNull
  @Override
  public PerlPolyNamedElementStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return new PerlPolyNamedElementStub(parentStub, this);
  }

  @Override
  public void indexStub(@NotNull PerlPolyNamedElementStub stub, @NotNull IndexSink sink) {
    // fixme: NYI
  }
}
