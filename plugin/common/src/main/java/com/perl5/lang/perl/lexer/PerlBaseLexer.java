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

package com.perl5.lang.perl.lexer;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.Trinity;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.extensions.parser.PerlParserExtension;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.idea.project.PerlNamesCache;
import com.perl5.lang.perl.parser.PerlParserImpl;
import com.perl5.lang.perl.psi.PerlString;
import com.perl5.lang.perl.psi.references.PerlImplicitDeclarationsService;
import com.perl5.lang.perl.util.PerlPackageUtilCore;
import com.perl5.lang.perl.util.PerlPluginUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Pattern;

import static com.perl5.lang.perl.lexer.PerlLexer.*;
import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.*;
import static com.perl5.lang.pod.parser.psi.PodSyntaxElements.CUT_COMMAND_WITH_LEADING_NEWLINE;


public abstract class PerlBaseLexer extends PerlProtoLexer {
  private static final Logger LOG = Logger.getInstance(PerlBaseLexer.class);

  private static final Pattern USE_TRYCATCH_PATTERN = Pattern.compile("use\\s+TryCatch");
  private Boolean myHasTryCatch = null;
  // this is used to lex single-quoted strings.
  private char mySingleOpenQuoteChar = 0;
  private char mySingleCloseQuoteChar = 0;

  private static final Map<IElementType, String> ALLOWED_REGEXP_MODIFIERS = Map.of(
    RESERVED_S, "nmsixpodualgcer",
    RESERVED_M, "nmsixpodualgc",
    RESERVED_QR, "nmsixpodual"
  );
  public static final String ALLOWED_TR_MODIFIERS = "cdsr";
  private static final List<IElementType> DQ_TOKENS = Arrays.asList(QUOTE_DOUBLE_OPEN, LP_STRING_QQ, QUOTE_DOUBLE_CLOSE, STRING_CONTENT_QQ);
  private static final List<IElementType> SQ_TOKENS = Arrays.asList(QUOTE_SINGLE_OPEN, LP_STRING_Q, QUOTE_SINGLE_CLOSE, STRING_CONTENT);
  private static final List<IElementType> XQ_TOKENS = Arrays.asList(QUOTE_TICK_OPEN, LP_STRING_QX, QUOTE_TICK_CLOSE, STRING_CONTENT_XQ);
  private static final List<IElementType> QW_TOKENS = Arrays.asList(QUOTE_SINGLE_OPEN, LP_STRING_QW, QUOTE_SINGLE_CLOSE);
  private static final List<IElementType> GLOB_TOKENS = Arrays.asList(LEFT_ANGLE, LP_STRING_QQ, RIGHT_ANGLE, STRING_CONTENT_QQ);

  private static final Map<IElementType, Trinity<IElementType, IElementType, IElementType>> SIGILS_TO_TOKENS_MAP = Map.of(
    SIGIL_SCALAR, Trinity.create(LEFT_BRACE_SCALAR, SCALAR_NAME, RIGHT_BRACE_SCALAR),
    SIGIL_SCALAR_INDEX, Trinity.create(LEFT_BRACE_SCALAR, SCALAR_NAME, RIGHT_BRACE_SCALAR),
    SIGIL_ARRAY, Trinity.create(LEFT_BRACE_ARRAY, ARRAY_NAME, RIGHT_BRACE_ARRAY),
    SIGIL_HASH, Trinity.create(LEFT_BRACE_HASH, HASH_NAME, RIGHT_BRACE_HASH),
    SIGIL_CODE, Trinity.create(LEFT_BRACE_CODE, SUB_NAME, RIGHT_BRACE_CODE),
    SIGIL_GLOB, Trinity.create(LEFT_BRACE_GLOB, GLOB_NAME, RIGHT_BRACE_GLOB)
  );

  protected static final String SUB_SIGNATURE = "Sub.Signature";

  private static final AtomicNotNullLazyValue<Boolean> ourListenersInitializer = AtomicNotNullLazyValue.createValue(()->{
    //noinspection deprecation
    PerlParserExtension.EP_NAME.addChangeListener(PerlBaseLexer::refreshExtensions, PerlPluginUtil.getUnloadAwareDisposable());
    refreshExtensions();
    return true;
  });

  private static void refreshExtensions() {
    PerlParserImpl.restoreDefaultExtendsSet();

    for (PerlParserExtension extension : PerlParserExtension.EP_NAME.getExtensionList()) {
      // add extensions tokens
      List<Pair<IElementType, TokenSet>> extensionSets = extension.getExtensionSets();
      for (Pair<IElementType, TokenSet> extensionSet : extensionSets) {
        extendParserTokens(extensionSet.first, extensionSet.getSecond());
      }
    }
  }

  private static void extendParserTokens(IElementType setToExtend, TokenSet extendWith) {
    for (int i = 0; i < PerlParserImpl.EXTENDS_SETS_.length; i++) {
      if (PerlParserImpl.EXTENDS_SETS_[i].contains(setToExtend)) {
        PerlParserImpl.EXTENDS_SETS_[i] = TokenSet.orSet(PerlParserImpl.EXTENDS_SETS_[i], extendWith);
        break;
      }
    }
  }

  // last captured heredoc marker
  protected final Queue<PerlHeredocQueueElement> heredocQueue = new ArrayDeque<>(5);
  protected final PerlBracesStack myBracesStack = new PerlBracesStack();
  protected final PerlBracesStack myBracketsStack = new PerlBracesStack();
  protected final PerlBracesStack myParensStack = new PerlBracesStack();
  // flags that we are waiting for format on the next line
  protected boolean myFormatWaiting = false;
  // number of sections for the next regexp
  protected int sectionsNumber = 0;
  /**
   * Regex processor qr{} m{} s{}{}
   **/
  protected IElementType regexCommand = null;
  /**
   * Indicates that we did encounter {@code <<} and the next line may be a heredoc
   */
  private boolean myIsHeredocLike = false;
  private IElementType myCurrentSigilToken;
  private boolean myIsPerlSwitchEnabled = false;

