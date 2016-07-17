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

import com.intellij.formatting.*;
import com.intellij.formatting.templateLanguages.DataLanguageBlockWrapper;
import com.intellij.formatting.templateLanguages.TemplateLanguageBlock;
import com.intellij.formatting.templateLanguages.TemplateLanguageFormattingModelBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.formatter.DocumentBasedFormattingModel;
import com.intellij.psi.formatter.FormattingDocumentModelImpl;
import com.intellij.psi.formatter.common.DefaultInjectedLanguageBlockBuilder;
import com.intellij.psi.formatter.common.InjectedLanguageBlockBuilder;
import com.intellij.psi.formatter.xml.HtmlPolicy;
import com.intellij.psi.templateLanguages.SimpleTemplateLanguageFormattingModelBuilder;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.tt2.TemplateToolkitLanguage;
import com.perl5.lang.tt2.elementTypes.TemplateToolkitElementTypes;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

import static com.perl5.lang.tt2.lexer.TemplateToolkitSyntaxElements.ALL_OPERATORS_TOKENSET;
import static com.perl5.lang.tt2.lexer.TemplateToolkitSyntaxElements.KEYWORDS_TOKENSET;

/**
 * Created by hurricup on 10.07.2016.
 * based on handlebars plugin formatter
 */
public class TemplateToolkitFormattingModelBuilder extends TemplateLanguageFormattingModelBuilder implements TemplateToolkitElementTypes
{
	private final Map<ASTNode, Alignment> myAssignAlignmentMap = new THashMap<ASTNode, Alignment>();
	private SpacingBuilder mySpacingBuilder;
	private InjectedLanguageBlockBuilder myInjectedLanguageBlockBuilder;

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
		if (mySpacingBuilder == null)
		{
			createSpacingBuilder(settings);
		}
		if (myInjectedLanguageBlockBuilder == null)
		{
			myInjectedLanguageBlockBuilder = new DefaultInjectedLanguageBlockBuilder(settings);
		}

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
	}

	public SpacingBuilder getSpacingBuilder()
	{
		return mySpacingBuilder;
	}

	public InjectedLanguageBlockBuilder getInjectedLanguageBlockBuilder()
	{
		return myInjectedLanguageBlockBuilder;
	}

	protected void createSpacingBuilder(CodeStyleSettings settings)
	{
		CommonCodeStyleSettings commonSettings = settings.getCommonSettings(PerlLanguage.INSTANCE);
//		PerlCodeStyleSettings perlSettings = settings.getCustomSettings(PerlCodeStyleSettings.class);

		mySpacingBuilder = new SpacingBuilder(commonSettings.getRootSettings(), TemplateToolkitLanguage.INSTANCE)
				.around(TT2_PERIOD).spaces(0)
				.after(TT2_MINUS_UNARY).spaces(0)
				.after(TT2_OUTLINE_TAG).spaces(1)
				.after(TT2_OPEN_TAG).spaces(1)
				.before(TT2_SEMI).spaces(0)
				.after(TT2_SEMI).spaces(1)
				.afterInside(TT2_NOT, UNARY_EXPR).spaces(0)
				.before(TT2_CLOSE_TAG).spaces(1)
				.around(KEYWORDS_TOKENSET).spaces(1)
				.before(MACRO_CONTENT).spaces(1)
				.before(DIRECTIVE_POSTFIX).spaces(1)
				.around(ALL_OPERATORS_TOKENSET).spaces(1)
		;

	}

	@Override
	public TemplateLanguageBlock createTemplateLanguageBlock(
			@NotNull ASTNode node,
			@Nullable Wrap wrap,
			@Nullable Alignment alignment,
			@Nullable List<DataLanguageBlockWrapper> foreignChildren,
			@NotNull CodeStyleSettings codeStyleSettings
	)
	{
		final FormattingDocumentModelImpl documentModel = FormattingDocumentModelImpl.createOn(node.getPsi().getContainingFile());
		return new TemplateToolkitFormattingBlock(this, codeStyleSettings, node, foreignChildren, new HtmlPolicy(codeStyleSettings, documentModel));
	}

	@Override
	public boolean dontFormatMyModel()
	{
		return false;
	}

	@NotNull
	public Alignment getAssignAlignment(@NotNull ASTNode defaultDirectiveNode)
	{
		Alignment result = myAssignAlignmentMap.get(defaultDirectiveNode);
		if (result != null)
		{
			return result;
		}

		result = Alignment.createAlignment(true);
		myAssignAlignmentMap.put(defaultDirectiveNode, result);
		return result;
	}
}
