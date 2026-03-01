/*
 * Copyright 2015-2026 Alexandr Evstigneev
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
package com.perl5.lang.perl.idea.editor.smartkeys.options

import com.intellij.openapi.options.BeanConfigurable
import com.intellij.openapi.options.UnnamedConfigurable
import com.intellij.openapi.util.Setter
import com.intellij.ui.IdeBorderFactory
import com.perl5.PerlBundle.message
import com.perl5.lang.perl.idea.codeInsight.Perl5CodeInsightSettings
import javax.swing.JComponent

class PerlSmartKeyOptions : BeanConfigurable<Perl5CodeInsightSettings>(Perl5CodeInsightSettings.getInstance()), UnnamedConfigurable {
  init {
    val settings = Perl5CodeInsightSettings.getInstance()
    checkBox(
      message("perl.options.auto.heredoc"),
      { settings.HEREDOC_AUTO_INSERTION },
      Setter { value: Boolean? -> settings.HEREDOC_AUTO_INSERTION = value!! })
    checkBox(
      message("perl.options.auto.colon"),
      { settings.AUTO_INSERT_COLON },
      Setter { value: Boolean? -> settings.AUTO_INSERT_COLON = value!! })
    checkBox(
      message("perl.options.auto.brace.substitution.hex"),
      { settings.AUTO_BRACE_HEX_SUBSTITUTION },
      Setter { value: Boolean? -> settings.AUTO_BRACE_HEX_SUBSTITUTION = value!! })
    checkBox(
      message("perl.options.auto.brace.substitution.oct"),
      { settings.AUTO_BRACE_OCT_SUBSTITUTION },
      Setter { value: Boolean? -> settings.AUTO_BRACE_OCT_SUBSTITUTION = value!! })
    checkBox(
      message("perl.options.smart.comma.sequence"),
      { settings.SMART_COMMA_SEQUENCE_TYPING },
      Setter { value: Boolean? -> settings.SMART_COMMA_SEQUENCE_TYPING = value!! })
  }

  override fun createComponent(): JComponent {
    val panel = super.createComponent()
    panel.setBorder(
      IdeBorderFactory.PlainSmallWithIndent.createTitledBorder(null, message("border.title.perl5"), 0, 0, null, null)
    )

    return panel
  }
}
