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

package com.perl5.lang.perl.parser.moose.stubs;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.perl5.lang.perl.psi.stubs.PerlPolyNamedElementStub;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PerlMooseAttributeWrapperStub extends PerlPolyNamedElementStub {

  @NotNull
  private final List<String> myAttributesNames;

  public PerlMooseAttributeWrapperStub(StubElement parent,
                                       IStubElementType elementType,
                                       @NotNull List<String> attributesNames,
                                       @NotNull List<StubElement> lightNamedElementsStubs) {
    super(parent, elementType, lightNamedElementsStubs);
    myAttributesNames = attributesNames;
  }

  @NotNull
  public List<String> getAttributesNames() {
    return myAttributesNames;
  }
}
