/*
 * Copyright 2015 Alexandr Evstigneev
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
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.LiteralTextEscaper;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Created by hurricup on 02.08.2015.
 */
public class PerlHeredocLiteralEscaper extends LiteralTextEscaper<PerlHeredocElementImpl> {
  private Map<TextRange, TextRange> myRangesMap = new THashMap<>();

  public PerlHeredocLiteralEscaper(PerlHeredocElementImpl host) {
    super(host);
  }

  @Override
  public boolean isOneLine() {
    return false;
  }

  @Override
  public boolean decode(@NotNull TextRange rangeInsideHost, @NotNull StringBuilder outChars) {
    myRangesMap.clear();
    return getRealIndentSize() > 0 ? decodeIndented(rangeInsideHost, outChars) : decodeNormal(rangeInsideHost, outChars);
  }

  private boolean decodeNormal(@NotNull TextRange rangeInsideHost, @NotNull StringBuilder outChars) {
    outChars.append(rangeInsideHost.subSequence(myHost.getNode().getChars()));
    TextRange fullRange = TextRange.create(0, rangeInsideHost.getLength() + 1);
    myRangesMap.put(fullRange, fullRange);
    return true;
  }

  private boolean decodeIndented(@NotNull TextRange rangeInsideHost, @NotNull StringBuilder outChars) {
    int indentSize = getRealIndentSize();
    CharSequence sourceText = rangeInsideHost.subSequence(myHost.getNode().getChars());

    int currentLineIndent = 0;
    int sourceOffset = 0;
    int targetOffset = 0;

    int sourceLength = sourceText.length();
    while (sourceOffset < sourceLength) {
      char currentChar = sourceText.charAt(sourceOffset);
      if (currentChar == '\n') {
        outChars.append(currentChar);
        myRangesMap.put(TextRange.from(targetOffset++, 1), TextRange.from(sourceOffset, 1));
        currentLineIndent = 0;
      }
      else if (Character.isWhitespace(currentChar) && currentLineIndent < indentSize) {
        currentLineIndent++;
      }
      else {
        // got non-space or indents ended, consume till the EOL or EOHost
        int sourceEnd = sourceOffset;
        while (sourceEnd < sourceLength) {
          currentChar = sourceText.charAt(sourceEnd);
          sourceEnd++;
          if (currentChar == '\n') {
            break;
          }
        }

        outChars.append(sourceText.subSequence(sourceOffset, sourceEnd));

        TextRange sourceRange = TextRange.create(sourceOffset, sourceEnd);
        int rangeSize = sourceEnd - sourceOffset;
        TextRange targetRange = TextRange.from(targetOffset, rangeSize);
        targetOffset += rangeSize;
        myRangesMap.put(targetRange, sourceRange);
        sourceOffset = sourceEnd;
        currentLineIndent = 0;
        continue;
      }
      sourceOffset++;
    }
    myRangesMap.put(TextRange.from(targetOffset, 1), TextRange.from(sourceOffset, 1)); // close marker
    return true;
  }

  private int getRealIndentSize() {
    int result = myHost.getIndentSize();
    CharSequence buffer = myHost.getNode().getChars();

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

  @Override
  public int getOffsetInHost(int offsetInDecoded, @NotNull final TextRange rangeInsideHost) {
    for (TextRange decodedRange : myRangesMap.keySet()) {
      if (decodedRange.contains(offsetInDecoded)) {
        TextRange encodedRange = myRangesMap.get(decodedRange);
        return rangeInsideHost.getStartOffset() + encodedRange.getStartOffset() + offsetInDecoded - decodedRange.getStartOffset();
      }
    }

    throw new RuntimeException("Missing offset: " + offsetInDecoded +
                               "; text: " + rangeInsideHost.subSequence(myHost.getText()) +
                               "; range in host " + rangeInsideHost);
  }

  @NotNull
  @Override
  public TextRange getRelevantTextRange() {
    return ElementManipulators.getManipulator(myHost).getRangeInElement(myHost);
  }
}
