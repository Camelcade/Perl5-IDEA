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

package com.perl5.lang.perl.psi.stubs.variables;

import com.intellij.openapi.util.Pair;
import com.intellij.psi.stubs.StubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.psi.stubs.variables.PerlHashNamespaceStubIndex.KEY_HASH_IN_NAMESPACE;


public class PerlHashStubIndex extends PerlVariableStubIndex {
  public static final StubIndexKey<String, PerlVariableDeclarationElement> KEY_HASH = StubIndexKey.createIndexKey("perl.global.hash");
  public static final Pair<StubIndexKey<String, PerlVariableDeclarationElement>, StubIndexKey<String, PerlVariableDeclarationElement>>
    HASH_KEYS = Pair.create(KEY_HASH, KEY_HASH_IN_NAMESPACE);

  @Override
  public @NotNull StubIndexKey<String, PerlVariableDeclarationElement> getKey() {
    return KEY_HASH;
  }

  public static @NotNull PerlHashStubIndex getInstance() {
    return StubIndexExtension.EP_NAME.findExtensionOrFail(PerlHashStubIndex.class);
  }
}
