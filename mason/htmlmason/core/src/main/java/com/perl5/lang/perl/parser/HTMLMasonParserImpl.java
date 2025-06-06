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

package com.perl5.lang.perl.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.WhitespacesBinders;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.htmlmason.HtmlMasonBundle;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes.*;
import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.*;


@SuppressWarnings("Duplicates")
public class HTMLMasonParserImpl extends PerlParserImpl implements HTMLMasonParser {
  protected static final TokenSet BAD_CHARACTER_FORBIDDEN_TOKENS = TokenSet.orSet(
    PerlParserTokenSets.BAD_CHARACTER_FORBIDDEN_TOKENS, TokenSet.create(
      HTML_MASON_ONCE_CLOSER,
      HTML_MASON_SHARED_CLOSER,
      HTML_MASON_CLEANUP_CLOSER,
      HTML_MASON_INIT_CLOSER,
      HTML_MASON_PERL_CLOSER,
      HTML_MASON_ATTR_CLOSER,
      HTML_MASON_ARGS_CLOSER,
      HTML_MASON_FILTER_CLOSER,
      HTML_MASON_FLAGS_CLOSER,
      HTML_MASON_CALL_CLOSE_TAG_START,

      HTML_MASON_METHOD_CLOSER,
      HTML_MASON_DEF_CLOSER
    ));

  protected static final TokenSet UNCONSUMABLE_SEMI_TOKENS = TokenSet.orSet(
    PerlParserTokenSets.UNCONSUMABLE_SEMI_TOKENS,
    TokenSet.create(
      HTML_MASON_FILTER_CLOSER
    )
  );

  @Override
  public @NotNull TokenSet getUnconsumableSemicolonTokens() {
    return UNCONSUMABLE_SEMI_TOKENS;
  }

  @Override
  public @NotNull TokenSet getBadCharacterForbiddenTokens() {
    return BAD_CHARACTER_FORBIDDEN_TOKENS;
  }

  @Override
  public boolean parseStatement(PsiBuilder b, int l) {
    IElementType tokenType = b.getTokenType();

    boolean r = false;
    PsiBuilder.Marker m = b.mark();

    if (tokenType == HTML_MASON_BLOCK_OPENER) {
      PsiBuilder.Marker statementMarker = b.mark();
      b.advanceLexer();
      if (PerlParserProxy.expr(b, l, -1)) {
        // parseStatement filter
        if (GeneratedParserUtilBase.consumeToken(b, HTML_MASON_EXPR_FILTER_PIPE)) {
          while ((tokenType = b.getTokenType()) == HTML_MASON_DEFAULT_ESCAPER_NAME || tokenType == HTML_MASON_ESCAPER_NAME) {
            b.advanceLexer();

            while (GeneratedParserUtilBase.consumeToken(b, HTML_MASON_DEFAULT_ESCAPER_NAME)) {
            }

            if (!GeneratedParserUtilBase.consumeToken(b, COMMA)) {
              break;
            }
          }
        }
      }
      if (r = MasonParserUtil.endOrRecover(b, HTML_MASON_BLOCK_CLOSER)) {
        statementMarker.done(STATEMENT);
      }
    }
    else if (tokenType == HTML_MASON_CALL_OPENER) {
      PsiBuilder.Marker statementMarker = b.mark();
      b.advanceLexer();
      PerlParserProxy.expr(b, l, -1);
      if (r = MasonParserUtil.endOrRecover(b, HTML_MASON_CALL_CLOSER)) {
        statementMarker.done(HTML_MASON_CALL_STATEMENT);
      }
    }
    else if (tokenType == HTML_MASON_CALL_FILTERING_OPENER) {
      PsiBuilder.Marker statementMarker = b.mark();
      b.advanceLexer();
      PerlParserProxy.expr(b, l, -1);

      if (MasonParserUtil.endOrRecover(b, HTML_MASON_CALL_CLOSER_UNMATCHED)) {
        statementMarker.done(HTML_MASON_CALL_STATEMENT);
      }

      PsiBuilder.Marker blockMarker = b.mark();
      PerlParserProxy.block_content(b, l);

      if (b.getTokenType() != HTML_MASON_CALL_CLOSE_TAG_START) // need recover
      {
        while (!b.eof() && b.getTokenType() != HTML_MASON_CALL_CLOSE_TAG_START) {
          b.advanceLexer();
        }
      }

      blockMarker.done(HTML_MASON_FILTERED_BLOCK);
      blockMarker.setCustomEdgeTokenBinders(WhitespacesBinders.GREEDY_LEFT_BINDER, WhitespacesBinders.GREEDY_RIGHT_BINDER);

      if (b.eof()) {
        b.mark().error(HtmlMasonBundle.message("parsing.error.unclosed.filtering.block"));
      }
      else {
        PsiBuilder.Marker tagMarker = b.mark();
        if (GeneratedParserUtilBase.consumeToken(b, HTML_MASON_CALL_CLOSE_TAG_START)) {
          string_bare(b, l);
          if (!GeneratedParserUtilBase.consumeToken(b, HTML_MASON_TAG_CLOSER)) {
            b.mark().error(HtmlMasonBundle.message("parsing.error.incomplete.close.tag"));
          }
          tagMarker.done(HTML_MASON_CALL_CLOSE_TAG);
        }
        else {
          tagMarker.drop();
        }
      }
      r = true;
    }
    else if (tokenType == HTML_MASON_ONCE_OPENER) {
      r = MasonParserUtil.parsePerlBlock(b, l, HTML_MASON_ONCE_CLOSER, HTML_MASON_ONCE_BLOCK);
    }
    else if (tokenType == HTML_MASON_SHARED_OPENER) {
      r = MasonParserUtil.parsePerlBlock(b, l, HTML_MASON_SHARED_CLOSER, HTML_MASON_SHARED_BLOCK);
    }
    else if (tokenType == HTML_MASON_CLEANUP_OPENER) {
      r = MasonParserUtil.parsePerlBlock(b, l, HTML_MASON_CLEANUP_CLOSER, HTML_MASON_CLEANUP_BLOCK);
    }
    else if (tokenType == HTML_MASON_INIT_OPENER) {
      r = MasonParserUtil.parsePerlBlock(b, l, HTML_MASON_INIT_CLOSER, HTML_MASON_INIT_BLOCK);
    }
    else if (tokenType == HTML_MASON_FILTER_OPENER) {
      r = MasonParserUtil.parsePerlBlock(b, l, HTML_MASON_FILTER_CLOSER, HTML_MASON_FILTER_BLOCK);
    }
    else if (tokenType == HTML_MASON_ARGS_OPENER) {
      r = parseArgsBlock(b, l);
    }
    else if (tokenType == HTML_MASON_FLAGS_OPENER) {
      PsiBuilder.Marker statementMarker = b.mark();
      b.advanceLexer();

      while (!b.eof() && b.getTokenType() != HTML_MASON_FLAGS_CLOSER) {
        if (!PerlParserProxy.expr(b, l, -1)) {
          break;
        }
      }
      r = MasonParserUtil.endOrRecover(b, HTML_MASON_FLAGS_CLOSER);
      statementMarker.done(HTML_MASON_FLAGS_STATEMENT);
    }
    else if (tokenType == HTML_MASON_ATTR_OPENER) {
      PsiBuilder.Marker statementMarker = b.mark();
      b.advanceLexer();

      while (!b.eof() && b.getTokenType() != HTML_MASON_ATTR_CLOSER) {
        if (!PerlParserProxy.expr(b, l, -1) && !parseHardNewline(b)) {
          break;
        }
      }

      r = MasonParserUtil.endOrRecover(b, HTML_MASON_ATTR_CLOSER);
      statementMarker.done(HTML_MASON_ATTR_BLOCK);
    }
    else if (tokenType == HTML_MASON_DOC_OPENER) {
      b.advanceLexer();
      GeneratedParserUtilBase.consumeToken(b, COMMENT_BLOCK);
      r = MasonParserUtil.endOrRecover(b, HTML_MASON_DOC_CLOSER);
    }
    else if (tokenType == HTML_MASON_TEXT_OPENER) {
      PsiBuilder.Marker stringMarker = b.mark();
      b.advanceLexer();
      GeneratedParserUtilBase.consumeToken(b, STRING_CONTENT);
      r = MasonParserUtil.endOrRecover(b, HTML_MASON_TEXT_CLOSER);
      stringMarker.done(HTML_MASON_TEXT_BLOCK);
    }
    else if (tokenType == HTML_MASON_METHOD_OPENER) {
      r = parseMasonNamedBlock(b, l, HTML_MASON_METHOD_CLOSER, HTML_MASON_METHOD_DEFINITION);
    }
    else if (tokenType == HTML_MASON_DEF_OPENER) {
      r = parseMasonNamedBlock(b, l, HTML_MASON_DEF_CLOSER, HTML_MASON_SUBCOMPONENT_DEFINITION);
    }

    if (r) {
      m.drop();
    }
    else {
      m.rollbackTo();
    }

    return r || super.parseStatement(b, l);
  }

