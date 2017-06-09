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

package com.perl5.lang.perl.psi.stubs.subsdeclarations;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.perl5.lang.perl.psi.PerlSubDeclaration;
import com.perl5.lang.perl.psi.stubs.PerlSubStub;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;

/**
 * Created by hurricup on 05.06.2015.
 */
public class PerlSubDeclarationStub extends PerlSubStub<PerlSubDeclaration> {
  public PerlSubDeclarationStub(StubElement parent,
                                String packageName,
                                String subName,
                                PerlSubAnnotations annotations, IStubElementType elementType) {
    super(parent, packageName, subName, annotations, elementType);
  }
}
