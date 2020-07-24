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
import com.intellij.lang.Language;
import com.intellij.lang.PsiBuilderUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.lexer.PerlElementTypesGenerated;
import com.perl5.lang.perl.lexer.PerlLexer;
import com.perl5.lang.perl.lexer.PerlLexingContext;
import com.perl5.lang.perl.lexer.adapters.PerlMergingLexerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.*;


public class PerlLazyCodeBlockElementType extends PerlLazyBlockElementType {

  public PerlLazyCodeBlockElementType(@NotNull String debugName,
                                      @NotNull Class<? extends PsiElement> clazz) {
    super(debugName, clazz);
  }

  @Override
  public boolean isParsable(@Nullable ASTNode parent,
                            @NotNull CharSequence buffer,
                            @NotNull Language fileLanguage,
                            @NotNull Project project) {
    // fixme we should probably check file for use TryCatch, hacky but still
    if (PsiUtilCore.getElementType(parent) == REPLACEMENT_REGEX) {
      ASTNode openQuoteNode = parent.findChildByType(REGEX_QUOTE_E);
      if (openQuoteNode == null) {
        openQuoteNode = parent.findChildByType(REGEX_QUOTE_OPEN_E);
      }
      if (openQuoteNode == null) {
        return false;
      }
      char openQuoteChar = openQuoteNode.getChars().charAt(0);
      return PerlLexer.checkQuoteLikeBodyConsistency(buffer, openQuoteChar);
    }
    else {
      PerlMergingLexerAdapter lexer = new PerlMergingLexerAdapter(PerlLexingContext.create(project));
      boolean result =
        PsiBuilderUtil.hasProperBraceBalance(buffer, lexer, PerlElementTypesGenerated.LEFT_BRACE, PerlElementTypesGenerated.RIGHT_BRACE);
      if (LOG.isDebugEnabled()) {
        LOG.debug("Block reparseable: ", result && lexer.getState() == 0,
                  "; balanced: ", result,
                  "; lexer state: ", lexer.getState());
      }
      return result && lexer.getState() == 0;
    }
  }

  @Override
  public String toString() {
    return "Perl5: " + super.toString();
  }
}
