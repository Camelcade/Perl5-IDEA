/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.editor.smartkeys.options;

import com.intellij.openapi.options.BeanConfigurable;
import com.intellij.openapi.options.UnnamedConfigurable;
import com.intellij.ui.IdeBorderFactory;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.codeInsight.Perl5CodeInsightSettings;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;


public class PerlSmartKeyOptions extends BeanConfigurable<Perl5CodeInsightSettings> implements UnnamedConfigurable {
  public PerlSmartKeyOptions() {
    super(Perl5CodeInsightSettings.getInstance());
    Perl5CodeInsightSettings settings = Perl5CodeInsightSettings.getInstance();
    checkBox(PerlBundle.message("perl.options.auto.heredoc"),
             () -> settings.HEREDOC_AUTO_INSERTION,
             value -> settings.HEREDOC_AUTO_INSERTION = value);
    checkBox(PerlBundle.message("perl.options.auto.colon"),
             () -> settings.AUTO_INSERT_COLON,
             value -> settings.AUTO_INSERT_COLON = value);
    checkBox(PerlBundle.message("perl.options.auto.brace.substitution.hex"),
             () -> settings.AUTO_BRACE_HEX_SUBSTITUTION,
             value -> settings.AUTO_BRACE_HEX_SUBSTITUTION = value);
    checkBox(PerlBundle.message("perl.options.auto.brace.substitution.oct"),
             () -> settings.AUTO_BRACE_OCT_SUBSTITUTION,
             value -> settings.AUTO_BRACE_OCT_SUBSTITUTION = value);
    checkBox(PerlBundle.message("perl.options.smart.comma.sequence"),
             () -> settings.SMART_COMMA_SEQUENCE_TYPING,
             value -> settings.SMART_COMMA_SEQUENCE_TYPING = value);
  }

  @Override
  public @NotNull JComponent createComponent() {
    JComponent panel = super.createComponent();
    if (panel != null) {
      panel.setBorder(
        IdeBorderFactory.PlainSmallWithIndent.createTitledBorder(null, PerlBundle.message("border.title.perl5"), 0, 0, null, null));
    }

    return panel;
  }
}