  private @Nullable Project myProject;
  private AtomicNotNullLazyValue<Set<String>> mySubNamesProvider;
  private AtomicNotNullLazyValue<Set<String>> myNamespaceNamesProvider;
  private PerlImplicitDeclarationsService myImplicitSubsService;
  private final Set<String> myLocalPackages = new HashSet<>();

  public PerlBaseLexer() {
    ourListenersInitializer.get();
  }

  public PerlBaseLexer withProject(@Nullable Project project) {
    myProject = project;
    if (project != null) {
      myIsPerlSwitchEnabled = PerlSharedSettings.getInstance(project).PERL_SWITCH_ENABLED;
    }
    return this;
  }

  protected IElementType getPerlSwitchToken(IElementType token) {
    if (myIsPerlSwitchEnabled) {
      yybegin(YYINITIAL);
      return token;
    }
    yybegin(AFTER_IDENTIFIER);
    return SUB_NAME;
  }

  @Override
  public boolean isInitialState() {
    return super.isInitialState() &&
           !myFormatWaiting &&
           !isHeredocLike() &&
           heredocQueue.isEmpty() &&
           myBracesStack.isEmpty() &&
           myBracketsStack.isEmpty() &&
           myParensStack.isEmpty() &&
           mySingleOpenQuoteChar == 0;
  }

  /**
   * @apiNote use this method to set a quote charater for currently lexed single-quoted string content.
   */
  public void setSingleOpenQuoteChar(char singleOpenQuoteChar) {
    mySingleOpenQuoteChar = singleOpenQuoteChar;
    if (singleOpenQuoteChar != 0) {
      mySingleCloseQuoteChar = PerlString.getQuoteCloseChar(singleOpenQuoteChar);
    }
    else {
      mySingleCloseQuoteChar = 0;
    }
  }

  /**
   * We've met any sigil
   */
  protected IElementType startUnbracedVariable(@NotNull IElementType sigilToken) {
    return startUnbracedVariable(getRealLexicalState(), sigilToken);
  }

  /**
   * We've met any sigil
   */
  protected IElementType startUnbracedVariable(int state, @NotNull IElementType sigilToken) {
    myCurrentSigilToken = sigilToken;
    yybegin(state);
    pushStateAndBegin(VARIABLE_UNBRACED);
    return sigilToken;
  }

  /**
   * We've met one of the $ / [${]
   */
  @SuppressWarnings("SameReturnValue")
  protected IElementType processUnbracedScalarSigil() {
    myCurrentSigilToken = SIGIL_SCALAR;
    return SIGIL_SCALAR;
  }

  protected IElementType startBracedVariable() {
    Trinity<IElementType, IElementType, IElementType> sigilTokens = SIGILS_TO_TOKENS_MAP.get(myCurrentSigilToken);
    assert sigilTokens != null;
    myBracesStack.push(0, sigilTokens.third);
    return getLeftBrace(sigilTokens.first, VARIABLE_BRACED);
  }

  protected IElementType startBracedBlock() {
    return startBracedBlockWithState(YYINITIAL);
  }

  protected IElementType startBracedBlock(int returnState) {
    yybegin(returnState);
    return startBracedBlockWithState(YYINITIAL);
  }

  /**
   * Starts a braced block
   *
   * @param blockState state to start after brace
   * @return token type
   */
  protected IElementType startBracedBlockWithState(int blockState) {
    myBracesStack.push(0);
    pushState();
    return getLeftBrace(blockState);
  }

  protected IElementType getLeftBrace() {
    return getLeftBrace(YYINITIAL);
  }

  protected IElementType getLeftBrace(int newState) {
    return getLeftBrace(LEFT_BRACE, newState);
  }

  private IElementType getLeftBrace(IElementType braceType, int newState) {
    if (!myBracesStack.isEmpty()) {
      myBracesStack.incLast();
    }
    yybegin(newState);
    return braceType;
  }

  @SuppressWarnings("Duplicates")
  protected IElementType getRightBrace(int afterState) {
    if (!myBracesStack.isEmpty()) {
      if (myBracesStack.decLast() == 0) {
        Object userData = myBracesStack.peekAdditional();
        myBracesStack.pop();
        popState();
        return userData instanceof IElementType elementType ? elementType : RIGHT_BRACE;
      }
    }
    yybegin(afterState);
    return RIGHT_BRACE;
  }

  protected IElementType startBracketedBlock() {
    return startBracketedBlockWithState(YYINITIAL);
  }

  protected IElementType startBracketedBlock(int returnState) {
    yybegin(returnState);
    return startBracketedBlockWithState(YYINITIAL);
  }

  protected IElementType startBracketedBlockWithState(@SuppressWarnings("SameParameterValue") int blockState) {
    myBracketsStack.push(0);
    pushState();
    return getLeftBracket(blockState);
  }

  @SuppressWarnings("SameReturnValue")
  protected IElementType getLeftBracket(int newState) {
    if (!myBracketsStack.isEmpty()) {
      myBracketsStack.incLast();
    }
    yybegin(newState);
    return LEFT_BRACKET;
  }

  @SuppressWarnings({"Duplicates", "SameReturnValue"})
  protected IElementType getRightBracket(int afterState) {
    if (!myBracketsStack.isEmpty()) {
      if (myBracketsStack.decLast() == 0) {
        myBracketsStack.pop();
        popState();
        return RIGHT_BRACKET;
      }
    }
    yybegin(afterState);
    return RIGHT_BRACKET;
  }

