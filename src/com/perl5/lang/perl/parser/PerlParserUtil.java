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

package com.perl5.lang.perl.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.lang.WhitespacesAndCommentsBinder;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageProcessor;
import com.perl5.lang.perl.idea.EP.PerlPackageProcessorEP;
import com.perl5.lang.perl.lexer.PerlBaseLexer;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.PerlTokenSets;
import com.perl5.lang.perl.parser.builder.PerlBuilder;
import org.jetbrains.annotations.NotNull;

import static com.intellij.lang.WhitespacesBinders.GREEDY_LEFT_BINDER;

/**
 * Created by hurricup on 01.05.2015.
 */
public class PerlParserUtil extends GeneratedParserUtilBase implements PerlElementTypes {


  public static final TokenSet VERSION_TOKENS = TokenSet.create(
    NUMBER,
    NUMBER_SIMPLE,
    NUMBER_VERSION
  );
  public static final TokenSet OPEN_QUOTES = TokenSet.create(
    QUOTE_DOUBLE_OPEN,
    QUOTE_TICK_OPEN,
    QUOTE_SINGLE_OPEN
  );
  public static final TokenSet CLOSE_QUOTES = TokenSet.create(
    QUOTE_DOUBLE_CLOSE,
    QUOTE_TICK_CLOSE,
    QUOTE_SINGLE_CLOSE
  );
  private static WhitespacesAndCommentsBinder NAMESPACE_RIGHT_BINDER = (tokens, atStreamEdge, getter) -> {
    int result = tokens.size();
    if (atStreamEdge || tokens.isEmpty()) {
      return result;
    }

    for (int i = tokens.size() - 1; i >= 0; i--) {
      IElementType currentToken = tokens.get(i);
      if (currentToken == COMMENT_ANNOTATION || currentToken == COMMENT_LINE) {
        result = i;
      }
      else if (currentToken != TokenType.WHITE_SPACE && currentToken != TokenType.NEW_LINE_INDENT) {
        break;
      }
    }

    return result;
  };

  /**
   * Wrapper for Builder class in order to implement additional per parser information in PerlBuilder
   *
   * @param root        root element
   * @param builder     psibuilder
   * @param parser      psiparser
   * @param extendsSets extends sets
   * @return PerlBuilder
   */
  public static PsiBuilder adapt_builder_(IElementType root, PsiBuilder builder, PsiParser parser, TokenSet[] extendsSets) {
    ErrorState state = new ErrorState();
    ErrorState.initState(state, builder, root, extendsSets);
    //		builder.setDebugMode(true);
    return new PerlBuilder(builder, state, parser);
  }


  /**
   * Smart parser for ->, makes }->[ optional
   *
   * @param b PerlBuilder
   * @param l parsing level
   * @return parsing result
   */
  public static boolean parseArrowSmart(PsiBuilder b, @SuppressWarnings("unused") int l) {
    IElementType tokenType = b.getTokenType();
    if (b.getTokenType() == OPERATOR_DEREFERENCE) {
      return consumeToken(b, OPERATOR_DEREFERENCE);
    }
    else {
      assert b instanceof PerlBuilder;
      PerlTokenData prevToken = ((PerlBuilder)b).lookupToken(-1);
      IElementType prevTokenType = prevToken == null ? null : prevToken.getTokenType();

      // optional }->[ or ]->{
      if (
        (prevTokenType == RIGHT_BRACE || prevTokenType == RIGHT_BRACKET)
        && (tokenType == LEFT_BRACE || tokenType == LEFT_BRACKET || tokenType == LEFT_PAREN)
        ) {
        return true;
      }
    }

    return false;
  }

  public static boolean parseExpressionLevel(PsiBuilder b, int l, int g) {
    return PerlParserImpl.expr(b, l, g);
  }


  /**
   * Smart semi checker decides if we need semi here
   *
   * @param b Perl builder
   * @param l Parsing level
   * @return checking result
   */
  public static boolean statementSemi(PsiBuilder b, int l) {
    return ((PerlBuilder)b).getPerlParser().parseStatementSemi(b, l);
  }


  protected static boolean isOperatorToken(PsiBuilder b, @SuppressWarnings("unused") int l) {
    return PerlTokenSets.OPERATORS_TOKENSET.contains(b.getTokenType());
  }


  /**
   * Statement recovery function. Should not consume token, only check;
   *
   * @param b PerlBuilder
   * @param l parsing level
   * @return parsing result
   */
  public static boolean recoverStatement(PsiBuilder b, @SuppressWarnings("unused") int l) {
    assert b instanceof PerlBuilder;
    IElementType currentTokenType = b.getTokenType();

    return !(
      currentTokenType == null ||                                                                                 // got end of file
      !((PerlBuilder)b).getPerlParser().getStatementRecoveryConsumableTokenSet().contains(currentTokenType)
    );
  }

