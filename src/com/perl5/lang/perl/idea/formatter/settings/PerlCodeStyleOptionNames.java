/*
 * Copyright 2015 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.formatter.settings;

/**
 * Created by hurricup on 06.09.2015.
 */
public interface PerlCodeStyleOptionNames {
  String SPACE_GROUP_AFTER_KEYWORD = "After keywords";
  String SPACE_OPTION_VARIABLE_DECLARATION_KEYWORD = "Variable declaration";

  String SPACE_OPTION_AROUND_RANGE_OPERATORS = "Range operators (..,...)";
  String SPACE_OPTION_AROUND_CONCAT_OPERATOR = "Concatenation (.)";

  String SPACE_OPTION_COMPOUND_EXPRESSION = "Compound condition or expression";
  String SPACE_OPTION_COMPOUND_BLOCK = "Compound block";
  String SPACE_OPTION_CALL_ARGUMENTS = "Call arguments";

  String SPACE_OPTION_COMPOUND_SECONDARY = "Secondary compound (else/elsif/continue/default)";
  String SPACE_OPTION_TERM_BLOCKS = "Term blocks (sub/eval/do)";

  String SPACE_GROUP_ANON_HASH = "Anonymous hashes";
  String SPACE_OPTION_ANON_HASH_AFTER_LEFT_BRACE = "After left brace";
  String SPACE_OPTION_ANON_HASH_BEFORE_RIGHT_BRACE = "Before right brace";

  String SPACE_GROUP_ANON_ARRAY = "Anonymous arrays";
  String SPACE_OPTION_ANON_ARRAY_AFTER_LEFT_BRACKET = "After left bracket";
  String SPACE_OPTION_ANON_ARRAY_BEFORE_RIGHT_BRACKET = "Before right bracket";


  String TAB_PERL_SETTINGS = "Perl5-specific";

  String QUOTATION_GROUP = "Optional quotation";

  String QUOTATION_OPTION_BEFORE_ARROW = "String before =>";
  String QUOTATION_OPTION_HASH_INDEX = "Hash index";
  String QUOTATION_OPTION_HEREDOC_OPENER = "QQ here-doc opener";

  String DEREFERENCE_GROUP = "Dereferencing";
  String DEREFERENCE_OPTION_BETWEEN_INDEXES = "-> between indexes";
  String DEREFERENCE_OPTION_HASHREF_ELEMENT = "Hashref element";
  String DEREFERENCE_OPTION_SIMPLE = "Simple dereference";

  String PARENTHESES_GROUP = "Optional parentheses";
  String PARENTHESES_OPTION_POSTFIX = "Statement modifiers";

  String OPTIONAL_ELEMENTS_GROUP = "Optional syntax";
  String PERL_OPTION_OPTIONAL_SEMI = "Semicolon [NYI]";

  String MISC_ELEMENTS_GROUP = "Miscellaneous";
  String PERL_OPTION_MISC_MAIN = "main package fromat";
}
