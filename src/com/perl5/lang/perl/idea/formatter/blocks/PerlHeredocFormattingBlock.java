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

package com.perl5.lang.perl.idea.formatter.blocks;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.lang.ASTNode;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.util.TextRange;
import com.perl5.lang.perl.idea.formatter.PerlFormattingContext;
import com.perl5.lang.perl.idea.formatter.PerlInjectedLanguageBlocksBuilder;
import com.perl5.lang.perl.psi.PerlHeredocTerminatorElement;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by hurricup on 20.05.2017.
 */
public class PerlHeredocFormattingBlock extends PerlFormattingBlock {


  public PerlHeredocFormattingBlock(@NotNull ASTNode node,
                                    @Nullable Alignment alignment,
                                    @NotNull PerlFormattingContext context
  ) {
    super(node, alignment, context);
    assert node.getPsi() instanceof PerlHeredocElementImpl : "Got " + node + "instead of heredoc.";
  }

  @NotNull
  private PerlHeredocElementImpl getPsi() {
    return (PerlHeredocElementImpl)myNode.getPsi();
  }

  @Override
  public boolean isLeaf() {
    PerlHeredocElementImpl psi = getPsi();
    return InjectedLanguageManager.getInstance(psi.getProject()).getInjectedPsiFiles(psi) == null;
  }

  @NotNull
  @Override
  protected List<Block> buildSubBlocks() {
    return PerlInjectedLanguageBlocksBuilder.compute(myContext.getSettings().getRootSettings(), getNode(), getTextRange());
  }

  @NotNull
  @Override
  public TextRange getTextRange() {
    TextRange originalRange = super.getTextRange();
    if (originalRange.isEmpty()) {
      return originalRange;
    }

    TextRange trimmedRange = TextRange.create(originalRange.getStartOffset(), originalRange.getEndOffset() - 1);

    PerlHeredocElementImpl heredocElement = getPsi();
    if (!heredocElement.isIndentable()) {
      return trimmedRange;
    }

    PerlHeredocTerminatorElement terminatorElement = heredocElement.getTerminatorElement();
    if (terminatorElement == null) {
      return trimmedRange;
    }

    // perlop: Tabs and spaces can be mixed, but are matched exactly. One tab will not be equal to 8 spaces!
    int spacesBeforeTerminator = terminatorElement.getTextRange().getStartOffset() - trimmedRange.getEndOffset() - 1;
    if (spacesBeforeTerminator == 0) {
      return trimmedRange;
    }

    CharSequence nodeChars = trimmedRange.shiftRight(-myNode.getStartOffset()).subSequence(myNode.getChars());
    int effectiveCharsLength = nodeChars.length();
    int realSpaces;
    int offset = 0;

    OUTER:
    while (offset < effectiveCharsLength) {
      for (realSpaces = 0; realSpaces <= spacesBeforeTerminator && offset < effectiveCharsLength; realSpaces++) {

        if (realSpaces == spacesBeforeTerminator) {
          break OUTER;
        }

        char currentChar = nodeChars.charAt(offset);
        if (currentChar == '\n') {
          offset++;
          break;
        }
        else if (!Character.isWhitespace(currentChar)) {
          break OUTER;
        }
        else {
          offset++;
        }
      }
    }
    return TextRange.create(trimmedRange.getStartOffset() + offset, trimmedRange.getEndOffset());
  }
}
