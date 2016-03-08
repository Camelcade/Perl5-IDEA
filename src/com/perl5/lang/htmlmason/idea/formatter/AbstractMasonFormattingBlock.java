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

package com.perl5.lang.htmlmason.idea.formatter;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.formatting.Wrap;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.formatter.common.InjectedLanguageBlockBuilder;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.idea.formatter.blocks.PerlFormattingBlock;
import com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 08.03.2016.
 */
public abstract class AbstractMasonFormattingBlock extends PerlFormattingBlock
{
	public AbstractMasonFormattingBlock(@NotNull ASTNode node, @Nullable Wrap wrap, @Nullable Alignment alignment, @NotNull CommonCodeStyleSettings codeStyleSettings, @NotNull PerlCodeStyleSettings perlCodeStyleSettings, @NotNull SpacingBuilder spacingBuilder, @NotNull InjectedLanguageBlockBuilder injectedLanguageBlockBuilder)
	{
		super(node, wrap, alignment, codeStyleSettings, perlCodeStyleSettings, spacingBuilder, injectedLanguageBlockBuilder);
	}

	protected abstract IElementType getLineOpenerToken();

	@Override
	protected boolean isNewLineForbidden(PerlFormattingBlock block)
	{
		if (super.isNewLineForbidden(block))
			return true;

		PsiElement element = block.getNode().getPsi();
		PsiFile file = element.getContainingFile();
		Document document = PsiDocumentManager.getInstance(file.getProject()).getDocument(file);
		if (document != null)
		{
			int offset = block.getTextRange().getStartOffset();
			int lineNumber = document.getLineNumber(offset);
			int lineStartOffset = document.getLineStartOffset(lineNumber);
			PsiElement firstElement = file.findElementAt(lineStartOffset);

			if (firstElement != null && firstElement.getNode().getElementType() == getLineOpenerToken())
			{
				return true;
			}
		}

		return false;
	}

}
