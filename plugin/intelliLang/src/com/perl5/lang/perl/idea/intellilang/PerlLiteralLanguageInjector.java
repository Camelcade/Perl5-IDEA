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

package com.perl5.lang.perl.idea.intellilang;


import com.intellij.lang.Language;
import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.psi.PerlVariable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.*;

/**
 * Common ancestor for heredocs and strings
 */
public abstract class PerlLiteralLanguageInjector implements MultiHostInjector {
  private static final TokenSet ELEMENTS_TO_SKIP = TokenSet.create(
    STRING_SPECIAL_FORMFEED, STRING_SPECIAL_BACKSPACE, STRING_SPECIAL_ALARM,
    STRING_SPECIAL_ESCAPE_CHAR,
    STRING_SPECIAL_LCFIRST, STRING_SPECIAL_TCFIRST, STRING_SPECIAL_LOWERCASE_START,
    STRING_SPECIAL_UPPERCASE_START, STRING_SPECIAL_FOLDCASE_START,
    STRING_SPECIAL_QUOTE_START, STRING_SPECIAL_MODIFIER_END
  );
  private static final TokenSet ELEMENTS_TO_REPLACE_WITH_DUMMY = TokenSet.create(
    STRING_SPECIAL_ESCAPE, STRING_SPECIAL_BACKREF,
    SCALAR_VARIABLE, SCALAR_CAST_EXPR, ARRAY_ELEMENT, HASH_ELEMENT, DEREF_EXPR,
    ARRAY_VARIABLE, ARRAY_INDEX_VARIABLE, ARRAY_ELEMENT, ARRAY_SLICE, HASH_ARRAY_SLICE
  );

  private static final Logger LOG = Logger.getInstance(PerlLiteralLanguageInjector.class);

  protected void injectLanguageIntoPsiRange(@NotNull PsiElement firstElement,
                                            @Nullable PsiElement stopElement,
                                            @NotNull MultiHostRegistrar registrar,
                                            @NotNull Language targetLanguage) {
    registrar.startInjecting(targetLanguage);
    PsiElement parent = firstElement.getParent();
    if (!(parent instanceof PsiLanguageInjectionHost)) {
      LOG.error("Failed attempt to inject: parent: " + parent + "; firstElement: " + firstElement + "; stopElement: " + stopElement);
      return;
    }
    PsiLanguageInjectionHost injectionHost = (PsiLanguageInjectionHost)parent;

    PsiElement run = firstElement;
    int startOffset = -1;
    while (true) {
      IElementType elementType = PsiUtilCore.getElementType(run);
      if (ELEMENTS_TO_SKIP.contains(elementType)) {
        if (startOffset >= 0) {
          TextRange injectionRange = TextRange.create(startOffset, run.getTextRangeInParent().getStartOffset());
          registrar.addPlace(null, null, injectionHost, injectionRange);
          startOffset = -1;
        }
      }
      else if (ELEMENTS_TO_REPLACE_WITH_DUMMY.contains(elementType)) {
        if (startOffset >= 0) {
          TextRange injectionRange = TextRange.create(startOffset, run.getTextRangeInParent().getStartOffset());
          registrar.addPlace(null, buildSuffix(run), injectionHost, injectionRange);
          startOffset = -1;
        }
      }
      else if (startOffset < 0) {
        startOffset = run.getStartOffsetInParent();
      }

      PsiElement nextSibling = run.getNextSibling();
      if (nextSibling == stopElement) {
        break;
      }
      run = nextSibling;
    }
    if (startOffset >= 0) {
      registrar.addPlace(null, null, injectionHost, TextRange.create(startOffset, run.getTextRangeInParent().getEndOffset()));
    }

    registrar.doneInjecting();
  }

  /**
   * @return a text suffix for replacing node {@code elementToReplace} to be replaced. E.g. {@code $somevar} => {@code somevar}
   */
  private @NotNull String buildSuffix(@NotNull PsiElement elementToReplace) {
    if (elementToReplace instanceof PerlVariable) {
      return ((PerlVariable)elementToReplace).getName();
    }
    return "perl_expression";
  }
}