  protected IElementType startParethesizedBlock(int afterState, int afterParenState) {
    return startParethesizedBlock(afterState, afterParenState, null);
  }

  protected IElementType startParethesizedBlock(int afterState, int afterParenState, Object userData) {
    myParensStack.push(0, userData);
    yybegin(afterState);
    pushState();
    return getLeftParen(afterParenState);
  }

  @SuppressWarnings("SameReturnValue")
  protected IElementType getLeftParen(int newState) {
    if (!myParensStack.isEmpty()) {
      myParensStack.incLast();
    }
    yybegin(newState);
    return LEFT_PAREN;
  }

  @SuppressWarnings({"Duplicates", "SameReturnValue"})
  protected IElementType getRightParen(int afterState) {
    if (!myParensStack.isEmpty()) {
      if (myParensStack.decLast() == 0) {
        myParensStack.pop();
        popState();
        return RIGHT_PAREN;
      }
    }
    yybegin(afterState);
    return RIGHT_PAREN;
  }

  /**
   * We've met identifier (variable name)
   */
  protected @NotNull IElementType getUnbracedVariableNameToken() {
    popState();
    char currentChar;
    if (
      !myParensStack.isEmpty() &&
      yylength() == 1 &&
      StringUtil.containsChar(",=)", currentChar = yytext().charAt(0)) &&
      myParensStack.peek() == 1 &&
      myParensStack.peekAdditional() == SUB_SIGNATURE) {
      if (currentChar == ',') {
        yybegin(YYINITIAL);
        return COMMA;
      }
      else if (currentChar == '=') {
        yybegin(YYINITIAL);
        return OPERATOR_ASSIGN;
      }
      else if (currentChar == ')') {
        return getRightParen(SUB_ATTRIBUTES);
      }
    }
    return getVariableNameTokenBySigil();
  }

  protected @NotNull IElementType getBracedVariableNameToken() {
    yybegin(YYINITIAL);
    return getVariableNameTokenBySigil();
  }

  private IElementType getVariableNameTokenBySigil() {
    IElementType nameToken = SIGILS_TO_TOKENS_MAP.get(myCurrentSigilToken).second;
    if (nameToken != SUB_NAME) {
      return nameToken;
    }

    CharSequence tokenText = yytext();
    int tokenLength = tokenText.length();
    if (tokenLength == 1) {
      return nameToken;
    }

    if (StringUtil.endsWithChar(tokenText, ':')) {
      return PACKAGE;
    }

    int nameIndex = StringUtil.lastIndexOfAny(tokenText, ":'") + 1;
    if (nameIndex == 0) {
      return nameToken;
    }

    yypushback(tokenLength - nameIndex);
    pushStateAndBegin(LEX_SUB_NAME);
    return QUALIFYING_PACKAGE;
  }

  @Override
  protected void resetInternals() {
    super.resetInternals();
    myBracesStack.clear();
    myBracketsStack.clear();
    myParensStack.clear();
    heredocQueue.clear();
    myFormatWaiting = false;
    setHeredocLike(false);
    myHasTryCatch = null;
    setSingleOpenQuoteChar((char)0);

    myImplicitSubsService = myProject == null ? null : PerlImplicitDeclarationsService.getInstance(myProject);

    mySubNamesProvider = AtomicNotNullLazyValue.createValue(() -> {
      assert myProject != null;
      return PerlNamesCache.getInstance(myProject).getSubsNamesSet();
    });

    myNamespaceNamesProvider = AtomicNotNullLazyValue.createValue(() -> {
      assert myProject != null;
      return PerlNamesCache.getInstance(myProject).getNamespacesNamesSet();
    });
    myLocalPackages.clear();
  }

  public void setHasTryCatch(Boolean hasTryCatch) {
    myHasTryCatch = hasTryCatch;
  }

  /**
   * Fast method for lexing spaces with or without newlines. Lexer invokes this method after lexing first space character.
   */
  @SuppressWarnings("SameReturnValue")
  protected final @NotNull IElementType captureSpaces() {
    var bufferEnd = getBufferEnd();
    var currentOffset = getTokenEnd() - 1;
    var buffer = getBuffer();
    while (currentOffset < bufferEnd) {
      var currentChar = buffer.charAt(currentOffset);
      if (currentChar == '\n') {
        setHeredocLike(false);
        if (myFormatWaiting) {
          currentOffset++;
          myFormatWaiting = false;
          yybegin(CAPTURE_FORMAT);
          break;
        }
        else if (!heredocQueue.isEmpty()) {
          currentOffset++;
          startHeredocCapture();
          break;
        }
      }
      else if (!Character.isWhitespace(currentChar)) {
        break;
      }
      currentOffset++;
    }
    setTokenEnd(currentOffset);
    return TokenType.WHITE_SPACE;
  }

  /**
   * Fast method for lexing line comment
   */
  @SuppressWarnings("SameReturnValue")
  protected final @NotNull IElementType captureComment() {
    var bufferEnd = getBufferEnd();
    var currentOffset = getTokenEnd();
    var buffer = getBuffer();
    while (currentOffset < bufferEnd) {
      if (buffer.charAt(currentOffset) == '\n') {
        break;
      }
      currentOffset++;
    }
    setTokenEnd(currentOffset);
    return COMMENT_LINE;
  }