  public static boolean parseArgsBlock(PsiBuilder b, int l) {
    boolean r;
    PsiBuilder.Marker innerMarker = b.mark();

    if (GeneratedParserUtilBase.consumeToken(b, HTML_MASON_ARGS_OPENER)) {
      while (parseArgument(b, l) || parseHardNewline(b)) {
      }
    }

    r = MasonParserUtil.endOrRecover(b, HTML_MASON_ARGS_CLOSER);

    innerMarker.done(HTML_MASON_ARGS_BLOCK);

    return r;
  }

  public static boolean parseHardNewline(PsiBuilder b) {
    if (b.getTokenType() == HTML_MASON_HARD_NEWLINE) {
      PsiBuilder.Marker m = b.mark();
      b.advanceLexer();
      m.collapse(TokenType.WHITE_SPACE);
      return true;
    }
    return false;
  }

  public static boolean parseArgument(PsiBuilder b, int l) {
    boolean r = variable_declaration_element(b, l);
    if (r && GeneratedParserUtilBase.consumeToken(b, FAT_COMMA)) {
      r = expr(b, l, -1);
    }
    return r;
  }

  public static boolean parseMasonNamedBlock(PsiBuilder b, int l, IElementType closeToken, IElementType statementTokenType) {
    boolean r = false;

    PsiBuilder.Marker methodMarker = b.mark();
    b.advanceLexer();
    PsiBuilder.Marker subMarker = b.mark();
    if (GeneratedParserUtilBase.consumeToken(b, IDENTIFIER)) {
      subMarker.collapse(SUB_NAME);
      if (GeneratedParserUtilBase.consumeToken(b, HTML_MASON_TAG_CLOSER)) {
        PsiBuilder.Marker blockMarker = b.mark();
        PerlParserProxy.block_content(b, l);

        if (b.getTokenType() == closeToken) {
          blockMarker.done(HTML_MASON_BLOCK);
          blockMarker.setCustomEdgeTokenBinders(WhitespacesBinders.GREEDY_LEFT_BINDER, WhitespacesBinders.GREEDY_RIGHT_BINDER);
          b.advanceLexer();
          methodMarker.done(statementTokenType);
          r = true;
        }
      }
    }

    if (!r) {
      methodMarker.rollbackTo();
    }

    return r || MasonParserUtil.recoverToGreedy(b, closeToken, HtmlMasonBundle.message("parsing.error.error"));
  }
}
