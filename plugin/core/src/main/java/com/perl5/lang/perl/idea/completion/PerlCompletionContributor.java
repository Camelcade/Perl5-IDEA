/*
 * Copyright 2015-2022 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.idea.completion.providers.*;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.PerlTokenSets;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.idea.PerlElementPatterns.*;


public class PerlCompletionContributor extends CompletionContributor implements PerlElementTypes {
  private static final Logger LOG = Logger.getInstance(PerlCompletionContributor.class);

  public PerlCompletionContributor() {
    extend(
      CompletionType.BASIC,
      STRING_CONTENT_PATTERN,
      new PerlStringContentCompletionProvider()
    );

    extend(
      CompletionType.BASIC,
      LABEL_PATTERN,
      new PerlLabelCompletionProvider()
    );

    extend(
      CompletionType.BASIC,
      USE_VERSION_PATTERN,
      new PerlVersionCompletionProvider()
    );

    extend(
      CompletionType.BASIC,
      NAMESPACE_NAME_PATTERN,
      new PerlPackageCompletionProvider()
    );

    extend(
      CompletionType.BASIC,
      SUB_NAME_PATTERN,
      new PerlSubNameElementCompletionProvider()
    );

    extend(
      CompletionType.BASIC,
      HANDLE_PATTERN,
      new PerlHandleCompletionProvider()
    );

    extend(
      CompletionType.BASIC,
      VARIABLE_NAME_PATTERN,
      new PerlVariableNameCompletionProvider()
    );

    // refactored
    extend(
      CompletionType.BASIC,
      SUB_NAME_PATTERN.and(IN_STATIC_METHOD_PATTERN),
      new PerlSubCallCompletionProvider()
    );

    // refactored
    extend(
      CompletionType.BASIC,
      SUB_NAME_PATTERN.and(IN_OBJECT_METHOD_PATTERN),
      new PerlSubCallCompletionProvider()
    );

    // refactored, adds packages when it's appropriate
    extend(
      CompletionType.BASIC,
      SUB_NAME_PATTERN.inside(METHOD_PATTERN),
      new PerlPackageSubCompletionProvider()
    );

    // refactored
    extend(
      CompletionType.BASIC,
      UNKNOWN_ANNOTATION_PATTERN,
      new PerlAnnotationCompletionProvider()
    );

    extend(
      CompletionType.BASIC,
      STRING_CHAR_NAME_PATTERN,
      new PerlUnicodeNamesCompletionProvider()
    );

    extend(
      CompletionType.BASIC,
      ANNOTATION_VARIABLE_NAME_PATTERN,
      new PerlAnnotationVariablesCompletionProvider()
    );

    // this is really heavy, so should be last
    extend(
      CompletionType.BASIC,
      SUB_NAME_PATTERN,
      new PerlVariableCompletionProvider()
    );
  }

  @Override
  public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
    long start = System.currentTimeMillis();
    super.fillCompletionVariants(parameters, result);
    LOG.debug("Completion finished in " + (System.currentTimeMillis() - start));
  }

  @Override
  public void beforeCompletion(@NotNull CompletionInitializationContext context) {
    adjustDummyIdentifier(context);
    adjustIdentifierEndOffset(context);
    super.beforeCompletion(context);
  }

  private void adjustDummyIdentifier(@NotNull CompletionInitializationContext context) {
    var offset = context.getCaret().getOffset();
    if (offset < 7) {
      return;
    }
    var editor = context.getEditor();
    var iterator = editor.getHighlighter().createIterator(offset - 1);
    if (iterator.getTokenType() == OPERATOR_CONCAT) {
      iterator.retreat();
    }
    if (iterator.getTokenType() == NUMBER_VERSION) {
      context.setDummyIdentifier("1");
    }
  }

  private void adjustIdentifierEndOffset(@NotNull CompletionInitializationContext context) {
    PsiElement elementAtCaret = context.getFile().findElementAt(context.getEditor().getCaretModel().getOffset());
    IElementType elementType = PsiUtilCore.getElementType(elementAtCaret);
    if (PerlTokenSets.VARIABLE_NAMES.contains(elementType) &&
        elementAtCaret.getTextLength() == 1) {
      char nameChar = elementAtCaret.getNode().getChars().charAt(0);
      if (nameChar != '_' && nameChar != '^' && !Character.isLetterOrDigit(nameChar)) {
        context.setReplacementOffset(elementAtCaret.getTextOffset());
      }
    }
    else if (elementType == STRING_CHAR_NAME) {
      context.setReplacementOffset(elementAtCaret.getTextRange().getEndOffset());
    }
  }
}
