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

package com.perl5.lang.tt2.formatter;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.FormattingModel;
import com.intellij.formatting.Wrap;
import com.intellij.formatting.templateLanguages.DataLanguageBlockWrapper;
import com.intellij.formatting.templateLanguages.TemplateLanguageBlock;
import com.intellij.formatting.templateLanguages.TemplateLanguageFormattingModelBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.formatter.DocumentBasedFormattingModel;
import com.intellij.psi.templateLanguages.SimpleTemplateLanguageFormattingModelBuilder;
import com.perl5.lang.tt2.elementTypes.TemplateToolkitElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by hurricup on 10.07.2016.
 * based on handlebars plugin formatter
 */
public class TemplateToolkitFormattingModelBuilder extends TemplateLanguageFormattingModelBuilder
{
	@Override
	public TemplateLanguageBlock createTemplateLanguageBlock(
			@NotNull ASTNode node,
			@Nullable Wrap wrap,
			@Nullable Alignment alignment,
			@Nullable List<DataLanguageBlockWrapper> foreignChildren,
			@NotNull CodeStyleSettings codeStyleSettings
	)
	{
		return new TemplateToolkitFormattingBlock(this, codeStyleSettings, node, foreignChildren);
	}

	/**
	 * We have to override {@link com.intellij.formatting.templateLanguages.TemplateLanguageFormattingModelBuilder#createModel}
	 * since after we delegate to some templated languages, those languages (xml/html for sure, potentially others)
	 * delegate right back to us to format the TemplateToolkitElementTypes.TT2_OUTER token we tell them to ignore,
	 * causing an stack-overflowing loop of polite format-delegation.
	 */

	@NotNull
	@Override
	public FormattingModel createModel(PsiElement element, CodeStyleSettings settings)
	{
		final PsiFile file = element.getContainingFile();
		Block rootBlock;

		ASTNode node = element.getNode();

		if (node.getElementType() == TemplateToolkitElementTypes.TT2_OUTER)
		{
			// If we're looking at a TemplateToolkitElementTypes.TT2_OUTER element, then we've been invoked by our templated
			// language.  Make a dummy block to allow that formatter to continue
			return new SimpleTemplateLanguageFormattingModelBuilder().createModel(element, settings);
		}
		else
		{
			rootBlock = getRootBlock(file, file.getViewProvider(), settings);
		}
		return new DocumentBasedFormattingModel(rootBlock, element.getProject(), settings, file.getFileType(), file);

//		return super.createModel(element, settings);
	}

	@Override
	public boolean dontFormatMyModel()
	{
		return false;
	}
}
