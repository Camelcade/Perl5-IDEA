/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

import com.intellij.psi.stubs.StubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import com.perl5.lang.perl.psi.PerlSubDeclarationElement;
import com.perl5.lang.perl.psi.stubs.PerlStubIndexBase;
import org.jetbrains.annotations.NotNull;

public class PerlSubDeclarationReverseIndex extends PerlStubIndexBase<PerlSubDeclarationElement> {
  public static final int VERSION = 1;
  public static final StubIndexKey<String, PerlSubDeclarationElement> KEY = StubIndexKey.createIndexKey("perl.sub.declaration.reverse");

  @Override
  public int getVersion() {
    return super.getVersion() + VERSION;
  }

  @Override
  public @NotNull StubIndexKey<String, PerlSubDeclarationElement> getKey() {
    return KEY;
  }

  @Override
  protected @NotNull Class<PerlSubDeclarationElement> getPsiClass() {
    return PerlSubDeclarationElement.class;
  }

  public static @NotNull PerlSubDeclarationReverseIndex getInstance() {
    return StubIndexExtension.EP_NAME.findExtensionOrFail(PerlSubDeclarationReverseIndex.class);
  }
}