  /**
   * Fast {@code __END__} block capture method. Invoked on the first character match after the block beginning.
   */
  @SuppressWarnings("SameReturnValue")
  protected final @NotNull IElementType captureEndBlock(){
    int offset = getTokenStart();
    var bufferEnd = getBufferEnd();
    var buffer = getBuffer();
    while( offset < bufferEnd){
      offset = StringUtil.indexOf(buffer, "\n=", offset) + 1;
      if( offset < 1){
        // no pod inside
        offset = bufferEnd;
        yybegin(YYINITIAL);
        break;
      }
      char nextChar = offset + 1 < bufferEnd ? buffer.charAt(offset + 1) : 0;
      if( nextChar >= 'a' && nextChar <= 'z' ||  nextChar >= 'A' && nextChar <= 'Z'){
        // valid pod starter
        break;
      }
      offset++;
    }
    setTokenEnd(offset);
    return COMMENT_BLOCK;
  }

  /**
   * Fast POD block capture method. Invoked after opening line was captured. No line beginning check was preformed.
   *
   * @param true iff pod block is not expecting to have end. Not sure why, but this was initial logic for the pod in the end blocks.
   */
  protected final IElementType capturePod(boolean isEndless) {
    var tokenStart = getTokenStart();
    var buffer = getBuffer();
    if (tokenStart != 0 && buffer.charAt(tokenStart - 1) != '\n') {
      yypushback(yylength() - 1);
      yybegin(YYINITIAL);
      return OPERATOR_ASSIGN;
    }

    int bufferEnd = getBufferEnd();
    if (isEndless) {
      setTokenEnd(bufferEnd);
      return POD;
    }

    int tokenEnd = getTokenEnd();
    while (tokenEnd < bufferEnd) {
      tokenEnd = StringUtil.indexOf(buffer, CUT_COMMAND_WITH_LEADING_NEWLINE, tokenEnd);
      if (tokenEnd < 0) {
        tokenEnd = bufferEnd;
        break;
      }
      tokenEnd += CUT_COMMAND_WITH_LEADING_NEWLINE.length();
      if (tokenEnd >= bufferEnd) {
        tokenEnd = bufferEnd;
        break;
      }
      if (!Character.isWhitespace(buffer.charAt(tokenEnd))) {
        continue;
      }
      tokenEnd = StringUtil.indexOf(buffer, '\n', tokenEnd);
      if (tokenEnd < 0) {
        tokenEnd = bufferEnd;
      }
      break;
    }
    setTokenEnd(tokenEnd);
    return POD;
  }

  /**
   * Distincts sub_name from qualified sub_name by :
   *
   * @return guessed token
   */
  protected @NotNull IElementType getIdentifierTokenWithoutIndex() {
    CharSequence tokenText = yytext();
    int lastIndex;
    if (StringUtil.endsWithChar(tokenText, ':')) {
      return PACKAGE;
    }

    int tokenLength = tokenText.length();
    if ((lastIndex = StringUtil.lastIndexOfAny(tokenText, ":'") + 1) > 0) {
      yypushback(tokenLength - lastIndex);
      pushStateAndBegin(LEX_SUB_NAME);
      return QUALIFYING_PACKAGE;
    }
    return SUB_NAME;
  }

  /**
   * Bareword parser, resolves built-ins and runs additional processings where it's necessary
   *
   * @return token type
   */
  protected IElementType getIdentifierToken() {
    String tokenText = yytext().toString();
    IElementType tokenType;

    if (StringUtil.endsWithChar(tokenText, ':')) {
      tokenType = PACKAGE;
    }
    else if (myProject != null) {
      String canonicalName = PerlPackageUtilCore.getCanonicalName(tokenText);
      if (!StringUtil.containsChar(canonicalName, ':')) {
        if (StringUtil.isCapitalized(canonicalName) &&
            (myNamespaceNamesProvider.getValue().contains(canonicalName) || myLocalPackages.contains(canonicalName))) {
          tokenType = PACKAGE;
        }
        else {
          tokenType = SUB_NAME;
        }
      }
      else if (StringUtil.equals(canonicalName, "UNIVERSAL::can")) {
        tokenType = QUALIFYING_PACKAGE;
      }
      else if (myImplicitSubsService.getSub(canonicalName) != null) {
        tokenType = QUALIFYING_PACKAGE;
      }
      else if (mySubNamesProvider.getValue().contains(canonicalName)) {
        tokenType = QUALIFYING_PACKAGE;
      }
      else if (myNamespaceNamesProvider.getValue().contains(canonicalName) || myLocalPackages.contains(canonicalName)) {
        tokenType = PACKAGE;
      }
      else {
        tokenType = QUALIFYING_PACKAGE;
      }
    }
    else {    // fallback for words scanner
      tokenType = IDENTIFIER;
    }

    yybegin(AFTER_IDENTIFIER);

    if (tokenType == QUALIFYING_PACKAGE) {
      int lastIndex = StringUtil.lastIndexOfAny(tokenText, ":'") + 1;
      yypushback(tokenText.length() - lastIndex);
      pushStateAndBegin(LEX_SUB_NAME);
    }

    return tokenType;
  }

  private List<IElementType> getStringTokens() {
    int currentState = getRealLexicalState();

    if (currentState == QUOTE_LIKE_OPENER_Q || currentState == QUOTE_LIKE_OPENER_Q_NOSHARP) {
      return SQ_TOKENS;
    }
    if (currentState == QUOTE_LIKE_OPENER_QQ || currentState == QUOTE_LIKE_OPENER_QQ_NOSHARP) {
      return DQ_TOKENS;
    }
    if (currentState == QUOTE_LIKE_OPENER_QX || currentState == QUOTE_LIKE_OPENER_QX_NOSHARP) {
      return XQ_TOKENS;
    }
    if (currentState == QUOTE_LIKE_OPENER_QW || currentState == QUOTE_LIKE_OPENER_QW_NOSHARP) {
      return QW_TOKENS;
    }
    if (currentState == QUOTE_LIKE_OPENER_GLOB) {
      return GLOB_TOKENS;
    }

    throw new RuntimeException("Unknown lexical state for string token " + currentState);
  }

