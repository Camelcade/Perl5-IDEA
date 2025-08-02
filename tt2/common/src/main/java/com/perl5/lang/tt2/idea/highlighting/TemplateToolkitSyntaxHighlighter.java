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

package com.perl5.lang.tt2.idea.highlighting;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter;
import com.perl5.lang.tt2.lexer.TemplateToolkitLexerAdapter;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;
import static com.perl5.lang.tt2.parser.TemplateToolkitElementTypesGenerated.*;


public class TemplateToolkitSyntaxHighlighter extends SyntaxHighlighterBase {
  public static final TextAttributesKey TT2_NUMBER_KEY = createTextAttributesKey("PERL_TT2_NUMBER", PerlSyntaxHighlighter.PERL_NUMBER);
  public static final TextAttributesKey TT2_MARKER_KEY = createTextAttributesKey("PERL_TT2_MARKER", PerlSyntaxHighlighter.EMBED_MARKER_KEY);
  public static final TextAttributesKey TT2_COMMENT_KEY = createTextAttributesKey("PERL_TT2_COMMENT", PerlSyntaxHighlighter.PERL_COMMENT);
  public static final TextAttributesKey TT2_IDENTIFIER_KEY = createTextAttributesKey("PERL_TT2_IDENTIFIER", PerlSyntaxHighlighter.PERL_SUB);
  public static final TextAttributesKey TT2_KEYWORD_KEY = createTextAttributesKey("PERL_TT2_KEYWORD", PerlSyntaxHighlighter.PERL_KEYWORD);
  public static final TextAttributesKey TT2_OPERATOR_KEY =
    createTextAttributesKey("PERL_TT2_OPERATOR", PerlSyntaxHighlighter.PERL_OPERATOR);
  public static final TextAttributesKey TT2_SQ_STRING_KEY =
    createTextAttributesKey("PERL_TT2_SQ_STRING", PerlSyntaxHighlighter.PERL_SQ_STRING);
  public static final TextAttributesKey TT2_DQ_STRING_KEY =
    createTextAttributesKey("PERL_TT2_DQ_STRING", PerlSyntaxHighlighter.PERL_DQ_STRING);
  private static final TextAttributesKey[] TT2_NUMBER_KEYS = new TextAttributesKey[]{TT2_NUMBER_KEY};
  private static final TextAttributesKey[] TT2_MARKER_KEYS = new TextAttributesKey[]{TT2_MARKER_KEY};
  private static final TextAttributesKey[] TT2_COMMENT_KEYS = new TextAttributesKey[]{TT2_COMMENT_KEY};
  private final Project myProject;
  private final TokenSet myMarkers = TokenSet.create(
    TT2_OPEN_TAG,
    TT2_CLOSE_TAG,
    TT2_OUTLINE_TAG
  );

  public TemplateToolkitSyntaxHighlighter(Project project) {
    myProject = project;
  }

  @Override
  public @NotNull Lexer getHighlightingLexer() {
    return new TemplateToolkitHighlightingLexer(new TemplateToolkitLexerAdapter(myProject));
  }

  @Override
  public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
    if (myMarkers.contains(tokenType)) {
      return TT2_MARKER_KEYS;
    }
    else if (tokenType == TT2_NUMBER || tokenType == TT2_NUMBER_SIMPLE) {
      return TT2_NUMBER_KEYS;
    }
    else if (tokenType == LINE_COMMENT) {
      return TT2_COMMENT_KEYS;
    }
    return TextAttributesKey.EMPTY_ARRAY;
  }
}
