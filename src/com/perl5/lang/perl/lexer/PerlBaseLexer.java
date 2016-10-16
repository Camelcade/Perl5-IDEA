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

package com.perl5.lang.perl.lexer;

import com.perl5.lang.mojolicious.MojoliciousElementTypes;
import com.perl5.lang.perl.parser.Class.Accessor.ClassAccessorElementTypes;
import com.perl5.lang.perl.parser.moose.MooseElementTypes;
import com.perl5.lang.perl.parser.perlswitch.PerlSwitchElementTypes;
import com.perl5.lang.perl.parser.trycatch.TryCatchElementTypes;

import java.util.Stack;
import java.util.regex.Pattern;

/**
 * Created by hurricup on 10.08.2015.
 */
public abstract class PerlBaseLexer extends PerlProtoLexer
		implements PerlElementTypes,
		PerlSwitchElementTypes,
		TryCatchElementTypes,
		ClassAccessorElementTypes,
		MojoliciousElementTypes,
		MooseElementTypes
{
	private static final String BASIC_IDENTIFIER_PATTERN_TEXT = "[_\\p{L}\\d][_\\p{L}\\d]*"; // something strange in Java with unicode props; Added digits to opener for package Encode::KR::2022_KR;
	private static final String PACKAGE_SEPARATOR_PATTERN_TEXT =
			"(?:" +
					"(?:::)+'?" +
					"|" +
					"(?:::)*'" +
					")";
	public static final Pattern AMBIGUOUS_PACKAGE_PATTERN = Pattern.compile(
			"(" +
					PACKAGE_SEPARATOR_PATTERN_TEXT + "?" +        // optional opening separator,
					"(?:" +
					BASIC_IDENTIFIER_PATTERN_TEXT +
					PACKAGE_SEPARATOR_PATTERN_TEXT +
					")*" +
					")" +
					"(" +
					BASIC_IDENTIFIER_PATTERN_TEXT +
					")");

	// last captured heredoc marker
	protected final Stack<PerlHeredocQueueElement> heredocQueue = new Stack<>();

}
