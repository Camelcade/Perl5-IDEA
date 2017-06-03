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

package com.perl5.lang.perl.psi.mixins;

import com.intellij.lang.ASTNode;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.LiteralTextEscaper;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.idea.configuration.settings.PerlLocalSettings;
import com.perl5.lang.perl.idea.regexp.Perl5RegexpLiteralEscaper;
import com.perl5.lang.perl.psi.PsiPerlReplacementRegex;
import com.perl5.lang.perl.psi.impl.PerlCompositeElementImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 30.11.2016.
 */
public class Perl5RegexpMixin extends PerlCompositeElementImpl implements PsiLanguageInjectionHost {
  public Perl5RegexpMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public boolean isValidHost() {
    return PerlLocalSettings.getInstance(getProject()).ENABLE_REGEX_INJECTIONS && isMatchRegexp();
  }

  @Override
  public PsiLanguageInjectionHost updateText(@NotNull String text) {
    return ElementManipulators.handleContentChange(this, text);
  }

  @NotNull
  @Override
  public LiteralTextEscaper<? extends PsiLanguageInjectionHost> createLiteralTextEscaper() {
    return new Perl5RegexpLiteralEscaper(this);
  }

  /**
   * @return true if it's a match part, false if replace one
   */
  public boolean isMatchRegexp() {
    PsiElement parent = getParent();
    if (!(parent instanceof PsiPerlReplacementRegex)) {
      return true;
    }
    Perl5RegexpMixin[] regexps = PsiTreeUtil.getChildrenOfType(parent, Perl5RegexpMixin.class);
    return regexps != null && regexps.length > 0 && regexps[0] == this;
  }
}
