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
	private static final String SPACING_CODE_SAMPLE = "my ($var1, $var2);\n" +
			"$var1 = 100;\n" +
			"$var2 = 200;\n" +
			"\n" +
			"if ($var1 == $var2) {\n" +
			"    my $arrayref = [1, 1, 2, 3, 5, { key => 'val'}];\n" +
			"    say $var1 && $var2;\n" +
			"    say $var1 | $var2;\n" +
			"    for (my $i = 1; $i < 2; $i++)    {\n" +
			"        $a = $var1 & $var2;\n" +
			"        $a = $var1 + $var2;\n" +
			"    }\n" +
			"    $a = $var1 * $var2;\n" +
			"    $a = $var1 << 2;\n" +
			"\n" +
			"    until (!$var1) {\n" +
			"        eval { say $var2 ? $var2 : $var1;};\n" +
			"    }\n" +
			"} elsif ($var2) {\n" +
			"    say ($var1) unless $var2;\n" +
			"    say $var1 for @ARGV;\n" +
			"}else {\n" +
			"    say ($var1) unless $var2;\n" +
			"    say $var1 for @ARGV;\n" +
			"}";
	private static final String INDENT_CODE_SAMPLE = "if( $cond )\n" +
			"{\n" +
			"    say sprintf\n" +
			"    \"here\",\n" +
			"    \"we\",\n" +
			"    \"go\";\n" +
			"}";
	private static final String BLANK_LINES_CODE_SAMPLE = "# Not yet implemented";
	private static final String LANGUAGE_SPECIFIC_CODE_SAMPLE = "my $hashref = {\n" +
			"    key1 => 42,\n" +
			"    key2 => [69],\n" +
			"};\n" +
			"\n" +
			"$hashref->{key3} = <<EOM;\n" +
			"    This is a test\n" +
			"EOM\n" +
			"\n" +
			"\n" +
			"say $hashref->{'key1'} unless $1;\n" +
			"say $hashref->{key2}[0] if $b;\n" +
			"\n" +
			"my %hash = %$hashref;\n" +
			"\n" +
			"our $VERSION = 1;\n" +
			"say $main::VERSION;\n";

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

					"SPACE_BEFORE_QUEST",
					"SPACE_AFTER_QUEST",
					"SPACE_BEFORE_COLON",
					"SPACE_AFTER_COLON",    // implemented

					"SPACE_AFTER_SEMICOLON",    // implemented
					"SPACE_BEFORE_SEMICOLON",    // implemented

					"SPACE_BEFORE_IF_PARENTHESES",    // implemented, any conditional block, for and iterator
					"SPACE_WITHIN_IF_PARENTHESES",    // condition, for iterator
					"SPACE_BEFORE_IF_LBRACE",        // implemented, any or undonditional conditional block, for,

					"SPACE_BEFORE_ELSE_KEYWORD",    // implemented, else,elsif,continue,default

					"SPACE_BEFORE_DO_LBRACE"        // implemented, sub_{}, do_{}, eval_{}
			);
			consumer.renameStandardOption("SPACE_BEFORE_IF_PARENTHESES", SPACE_OPTION_COMPOUND_EXPRESSION);
			consumer.renameStandardOption("SPACE_WITHIN_IF_PARENTHESES", SPACE_OPTION_COMPOUND_EXPRESSION);
			consumer.renameStandardOption("SPACE_BEFORE_IF_LBRACE", SPACE_OPTION_COMPOUND_BLOCK);
			consumer.renameStandardOption("SPACE_BEFORE_ELSE_KEYWORD", SPACE_OPTION_COMPOUND_SECONDARY);
			consumer.renameStandardOption("SPACE_BEFORE_DO_LBRACE", SPACE_OPTION_TERM_BLOCKS);

			consumer.showCustomOption(PerlCodeStyleSettings.class, "SPACE_AFTER_VARIABLE_DECLARATION_KEYWORD", SPACE_OPTION_VARIABLE_DECLARATION_KEYWORD, SPACE_GROUP_AFTER_KEYWORD);

			consumer.showCustomOption(PerlCodeStyleSettings.class, "SPACE_ANON_HASH_AFTER_LEFT_BRACE", SPACE_OPTION_ANON_HASH_AFTER_LEFT_BRACE, SPACE_GROUP_ANON_HASH);
			consumer.showCustomOption(PerlCodeStyleSettings.class, "SPACE_ANON_HASH_BEFORE_RIGHT_BRACE", SPACE_OPTION_ANON_HASH_BEFORE_RIGHT_BRACE, SPACE_GROUP_ANON_HASH);

			consumer.showCustomOption(PerlCodeStyleSettings.class, "SPACE_ANON_ARRAY_AFTER_LEFT_BRACKET", SPACE_OPTION_ANON_ARRAY_AFTER_LEFT_BRACKET, SPACE_GROUP_ANON_ARRAY);
			consumer.showCustomOption(PerlCodeStyleSettings.class, "SPACE_ANON_ARRAY_BEFORE_RIGHT_BRACKET", SPACE_OPTION_ANON_ARRAY_BEFORE_RIGHT_BRACKET, SPACE_GROUP_ANON_ARRAY);
		}
		else if (settingsType == SettingsType.LANGUAGE_SPECIFIC)
		{
			consumer.showCustomOption(PerlCodeStyleSettings.class, "OPTIONAL_QUOTES", QUOTATION_OPTION_BEFORE_ARROW, QUOTATION_GROUP, PerlCodeStyleSettings.OptionalConstructions.OPTIONS_DEFAULT);
			consumer.showCustomOption(PerlCodeStyleSettings.class, "OPTIONAL_QUOTES_HASH_INDEX", QUOTATION_OPTION_HASH_INDEX, QUOTATION_GROUP, PerlCodeStyleSettings.OptionalConstructions.OPTIONS_DEFAULT);
			consumer.showCustomOption(PerlCodeStyleSettings.class, "OPTIONAL_QUOTES_HEREDOC_OPENER", QUOTATION_OPTION_HEREDOC_OPENER, QUOTATION_GROUP, PerlCodeStyleSettings.OptionalConstructions.OPTIONS_DEFAULT);

			consumer.showCustomOption(PerlCodeStyleSettings.class, "OPTIONAL_DEREFERENCE", DEREFERENCE_OPTION_BETWEEN_INDEXES, DEREFERENCE_GROUP, PerlCodeStyleSettings.OptionalConstructions.OPTIONS_DEFAULT);
			consumer.showCustomOption(PerlCodeStyleSettings.class, "OPTIONAL_DEREFERENCE_HASHREF_ELEMENT", DEREFERENCE_OPTION_HASHREF_ELEMENT, DEREFERENCE_GROUP, PerlCodeStyleSettings.OptionalConstructions.OPTIONS_HASHREF_ELEMENT);
			consumer.showCustomOption(PerlCodeStyleSettings.class, "OPTIONAL_DEREFERENCE_SIMPLE", DEREFERENCE_OPTION_SIMPLE, DEREFERENCE_GROUP, PerlCodeStyleSettings.OptionalConstructions.OPTIONS_SIMPLE_DEREF_STYLE);

			consumer.showCustomOption(PerlCodeStyleSettings.class, "OPTIONAL_PARENTHESES", PARENTHESES_OPTION_POSTFIX, PARENTHESES_GROUP, PerlCodeStyleSettings.OptionalConstructions.OPTIONS_DEFAULT);

//			consumer.showCustomOption(PerlCodeStyleSettings.class, "OPTIONAL_SEMI", PERL_OPTION_OPTIONAL_SEMI, OPTIONAL_ELEMENTS_GROUP, PerlCodeStyleSettings.OptionalConstructions.OPTIONS_DEFAULT);

			consumer.showCustomOption(PerlCodeStyleSettings.class, "MAIN_FORMAT", PERL_OPTION_MISC_MAIN, MISC_ELEMENTS_GROUP, PerlCodeStyleSettings.OptionalConstructions.OPTIONS_MAIN_FORMAT);
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
	public String getCodeSample(@NotNull SettingsType settingsType)
	{
		if (settingsType == SettingsType.SPACING_SETTINGS)
		{
			return SPACING_CODE_SAMPLE;
		}
		else if (settingsType == SettingsType.INDENT_SETTINGS)
		{
			return INDENT_CODE_SAMPLE;
		}
		else if (settingsType == SettingsType.LANGUAGE_SPECIFIC)
		{
			return LANGUAGE_SPECIFIC_CODE_SAMPLE;
		}
		return DEFAULT_CODE_SAMPLE;
	}
}
