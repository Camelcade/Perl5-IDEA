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

import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;

/**
 * Created by hurricup on 03.09.2015.
 */
public class PerlCodeStyleSettings extends CustomCodeStyleSettings
{
	public int OPTIONAL_QUOTES = OptionalConstructions.WHATEVER;
	public int OPTIONAL_QUOTES_HASH_INDEX = OptionalConstructions.WHATEVER;
	public int OPTIONAL_QUOTES_HEREDOC_OPENER = OptionalConstructions.WHATEVER;

	public int OPTIONAL_DEREFERENCE = OptionalConstructions.WHATEVER;
	public int OPTIONAL_DEREFERENCE_HASHREF_ELEMENT = OptionalConstructions.WHATEVER;
	public int OPTIONAL_DEREFERENCE_SIMPLE = OptionalConstructions.WHATEVER;

	public int OPTIONAL_PARENTHESES = OptionalConstructions.WHATEVER;
	public int OPTIONAL_SEMI = OptionalConstructions.WHATEVER;
	public boolean SPACE_AFTER_VARIABLE_DECLARATION_KEYWORD = true;

	public PerlCodeStyleSettings(CodeStyleSettings container)
	{
		super("Perl5CodeStyleSettings", container);
	}

	public interface OptionalConstructions
	{
		int WHATEVER = 0x00;
		int FORCE = 0x01;
		int SUPPRESS = 0x02;
		int[] VALUES = {WHATEVER, FORCE, SUPPRESS};

		String[] LABELS_DEFAULT = {"Whatever", "Force", "Suppress"};
		Object[] OPTIONS_DEFAULT = {LABELS_DEFAULT, VALUES};

		String[] LABELS_HASHREF_ELEMENT_STYLE = {"As is", "$$hashref{key}", "$hashref->{key}"};
		Object[] OPTIONS_HASHREF_ELEMENT = {LABELS_HASHREF_ELEMENT_STYLE, VALUES};

		String[] LABELS_SIMPLE_DEREF_STYLE = {"As is", "@{$reference}", "@$reference"};
		Object[] OPTIONS_SIMPLE_DEREF_STYLE = {LABELS_SIMPLE_DEREF_STYLE, VALUES};
	}

}
