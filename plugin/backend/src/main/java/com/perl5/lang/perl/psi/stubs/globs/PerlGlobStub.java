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

package com.perl5.lang.perl.psi.stubs.globs;

import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import com.perl5.lang.perl.psi.PerlGlobVariable;
import com.perl5.lang.perl.psi.PsiPerlGlobVariable;
import com.perl5.lang.perl.psi.stubs.PerlStubElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerlGlobStub extends StubBase<PsiPerlGlobVariable> implements StubElement<PsiPerlGlobVariable>, PerlGlobVariable {
  private final @NotNull String myNamespaceName;
  private final String myVariableName;
  private final boolean myIsLeftSideOfAssignment;

  public PerlGlobStub(final StubElement<?> parent,
                      @NotNull String namespaceName,
                      String variableName,
                      boolean isLeftSideOfAssignment) {
    super(parent, PerlStubElementTypes.PERL_GLOB);
    myNamespaceName = namespaceName;
    myVariableName = variableName;
    myIsLeftSideOfAssignment = isLeftSideOfAssignment;
  }

  @Override
  public @NotNull String getNamespaceName() {
    return myNamespaceName;
  }

  @Override
  public @Nullable String getExplicitNamespaceName() {
    return getNamespaceName();
  }

  @Override
  public String getGlobName() {
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
           "\tIs left side: " + myIsLeftSideOfAssignment
      ;
  }
}
