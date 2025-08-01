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

package com.perl5.lang.perl.idea.regexp;

import com.intellij.lang.Language;
import com.intellij.openapi.util.NlsSafe;
import org.intellij.lang.regexp.RegExpLanguage;


public class Perl5RegexpLanguage extends Language {
  public static final Perl5RegexpLanguage INSTANCE = new Perl5RegexpLanguage();
  public static final @NlsSafe String NAME = "Perl5Regexp";

  private Perl5RegexpLanguage() {
    super(RegExpLanguage.INSTANCE, NAME);
  }
}
