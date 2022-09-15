/*
 * Copyright 2015-2021 Alexandr Evstigneev
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

package com.perl5.lang.perl.intellilang;

import com.intellij.lang.Language;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.psi.PsiLanguageInjectionHost;
import org.intellij.plugins.intelliLang.inject.InjectedLanguage;
import org.intellij.plugins.intelliLang.inject.TemporaryPlacesRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerlTemporaryInjectedLanguageDetector {
  public @Nullable Language getTemporaryInjectedLanguage(@NotNull PsiLanguageInjectionHost host) {
    InjectedLanguage injectedLanguage = TemporaryPlacesRegistry.getInstance(host.getProject())
      .getLanguageFor(host, host.getContainingFile());

    return injectedLanguage == null ? null : injectedLanguage.getLanguage();
  }

  public static PerlTemporaryInjectedLanguageDetector getInstance() {
    return ApplicationManager.getApplication().getService(PerlTemporaryInjectedLanguageDetector.class);
  }
}
