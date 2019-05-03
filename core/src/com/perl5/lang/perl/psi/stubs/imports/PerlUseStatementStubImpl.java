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

import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import com.perl5.lang.perl.psi.PerlUseStatement;
import com.perl5.lang.perl.psi.stubs.PerlStubElementTypes;

import java.util.List;

public class PerlUseStatementStubImpl extends StubBase<PerlUseStatement> implements PerlUseStatementStub {
  private final String myPackageName;
  private final String myOuterPackageName;
  private final List<String> myImportOptions;

  public PerlUseStatementStubImpl(StubElement parent, String outerPackageName, String packageName, List<String> importOptions) {
    super(parent, PerlStubElementTypes.PERL_USE_STATEMENT);
    assert packageName != null;
    assert outerPackageName != null;
    this.myPackageName = packageName;
    this.myOuterPackageName = outerPackageName;
    this.myImportOptions = importOptions;
  }

  @Override
  public String getPackageName() {
    return myPackageName;
  }

  @Override
  public String getOuterPackageName() {
    return myOuterPackageName;
  }

  @Override
  public List<String> getImportParameters() {
    return myImportOptions;
  }

  @Override
  public String toString() {
    return super.toString() + "\n" +
           "\tPackage name: " + myPackageName + "\n" +
           "\tOuter package name: " + myOuterPackageName + "\n" +
           "\tImport options: " + myImportOptions
      ;
  }
}
