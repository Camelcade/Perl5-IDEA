/*
 * Copyright 2015-2023 Alexandr Evstigneev
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

package com.perl5.lang.tt2.idea.highlighting;

import com.intellij.lexer.Lexer;
import com.intellij.lexer.MergeFunction;
import com.intellij.lexer.MergingLexerAdapterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.tt2.elementTypes.TemplateToolkitElementTypes.TT2_PERL_CODE;
import static com.perl5.lang.tt2.elementTypes.TemplateToolkitElementTypes.TT2_RAWPERL_CODE;
import static com.perl5.lang.tt2.parser.TemplateToolkitElementTypesGenerated.*;

public class TemplateToolkitHighlightingLexer extends MergingLexerAdapterBase {
  private IElementType myPreviousType = null;
  private IElementType myPreviousPreviousType = null;

  public TemplateToolkitHighlightingLexer(Lexer original) {
    super(original);
  }

  @Override
  public MergeFunction getMergeFunction() {
    return (type, originalLexer) -> {
      if (myPreviousType == TT2_CLOSE_TAG) {
        if (myPreviousPreviousType == TT2_PERL) {
          type = collapseTo(TT2_PERL_CODE, originalLexer);
        }
        else if (myPreviousPreviousType == TT2_RAWPERL) {
          type = collapseTo(TT2_RAWPERL_CODE, originalLexer);
        }
      }
      return registerElement(type);
    };
  }

  private @NotNull IElementType collapseTo(@NotNull IElementType targetType, @NotNull Lexer originalLexer) {
    while (true) {
      var currentTokenType = originalLexer.getTokenType();
      if (currentTokenType == null || currentTokenType == TT2_OPEN_TAG || currentTokenType == TT2_OUTLINE_TAG) {
        break;
      }
      originalLexer.advance();
    }
    return targetType;
  }

  @Contract("null -> null; !null -> !null")
  private @Nullable IElementType registerElement(@Nullable IElementType elementType) {
    if (elementType != TokenType.WHITE_SPACE) {
      myPreviousPreviousType = myPreviousType;
      myPreviousType = elementType;
    }
    return elementType;
  }
}
