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

package com.perl5.lang.perl.psi.stubs.imports;

import com.intellij.psi.stubs.StubElement;
import com.perl5.lang.perl.psi.PerlUseStatement;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement;
import com.perl5.lang.perl.psi.stubs.PerlPolyNamedElementStub;
import com.perl5.lang.perl.psi.stubs.PerlStubElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PerlUseStatementStub extends PerlPolyNamedElementStub<PerlUseStatementElement> implements StubElement<PerlUseStatementElement>,
                                                                                                       PerlUseStatement {
  @NotNull
  private final String myPackageName;
  @NotNull
  private final String myNamespaceName;
  @Nullable
  private final List<String> myImportOptions;

  public PerlUseStatementStub(StubElement parent,
                              @NotNull String namespaceName,
                              @NotNull String packageName,
                              @Nullable List<String> importOptions,
                              @NotNull List<StubElement> lightNamedElementsStubs) {
    super(parent, PerlStubElementTypes.USE_STATEMENT, lightNamedElementsStubs);
    myPackageName = packageName;
    myNamespaceName = namespaceName;
    myImportOptions = importOptions;
  }

  @NotNull
  @Override
  public String getPackageName() {
    return myPackageName;
  }

  @NotNull
  @Override
  public String getNamespaceName() {
    return myNamespaceName;
  }

  @Nullable
  @Override
  public List<String> getImportParameters() {
    return myImportOptions;
  }

  @Override
  public String toString() {
    return super.toString() + "\n" +
           "\tPackage name: " + myPackageName + "\n" +
           "\tOuter package name: " + myNamespaceName + "\n" +
           "\tImport options: " + myImportOptions
      ;
  }
}