  /**
   * Finds opening quote, body and close quote of the quote-like structure. Pushes them as pre-parsed tokens.
   *
   * @return token of the opening quote for the string.
   * @see #captureRegex()
   * @see #captureTr()
   */
  protected IElementType captureString() {
    CharSequence buffer = getBuffer();
    int currentPosition = getTokenStart();
    int bufferEnd = getBufferEnd();

    char openQuote = buffer.charAt(currentPosition);

    List<IElementType> stringTokens = getStringTokens();
    pushPreparsedToken(currentPosition++, currentPosition, stringTokens.getFirst());

    int contentStart = currentPosition;
    boolean hasEscape = false;
    boolean hasSigil = false;

    boolean isEscaped = false;
    int quotesDepth = 0;
    char closeQuote = PerlString.getQuoteCloseChar(openQuote);
    boolean quotesDiffer = openQuote != closeQuote;

    while (currentPosition < bufferEnd) {
      char currentChar = buffer.charAt(currentPosition);

      if (!isEscaped && quotesDepth == 0 && currentChar == closeQuote) {
        break;
      }

      //noinspection Duplicates
      if (!isEscaped && quotesDiffer) {
        if (currentChar == openQuote) {
          quotesDepth++;
        }
        else if (currentChar == closeQuote) {
          quotesDepth--;
        }
      }

      isEscaped = !isEscaped && currentChar == '\\';
      hasEscape = hasEscape || currentChar == '\\';
      hasSigil = hasSigil || currentChar == '$' || currentChar == '@';

      currentPosition++;
    }

    if (currentPosition > contentStart) {
      IElementType contentTokenType = stringTokens.get(1);
      if (contentTokenType == LP_STRING_Q && !hasEscape) {
        contentTokenType = stringTokens.get(3);
      }
      else if (contentTokenType == LP_STRING_QX && openQuote == '\'') {
        contentTokenType = LP_STRING_QX_RESTRICTED;
      }
      else if ((contentTokenType == LP_STRING_QQ || contentTokenType == LP_STRING_QX) && !hasEscape && !hasSigil) {
        contentTokenType = stringTokens.get(3);
      }

      pushPreparsedToken(contentStart, currentPosition, contentTokenType);
    }

    if (currentPosition < bufferEnd) {
      // got close quote
      pushPreparsedToken(currentPosition++, currentPosition, stringTokens.get(2));
    }
    popState();
    return getPreParsedToken();
  }

  /**
   * Parsing tr/y content
   *
   * @return first token
   */
  public IElementType captureTr() {
    popState();
    yybegin(AFTER_VALUE);
    CharSequence buffer = getBuffer();
    int currentOffset = getTokenStart();
    int bufferEnd = getBufferEnd();

    // search block
    char openQuote = buffer.charAt(currentOffset);
    char closeQuote = PerlString.getQuoteCloseChar(openQuote);
    boolean quotesDiffer = openQuote != closeQuote;
    pushPreparsedToken(currentOffset++, currentOffset, REGEX_QUOTE_OPEN);

    currentOffset = parseTrBlockContent(currentOffset, openQuote, closeQuote);

    // close quote
    if (currentOffset < bufferEnd) {
      pushPreparsedToken(currentOffset++, currentOffset, quotesDiffer ? REGEX_QUOTE_CLOSE : REGEX_QUOTE);
    }

    // between blocks
    if (quotesDiffer) {
      currentOffset = lexWhiteSpacesAndComments(currentOffset);
    }

    // second block
    if (currentOffset < bufferEnd) {
      if (quotesDiffer) {
        openQuote = buffer.charAt(currentOffset);
        closeQuote = PerlString.getQuoteCloseChar(openQuote);
        pushPreparsedToken(currentOffset++, currentOffset, REGEX_QUOTE_OPEN);
      }

      currentOffset = parseTrBlockContent(currentOffset, openQuote, closeQuote);
    }

    // close quote
    if (currentOffset < bufferEnd) {
      pushPreparsedToken(currentOffset++, currentOffset, REGEX_QUOTE_CLOSE);
    }


    // trans modifiers
    if (currentOffset < bufferEnd) {
      int blockStart = currentOffset;
      while (currentOffset < bufferEnd && StringUtil.containsChar(ALLOWED_TR_MODIFIERS, buffer.charAt(currentOffset))) {
        currentOffset++;
      }

      if (blockStart < currentOffset) {
        pushPreparsedToken(blockStart, currentOffset, REGEX_MODIFIER);
      }
    }

    return getPreParsedToken();
  }

  /**
   * Parsing tr block content till close quote
   *
   * @param currentOffset start offset
   * @param closeQuote    close quote character
   * @return next offset
   */
  private int parseTrBlockContent(int currentOffset, char openQuote, char closeQuote) {
    int blockStartOffset = currentOffset;
    CharSequence buffer = getBuffer();
    int bufferEnd = getBufferEnd();
    boolean isEscaped = false;
    boolean isQuoteDiffers = openQuote != closeQuote;
    int quotesLevel = 0;

    while (currentOffset < bufferEnd) {
      char currentChar = buffer.charAt(currentOffset);

      if (!isEscaped && quotesLevel == 0 && currentChar == closeQuote) {
        if (currentOffset > blockStartOffset) {
          pushPreparsedToken(blockStartOffset, currentOffset, openQuote == '\'' ? LP_STRING_QQ_RESTRICTED : LP_STRING_TR);
        }
        break;
      }
      //noinspection Duplicates
      if (isQuoteDiffers && !isEscaped) {
        if (currentChar == openQuote) {
          quotesLevel++;
        }
        else if (currentChar == closeQuote) {
          quotesLevel--;
        }
      }

      isEscaped = (currentChar == '\\' && !isEscaped);
      currentOffset++;
    }

    return currentOffset;
  }

