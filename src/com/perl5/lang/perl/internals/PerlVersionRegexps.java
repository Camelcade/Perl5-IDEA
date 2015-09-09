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

package com.perl5.lang.perl.internals;

import java.util.regex.Pattern;

/**
 * Created by hurricup on 07.09.2015.
 * regexps taken from verion/regex.pm original names are kept, but in camelcase
 */
public interface PerlVersionRegexps
{
	static final Pattern numericVersion = Pattern.compile(
			"(0|[1-9]\\d*)" +                // revision
					"(?:\\." +
					"([\\d_]+)" +            // major
					")?"
	);

	static final Pattern dottedVersion = Pattern.compile(
			"v(?:0|[1-9]\\d*)" +            // revision
					"(?:\\.\\d+)*" +    // major, minor and others
					"(_\\d+)?"        // alpha
	);

	static final String fractionPart = "(?:\\.([0-9]+))";

	static final String strictIntegerPart = "(0|[1-9][0-9]*)";
	static final String strictDottedDecimalPart = "(?:\\.([0-9]{1,3}))";

	static final String laxIntegerPart = "([0-9]+)";
	static final String laxDottedDecimalPart = "(?:\\.([0-9]+))";
	static final String laxAlphaPart = "(?:_([0-9]+))";

	// strict versions
	static final Pattern strictDecimalVersion = Pattern.compile(
			strictIntegerPart + fractionPart + "?"
	);

	static final Pattern strictDottedDecimalVersion = Pattern.compile(
			"v" + strictIntegerPart + strictDottedDecimalPart + strictDottedDecimalPart + "+"
	);

	static final Pattern strict = Pattern.compile(
			strictDecimalVersion + "|" + strictDottedDecimalVersion
	);

	// lax versions
	static final Pattern laxDecimalVersion = Pattern.compile(
			laxIntegerPart + "(?:\\.|" + fractionPart + laxAlphaPart + "?)?"
					+ "|"
					+ fractionPart + laxAlphaPart + "?"
	);

	static final Pattern laxDottedDecimalVersion = Pattern.compile(
			"v" + laxIntegerPart + "(?:" + laxDottedDecimalPart + laxAlphaPart + "?)?"
					+ "|"
					+ laxIntegerPart + "?" + laxDottedDecimalPart + laxDottedDecimalPart + "+" + laxAlphaPart + "?"
	);

	static final Pattern lax = Pattern.compile(
			laxDecimalVersion + "|" + laxDottedDecimalVersion
	);

}
