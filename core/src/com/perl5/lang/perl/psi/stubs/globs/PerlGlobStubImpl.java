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

package com.perl5.lang.perl.psi.stubs.globs;

import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import com.perl5.lang.perl.psi.PsiPerlGlobVariable;
import com.perl5.lang.perl.psi.stubs.PerlStubElementTypes;

public class PerlGlobStubImpl extends StubBase<PsiPerlGlobVariable> implements PerlGlobStub {
  private final String myNamespaceName;
  private final String myVariableName;
  private final boolean myIsLeftSideOfAssignment;

  public PerlGlobStubImpl(final StubElement parent, final String namespaceName, final String variableName, boolean isLeftSideOfAssignment) {
    super(parent, PerlStubElementTypes.PERL_GLOB);
    myNamespaceName = namespaceName;
    myVariableName = variableName;
    myIsLeftSideOfAssignment = isLeftSideOfAssignment;
  }

  @Override
  public String getNamespaceName() {
    return myNamespaceName;
  }

  @Override
  public String getName() {
    return myVariableName;
  }

  @Override
  public boolean isLeftSideOfAssignment() {
    return myIsLeftSideOfAssignment;
  }

  @Override
  public String toString() {
    return super.toString() + "\n" +
           "\tNamespace name: " + myNamespaceName + "\n" +
           "\tVariable name: " + myVariableName + "\n" +
           "\tIs left side: " + myIsLeftSideOfAssignment + "\n"
      ;
  }
}
