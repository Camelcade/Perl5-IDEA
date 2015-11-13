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

import com.intellij.application.options.IndentOptionsEditor;
import com.intellij.application.options.SmartIndentOptionsEditor;
import com.intellij.lang.Language;
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider;
import com.perl5.lang.perl.PerlLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 03.09.2015.
 */
public class PerlLanguageCodeStyleSettingsProvider extends LanguageCodeStyleSettingsProvider implements PerlCodeStyleOptionNames
{
	private static final String DEFAULT_CODE_SAMPLE = "# Not yet implemented";
	private static final String INDENT_CODE_SAMPLE = "# Not yet implemented";
	private static final String BLANK_LINES_CODE_SAMPLE = "# Not yet implemented";

	@Override
	public void customizeSettings(@NotNull CodeStyleSettingsCustomizable consumer, @NotNull SettingsType settingsType)
	{
		if (settingsType == SettingsType.SPACING_SETTINGS)
		{
			consumer.showStandardOptions(
					"SPACE_AROUND_ASSIGNMENT_OPERATORS",    // implemented
					"SPACE_AROUND_LOGICAL_OPERATORS",        // implemented
					"SPACE_AROUND_EQUALITY_OPERATORS",        // implemented
					"SPACE_AROUND_RELATIONAL_OPERATORS",    // implemented
					"SPACE_AROUND_BITWISE_OPERATORS",        // implemented
					"SPACE_AROUND_ADDITIVE_OPERATORS",        // implemented
					"SPACE_AROUND_MULTIPLICATIVE_OPERATORS",// implemented
					"SPACE_AROUND_SHIFT_OPERATORS",            // implemented
					"SPACE_AROUND_UNARY_OPERATOR",            // implemented

					"SPACE_AFTER_COMMA",    // implemented
					"SPACE_BEFORE_COMMA",   // implemented

					"SPACE_AFTER_COLON",    // implemented

					"SPACE_AFTER_SEMICOLON",    // implemented
					"SPACE_BEFORE_SEMICOLON",    // implemented

					"SPACE_BEFORE_IF_PARENTHESES",    // implemented, any conditional block, for and iterator
					"SPACE_WITHIN_IF_PARENTHESES",    // condition, for iterator
					"SPACE_BEFORE_IF_LBRACE",        // implemented, any or undonditional conditional block, for,

					"SPACE_BEFORE_ELSE_KEYWORD",    // implemented, else,elsif,continue,default

					"SPACE_BEFORE_WHILE_KEYWORD",    // implemented, postfix if/for/etc
					"SPACE_BEFORE_DO_LBRACE"        // implemented, sub_{}, do_{}, eval_{}
			);
			consumer.renameStandardOption("SPACE_BEFORE_IF_PARENTHESES", SPACE_OPTION_COMPOUND_EXPRESSION);
			consumer.renameStandardOption("SPACE_WITHIN_IF_PARENTHESES", SPACE_OPTION_COMPOUND_EXPRESSION);
			consumer.renameStandardOption("SPACE_BEFORE_IF_LBRACE", SPACE_OPTION_COMPOUND_BLOCK);
			consumer.renameStandardOption("SPACE_BEFORE_ELSE_KEYWORD", SPACE_OPTION_COMPOUND_SECONDARY);
			consumer.renameStandardOption("SPACE_BEFORE_WHILE_KEYWORD", SPACE_OPTION_STATEMENT_MODIFIERS);
			consumer.renameStandardOption("SPACE_BEFORE_DO_LBRACE", SPACE_OPTION_TERM_BLOCKS);

			consumer.showCustomOption(PerlCodeStyleSettings.class, "SPACE_AFTER_VARIABLE_DECLARATION_KEYWORD", SPACE_OPTION_VARIABLE_DECLARATION_KEYWORD, SPACE_GROUP_AFTER_KEYWORD);
		}
		else if (settingsType == SettingsType.LANGUAGE_SPECIFIC)
		{
			// fixme this should be in perl-related tab, see #422
			consumer.showCustomOption(PerlCodeStyleSettings.class, "OPTIONAL_QUOTES", PERL_OPTION_OPTIONAL_QUOTES_BEFORE_ARROW, WRAP_GROUP_PERL_SPECIFIC, PerlCodeStyleSettings.OptionalConstructions.OPTIONS);
			consumer.showCustomOption(PerlCodeStyleSettings.class, "OPTIONAL_QUOTES_HASH_INDEX", PERL_OPTION_OPTIONAL_QUOTES_HASH_INDEX, WRAP_GROUP_PERL_SPECIFIC, PerlCodeStyleSettings.OptionalConstructions.OPTIONS);
			consumer.showCustomOption(PerlCodeStyleSettings.class, "OPTIONAL_QUOTES_HEREDOC_OPENER", PERL_OPTION_OPTIONAL_QUOTES_HEREDOC_OPENER, WRAP_GROUP_PERL_SPECIFIC, PerlCodeStyleSettings.OptionalConstructions.OPTIONS);
			consumer.showCustomOption(PerlCodeStyleSettings.class, "OPTIONAL_DEREFERENCE", PERL_OPTION_OPTIONAL_DEREFERENCE, WRAP_GROUP_PERL_SPECIFIC, PerlCodeStyleSettings.OptionalConstructions.OPTIONS);
			consumer.showCustomOption(PerlCodeStyleSettings.class, "OPTIONAL_PARENTHESES", PERL_OPTION_OPTIONAL_PARENTHESES, WRAP_GROUP_PERL_SPECIFIC, PerlCodeStyleSettings.OptionalConstructions.OPTIONS);
			consumer.showCustomOption(PerlCodeStyleSettings.class, "OPTIONAL_SEMI", PERL_OPTION_OPTIONAL_SEMI, WRAP_GROUP_PERL_SPECIFIC, PerlCodeStyleSettings.OptionalConstructions.OPTIONS);

		}
	}

	@Override
	public IndentOptionsEditor getIndentOptionsEditor()
	{
		return new SmartIndentOptionsEditor();
	}

	@Nullable
	@Override
	public CommonCodeStyleSettings getDefaultCommonSettings()
	{
		CommonCodeStyleSettings defaultSettings = new CommonCodeStyleSettings(getLanguage());
		CommonCodeStyleSettings.IndentOptions indentOptions = defaultSettings.initIndentOptions();
		indentOptions.INDENT_SIZE = 4;
		indentOptions.CONTINUATION_INDENT_SIZE = 4;
		indentOptions.TAB_SIZE = 4;

		return defaultSettings;
	}

	@NotNull
	@Override
	public Language getLanguage()
	{
		return PerlLanguage.INSTANCE;
	}

	@Override
	public String getCodeSample(SettingsType settingsType)
	{
		if (settingsType == SettingsType.SPACING_SETTINGS || settingsType == SettingsType.WRAPPING_AND_BRACES_SETTINGS)
		{
			return DEFAULT_CODE_SAMPLE;
		}
		if (settingsType == SettingsType.INDENT_SETTINGS)
		{
			return INDENT_CODE_SAMPLE;
		}
		return BLANK_LINES_CODE_SAMPLE;
	}
}
