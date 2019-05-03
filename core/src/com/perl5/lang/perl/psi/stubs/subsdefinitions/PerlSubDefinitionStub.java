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

package com.perl5.lang.perl.psi.stubs.subsdefinitions;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.psi.PerlSubDefinition;
import com.perl5.lang.perl.psi.PerlSubDefinitionElement;
import com.perl5.lang.perl.psi.stubs.PerlSubStub;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PerlSubDefinitionStub extends PerlSubStub<PerlSubDefinitionElement> implements PerlSubDefinition {
  @NotNull
  private final List<PerlSubArgument> myArguments;
  @NotNull
  private final PerlValue myReturnValueFromCode;

  public PerlSubDefinitionStub(StubElement parent,
                               String packageName,
                               String subName,
                               @NotNull List<PerlSubArgument> arguments,
                               PerlSubAnnotations annotations,
                               @NotNull PerlValue returnValueFromCode,
                               IStubElementType elementType) {
    super(parent, packageName, subName, annotations, elementType);
    myArguments = arguments;
    myReturnValueFromCode = returnValueFromCode;
  }

  @NotNull
  @Override
  public PerlValue getReturnValueFromCode() {
    return myReturnValueFromCode;
  }

  @NotNull
  public List<PerlSubArgument> getSubArgumentsList() {
    return myArguments;
  }
}
