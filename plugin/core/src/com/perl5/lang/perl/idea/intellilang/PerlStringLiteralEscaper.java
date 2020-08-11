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

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.LiteralTextEscaper;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.lexer.PerlTokenSets;
import com.perl5.lang.perl.psi.PerlCharSubstitution;
import com.perl5.lang.perl.psi.mixins.PerlStringMixin;
import gnu.trove.TIntArrayList;
import org.jetbrains.annotations.NotNull;


public class PerlStringLiteralEscaper extends LiteralTextEscaper<PerlStringMixin> {
  private static final Logger LOG = Logger.getInstance(PerlStringLiteralEscaper.class);

  private final TIntArrayList myHostOffsets = new TIntArrayList();

  public PerlStringLiteralEscaper(@NotNull PerlStringMixin host) {
    super(host);
  }

  @Override
  public boolean decode(@NotNull TextRange rangeInsideHost, @NotNull StringBuilder outChars) {
    PsiElement openQuoteElement = myHost.getOpenQuoteElement();
    if (openQuoteElement == null) {
      LOG.error("No open quote for " + myHost);
      return false;
    }
    PsiElement run = openQuoteElement.getNextSibling();
    int startOffset = rangeInsideHost.getStartOffset();
    while (run != null && run.getTextRangeInParent().getStartOffset() < startOffset) {
      run = run.getNextSibling();
    }
    if (run == null) {
      return false;
    }

    int lastElementEnd = 0;
    int endOffset = rangeInsideHost.getEndOffset();
    while (run != null && run.getTextRangeInParent().getEndOffset() <= endOffset) {
      int startOffsetInParent = run.getStartOffsetInParent();
      IElementType runType = PsiUtilCore.getElementType(run);
      if (run instanceof PerlCharSubstitution) {
        lastElementEnd = run.getTextRangeInParent().getEndOffset();
        int point = ((PerlCharSubstitution)run).getCodePoint();
        char[] runChars = Character.toChars(point);
        outChars.append(runChars);
        for (int i = 0; i < runChars.length; i++) {
          myHostOffsets.add(startOffsetInParent + i);
        }
      }
      else if (PerlTokenSets.STRING_CONTENT_TOKENSET.contains(runType)) {
        lastElementEnd = run.getTextRangeInParent().getEndOffset();
        CharSequence runChars = run.getNode().getChars();
        outChars.append(runChars);
        for (int i = 0; i < runChars.length(); i++) {
          myHostOffsets.add(startOffsetInParent + i);
        }
      }
      run = run.getNextSibling();
    }
    myHostOffsets.add(lastElementEnd);

    return !myHostOffsets.isEmpty();
  }


  @Override
  public int getOffsetInHost(int offsetInDecoded, @NotNull TextRange rangeInsideHost) {
    return myHostOffsets.get(offsetInDecoded);
  }

  @Override
  public boolean isOneLine() {
    return false;
  }

  @Override
  public @NotNull TextRange getRelevantTextRange() {
    return ElementManipulators.getValueTextRange(myHost);
  }
}
