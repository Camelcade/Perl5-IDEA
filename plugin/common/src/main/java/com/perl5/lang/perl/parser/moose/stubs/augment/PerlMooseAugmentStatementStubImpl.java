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

package com.perl5.lang.perl.parser.moose.stubs.augment;

import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.parser.moose.psi.PerlMooseAugmentStatement;

public class PerlMooseAugmentStatementStubImpl extends StubBase<PerlMooseAugmentStatement> implements PerlMooseAugmentStatementStub {
  private final String mySubName;

  public PerlMooseAugmentStatementStubImpl(StubElement parent, IElementType elementType, String subName) {
    super(parent, elementType);
    mySubName = subName;
  }

  @Override
  public String getSubName() {
    return mySubName;
  }

  @Override
  public String toString() {
    return super.toString() + "\n\tSub name: " + mySubName;
  }
}
