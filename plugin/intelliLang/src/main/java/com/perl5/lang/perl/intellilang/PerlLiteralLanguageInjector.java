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
import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.lexer.PerlTokenSets;
import com.perl5.lang.perl.psi.PerlCharSubstitution;
import com.perl5.lang.perl.psi.PerlVariable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.*;

/**
 * Common ancestor for heredocs and strings
 */
public abstract class PerlLiteralLanguageInjector implements MultiHostInjector {
  private static final TokenSet ELEMENTS_TO_IGNORE = TokenSet.create(
    STRING_SPECIAL_LCFIRST, STRING_SPECIAL_TCFIRST, STRING_SPECIAL_LOWERCASE_START,
    STRING_SPECIAL_UPPERCASE_START, STRING_SPECIAL_FOLDCASE_START,
    STRING_SPECIAL_QUOTE_START, STRING_SPECIAL_MODIFIER_END
  );

  private static final TokenSet ELEMENTS_TO_REPLACE_WITH_DUMMY = TokenSet.orSet(
    PerlTokenSets.STRING_CHAR_UNRENDERABLE_ALIASES, TokenSet.create(
      STRING_SPECIAL_BACKREF,
      SCALAR_VARIABLE, ARRAY_VARIABLE, ARRAY_INDEX_VARIABLE,
      SCALAR_CAST_EXPR, ARRAY_CAST_EXPR,
      ARRAY_ELEMENT, HASH_ELEMENT,
      DEREF_EXPR,
      ARRAY_SLICE, HASH_SLICE, HASH_ARRAY_SLICE
    ));

  private static final Logger LOG = Logger.getInstance(PerlLiteralLanguageInjector.class);

  protected void injectLanguageIntoPsiRange(@NotNull PsiElement firstElement,
                                            @Nullable PsiElement stopElement,
                                            @NotNull MultiHostRegistrar registrar,
                                            @NotNull Language targetLanguage) {
    PsiElement parent = firstElement.getParent();
    if (!(parent instanceof PsiLanguageInjectionHost)) {
      LOG.error("Failed attempt to inject: parent: " + parent + "; firstElement: " + firstElement + "; stopElement: " + stopElement);
      return;
    }

    List<Descriptor> descriptors = collectDescriptors(firstElement, stopElement);
    if (descriptors.isEmpty() || descriptors.size() == 1 && !descriptors.get(0).inject) {
      return;
    }
    injectDescriptors((PsiLanguageInjectionHost)parent, registrar, targetLanguage, descriptors);
  }

  private void injectDescriptors(@NotNull PsiLanguageInjectionHost injectionHost,
                                 @NotNull MultiHostRegistrar registrar,
                                 @NotNull Language targetLanguage,
                                 @NotNull List<Descriptor> descriptors) {
    registrar.startInjecting(targetLanguage);
    Iterator<Descriptor> iterator = descriptors.iterator();
    String prefix = null;
    while (iterator.hasNext()) {
      Descriptor descriptor = iterator.next();
      if (descriptor.inject) {
        String suffix = iterator.hasNext() ? iterator.next().text.toString() : null;
        registrar.addPlace(prefix, suffix, injectionHost, descriptor.getRange());
        prefix = null;
      }
      else {
        LOG.assertTrue(prefix == null);
        prefix = descriptor.text.toString();
      }
    }

    registrar.doneInjecting();
  }

  /**
   * Iterates literal's children starting from {@code run} till the {@code stopElement} and classifying ranges to inject or not and
   * prepares prefix/suffix texts
   */
  private @NotNull List<Descriptor> collectDescriptors(@NotNull PsiElement run, @Nullable PsiElement stopElement) {
    List<Descriptor> descriptors = new ArrayList<>();
    Descriptor currentDescriptor = null;
    while (true) {
      IElementType elementType = PsiUtilCore.getElementType(run);
      boolean shouldIgnore = ELEMENTS_TO_IGNORE.contains(elementType);
      boolean shouldReplace = !shouldIgnore && ELEMENTS_TO_REPLACE_WITH_DUMMY.contains(elementType);
      if (!shouldIgnore && !shouldReplace && run instanceof PerlCharSubstitution) {
        var codePoint = ((PerlCharSubstitution)run).getCodePoint();
        shouldReplace = !Character.isValidCodePoint(codePoint);
      }

      if (shouldIgnore || shouldReplace) {
        if (currentDescriptor != null && !currentDescriptor.inject) {
          currentDescriptor.endOffset = run.getTextRangeInParent().getEndOffset();
        }
        else {
          currentDescriptor = new Descriptor(run.getTextRangeInParent(), false);
          descriptors.add(currentDescriptor);
        }

        if (shouldReplace) {
          currentDescriptor.text.append(buildReplacement(run));
        }
      }
      else {
        if (currentDescriptor != null && currentDescriptor.inject) {
          currentDescriptor.endOffset = run.getTextRangeInParent().getEndOffset();
        }
        else {
          currentDescriptor = new Descriptor(run.getTextRangeInParent(), true);
          descriptors.add(currentDescriptor);
        }
      }

      PsiElement nextSibling = run.getNextSibling();
      if (nextSibling == stopElement) {
        break;
      }
      run = nextSibling;
    }
    return descriptors;
  }

  /**
   * @return a text suffix for replacing node {@code elementToReplace} to be replaced. E.g. {@code $somevar} => {@code somevar}
   */
  private @NotNull String buildReplacement(@NotNull PsiElement elementToReplace) {
    if (elementToReplace instanceof PerlVariable) {
      return ((PerlVariable)elementToReplace).getName();
    }
    else if (PerlTokenSets.STRING_CHAR_UNRENDERABLE_ALIASES.contains(PsiUtilCore.getElementType(elementToReplace))) {
      return " ";
    }
    else if (elementToReplace instanceof PerlCharSubstitution) {
      return "*";
    }
    return "perl_expression";
  }

  private static class Descriptor {
    int startOffset;
    int endOffset;
    final boolean inject;
    final StringBuilder text = new StringBuilder();

    public Descriptor(@NotNull TextRange textRange, boolean inject) {
      startOffset = textRange.getStartOffset();
      endOffset = textRange.getEndOffset();
      this.inject = inject;
    }

    @NotNull TextRange getRange() {
      return TextRange.create(startOffset, endOffset);
    }
  }
}
