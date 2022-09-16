/*
 * Copyright 2015-2021 Alexandr Evstigneev
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

package com.perl5.lang.perl.parser.elementTypes;

import com.intellij.lang.*;
import com.intellij.lexer.DelegateLexer;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.ClearableLazyValue;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.text.BlockSupport;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IReparseableElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.parser.PerlParserImpl;
import com.perl5.lang.perl.psi.PerlLexerAwareParserDefinition;
import com.perl5.lang.perl.psi.impl.PerlCompositeElementImpl;
import com.perl5.lang.perl.util.PerlTimeLogger;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Stack;
import java.util.function.Function;

import static com.perl5.lang.perl.idea.editor.PerlBraceMatcher.PERL_BRACES_MAP;
import static com.perl5.lang.perl.idea.editor.PerlBraceMatcher.PERL_BRACES_MAP_REVERSED;
import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.*;


public abstract class PerlReparseableElementType extends IReparseableElementType implements PsiElementProvider {
  private static final ClearableLazyValue<TokenSet> REPARSING_PREVENTING_CONTAINERS = ClearableLazyValue.create(
    () -> TokenSet.create(
      HEREDOC_QQ, HEREDOC_QX, STRING_DQ, STRING_XQ, MATCH_REGEX, COMPILE_REGEX, REPLACEMENT_REGEX
    ));
  protected static final Logger LOG = Logger.getInstance(PerlReparseableElementType.class);
  private final Function<ASTNode, PsiElement> myInstanceFactory;

  public PerlReparseableElementType(@NotNull @NonNls String debugName) {
    this(debugName, PerlCompositeElementImpl.class);
  }

  public PerlReparseableElementType(@NotNull @NonNls String debugName, @NotNull Class<? extends PsiElement> clazz) {
    super(debugName, PerlLanguage.INSTANCE);
    myInstanceFactory = PerlElementTypeEx.createInstanceFactory(clazz);
  }

  @Override
  public @Nullable ASTNode createNode(CharSequence text) {
    return ASTFactory.DefaultFactoryHolder.DEFAULT.createLazy(this, text);
  }

  @Override
  public ASTNode parseContents(@NotNull ASTNode chameleon) {
    PerlTimeLogger logger = PerlTimeLogger.create(LOG);
    PsiElement parentElement = chameleon.getTreeParent().getPsi();
    Project project = parentElement.getProject();
    CharSequence newChars = chameleon.getChars();
    PsiBuilder builder = PsiBuilderFactory.getInstance().createBuilder(
      project,
      chameleon,
      createLexer(chameleon),
      getLanguage(),
      newChars);

    ASTNode result = PerlParserImpl.INSTANCE.parse(this, builder).getFirstChildNode();
    logger.trace("Parsed: ", PerlTimeLogger.kb(newChars.length()), " kb of ", this);
    return result;
  }

  /**
   * @return real node for reparsing or current node for lazy parsing.
   */
  protected @NotNull ASTNode getRealNode(@NotNull ASTNode chameleon) {
    Pair<ASTNode, CharSequence> originalNodeData = BlockSupport.TREE_TO_BE_REPARSED.get(chameleon);
    return originalNodeData == null ? chameleon : originalNodeData.first;
  }

  @Override
  public final @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
    return myInstanceFactory.apply(node);
  }

  @Override
  public final boolean isParsable(@Nullable ASTNode parent,
                                  @NotNull CharSequence buffer,
                                  @NotNull Language fileLanguage,
                                  @NotNull Project project) {
    if (parent == null || !isParentOk(parent)) {
      return false;
    }
    return isReparseableOld(parent, buffer, fileLanguage, project);
  }

  protected boolean isParentOk(@NotNull ASTNode parent) {
    ASTNode run = parent;
    while (run != null) {
      if (REPARSING_PREVENTING_CONTAINERS.getValue().contains(PsiUtilCore.getElementType(run))) {
        return false;
      }
      run = run.getTreeParent();
    }
    return true;
  }

  protected abstract boolean isReparseableOld(@NotNull ASTNode parent,
                                              @NotNull CharSequence buffer,
                                              @NotNull Language fileLanguage,
                                              @NotNull Project project);

  @Override
  public String toString() {
    return "Perl5: " + super.toString();
  }

  protected static boolean checkBracesBalance(@NotNull Lexer lexer, Stack<IElementType> bracesStack) {
    while (true) {
      ProgressManager.checkCanceled();
      IElementType type = lexer.getTokenType();

      if (type == null) {
        //eof: checking balance
        return bracesStack.isEmpty();
      }

      if (bracesStack.isEmpty()) {
        //the last brace is not the last token
        return false;
      }

      if (PERL_BRACES_MAP.containsKey(type)) {
        bracesStack.push(type);
      }
      else {
        IElementType oppositeBrace = PERL_BRACES_MAP_REVERSED.get(type);
        if (oppositeBrace != null) {
          if (bracesStack.isEmpty() || bracesStack.pop() != oppositeBrace) {
            return false;
          }
        }
      }
      lexer.advance();
    }
  }

  protected static void skipSpaces(@NotNull Lexer flexAdapter) {
    while (flexAdapter.getTokenType() == TokenType.WHITE_SPACE) {
      flexAdapter.advance();
    }
  }

  protected static @NotNull DelegateLexer createLexerWithCustomInitialState(int startState, Lexer lexer) {
    return new DelegateLexer(lexer) {
      @Override
      public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
        super.start(buffer, startOffset, endOffset, startState);
      }
    };
  }

  public static @NotNull Lexer createLexer(@NotNull ASTNode nodeToLex) {
    return createLexer(nodeToLex, nodeToLex.getElementType());
  }

  public static @NotNull Lexer createLexer(@NotNull ASTNode context, @NotNull IElementType elementType) {
    PsiElement psiElement = context.getPsi();
    PsiFile containingFile = psiElement.getContainingFile();
    FileViewProvider fileViewProvider = containingFile.getViewProvider();
    Language baseLanguage = fileViewProvider.getBaseLanguage();
    ParserDefinition parserDefinition = LanguageParserDefinitions.INSTANCE.forLanguage(baseLanguage);
    Lexer lexer = parserDefinition.createLexer(psiElement.getProject());
    int alternativeInitialState = parserDefinition instanceof PerlLexerAwareParserDefinition ?
                                  ((PerlLexerAwareParserDefinition)parserDefinition).getLexerStateFor(context, elementType) : 0;
    return alternativeInitialState == 0 ? lexer : createLexerWithCustomInitialState(alternativeInitialState, lexer);
  }
}