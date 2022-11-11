/*
 * Copyright 2015-2022 Alexandr Evstigneev
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

package com.perl5.lang.perl.psi.stubs.variables;

import com.intellij.psi.stubs.StubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import org.jetbrains.annotations.NotNull;


public class PerlScalarsStubIndex extends PerlVariablesStubIndex {
  public static final StubIndexKey<String, PerlVariableDeclarationElement> KEY_SCALAR = StubIndexKey.createIndexKey("perl.global.scalar");

  @Override
  public @NotNull StubIndexKey<String, PerlVariableDeclarationElement> getKey() {
    return KEY_SCALAR;
  }

  public static @NotNull PerlScalarsStubIndex getInstance() {
    return StubIndexExtension.EP_NAME.findExtensionOrFail(PerlScalarsStubIndex.class);
  }
}
