/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

import com.intellij.application.options.TabbedLanguageCodeStylePanel;
import com.intellij.application.options.codeStyle.CodeStyleSpacesPanel;
import com.intellij.application.options.codeStyle.WrappingAndBracesPanel;
import com.intellij.lang.Language;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider;
import com.perl5.lang.perl.PerlLanguage;

/**
 * Created by hurricup on 06.09.2015.
 */
public class PerlCodeStyleMainPanel extends TabbedLanguageCodeStylePanel implements PerlCodeStyleOptionNames {
  public PerlCodeStyleMainPanel(CodeStyleSettings currentSettings, CodeStyleSettings settings) {
    super(PerlLanguage.INSTANCE, currentSettings, settings);
  }

  protected void addSpacesTab(CodeStyleSettings settings) {
    addTab(new PerlCodeStyleSpacesPanel(settings));
  }

  @Override
  protected void addWrappingAndBracesTab(CodeStyleSettings settings) {
    super.addWrappingAndBracesTab(settings);
  }

  @Override
  protected void addBlankLinesTab(CodeStyleSettings settings) {
    //		super.addBlankLinesTab(settings);
    addTab(new PerlSpecificCodeStylePanel(settings));
  }

  protected class PerlSpecificCodeStylePanel extends WrappingAndBracesPanel {
    public PerlSpecificCodeStylePanel(CodeStyleSettings settings) {
      super(settings);
    }

    @Override
    protected String getTabTitle() {
      return TAB_PERL_SETTINGS;
    }

    @Override
    public Language getDefaultLanguage() {
      return PerlCodeStyleMainPanel.this.getDefaultLanguage();
    }

    @Override
    public LanguageCodeStyleSettingsProvider.SettingsType getSettingsType() {
      return LanguageCodeStyleSettingsProvider.SettingsType.LANGUAGE_SPECIFIC;
    }
  }

  protected class PerlCodeStyleSpacesPanel extends CodeStyleSpacesPanel {
    public PerlCodeStyleSpacesPanel(CodeStyleSettings settings) {
      super(settings);
    }

    @Override
    protected boolean shouldHideOptions() {
      return true;
    }

    @Override
    public Language getDefaultLanguage() {
      return PerlCodeStyleMainPanel.this.getDefaultLanguage();
    }

    @Override
    protected void initTables() {
      initCustomOptions(SPACE_GROUP_AFTER_KEYWORD);
      initCustomOptions(SPACE_GROUP_ANON_ARRAY);
      initCustomOptions(SPACE_GROUP_ANON_HASH);
      super.initTables();
    }
  }
}