  /**
   * Parses statement modifier and rolls back if it looks like compound
   *
   * @param b PerlBuilder
   * @param l parsing level
   * @return check result
   */
  @SuppressWarnings("UnusedReturnValue")
  public static boolean parseStatementModifier(PsiBuilder b, int l) {
    assert b instanceof PerlBuilder;
    if (((PerlBuilder)b).getPerlParser().parseStatementModifier(b, l)) {
      return true;
    }

    PsiBuilder.Marker m = b.mark();

    if (PerlParserImpl.statement_modifier(b, l)) {
      IElementType tokenType = b.getTokenType();
      if (((PerlBuilder)b).getPerlParser().getConsumableSemicolonTokens().contains(tokenType) ||
          ((PerlBuilder)b).getPerlParser().getUnconsumableSemicolonTokens().contains(tokenType))    // we accepts only strict modifiers;
      {
        m.drop();
        return true;
      }
      else    // we suppose that it's compound
      {
        m.rollbackTo();
        return false;
      }
    }
    else {
      m.drop();
      return false;
    }
  }

  /**
   * Checks for version token and convert if necessary
   *
   * @param b PerlBuilder
   * @param l parsing level
   * @return parsing result
   */
  public static boolean parsePerlVersion(PsiBuilder b, @SuppressWarnings("unused") int l) {
    if (VERSION_TOKENS.contains(b.getTokenType())) {
      PsiBuilder.Marker m = b.mark();
      b.advanceLexer();
      m.collapse(VERSION_ELEMENT);
      return true;
    }
    return false;
  }

  /**
   * Parses comma sequence with trailing comma support
   *
   * @param b PerlBuilder
   * @param l parsing level
   * @return parsing result
   */
  public static boolean parseCommaSequence(PsiBuilder b, int l) {
    boolean r = false;
    while (true) {
      if (consumeToken(b, COMMA) || consumeToken(b, FAT_COMMA))    // got comma
      {
        r = true;

        // consume sequential commas
        while (true) {
          if (!(consumeToken(b, COMMA) || consumeToken(b, FAT_COMMA))) {
            break;
          }
        }

        if (!PerlParserImpl.expr(b, l, 4))    // looks like an end
        {
          break;
        }
      }
      else {
        break;
      }
    }
    return r;
  }

  /**
   * Parses SQ string with optional conversion to the use_vars lp string
   *
   * @param b PerlBuilder
   * @param l parsing level
   * @return parsing result
   */
  public static boolean mapUseVars(PsiBuilder b, int l, Parser parser) {
    PsiBuilder.Marker m = b.mark();

    boolean r = parser.parse(b, l);

    // fixme prepend last done marker
    if (r) {
      m.collapse(PARSABLE_STRING_USE_VARS);
    }
    else {
      m.drop();
    }

    return r;
  }

  // fixme replace with looking to upper frames ?
  public static boolean isUseVars(PsiBuilder b, @SuppressWarnings("unused") int l) {
    return ((PerlBuilder)b).isUseVarsContent();
  }

  /**
   * Consuming unexpected token
   *
   * @param b perlbuilder
   * @param l parsing level
   * @return true
   **/
  public static boolean parseBadCharacters(PsiBuilder b, @SuppressWarnings("unused") int l) {
    IElementType tokenType = b.getTokenType();

    if (tokenType == null || ((PerlBuilder)b).getPerlParser().getBadCharacterForbiddenTokens().contains(tokenType)) {
      return false;
    }

    PsiBuilder.Marker m = b.mark();
    b.advanceLexer();

    if (tokenType == TokenType.BAD_CHARACTER) {
      while (b.getTokenType() == TokenType.BAD_CHARACTER) {
        b.advanceLexer();
      }
      m.error("Unexpected tokens, plugin currently supports only ASCII identifiers");
    }
    else if (tokenType == RIGHT_PAREN) {
      m.error("Unopened closing parenthesis");
    }
    else if (tokenType == RIGHT_BRACKET) {
      m.error("Unopened closing bracket");
    }
    else {
      m.error("Unexpected token");
    }

    return true;
  }

  /**
   * Checks if anon hash has proper suffix
   *
   * @param b PerlBuilder
   * @param l parsing level
   * @return chack result.
   */
  public static boolean validateAnonHashSuffix(PsiBuilder b, int l) {
    IElementType tokenType = b.getTokenType();
    if (tokenType == null || ((PerlBuilder)b).getPerlParser().getAnonHashSuffixTokens().contains(tokenType)) {
      return true;
    }
    else {
      PsiBuilder.Marker m = b.mark();
      boolean r = PerlParserImpl.statement_modifier(b, l);
      r = r && (b.getTokenType() != LEFT_BRACE);
      m.rollbackTo();
      return r;
    }
  }