  /**
   * Lexing empty spaces and comments between regex/tr blocks and adding tokens to the target list
   *
   * @param currentOffset start offset
   * @return new offset
   */
  protected int lexWhiteSpacesAndComments(int currentOffset) {
    CharSequence buffer = getBuffer();
    int bufferEnd = getBufferEnd();
    while (currentOffset < bufferEnd) {
      char currentChar = buffer.charAt(currentOffset);

      if (currentChar == '\n') {
        // fixme check heredocs ?
        pushPreparsedToken(currentOffset++, currentOffset, TokenType.WHITE_SPACE);
      }
      else if (Character.isWhitespace(currentChar))    // white spaces
      {
        int whiteSpaceStart = currentOffset;
        while (currentOffset < bufferEnd && Character.isWhitespace(currentChar = buffer.charAt(currentOffset)) && currentChar != '\n') {
          currentOffset++;
        }
        pushPreparsedToken(whiteSpaceStart, currentOffset, TokenType.WHITE_SPACE);
      }
      else if (currentChar == '#')    // line comment
      {
        int commentStart = currentOffset;
        while (currentOffset < bufferEnd && buffer.charAt(currentOffset) != '\n') {
          currentOffset++;
        }
        pushPreparsedToken(getCustomToken(commentStart, currentOffset, COMMENT_LINE));
      }
      else {
        break;
      }
    }

    return currentOffset;
  }

  public int getRegexBlockEndOffset(int startOffset, char openingChar) {
    char closingChar = PerlString.getQuoteCloseChar(openingChar);
    CharSequence buffer = getBuffer();
    int bufferEnd = getBufferEnd();

    boolean isEscaped = false;
    boolean isQuotesDiffers = closingChar != openingChar;

    int delimiterLevel = 0;

    int currentOffset = startOffset;

    while (currentOffset < bufferEnd) {

      char currentChar = buffer.charAt(currentOffset);

      if (delimiterLevel == 0 && !isEscaped && closingChar == currentChar) {
        return currentOffset;
      }

      if (!isEscaped && isQuotesDiffers) {
        if (currentChar == openingChar) {
          delimiterLevel++;
        }
        else if (currentChar == closingChar && delimiterLevel > 0) {
          delimiterLevel--;
        }
      }

      isEscaped = !isEscaped && closingChar != '\\' && currentChar == '\\';

      currentOffset++;
    }
    return currentOffset;
  }

  /**
   * Parses regexp from the current position (opening delimiter) and preserves tokens in preparsedTokensList
   * REGEX_MODIFIERS = [msixpodualgcerxx]
   *
   * @return opening delimiter type of the regular expressions
   * @see #captureTr()
   * @see #captureString()
   */
  public IElementType captureRegex() {
    popState();
    yybegin(AFTER_VALUE);
    CharSequence buffer = getBuffer();
    int currentOffset = getTokenStart();
    int bufferEnd = getBufferEnd();

    char firstBlockOpeningQuote = buffer.charAt(currentOffset);
    boolean isInterpolationAllowedInMatch = firstBlockOpeningQuote != '\'';
    pushPreparsedToken(currentOffset++, currentOffset, REGEX_QUOTE_OPEN);

    // find block 1
    int firstBlockEndOffset = getRegexBlockEndOffset(currentOffset, firstBlockOpeningQuote);
    CustomToken firstBlockToken = null;
    if (firstBlockEndOffset > currentOffset) {
      firstBlockToken = new CustomToken(currentOffset, firstBlockEndOffset, isInterpolationAllowedInMatch ? LP_REGEX : LP_REGEX_SQ);
      pushPreparsedToken(firstBlockToken);
    }

    currentOffset = firstBlockEndOffset;

    // find block 2
    CustomToken secondBlockOpeningToken = null;
    CustomToken secondBlockToken = null;

    if (currentOffset < bufferEnd) {
      if (sectionsNumber == 1) {
        pushPreparsedToken(currentOffset++, currentOffset, REGEX_QUOTE_CLOSE);
      }
      else // should have second part
      {
        char secondBlockOpeningQuote = firstBlockOpeningQuote;
        if (firstBlockOpeningQuote == PerlString.getQuoteCloseChar(firstBlockOpeningQuote)) {
          secondBlockOpeningToken = new CustomToken(currentOffset++, currentOffset, REGEX_QUOTE);
          pushPreparsedToken(secondBlockOpeningToken);
        }
        else {
          pushPreparsedToken(currentOffset++, currentOffset, REGEX_QUOTE_CLOSE);
          currentOffset = lexWhiteSpacesAndComments(currentOffset);

          if (currentOffset < bufferEnd) {
            secondBlockOpeningQuote = buffer.charAt(currentOffset);
            secondBlockOpeningToken = new CustomToken(currentOffset++, currentOffset, REGEX_QUOTE_OPEN);
            pushPreparsedToken(secondBlockOpeningToken);
          }
        }

        if (currentOffset < bufferEnd) {
          int secondBlockEndOffset = getRegexBlockEndOffset(currentOffset, secondBlockOpeningQuote);

          if (secondBlockEndOffset > currentOffset) {
            secondBlockToken = new CustomToken(
              currentOffset, secondBlockEndOffset, secondBlockOpeningQuote == '\'' ? LP_STRING_QQ_RESTRICTED : LP_STRING_RE);
            pushPreparsedToken(secondBlockToken);
            currentOffset = secondBlockEndOffset;
          }
        }

        if (currentOffset < bufferEnd) {
          pushPreparsedToken(currentOffset++, currentOffset, REGEX_QUOTE_CLOSE);
        }
      }
    }

    // check modifiers for x
    assert regexCommand != null;
    String allowedModifiers = ALLOWED_REGEXP_MODIFIERS.get(regexCommand);

    while (currentOffset < bufferEnd) {
      char currentChar = buffer.charAt(currentOffset);
      int modifierEndOffset = currentOffset + 1;
      if (!StringUtil.containsChar(allowedModifiers, currentChar))    // unknown modifier
      {
        break;
      }
      else if (currentChar == 'a' && modifierEndOffset < bufferEnd && buffer.charAt(modifierEndOffset) == 'a') {
        modifierEndOffset++;
      }
      else if (currentChar == 'x')    // mark as extended
      {
        if (firstBlockToken != null) {
          if (modifierEndOffset < bufferEnd && buffer.charAt(modifierEndOffset) == 'x') {
            firstBlockToken.setTokenType(isInterpolationAllowedInMatch ? LP_REGEX_XX : LP_REGEX_XX_SQ);
            modifierEndOffset++;
          }
          else {
            firstBlockToken.setTokenType(isInterpolationAllowedInMatch ? LP_REGEX_X : LP_REGEX_X_SQ);
          }
        }
      }
      else if (currentChar == 'e')    // mark as evaluated
      {
        if (modifierEndOffset < bufferEnd && buffer.charAt(modifierEndOffset) == 'e') {
          modifierEndOffset++;
        }
        if (secondBlockOpeningToken != null) {
          IElementType secondBlockOpeningTokenType = secondBlockOpeningToken.getTokenType();
          if (secondBlockOpeningTokenType == REGEX_QUOTE_OPEN || secondBlockOpeningTokenType == REGEX_QUOTE_OPEN_E) {
            secondBlockOpeningToken.setTokenType(REGEX_QUOTE_OPEN_E);
          }
          else if (secondBlockOpeningTokenType == REGEX_QUOTE || secondBlockOpeningTokenType == REGEX_QUOTE_E) {
            secondBlockOpeningToken.setTokenType(REGEX_QUOTE_E);
          }
          else {
            throw new RuntimeException("Bug, got: " + secondBlockOpeningTokenType);
          }
        }
        if (secondBlockToken != null) {
          secondBlockToken.setTokenType(getLPCodeBlockElementType());
        }
      }

      pushPreparsedToken(currentOffset, modifierEndOffset, REGEX_MODIFIER);
      currentOffset = modifierEndOffset;
    }

    return getPreParsedToken();
  }

