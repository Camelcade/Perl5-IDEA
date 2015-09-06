/*
 * Copyright 2015 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.formatter;

import com.intellij.formatting.FormattingModel;
import com.intellij.formatting.FormattingModelBuilder;
import com.intellij.formatting.FormattingModelProvider;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 03.09.2015.
 */
public class PerlFormattingModelBuilder implements FormattingModelBuilder, PerlElementTypes
{
	private static SpacingBuilder createSpacingBuilder(@NotNull CommonCodeStyleSettings settings, @NotNull PerlCodeStyleSettings perlSettings)
	{
		SpacingBuilder spacingBuilder = new SpacingBuilder(settings.getRootSettings(), PerlLanguage.INSTANCE)
				.before(HEREDOC_END).none()

				.after(RESERVED_MY).spaceIf(perlSettings.SPACE_AFTER_VARIABLE_DECLARATION_KEYWORD)
				.after(RESERVED_OUR).spaceIf(perlSettings.SPACE_AFTER_VARIABLE_DECLARATION_KEYWORD)
				.after(RESERVED_LOCAL).spaceIf(perlSettings.SPACE_AFTER_VARIABLE_DECLARATION_KEYWORD)
				.after(RESERVED_STATE).spaceIf(perlSettings.SPACE_AFTER_VARIABLE_DECLARATION_KEYWORD)

				.around(OPERATOR_ASSIGN).spaceIf(settings.SPACE_AROUND_ASSIGNMENT_OPERATORS)

				.before(OPERATOR_COMMA).spaceIf(settings.SPACE_BEFORE_COMMA)
				.after(OPERATOR_COMMA).spaceIf(settings.SPACE_AFTER_COMMA)
				.around(OPERATOR_COMMA_ARROW).spacing(1, Integer.MAX_VALUE, 0, true, 1);

		for (IElementType type : PerlFormattingBlock.LF_ELEMENTS.getTypes())
			spacingBuilder = spacingBuilder.after(type).lineBreakInCode();

		return spacingBuilder;
	}

	@NotNull
	@Override
	public FormattingModel createModel(PsiElement element, CodeStyleSettings settings)
	{
		CommonCodeStyleSettings commonSettings = settings.getCommonSettings(PerlLanguage.INSTANCE);
		PerlCodeStyleSettings perlSettings = settings.getCustomSettings(PerlCodeStyleSettings.class);
		SpacingBuilder spacingBuilder = createSpacingBuilder(commonSettings, perlSettings);
		PerlFormattingBlock block = new PerlFormattingBlock(element.getNode(), null, null, commonSettings, perlSettings, spacingBuilder, -1);
		return FormattingModelProvider.createFormattingModelForPsiFile(element.getContainingFile(), block, settings);
	}

	@Nullable
	@Override
	public TextRange getRangeAffectingIndent(PsiFile file, int offset, ASTNode elementAtOffset)
	{
		return null;
	}
}
