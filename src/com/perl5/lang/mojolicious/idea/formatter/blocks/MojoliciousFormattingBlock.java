/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.mojolicious.idea.formatter.blocks;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.formatting.Wrap;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.formatter.common.InjectedLanguageBlockBuilder;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.mojolicious.MojoliciousElementTypes;
import com.perl5.lang.mojolicious.idea.formatter.MojoliciousIndentProcessor;
import com.perl5.lang.perl.idea.formatter.PerlIndentProcessor;
import com.perl5.lang.perl.idea.formatter.blocks.PerlFormattingBlock;
import com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 09.01.2016.
 */
public class MojoliciousFormattingBlock extends PerlFormattingBlock implements MojoliciousElementTypes {
  private static final TokenSet LINE_OPENERS = TokenSet.create(
    MOJO_LINE_OPENER, MOJO_LINE_EXPR_OPENER, MOJO_LINE_EXPR_ESCAPED_OPENER
  );

  public MojoliciousFormattingBlock(@NotNull ASTNode node,
                                    @Nullable Wrap wrap,
                                    @Nullable Alignment alignment,
                                    @NotNull CommonCodeStyleSettings codeStyleSettings,
                                    @NotNull PerlCodeStyleSettings perlCodeStyleSettings,
                                    @NotNull SpacingBuilder spacingBuilder,
                                    @NotNull InjectedLanguageBlockBuilder injectedLanguageBlockBuilder
  ) {
    super(node, wrap, alignment, codeStyleSettings, perlCodeStyleSettings, spacingBuilder, injectedLanguageBlockBuilder);
  }

  @Override
  protected boolean isNewLineForbidden(PerlFormattingBlock block) {
    if (super.isNewLineForbidden(block)) {
      return true;
    }

    PsiElement element = block.getNode().getPsi();
    PsiFile file = element.getContainingFile();
    Document document = PsiDocumentManager.getInstance(file.getProject()).getDocument(file);
    if (document != null) {
      int offset = block.getTextRange().getStartOffset();
      int lineNumber = document.getLineNumber(offset);
      int lineStartOffset = document.getLineStartOffset(lineNumber);
      PsiElement firstElement = file.findElementAt(lineStartOffset);


      while (!element.equals(firstElement)) {
        if (firstElement == null) {
          return false;
        }

        if (LINE_OPENERS.contains(PsiUtilCore.getElementType(firstElement))) {
          return true;
        }
        if (!(firstElement instanceof PsiWhiteSpace)) {
          return false;
        }

        firstElement = firstElement.getNextSibling();
      }

      if (LINE_OPENERS.contains(PsiUtilCore.getElementType(firstElement))) {
        return true;
      }
    }

    return false;
  }

  @Override
  protected PerlFormattingBlock createBlock(@NotNull ASTNode node, @Nullable Wrap wrap, @Nullable Alignment alignment) {
    return new MojoliciousFormattingBlock(node, wrap, alignment, getSettings(), getPerl5Settings(), getSpacingBuilder(),
                                          getInjectedLanguageBlockBuilder());
  }

  @Override
  protected PerlIndentProcessor getIndentProcessor() {
    return MojoliciousIndentProcessor.INSTANCE;
  }
}
