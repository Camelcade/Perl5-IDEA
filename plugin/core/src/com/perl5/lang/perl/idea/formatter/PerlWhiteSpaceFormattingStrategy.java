/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.formatter;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.formatter.AbstractWhiteSpaceFormattingStrategy;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import org.jetbrains.annotations.NotNull;


public class PerlWhiteSpaceFormattingStrategy extends AbstractWhiteSpaceFormattingStrategy {

  @NotNull
  @Override
  public CharSequence adjustWhiteSpaceIfNecessary(@NotNull CharSequence whiteSpaceText,
                                                  @NotNull CharSequence text,
                                                  int startOffset,
                                                  int endOffset,
                                                  CodeStyleSettings codeStyleSettings,
                                                  ASTNode nodeAfter) {
    if (nodeAfter == null) {
      return super.adjustWhiteSpaceIfNecessary(whiteSpaceText, text, startOffset, endOffset, codeStyleSettings, null);
    }

    PsiElement psi = nodeAfter.getPsi();
    if (!(psi instanceof PerlHeredocElementImpl && ((PerlHeredocElementImpl)psi).isIndentable())) {
      return super.adjustWhiteSpaceIfNecessary(whiteSpaceText, text, startOffset, endOffset, codeStyleSettings, nodeAfter);
    }


    return super.adjustWhiteSpaceIfNecessary(whiteSpaceText, text, startOffset, endOffset, codeStyleSettings, nodeAfter);
    //return whiteSpaceText;
  }

  @Override
  public int check(@NotNull CharSequence text, int start, int end) {
    for (int i = start; i < end; i++) {
      if (!Character.isWhitespace(text.charAt(i))) {
        return i;
      }
    }
    return end;
  }
}
