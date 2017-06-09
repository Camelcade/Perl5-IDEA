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

package com.perl5.lang.perl.psi.stubs;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import com.perl5.lang.perl.psi.PerlSub;
import com.perl5.lang.perl.psi.PerlSubElement;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.util.PerlPackageUtil;

public abstract class PerlSubStub<Psi extends PerlSubElement> extends StubBase<Psi> implements StubElement<Psi>, PerlSub {
  private final String myPackageName;
  private final String mySubName;
  private final PerlSubAnnotations myAnnotations;

  public PerlSubStub(final StubElement parent,
                     final String packageName,
                     final String subName,
                     PerlSubAnnotations annotations,
                     IStubElementType elementType) {
    super(parent, elementType);
    myPackageName = packageName;
    mySubName = subName;
    myAnnotations = annotations;
  }

  @Override
  public String getPackageName() {
    return myPackageName;
  }

  @Override
  public String getSubName() {
    return mySubName;
  }

  @Override
  public PerlSubAnnotations getAnnotations() {
    return myAnnotations;
  }

  @Override
  public String getCanonicalName() {
    return getPackageName() + PerlPackageUtil.PACKAGE_SEPARATOR + getSubName();
  }
}
