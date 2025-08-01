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

package com.perl5.lang.tt2;

import com.intellij.lang.Language;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.templateLanguages.TemplateLanguage;


public class TemplateToolkitLanguage extends Language implements TemplateLanguage {
  public static final Language INSTANCE = new TemplateToolkitLanguage();
  public static final @NlsSafe String NAME = "Template Toolkit 2";

  private TemplateToolkitLanguage() {
    super(NAME);
  }

  @Override
  public boolean isCaseSensitive() {
    return true;
  }
}
