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
public interface PerlCodeStyleOptionNames
{
	String GROUP_SPACE_AFTER_KEYWORD = "After keywords";

	String SPACE_OPTION_VARIABLE_DECLARATION_KEYWORD = "Variable declaration";
	String SPACE_OPTION_COMPOUND_EXPRESSION = "Compound condition or expression";
	String SPACE_OPTION_COMPOUND_BLOCK = "Compound block";

	String SPACE_OPTION_COMPOUND_SECONDARY = "Secondary compound (else/elsif/continue/default)";
	String SPACE_OPTION_TERM_BLOCKS = "Term blocks (sub/eval/do)";

	String SPACE_OPTION_STATEMENT_MODIFIERS = "Statement modifiers";

	String TAB_PERL_SETTINGS = "Perl5-specific";

	String GROUP_PERL_SETTINGS_OPTIONAL_QUOTES = "Optional quotation";

	String PERL_OPTION_OPTIONAL_QUOTES_BEFORE_ARROW = "String before =>";
	String PERL_OPTION_OPTIONAL_QUOTES_HASH_INDEX = "Hash index";
	String PERL_OPTION_OPTIONAL_QUOTES_HEREDOC_OPENER = "Here-doc opener";

	String GROUP_PERL_SETTINGS_OPTIONAL_ELEMENTS = "Optional syntax";
	String PERL_OPTION_OPTIONAL_DEREFERENCE = "-> between indexes";
	String PERL_OPTION_OPTIONAL_PARENTHESES = "Parentheses [NYI]";
	String PERL_OPTION_OPTIONAL_SEMI = "Semicolon [NYI]";



}
