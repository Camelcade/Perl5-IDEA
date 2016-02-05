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

package com.perl5.lang.mason2.elementType;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.templateLanguages.TemplateDataElementType;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.mason2.MasonSyntaxElements;
import com.perl5.lang.mason2.MasonTemplatingLanguage;
import com.perl5.lang.mason2.psi.impl.*;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 21.12.2015.
 */
public interface MasonElementTypes extends MasonSyntaxElements
{
	IElementType MASON_TEMPLATE_BLOCK_HTML = new MasonTemplatingTokenType("MASON_TEMPLATE_BLOCK_HTML");
	IElementType MASON_OUTER_ELEMENT_TYPE = new MasonTemplatingElementType("MASON_OUTER_ELEMENT_TYPE");
	IElementType MASON_HTML_TEMPLATE_DATA = new TemplateDataElementType("MASON_HTML_TEMPLATE_DATA", MasonTemplatingLanguage.INSTANCE, MASON_TEMPLATE_BLOCK_HTML, MASON_OUTER_ELEMENT_TYPE);

	IElementType MASON_FILTERED_BLOCK_OPENER = new MasonTemplatingTokenType(KEYWORD_FILTERED_BLOCK_OPENER);
	IElementType MASON_FILTERED_BLOCK_CLOSER = new MasonTemplatingTokenType(KEYWORD_FILTERED_BLOCK_CLOSER);
	IElementType MASON_METHOD_MODIFIER_NAME = new MasonTemplatingTokenTypeEx("MASON_METHOD_MODIFIER_NAME")
	{
		@NotNull
		@Override
		public ASTNode createLeafNode(CharSequence leafText)
		{
			return new MasonMethodModifierNameImpl(this, leafText);
		}
	};
	IElementType MASON_SELF_POINTER = new MasonTemplatingTokenType(KEYWORD_SELF_POINTER);

	IElementType MASON_BLOCK_OPENER = new MasonTemplatingTokenType(KEYWORD_BLOCK_OPENER);

	IElementType MASON_BLOCK_CLOSER = new MasonTemplatingTokenType(KEYWORD_BLOCK_CLOSER);
	IElementType MASON_TAG_CLOSER = new MasonTemplatingTokenType(">");

	IElementType MASON_CALL_OPENER = new MasonTemplatingTokenType(KEYWORD_CALL_OPENER);
	IElementType MASON_CALL_CLOSER = new MasonTemplatingTokenType(KEYWORD_CALL_CLOSER);

	IElementType MASON_LINE_OPENER = new MasonTemplatingTokenType("%");
	IElementType MASON_EXPR_FILTER_PIPE = new MasonTemplatingTokenType("|");

	IElementType MASON_METHOD_OPENER = new MasonTemplatingTokenType(KEYWORD_METHOD_OPENER);
	IElementType MASON_METHOD_CLOSER = new MasonTemplatingTokenType(KEYWORD_METHOD_CLOSER);

	IElementType MASON_CLASS_OPENER = new MasonTemplatingTokenType(KEYWORD_CLASS_OPENER);
	IElementType MASON_CLASS_CLOSER = new MasonTemplatingTokenType(KEYWORD_CLASS_CLOSER);

	IElementType MASON_DOC_OPENER = new MasonTemplatingTokenType(KEYWORD_DOC_OPENER);
	IElementType MASON_DOC_CLOSER = new MasonTemplatingTokenType(KEYWORD_DOC_CLOSER);

	IElementType MASON_FLAGS_OPENER = new MasonTemplatingTokenType(KEYWORD_FLAGS_OPENER);
	IElementType MASON_FLAGS_CLOSER = new MasonTemplatingTokenType(KEYWORD_FLAGS_CLOSER);

	IElementType MASON_INIT_OPENER = new MasonTemplatingTokenType(KEYWORD_INIT_OPENER);
	IElementType MASON_INIT_CLOSER = new MasonTemplatingTokenType(KEYWORD_INIT_CLOSER);

	IElementType MASON_PERL_OPENER = new MasonTemplatingTokenType(KEYWORD_PERL_OPENER);
	IElementType MASON_PERL_CLOSER = new MasonTemplatingTokenType(KEYWORD_PERL_CLOSER);

	IElementType MASON_TEXT_OPENER = new MasonTemplatingTokenType(KEYWORD_TEXT_OPENER);
	IElementType MASON_TEXT_CLOSER = new MasonTemplatingTokenType(KEYWORD_TEXT_CLOSER);

	IElementType MASON_FILTER_OPENER = new MasonTemplatingTokenType(KEYWORD_FILTER_OPENER);
	IElementType MASON_FILTER_CLOSER = new MasonTemplatingTokenType(KEYWORD_FILTER_CLOSER);

