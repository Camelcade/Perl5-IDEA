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

package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.*;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.idea.intellilang.PerlHeredocLiteralEscaper;
import com.perl5.lang.perl.psi.PerlHeredocTerminatorElement;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.HEREDOC_END_INDENTABLE;

/**
 * Created by hurricup on 10.06.2015.
 */
public class PerlHeredocElementImpl extends PerlCompositeElementImpl implements PsiLanguageInjectionHost {
  public PerlHeredocElementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public boolean isValidHost() {
    return PerlSharedSettings.getInstance(getProject()).ALLOW_INJECTIONS_WITH_INTERPOLATION || getChildren().length == 0;
  }

  @Override
  public PsiLanguageInjectionHost updateText(@NotNull final String text) {
    return ElementManipulators.handleContentChange(this, text);
  }


  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiPerlVisitor) {
      ((PerlVisitor)visitor).visitHeredocElement(this);
    }
    else {
      super.accept(visitor);
    }
  }

  @NotNull
  @Override
  public LiteralTextEscaper<PerlHeredocElementImpl> createLiteralTextEscaper() {
    return new PerlHeredocLiteralEscaper(this);
  }

  public boolean isIndentable() {
    return PsiUtilCore.getElementType(getTerminatorElement()) == HEREDOC_END_INDENTABLE;
  }

  @Nullable
  public PerlHeredocTerminatorElement getTerminatorElement() {
    return CachedValuesManager.getCachedValue(this, () -> {
      PsiElement terminator = getNextSibling();
      while (terminator instanceof PsiWhiteSpace) {
        terminator = terminator.getNextSibling();
      }
      return CachedValueProvider.Result.create(
        terminator instanceof PerlHeredocTerminatorElementImpl ? (PerlHeredocTerminatorElement)terminator : null,
        this
      );
    });
  }

  public int getIndentSize() {
    if (!isIndentable()) {
      return 0;
    }

    PerlHeredocTerminatorElement terminatorElement = getTerminatorElement();
    if (terminatorElement == null) {
      return 0;
    }

    return terminatorElement.getTextRange().getStartOffset() - getTextRange().getEndOffset();
  }

  /**
   * @return real indentaion of heredoc. Handles cases of improperly formatted heredocs, where content is less indented
   * than terminator; returns 0 for unindentable heredocs
   */
  public int getRealIndentSize() {
    int explicitIndentSize = getIndentSize();
    if (explicitIndentSize == 0) {
      return 0;
    }
    return calcRealIndent(getNode().getChars(), explicitIndentSize);
  }

  /**
   * Calculates indentation for a buffer
   *
   * @param buffer         buffer with heredoc text
   * @param explicitIndent explicit indent, defined by terminator indentation
   * @return min of explicit or implicit indents
   */
  public static int calcRealIndent(@NotNull CharSequence buffer, int explicitIndent) {
    int result = explicitIndent;
    int currentOffset = 0;
    int bufferLength = buffer.length();
    int lineIndentSize = 0;
    while (currentOffset < bufferLength) {
      char currentChar = buffer.charAt(currentOffset);
      if (currentChar == '\n') {
        lineIndentSize = 0;
        currentOffset++;
      }
      else if (Character.isWhitespace(currentChar)) {
        lineIndentSize++;
        currentOffset++;
      }
      else {
        if (lineIndentSize == 0) {
          return 0;
        }
        result = Integer.min(lineIndentSize, result);
        while (currentOffset < bufferLength && buffer.charAt(currentOffset) != '\n') {
          currentOffset++;
        }
        lineIndentSize = 0;
      }
    }

    return result;
  }

}
