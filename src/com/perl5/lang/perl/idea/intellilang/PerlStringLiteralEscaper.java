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

package com.perl5.lang.perl.idea.intellilang;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ElementManipulator;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.LiteralTextEscaper;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.idea.manipulators.PerlStringManipulator;
import com.perl5.lang.perl.lexer.PerlLexer;
import com.perl5.lang.perl.psi.mixins.PerlStringMixin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hurricup on 27.02.2016.
 */
public class PerlStringLiteralEscaper extends LiteralTextEscaper<PerlStringMixin> {
  private Map<Integer, Integer> offsetsMap;

  public PerlStringLiteralEscaper(@NotNull PerlStringMixin host) {
    super(host);
  }

  @Override
  public boolean decode(@NotNull TextRange rangeInsideHost, @NotNull StringBuilder outChars) {
    ElementManipulator<PerlStringMixin> manipulator = ElementManipulators.getNotNullManipulator(myHost);
    assert manipulator instanceof PerlStringManipulator;
    PsiElement openQuoteElement = ((PerlStringManipulator)manipulator).getOpeningQuote(myHost);
    char openQuote = openQuoteElement.getText().charAt(0);
    char closeQuote = PerlLexer.getQuoteCloseChar(openQuote);
    offsetsMap = new HashMap<>();
    CharSequence sourceText = rangeInsideHost.subSequence(myHost.getText());
    Integer sourceOffset = 0;
    Integer targetOffset = 0;
    Integer sourceLength = sourceText.length();
    boolean isEscaped = false;

    while (sourceOffset < sourceLength) {
      char currentChar = sourceText.charAt(sourceOffset);

      if (isEscaped) {
        if (currentChar != openQuote && currentChar != closeQuote) {
          assert sourceOffset > 0;
          outChars.append('\\');
          offsetsMap.put(targetOffset++, sourceOffset - 1);
        }
        outChars.append(currentChar);
        offsetsMap.put(targetOffset++, sourceOffset);
        isEscaped = false;
      }
      else if (currentChar == '\\') {
        isEscaped = true;
      }
      else {
        outChars.append(currentChar);
        offsetsMap.put(targetOffset++, sourceOffset);
      }

      sourceOffset++;
    }
    if (isEscaped) // end with escape, not sure if possible
    {
      assert sourceOffset > 0;
      outChars.append('\\');
      offsetsMap.put(targetOffset++, sourceOffset - 1);
    }

    offsetsMap.put(targetOffset, sourceOffset);    // end marker

    return true;
  }


  @Override
  public int getOffsetInHost(int offsetInDecoded, @NotNull TextRange rangeInsideHost) {
    Integer offsetInEncoded = offsetsMap.get(offsetInDecoded);
    assert offsetInEncoded != null : "Missing offset: " + offsetInDecoded +
                                     "; text: " + rangeInsideHost.subSequence(myHost.getText()) +
                                     "; range in host " + rangeInsideHost;

    return offsetInEncoded + rangeInsideHost.getStartOffset();
  }

  @Override
  public boolean isOneLine() {
    return false;
  }

  @NotNull
  @Override
  public TextRange getRelevantTextRange() {
    return ElementManipulators.getValueTextRange(myHost);
  }
}
