/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package com.perl5.lang.perl.parser.elementTypes;

import com.intellij.lexer.FlexAdapter;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.TokenType;
import com.perl5.lang.perl.lexer.PerlLexer;
import org.jetbrains.annotations.NotNull;

/**
 * Common parent for all quote-like operators available: stirngs, regexps, replacements, transliterations
 */
public abstract class PerlReparseableQuoteLikeElementType extends PerlLazyBlockElementType {
  public PerlReparseableQuoteLikeElementType(@NotNull String debugName,
                                             @NotNull Class<? extends PsiElement> clazz) {
    super(debugName, clazz);
  }

  protected static @NotNull FlexAdapter createLexer(@NotNull Project project) {
    return new FlexAdapter(new PerlLexer(null).withProject(project));
  }

  protected static void skipSpaces(@NotNull FlexAdapter flexAdapter) {
    while (flexAdapter.getTokenType() == TokenType.WHITE_SPACE) {
      flexAdapter.advance();
    }
  }

  @Override
  public String toString() {
    return "Perl5: " + super.toString();
  }
}