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
public class PerlLanguageCodeStyleSettingsProvider extends LanguageCodeStyleSettingsProvider
{
	private static final String DEFAULT_CODE_SAMPLE = "";
	private static final String INDENT_CODE_SAMPLE = "";
	private static final String BLANK_LINES_CODE_SAMPLE = "";

	@Override
	public void customizeSettings(@NotNull CodeStyleSettingsCustomizable consumer, @NotNull SettingsType settingsType)
	{
		consumer.showCustomOption(PerlCodeStyleSettings.class, "OPTIONAL_QUOTES", "Optional quotes", null, PerlCodeStyleSettings.OptionalConstructions.OPTIONS);
		consumer.showCustomOption(PerlCodeStyleSettings.class, "OPTIONAL_DEREFERENCE", "Optional dereferences", null, PerlCodeStyleSettings.OptionalConstructions.OPTIONS);
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
		indentOptions.INDENT_SIZE = 2;
		indentOptions.CONTINUATION_INDENT_SIZE = 4;
		indentOptions.TAB_SIZE = 2;

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
