/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.mason2;

import com.intellij.lang.Language;
import com.intellij.psi.templateLanguages.TemplateLanguage;

/**
 * Created by hurricup on 13.01.2016.
 */
public class Mason2TemplatingLanguage extends Language implements TemplateLanguage {
  public static final Mason2TemplatingLanguage INSTANCE = new Mason2TemplatingLanguage();
  public static final String NAME = "Mason2 Templating Language";

  public Mason2TemplatingLanguage() {
    super(Mason2Language.INSTANCE, NAME);
  }
}
