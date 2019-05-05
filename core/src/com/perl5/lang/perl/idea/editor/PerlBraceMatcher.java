/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.editor;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlSubDefinitionElement;
import com.perl5.lang.perl.psi.PsiPerlBlock;
import com.perl5.lang.perl.psi.PsiPerlConditionalBlock;
import com.perl5.lang.perl.psi.PsiPerlForCompound;
import com.perl5.lang.perl.psi.impl.PsiPerlIfCompoundImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class PerlBraceMatcher implements PairedBraceMatcher, PerlElementTypes {
  private static final BracePair[] PAIRS = new BracePair[]{
    new BracePair(REGEX_QUOTE_OPEN, REGEX_QUOTE_CLOSE, false),
    new BracePair(REGEX_QUOTE_OPEN_E, REGEX_QUOTE_CLOSE, false),
    new BracePair(QUOTE_DOUBLE_OPEN, QUOTE_DOUBLE_CLOSE, false),
    new BracePair(QUOTE_SINGLE_OPEN, QUOTE_SINGLE_CLOSE, false),
    new BracePair(QUOTE_TICK_OPEN, QUOTE_TICK_CLOSE, false),
    new BracePair(LEFT_PAREN, RIGHT_PAREN, false),
    new BracePair(LEFT_BRACKET, RIGHT_BRACKET, false),
    new BracePair(LEFT_ANGLE, RIGHT_ANGLE, false),
    new BracePair(LEFT_BRACE, RIGHT_BRACE, true),
    new BracePair(LEFT_BRACE_SCALAR, RIGHT_BRACE_SCALAR, true),
    new BracePair(LEFT_BRACE_ARRAY, RIGHT_BRACE_ARRAY, true),
    new BracePair(LEFT_BRACE_HASH, RIGHT_BRACE_HASH, true),
    new BracePair(LEFT_BRACE_CODE, RIGHT_BRACE_CODE, true),
    new BracePair(LEFT_BRACE_GLOB, RIGHT_BRACE_GLOB, true),
  };

  @NotNull
  @Override
  public BracePair[] getPairs() {
    return PAIRS;
  }

  @Override
  public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType lbraceType, @Nullable IElementType contextType) {
    return true;
  }

  @Override
  public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
    PsiElement element = file.findElementAt(openingBraceOffset);
    if (element == null || element instanceof PsiFile) {
      return openingBraceOffset;
    }
    PsiElement codeBlock = element.getParent();

    if (!(codeBlock instanceof PsiPerlBlock)) {
      return openingBraceOffset;
    }
    PsiElement blockContainer = codeBlock.getParent();

    if (blockContainer == null) {
      return openingBraceOffset;
    }
    if (blockContainer instanceof PerlSubDefinitionElement || blockContainer instanceof PsiPerlForCompound) {
      return blockContainer.getTextOffset();
    }

    if (!(blockContainer instanceof PsiPerlConditionalBlock) && !(blockContainer instanceof PsiPerlIfCompoundImpl)) {
      return openingBraceOffset;
    }

    PsiElement keyword = blockContainer.getPrevSibling();

    while ((keyword instanceof PsiWhiteSpace || keyword instanceof PsiComment)) {
      keyword = keyword.getPrevSibling();
    }

    return keyword != null ? keyword.getTextOffset() : openingBraceOffset;
  }
}
