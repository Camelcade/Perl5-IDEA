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

package com.perl5.lang.perl.idea.findusages;

import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordOccurrence;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.TreeUtil;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.Processor;
import com.perl5.lang.perl.PerlParserDefinition;
import com.perl5.lang.perl.lexer.PerlLexingContext;
import com.perl5.lang.perl.lexer.adapters.PerlMergingLexerAdapter;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.*;
import static com.perl5.lang.perl.lexer.PerlTokenSets.*;


public class PerlWordsScanner extends DefaultWordsScanner implements PsiBasedWordScanner {
  private static final TokenSet IGNORE_CODE_TOKENSET = TokenSet.orSet(
    SIGILS, SPECIAL_STRING_TOKENS, KEYWORDS_TOKENSET, OPERATORS_TOKENSET,
    QUOTE_OPEN_ANY, QUOTE_CLOSE_FIRST_ANY,
    TokenSet.andNot(TAGS_TOKEN_SET, TokenSet.create(TAG_PACKAGE)),
    TokenSet.create(
      REGEX_MODIFIER, REGEX_TOKEN,
      NUMBER, NUMBER_BIN, NUMBER_OCT, NUMBER_HEX,
      OPERATOR_FILETEST, BUILTIN_ARGUMENTLESS, BUILTIN_UNARY, BUILTIN_LIST,
      NUMBER_VERSION, VERSION_ELEMENT, SUB_PROTOTYPE_TOKEN
    ));
  private static final @NotNull TokenSet COMMENTS_TOKENSET = TokenSet.andNot(
    TokenSet.orSet(PerlParserDefinition.COMMENTS, TokenSet.create(POD)),
    TokenSet.create(HEREDOC_END)
  );
  private static final TokenSet IDENTIFIERS_TOKENSET = PerlParserDefinition.IDENTIFIERS;
  private static final TokenSet LITERALS_TOKENSET = PerlParserDefinition.LITERALS;


  public PerlWordsScanner() {
    super(new PerlMergingLexerAdapter(PerlLexingContext.create(null).withEnforcedSublexing(true)),
          IDENTIFIERS_TOKENSET, COMMENTS_TOKENSET, LITERALS_TOKENSET, IGNORE_CODE_TOKENSET
    );
  }

  private static @NotNull PerlMergingLexerAdapter createLexer() {
    return new PerlMergingLexerAdapter(PerlLexingContext.create(null).withEnforcedSublexing(true));
  }

  @Override
  public void processWordsUsingPsi(@NotNull PsiFile psiFile, @NotNull Processor<? super WordOccurrence> processor) {
    var fileNode = psiFile.getNode();
    var run = TreeUtil.findFirstLeaf(fileNode);
    if (run == null) {
      return;
    }
    var fileText = psiFile.getText();
    WordOccurrence occurrence = new WordOccurrence(fileText, 0, 0, WordOccurrence.Kind.CODE); // shared occurrence
    while (run != null) {
      if (!processToken(fileText, processor, run.getElementType(), run.getStartOffset(), run.getStartOffset() + run.getTextLength(),
                        occurrence)) {
        return;
      }
      run = TreeUtil.nextLeaf(run);
    }
  }

  @Override
  public void processWords(final @NotNull CharSequence fileText, final @NotNull Processor<? super WordOccurrence> processor) {
    var lexer = createLexer();
    lexer.start(fileText);
    WordOccurrence occurrence = new WordOccurrence(fileText, 0, 0, WordOccurrence.Kind.CODE); // shared occurrence

    while (lexer.getTokenType() != null) {
      if (!processToken(fileText, processor, lexer.getTokenType(), lexer.getTokenStart(), lexer.getTokenEnd(), occurrence)) {
        return;
      }
      lexer.advance();
    }
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  protected static boolean processToken(@NotNull CharSequence fileText,
                                        @NotNull Processor<? super WordOccurrence> processor,
                                        @NotNull IElementType type,
                                        int start,
                                        int end,
                                        @NotNull WordOccurrence occurrence) {
    WordOccurrence.Kind kind;
    if (IDENTIFIERS_TOKENSET.contains(type)) {
      kind = WordOccurrence.Kind.CODE;
    }
    else if (COMMENTS_TOKENSET.contains(type)) {
      kind = WordOccurrence.Kind.COMMENTS;
    }
    else if (LITERALS_TOKENSET.contains(type)) {
      kind = WordOccurrence.Kind.LITERALS;
    }
    else if (!IGNORE_CODE_TOKENSET.contains(type)) {
      kind = WordOccurrence.Kind.CODE;
    }
    else {
      return true;
    }
    return stripWords(processor, fileText, start, end, kind, occurrence, false);
  }
}
