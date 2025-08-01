/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package com.perl5.lang.mojolicious.idea.highlighter;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.mojolicious.lexer.MojoliciousLexerAdapter;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighterEmbedded;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.mojolicious.MojoliciousElementTypes.*;
import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.RESERVED_IF;


public class MojoliciousSyntaxHighlighter extends PerlSyntaxHighlighterEmbedded {
  public static final TokenSet MARKER_TOKENS = TokenSet.create(
    MOJO_BLOCK_OPENER,
    MOJO_BLOCK_EXPR_OPENER,
    MOJO_BLOCK_EXPR_ESCAPED_OPENER,

    MOJO_BLOCK_CLOSER,
    MOJO_BLOCK_NOSPACE_CLOSER,
    MOJO_BLOCK_CLOSER_SEMI,
    MOJO_BLOCK_EXPR_NOSPACE_CLOSER,
    MOJO_BLOCK_EXPR_CLOSER,

    MOJO_LINE_OPENER,
    MOJO_LINE_EXPR_OPENER,
    MOJO_LINE_EXPR_ESCAPED_OPENER,

    MOJO_BLOCK_OPENER_TAG,
    MOJO_LINE_OPENER_TAG
  );

  public MojoliciousSyntaxHighlighter(Project project) {
    super(project);
  }

  @Override
  public TokenSet getMarkersTokenSet() {
    return MARKER_TOKENS;
  }

  @Override
  public @NotNull Lexer getHighlightingLexer() {
    return new MojoliciousLexerAdapter(myProject, true);
  }

  @Override
  public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
    if (tokenType == MOJO_BEGIN || tokenType == MOJO_END) {
      tokenType = RESERVED_IF;
    }
    return super.getTokenHighlights(tokenType);
  }
}