	IElementType MASON_AFTER_OPENER = new MasonTemplatingTokenType(KEYWORD_AFTER_OPENER);
	IElementType MASON_AFTER_CLOSER = new MasonTemplatingTokenType(KEYWORD_AFTER_CLOSER);

	IElementType MASON_AUGMENT_OPENER = new MasonTemplatingTokenType(KEYWORD_AUGMENT_OPENER);
	IElementType MASON_AUGMENT_CLOSER = new MasonTemplatingTokenType(KEYWORD_AUGMENT_CLOSER);

	IElementType MASON_AROUND_OPENER = new MasonTemplatingTokenType(KEYWORD_AROUND_OPENER);
	IElementType MASON_AROUND_CLOSER = new MasonTemplatingTokenType(KEYWORD_AROUND_CLOSER);

	IElementType MASON_BEFORE_OPENER = new MasonTemplatingTokenType(KEYWORD_BEFORE_OPENER);
	IElementType MASON_BEFORE_CLOSER = new MasonTemplatingTokenType(KEYWORD_BEFORE_CLOSER);

	IElementType MASON_OVERRIDE_OPENER = new MasonTemplatingTokenType(KEYWORD_OVERRIDE_OPENER);
	IElementType MASON_OVERRIDE_CLOSER = new MasonTemplatingTokenType(KEYWORD_OVERRIDE_CLOSER);

	IElementType MASON_NAMESPACE_DEFINITION = new MasonNamespaceElementType("MASON_PACKAGE");

	IElementType MASON_AUGMENT_MODIFIER = new MasonAugmentMethodModifierElementType("MASON_AUGMENT_MODIFIER");
	IElementType MASON_OVERRIDE_DEFINITION = new MasonOverrideStubElementType("MASON_OVERRIDE_DEFINITION");
	IElementType MASON_METHOD_DEFINITION = new MasonMethodDefinitionStubElementType("MASON_METHOD_DEFINITION");
	IElementType MASON_FILTER_DEFINITION = new MasonFilterDefinitionStubElementType("MASON_FILTER_DEFINITION");

	IElementType MASON_AFTER_MODIFIER = new MasonMethodModifierElementType("MASON_AFTER_MODIFIER");
	IElementType MASON_BEFORE_MODIFIER = new MasonMethodModifierElementType("MASON_BEFOE_MODIFIER");

	IElementType MASON_FLAGS_STATEMENT = new MasonTemplatingElementType("FLAGS_STATEMENT")
	{
		@NotNull
		@Override
		public PsiElement getPsiElement(@NotNull ASTNode node)
		{
			return new MasonFlagsStatementImpl(node);
		}
	};
	IElementType MASON_CALL_STATEMENT = new MasonTemplatingElementType("MASON_CALL_STATEMENT")
	{
		@NotNull
		@Override
		public PsiElement getPsiElement(@NotNull ASTNode node)
		{
			return new MasonCallStatementImpl(node);
		}
	};

	IElementType MASON_AROUND_MODIFIER = new MasonTemplatingElementType("MASON_AROUND_MODIFIER")
	{
		@NotNull
		@Override
		public PsiElement getPsiElement(@NotNull ASTNode node)
		{
			return new MasonAroundMethodModifierImpl(node);
		}
	};

	IElementType MASON_ABSTRACT_BLOCK = new MasonTemplatingElementType("MASON_ABSTRACT_BLOCK")
	{
		@NotNull
		@Override
		public PsiElement getPsiElement(@NotNull ASTNode node)
		{
			return new MasonAbstractBlockImpl(node);
		}
	};

	IElementType MASON_TEXT_BLOCK = new MasonTemplatingElementType("MASON_TEXT_BLOCK")
	{
		@NotNull
		@Override
		public PsiElement getPsiElement(@NotNull ASTNode node)
		{
			return new MasonTextBlockImpl(node);
		}
	};

	IElementType MASON_FILTERED_BLOCK = new MasonTemplatingElementType("MASON_FILTERED_BLOCK")
	{
		@NotNull
		@Override
		public PsiElement getPsiElement(@NotNull ASTNode node)
		{
			return new MasonFilteredBlockImpl(node);
		}
	};

	IElementType MASON_SIMPLE_DEREF_EXPR = new MasonTemplatingElementType("MASON_DEREF_EXPRESSION")
	{
		@NotNull
		@Override
		public PsiElement getPsiElement(@NotNull ASTNode node)
		{
			return new MasonSimpleDerefExpressionImpl(node);
		}
	};

}
