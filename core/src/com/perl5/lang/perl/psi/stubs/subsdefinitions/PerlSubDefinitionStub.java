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

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.perl5.lang.perl.psi.PerlSubDefinitionElement;
import com.perl5.lang.perl.psi.stubs.PerlSubStub;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;

import java.util.List;

/**
 * Created by hurricup on 25.05.2015.
 */
public class PerlSubDefinitionStub extends PerlSubStub<PerlSubDefinitionElement> {
  private final List<PerlSubArgument> myArguments;

  public PerlSubDefinitionStub(StubElement parent,
                               String packageName,
                               String subName,
                               List<PerlSubArgument> arguments,
                               PerlSubAnnotations annotations,
                               IStubElementType elementType) {
    super(parent, packageName, subName, annotations, elementType);
    myArguments = arguments;
  }

  public List<PerlSubArgument> getSubArgumentsList() {
    return myArguments;
  }
}
