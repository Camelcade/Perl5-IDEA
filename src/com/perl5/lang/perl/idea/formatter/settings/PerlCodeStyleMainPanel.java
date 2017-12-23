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
import com.intellij.application.options.codeStyle.WrappingAndBracesPanel;
import com.intellij.lang.Language;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.PerlLanguage;

/**
 * Created by hurricup on 06.09.2015.
 */
public class PerlCodeStyleMainPanel extends TabbedLanguageCodeStylePanel {
  public PerlCodeStyleMainPanel(CodeStyleSettings currentSettings, CodeStyleSettings settings) {
    super(PerlLanguage.INSTANCE, currentSettings, settings);
  }

  @Override
  protected void addWrappingAndBracesTab(CodeStyleSettings settings) {
    addTab(new PerlWrappingAndBracesPanel(settings));
  }

  @Override
  protected void addBlankLinesTab(CodeStyleSettings settings) {
  }

  @Override
  protected void initTabs(CodeStyleSettings settings) {
    super.initTabs(settings);
    addTab(new PerlSpecificCodeStylePanel(settings));
  }

  protected class PerlSpecificCodeStylePanel extends WrappingAndBracesPanel {
    public PerlSpecificCodeStylePanel(CodeStyleSettings settings) {
      super(settings);
    }

    @Override
    protected String getTabTitle() {
      return PerlBundle.message("perl.formatting.tab.perl5");
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

  protected class PerlWrappingAndBracesPanel extends MyWrappingAndBracesPanel {
    public PerlWrappingAndBracesPanel(CodeStyleSettings settings) {
      super(settings);
    }

    @Override
    protected String getTabTitle() {
      return PerlBundle.message("perl.formatting.wrap.tab.name");
    }
  }

}