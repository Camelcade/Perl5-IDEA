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

package com.perl5.lang.tt2.formatter;

import com.intellij.psi.tree.TokenSet;

import static com.perl5.lang.tt2.elementTypes.TemplateToolkitElementTypes.*;
import static com.perl5.lang.tt2.parser.TemplateToolkitElementTypesGenerated.*;

final class TemplateToolkitFormattingTokenSets {
  static final TokenSet ALIGNABLE_ASSIGN_EXPRESSIONS_CONTAINERS = TokenSet.create(
    DEFAULT_DIRECTIVE,
    SET_DIRECTIVE,
    INCLUDE_DIRECTIVE,
    EXCEPTION_ARGS,
    WRAPPER_DIRECTIVE
  );
  static final TokenSet ALIGNABLE_PAIR_EXPRESSIONS_CONTAINERS = TokenSet.create(
    HASH_EXPR,
    META_DIRECTIVE,
    CALL_ARGUMENTS
  );
  static final TokenSet NORMAL_INDENTED_CONTAINERS_WITH_CLOSE_TAG = TokenSet.create(
    ANON_BLOCK,
    NAMED_BLOCK,

    FILTER_BLOCK,
    FOREACH_BLOCK,
    WHILE_BLOCK,
    SWITCH_BLOCK,
    CASE_BLOCK,
    WRAPPER_BLOCK
  );
  static final TokenSet NORMAL_INDENTED_CONTAINERS = TokenSet.create(
    IF_BRANCH,
    UNLESS_BRANCH,
    ELSIF_BRANCH,
    ELSE_BRANCH,

    TRY_BRANCH,
    CATCH_BRANCH,
    FINAL_BRANCH
  );
  static final TokenSet CONTINUOUS_INDENTED_CONTAINERS = TokenSet.create(
    ASSIGN_EXPR,
    PAIR_EXPR,
    CALL_ARGUMENTS,

    ANON_BLOCK_DIRECTIVE,
    BLOCK_DIRECTIVE,
    CALL_DIRECTIVE,
    CLEAR_DIRECTIVE,
    DEBUG_DIRECTIVE,
    DEFAULT_DIRECTIVE,
    ELSIF_DIRECTIVE,
    FILTER_DIRECTIVE,
    FOREACH_DIRECTIVE,
    GET_DIRECTIVE,
    IF_DIRECTIVE,
    INCLUDE_DIRECTIVE,
    INSERT_DIRECTIVE,
    LAST_DIRECTIVE,
    MACRO_DIRECTIVE,
    META_DIRECTIVE,
    NEXT_DIRECTIVE,
    PROCESS_DIRECTIVE,
    RETURN_DIRECTIVE,
    SET_DIRECTIVE,
    STOP_DIRECTIVE,

    SWITCH_DIRECTIVE,
    CASE_DIRECTIVE,

    CATCH_DIRECTIVE,
    THROW_DIRECTIVE,
    USE_DIRECTIVE,
    WHILE_DIRECTIVE,
    WRAPPER_DIRECTIVE
  );
  static final TokenSet CONTINUOUS_INDENTED_CONTAINERS_WITH_CLOSE_TAG = TokenSet.create(
    ARRAY_EXPR,
    HASH_EXPR
  );
  static final TokenSet PERL_BLOCKS = TokenSet.create(
    TT2_RAWPERL_CODE,
    TT2_PERL_CODE
  );
  static final TokenSet NORMAL_INDENTED_CONTAINERS_PARENTS = TokenSet.create(
    TRY_CATCH_BLOCK,
    IF_BLOCK,
    UNLESS_BLOCK,
    SWITCH_BLOCK,
    CASE_BLOCK
  );
  static final TokenSet NORMAL_CHILD_INDENTED_CONTAINERS = TokenSet.orSet(
    NORMAL_INDENTED_CONTAINERS,
    NORMAL_INDENTED_CONTAINERS_WITH_CLOSE_TAG,
    NORMAL_INDENTED_CONTAINERS_PARENTS
  );
  static final TokenSet CONTINUOUS_CHILD_INDENTED_CONTAINERS = TokenSet.orSet(
    CONTINUOUS_INDENTED_CONTAINERS,
    CONTINUOUS_INDENTED_CONTAINERS_WITH_CLOSE_TAG
  );

  private TemplateToolkitFormattingTokenSets() {
  }
}
