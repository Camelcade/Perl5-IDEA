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
  private final String packageName;
  private final String variableName;
  private final boolean isLeftSideOfAssignment;

  public PerlGlobStubImpl(final StubElement parent, final String packageName, final String variableName, boolean isLeftSideOfAssignment) {
    super(parent, PerlStubElementTypes.PERL_GLOB);
    this.packageName = packageName;
    this.variableName = variableName;
    this.isLeftSideOfAssignment = isLeftSideOfAssignment;
  }

  @Override
  public String getPackageName() {
    return packageName;
  }

  @Override
  public String getName() {
    return variableName;
  }

  @Override
  public boolean isLeftSideOfAssignment() {
    return isLeftSideOfAssignment;
  }

  @Override
  public String toString() {
    return super.toString() + "\n" +
           "\tNamespace name: " + packageName + "\n" +
           "\tVariable name: " + variableName + "\n" +
           "\tIs left side: " + isLeftSideOfAssignment + "\n"
      ;
  }
}
