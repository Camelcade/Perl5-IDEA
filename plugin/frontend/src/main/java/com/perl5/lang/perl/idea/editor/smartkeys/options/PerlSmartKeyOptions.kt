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

import com.intellij.openapi.options.UiDslUnnamedConfigurable
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.bindSelected
import com.perl5.PerlBundle.message
import com.perl5.lang.perl.idea.codeInsight.Perl5CodeInsightSettings

class PerlSmartKeyOptions : UiDslUnnamedConfigurable.Simple() {

  override fun Panel.createContent() {
    val settings = Perl5CodeInsightSettings.getInstance()
    group(message("border.title.perl5")) {
      row {
        checkBox(message("perl.options.auto.heredoc"))
          .bindSelected(
            { settings.HEREDOC_AUTO_INSERTION },
            { value: Boolean? -> settings.HEREDOC_AUTO_INSERTION = value!! })
      }
      row {
        checkBox(message("perl.options.auto.colon"))
          .bindSelected(
            { settings.AUTO_INSERT_COLON },
            { value: Boolean? -> settings.AUTO_INSERT_COLON = value!! })
      }
      row {
        checkBox(message("perl.options.auto.brace.substitution.hex"))
          .bindSelected(
            { settings.AUTO_BRACE_HEX_SUBSTITUTION },
            { value: Boolean? -> settings.AUTO_BRACE_HEX_SUBSTITUTION = value!! })
      }
      row {
        checkBox(message("perl.options.auto.brace.substitution.oct"))
          .bindSelected(
            { settings.AUTO_BRACE_OCT_SUBSTITUTION },
            { value: Boolean? -> settings.AUTO_BRACE_OCT_SUBSTITUTION = value!! })
      }
      row {
        checkBox(message("perl.options.smart.comma.sequence"))
          .bindSelected(
            { settings.SMART_COMMA_SEQUENCE_TYPING },
            { value: Boolean? -> settings.SMART_COMMA_SEQUENCE_TYPING = value!! })
      }
    }
  }
}
