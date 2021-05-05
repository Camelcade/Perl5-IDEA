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

package com.perl5.lang.perl.idea.intellilang;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.util.Consumer;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.lexer.PerlAnnotations;
import com.perl5.lang.perl.psi.PerlCompositeElement;
import org.intellij.plugins.intelliLang.inject.AbstractLanguageInjectionSupport;
import org.intellij.plugins.intelliLang.inject.config.BaseInjection;
import org.jetbrains.annotations.NotNull;

public class PerlInjectionSupport extends AbstractLanguageInjectionSupport {
  private static final Class<?>[] PATTERNS_CLASSES = new Class[]{PerlInjectionPatterns.class};

  @Override
  public @NotNull String getId() {
    return "perl5";
  }

  @Override
  public boolean isApplicableTo(PsiLanguageInjectionHost host) {
    return host instanceof PerlCompositeElement && !PerlAnnotations.isInjectionSuppressed(host);
  }

  @Override
  public boolean useDefaultInjector(PsiLanguageInjectionHost host) {
    return true;
  }

  @Override
  public @NotNull Class<?> @NotNull [] getPatternClasses() {
    return PATTERNS_CLASSES;
  }

  @Override
  public AnAction[] createAddActions(Project project, Consumer<? super BaseInjection> consumer) {
    var defaultAddAction = createDefaultAddAction(project, consumer, this);
    var templatePresentation = defaultAddAction.getTemplatePresentation();
    templatePresentation.setIcon(PerlIcons.PERL_LANGUAGE_ICON);
    templatePresentation.setText(PerlIntelliLangBundle.message("settings.action.text"));
    return new AnAction[]{defaultAddAction};
  }
}
