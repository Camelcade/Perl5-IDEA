/*
 * Copyright 2015-2023 Alexandr Evstigneev
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

package com.perl5.lang.mojolicious;

import com.intellij.lang.Language;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.templateLanguages.TemplateLanguage;
import com.perl5.lang.perl.PerlLanguage;


public class MojoliciousLanguage extends Language implements TemplateLanguage {
  public static final MojoliciousLanguage INSTANCE = new MojoliciousLanguage();
  public static final @NlsSafe String NAME = "Mojolicious Perl";

  private MojoliciousLanguage() {
    super(PerlLanguage.INSTANCE, NAME);
  }
}
