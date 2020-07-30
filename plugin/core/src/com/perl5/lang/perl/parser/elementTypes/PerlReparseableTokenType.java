/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

import com.intellij.lang.ASTNode;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.impl.source.tree.FileElement;
import com.intellij.psi.impl.source.tree.TreeElement;
import com.intellij.psi.impl.source.tree.TreeUtil;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IReparseableLeafElementType;
import com.perl5.lang.perl.lexer.PerlLexingContext;
import com.perl5.lang.perl.lexer.adapters.PerlMergingLexerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PerlReparseableTokenType extends PerlTokenTypeEx implements IReparseableLeafElementType<ASTNode> {
  private static final Logger LOG = Logger.getInstance(PerlReparseableTokenType.class);

  public PerlReparseableTokenType(@NotNull String debugName,
                                  Class<? extends ASTNode> clazz) {
    super(debugName, clazz);
  }

  /**
   * @return true iff {@code newText} may replace the {@code leaf} text without breaking things
   */
  protected boolean isReparseable(@NotNull ASTNode leaf, @NotNull CharSequence newText) {
    TextRange confirmationRange = getLexerConfirmationRange(leaf);
    if (confirmationRange.isEmpty()) {
      return false;
    }
    TextRange leafTextRange = leaf.getTextRange();
    if (!confirmationRange.contains(leafTextRange)) {
      LOG.error("Confirmation range must cover the leaf: " +
                "; confirmation range: " + confirmationRange +
                "; leaf: " + leaf +
                "; leafRange: " + leafTextRange);
      return false;
    }

    Lexer lexer = createLexer(leaf);
    if (lexer == null) {
      LOG.debug("Unable to create lexer for ", leaf);
      return false;
    }

    FileElement fileElement = TreeUtil.getFileElement((TreeElement)leaf);
    if (fileElement == null) {
      LOG.debug("No file found for ", leaf);
      return false;
    }
    CharSequence currentFileChars = fileElement.getChars();
    String textToRelex = currentFileChars.subSequence(confirmationRange.getStartOffset(), leaf.getStartOffset()).toString() +
                         newText +
                         currentFileChars.subSequence(leaf.getStartOffset() + leaf.getTextLength(), confirmationRange.getEndOffset());
    LOG.debug("Re-lexing for ", leaf, ": ", textToRelex);
    lexer.start(textToRelex);
    int newTokenStartOffset = leaf.getStartOffset() - confirmationRange.getStartOffset();
    int newTokenEndOffset = newTokenStartOffset + newText.length();
    while (lexer.getTokenType() != null) {
      int startOffset = lexer.getTokenStart();
      if (startOffset == newTokenStartOffset) {
        if (lexer.getTokenEnd() == newTokenEndOffset && lexer.getTokenType() == this) {
          lexer.advance();
          break;
        }
        return false;
      }
      if (lexer.getTokenEnd() > newTokenStartOffset) {
        return false;
      }
      lexer.advance();
    }

    // checking tokens after leaf
    int offsetShift = leafTextRange.getEndOffset() - newTokenEndOffset;
    ASTNode run = TreeUtil.nextLeaf(leaf);
    while (true) {
      IElementType tokenType = lexer.getTokenType();
      if (tokenType == null) {
        break;
      }
      if (run == null) {
        return false;
      }
      if (tokenType != run.getElementType()) {
        return false;
      }
      if (lexer.getTokenEnd() != run.getTextRange().getEndOffset() - offsetShift) {
        return false;
      }

      lexer.advance();
      run = TreeUtil.nextLeaf(run);
    }

    return true;
  }

  protected @Nullable Lexer createLexer(@NotNull ASTNode nodeToLex) {
    return new PerlMergingLexerAdapter(PerlLexingContext.create(nodeToLex.getPsi().getProject()).withEnforcedSublexing(true));
  }

  /**
   * @return range of the file text which is safe to re-lex to check this token for consistency or empty range if not possible
   */
  protected abstract @NotNull TextRange getLexerConfirmationRange(@NotNull ASTNode leaf);

  @Override
  public final @Nullable ASTNode reparseLeaf(@NotNull ASTNode leaf, @NotNull CharSequence newText) {
    return isReparseable(leaf, newText) ? createLeafNode(newText) : null;
  }
}