  /**
   * Parses and wraps declaration of scalar variable; NB: special variable names suppressed
   *
   * @param b Perl builder
   * @param l parsing level
   * @return check result
   */
  public static boolean scalarDeclarationWrapper(PsiBuilder b, int l) {
    PsiBuilder.Marker m = b.mark();
    boolean r = false;
    assert b instanceof PerlBuilder;
    boolean flagBackup = ((PerlBuilder)b).setSpecialVariableNamesAllowed(false);

    if (PerlParserImpl.scalar_variable(b, l)) {
      m.done(VARIABLE_DECLARATION_ELEMENT);
      r = true;
    }
    else {
      m.drop();
    }
    ((PerlBuilder)b).setSpecialVariableNamesAllowed(flagBackup);
    return r;
  }

  /**
   * attempting to parse statement using parserExtensions
   *
   * @param b PerlBuilder
   * @param l parsing level
   * @return parsing result
   */
  public static boolean parseParserExtensionStatement(PsiBuilder b, int l) {
    assert b instanceof PerlBuilder;
    return ((PerlBuilder)b).getPerlParser().parseStatement(b, l);
  }

  /**
   * attempting to parse term using parserExtensions
   *
   * @param b PerlBuilder
   * @param l parsing level
   * @return parsing result
   */
  public static boolean parseParserExtensionTerm(PsiBuilder b, int l) {
    assert b instanceof PerlBuilder;
    return ((PerlBuilder)b).getPerlParser().parseTerm(b, l);
  }

  public static boolean parseFileContent(PsiBuilder b, int l) {
    assert b instanceof PerlBuilder;
    return ((PerlBuilder)b).getPerlParser().parseFileContents(b, l);
  }

  public static boolean parseNestedElementVariation(PsiBuilder b, int l) {
    assert b instanceof PerlBuilder;
    return ((PerlBuilder)b).getPerlParser().parseNestedElementVariation(b, l);
  }

  public static boolean checkSemicolon(PsiBuilder b, @SuppressWarnings("unused") int l) {
    return ((PerlBuilder)b).getPerlParser().getConsumableSemicolonTokens().contains(b.getTokenType());
  }

  public static boolean parseSemicolon(PsiBuilder b, int l) {
    if (checkSemicolon(b, l)) {
      b.advanceLexer();
      return true;
    }
    return false;
  }

  public static boolean parseNamespaceContent(PsiBuilder b, int l) {
    PsiBuilder.Marker m = b.mark();
    if (PerlParserGenerated.real_namespace_content(b, l)) {
      m.done(NAMESPACE_CONTENT);
      m.setCustomEdgeTokenBinders(GREEDY_LEFT_BINDER, NAMESPACE_RIGHT_BINDER);
      return true;
    }
    m.rollbackTo();
    return false;
  }

  public static boolean parseLabelDeclaration(PsiBuilder b, @SuppressWarnings("unused") int l) {
    if (b.lookAhead(1) == COLON && b.getTokenType() != RESERVED_SUB) {
      String tokenText = b.getTokenText();
      if (tokenText != null && PerlBaseLexer.IDENTIFIER_PATTERN.matcher(tokenText).matches()) {
        b.advanceLexer();
        b.advanceLexer();
        return true;
      }
    }
    return false;
  }

  /**
   * Parses leftward or rightward call with custom sub token
   *
   * @param b builder
   * @param l level
   * @return result
   */
  public static boolean parseCustomCallExpr(PsiBuilder b, int l, Parser tokenParser) {
    PsiBuilder.Marker m = b.mark();
    if (PerlParserImpl.leftward_call(b, l, tokenParser) || PerlParserImpl.rightward_call(b, l, tokenParser)) {
      m.done(SUB_CALL_EXPR);
      return true;
    }
    m.rollbackTo();
    return false;
  }

  /**
   * Parses use parameters with package processor if it's possible. If not, uses default parsing logic.
   */
  public static boolean parseUseParameters(@NotNull PsiBuilder b, int l, @NotNull Parser defaultParser) {
    if (b.getTokenType() == PACKAGE) {
      String packageName = b.getTokenText();
      if (StringUtil.isEmpty(packageName)) {
        return false;
      }
      PerlPackageProcessor packageProcessor = PerlPackageProcessorEP.EP.findSingle(packageName);
      if (packageProcessor != null) {
        assert b instanceof PerlBuilder;
        PsiBuilder.Marker m = b.mark();
        if (packageProcessor.parseUseParameters((PerlBuilder)b, l)) {
          m.drop();
          return true;
        }
        m.rollbackTo();
      }
    }
    return defaultParser.parse(b, l);
  }

  /**
   * Helper method to pass package [version] in use statement
   */
  public static boolean passPackageAndVersion(@NotNull PerlBuilder b, int l) {
    assert PerlParserUtil.consumeTokenFast(b, PACKAGE);
    PerlParserImpl.perl_version(b, l);
    return true;
  }
}