  protected void startHeredocCapture() {
    pushState();
    var headElement = heredocQueue.peek();
    LOG.assertTrue(headElement != null);
    if (!headElement.getMarker().isEmpty()) {
      yybegin(CAPTURE_HEREDOC);
    }
    else {
      yybegin(CAPTURE_HEREDOC_WITH_EMPTY_MARKER);
    }
  }

  /**
   * Fast here-document capture method. Invoked on the first character after the newline
   */
  protected final @NotNull IElementType captureHeredoc(){
    final int bodyStartOffset = getTokenStart();
    int offset = bodyStartOffset;
    var buffer = getBuffer();
    var bufferEnd = getBufferEnd();
    PerlHeredocQueueElement currentHeredoc = heredocQueue.poll();
    popState();
    LOG.assertTrue(currentHeredoc != null);

    var closeMarker = currentHeredoc.getMarker();

    while( offset < bufferEnd){
      int markerStartOffset = StringUtil.indexOf(buffer, closeMarker, offset);
      if (markerStartOffset < 0 || markerStartOffset >= bufferEnd) {
        // no end marker text ahead
        break;
      }

      int markerEndOffset = markerStartOffset + closeMarker.length();
      if( markerEndOffset < bufferEnd && buffer.charAt(markerEndOffset) != '\n' ){
        // no newline or end of the buffer after end marker
        offset = markerEndOffset;
        continue;
      }

      int spacesStartOffset = markerStartOffset;
      if( currentHeredoc.isIndentable()){
        // looking for leading spaces for indented heredoc
        while( spacesStartOffset > 0 ){
          char prevChar = buffer.charAt(spacesStartOffset-1);
          if( prevChar == '\n'){
            break;
          }
          if( !Character.isWhitespace(prevChar)){
            break;
          }
          spacesStartOffset--;
        }
      }

      if( spacesStartOffset > 0 && buffer.charAt(spacesStartOffset-1) == '\n'){
        // we found the valid end
        if( spacesStartOffset > bodyStartOffset){
          pushPreparsedToken(bodyStartOffset, spacesStartOffset, currentHeredoc.getTargetElement());
        }
        if( spacesStartOffset < markerStartOffset){
          pushPreparsedToken(spacesStartOffset, markerStartOffset, TokenType.WHITE_SPACE);
        }
        pushPreparsedToken(markerStartOffset, markerEndOffset, currentHeredoc.getTerminatorElementType());
        return getPreParsedToken();
      }
      offset = markerEndOffset;
    }

    yybegin(YYINITIAL);
    setTokenEnd(bufferEnd);
    return currentHeredoc.getTargetElement();
  }

