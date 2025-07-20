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

package com.perl5.lang.perl.parser.moose;

import com.intellij.psi.tree.TokenSet;

import static com.perl5.lang.perl.parser.moose.MooseElementTypes.*;

public final class MooseTokenSets {
  public static final TokenSet MOOSE_STATEMENTS = TokenSet.create(
    MOOSE_STATEMENT_WITH, MOOSE_STATEMENT_EXTENDS, MOOSE_STATEMENT_META, MOOSE_STATEMENT_AROUND, MOOSE_STATEMENT_AFTER,
    MOOSE_STATEMENT_BEFORE, MOOSE_STATEMENT_AUGMENT, MOOSE_STATEMENT_OVERRIDE
  );

  private MooseTokenSets() {
  }
}
