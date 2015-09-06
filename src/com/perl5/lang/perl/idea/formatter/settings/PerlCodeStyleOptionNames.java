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
	public static final String SPACE_GROUP_AFTER_KEYWORD = "After keywords";

	// fixme temporary solution, should be on a separate tab
	public static final String WRAP_GROUP_PERL_SPECIFIC = "Perl specific";

	public static final String SPACE_OPTION_VARIABLE_DECLARATION_KEYWORD = "Variable declaration";

	public static final String SPACE_OPTION_COMPOUND_EXPRESSION = "Compound condition or expression";
	public static final String SPACE_OPTION_COMPOUND_BLOCK = "Compound block";

	public static final String SPACE_OPTION_COMPOUND_SECONDARY = "Secondary compound (else/elsif/continue/default)";
	public static final String SPACE_OPTION_TERM_BLOCKS = "Term blocks (sub/eval/do)";

	public static final String SPACE_OPTION_STATEMENT_MODIFIERS = "Statement modifiers";

	public static final String PERL_OPTION_OPTIONAL_QUOTES = "Optional quotes [NYI]";
	public static final String PERL_OPTION_OPTIONAL_PARENTHESES = "Optional parentheses [NYI]";
	public static final String PERL_OPTION_OPTIONAL_DEREFERENCE = "Optional dereferences [NYI]";
	public static final String PERL_OPTION_OPTIONAL_SEMI = "Optional semicolons [NYI]";
}