  /**
   * Fast here-document capture method with empty close marker. Invoked on the first character after the newline
   */
  protected final @NotNull IElementType captureHeredocWithEmptyMarker(){
    final int bodyStartOffset = getTokenStart();
    var buffer = getBuffer();
    PerlHeredocQueueElement currentHeredoc = heredocQueue.poll();
    LOG.assertTrue(currentHeredoc != null);
    popState();

    if( buffer.charAt(bodyStartOffset) == '\n'){
      // empty heredoc with empty marker
      if( !heredocQueue.isEmpty()){
        startHeredocCapture();
      }
      return currentHeredoc.getTerminatorElementType();
    }

    var bufferEnd = getBufferEnd();

    int markerStartOffset = StringUtil.indexOf(buffer, "\n\n", bodyStartOffset) + 1;
    if (markerStartOffset < 1) {
      // no end marker text ahead
      yybegin(YYINITIAL);
      setTokenEnd(bufferEnd);
      return currentHeredoc.getTargetElement();
    }

    int markerEndOffset = markerStartOffset + 1;

    pushPreparsedToken(bodyStartOffset, markerStartOffset, currentHeredoc.getTargetElement());
    pushPreparsedToken(markerStartOffset, markerEndOffset, currentHeredoc.getTerminatorElementType());
    if( !heredocQueue.isEmpty()){
      startHeredocCapture();
    }

    return getPreParsedToken();
  }


  protected IElementType registerPackage(IElementType tokenType) {
    myLocalPackages.add(PerlPackageUtilCore.getCanonicalNamespaceName(yytext().toString()));
    return tokenType;
  }

  /**
   * Changes current state to nosharp one if necessary
   */
  protected void setNoSharpState() {
    int realLexicalState = getRealLexicalState();
    if (realLexicalState == QUOTE_LIKE_OPENER_Q) {
      yybegin(QUOTE_LIKE_OPENER_Q_NOSHARP);
    }
    else if (realLexicalState == QUOTE_LIKE_OPENER_QQ) {
      yybegin(QUOTE_LIKE_OPENER_QQ_NOSHARP);
    }
    else if (realLexicalState == QUOTE_LIKE_OPENER_QX) {
      yybegin(QUOTE_LIKE_OPENER_QX_NOSHARP);
    }
    else if (realLexicalState == QUOTE_LIKE_OPENER_QW) {
      yybegin(QUOTE_LIKE_OPENER_QW_NOSHARP);
    }
    else if (realLexicalState == TRANS_OPENER) {
      yybegin(TRANS_OPENER_NO_SHARP);
    }
    else if (realLexicalState == REGEX_OPENER) {
      yybegin(REGEX_OPENER_NO_SHARP);
    }
  }

  /**
   * Captures quoted here-doc opener after <<[~] operator. Pushes heredoc data into queue
   *
   * @param heredocElementType element type for heredoc body
   * @param stringOpenerState  state for string capture
   * @return next string token type
   */
  public IElementType captureQuotedHeredocMarker(IElementType heredocElementType, int stringOpenerState, boolean isIndentable) {
    yybegin(AFTER_VALUE);
    pushState();
    heredocQueue.add(new PerlHeredocQueueElement(heredocElementType, yytext().subSequence(1, yylength() - 1), isIndentable));
    yybegin(stringOpenerState);
    return captureString();
  }

  /**
   * Captures bare here-doc opener after <<[~] operator. Pushes heredoc data into queue
   *
   * @param heredocElementType element type for heredoc body
   * @return string token type
   */
  @SuppressWarnings("SameReturnValue")
  public IElementType captureBareHeredocMarker(IElementType heredocElementType, boolean isIndentable) {
    yybegin(AFTER_VALUE);
    heredocQueue.add(new PerlHeredocQueueElement(heredocElementType, yytext(), isIndentable));
    return STRING_CONTENT;
  }

  public boolean isHeredocLike() {
    return myIsHeredocLike;
  }

  public void setHeredocLike(boolean heredocLike) {
    myIsHeredocLike = heredocLike;
  }

  /**
   * Registers used package if necessary. Used for TryCatch logic for now
   *
   * @return PACKAGE element type
   */
  @SuppressWarnings("SameReturnValue")
  protected IElementType registerUse() {
    if (StringUtil.equals(yytext(), "TryCatch")) {
      myHasTryCatch = true;
    }
    return PACKAGE;
  }

  /**
   * @return true if we are suppose to use TryCatch compounds
   * Logic of detection is highly optimized and put some restrictions:
   * buffer should explicitly contains {@code use TryCatch;} before first try/catch
   */
  protected boolean hasTryCatch() {
    if (myHasTryCatch != null) {
      return myHasTryCatch;
    }
    if (getBufferStart() == 0) {
      return myHasTryCatch = false;
    }
    return myHasTryCatch = USE_TRYCATCH_PATTERN.matcher(getBuffer().subSequence(0, getBufferStart())).find();
  }

  /**
   * Handles try token depending on TryCatch usege
   */
  protected IElementType handleTry() {
    if (hasTryCatch()) {
      pushback();
      yybegin(BEFORE_TRY_TRYCATCH);
      return RESERVED_TRYCATCH;
    }
    else {
      yybegin(AFTER_TRY);
      return RESERVED_TRY;
    }
  }

  public @NotNull IElementType getLPCodeBlockElementType() {
    return hasTryCatch() ? LP_CODE_BLOCK_WITH_TRYCATCH : LP_CODE_BLOCK;
  }

  /**
   * @return true iff current token ends at the end of a buffer
   */
  protected boolean isLastChar() {
    return getTokenEnd() == getBufferEnd();
  }

  /**
   * @return token type for the back-slash we are standing on. Depends on character ahead: slash for quotes and string for the rest
   */
  protected IElementType getSQBackSlashTokenType() {
    if (mySingleOpenQuoteChar == 0) {
      return STRING_CONTENT;
    }
    int tokenEnd = getTokenEnd();
    int bufferEnd = getBufferEnd();
    if (bufferEnd <= tokenEnd) {
      return STRING_CONTENT;
    }
    char nextChar = getBuffer().charAt(tokenEnd);
    return nextChar == '\\' || nextChar == mySingleOpenQuoteChar || nextChar == mySingleCloseQuoteChar ?
           STRING_SPECIAL_ESCAPE_CHAR : STRING_CONTENT;
  }
}
