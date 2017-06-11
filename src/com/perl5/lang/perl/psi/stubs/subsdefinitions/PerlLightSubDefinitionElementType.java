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

package com.perl5.lang.perl.psi.stubs.subsdefinitions;

import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.perl5.lang.perl.psi.PerlDelegatingSubElement;
import com.perl5.lang.perl.psi.PerlSubDefinitionElement;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PerlLightSubDefinitionElementType extends PerlSubDefinitionStubElementType {
  public PerlLightSubDefinitionElementType(String name) {
    super(name);
  }

  @Override
  public PerlSubDefinitionElement createPsi(@NotNull PerlSubDefinitionStub stub) {
    return new PerlDelegatingSubElement(stub);
  }

  @Override
  public void indexStub(@NotNull PerlSubDefinitionStub stub, @NotNull IndexSink sink) {
    // we should index parent stub
    sink.occurrence(PerlLightSubDefinitionIndex.KEY, stub.getCanonicalName());
    sink.occurrence(PerlLightSubDefinitionIndex.KEY, "*" + stub.getPackageName());
  }

  @NotNull
  @Override
  protected PerlSubDefinitionStub createStubElement(StubElement parentStub,
                                                    String packageName,
                                                    String functionName,
                                                    List<PerlSubArgument> arguments,
                                                    PerlSubAnnotations annotations) {
    return new PerlLightSubDefinitionStub(parentStub, packageName, functionName, arguments, annotations, this);
  }
}
