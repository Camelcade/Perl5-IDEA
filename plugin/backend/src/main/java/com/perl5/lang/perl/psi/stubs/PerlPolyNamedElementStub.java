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

package com.perl5.lang.perl.psi.stubs;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.psi.impl.PerlPolyNamedElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class PerlPolyNamedElementStub<Psi extends PerlPolyNamedElement<?>> extends StubBase<Psi> {
  private final @NotNull List<StubElement<?>> myLightNamedElementsStubs;

  public PerlPolyNamedElementStub(StubElement<?> parent,
                                  IElementType elementType,
                                  @NotNull List<StubElement<?>> lightNamedElementsStubs) {
    super(parent, elementType);
    myLightNamedElementsStubs = lightNamedElementsStubs;
  }

  public @NotNull List<StubElement<?>> getLightNamedElementsStubs() {
    return myLightNamedElementsStubs;
  }

  @Override
  public String toString() {
    List<String> chunks = new ArrayList<>();
    chunks.add(super.toString());
    chunks.add("\tLight elements: " + myLightNamedElementsStubs.size());
    for (StubElement<?> lightNamedElementsStub : myLightNamedElementsStubs) {
      chunks.add("\t" + lightNamedElementsStub);
    }
    return StringUtil.join(chunks, "\n");
  }
}
