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

import com.intellij.formatting.Indent;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.htmlmason.HTMLMasonParserDefinition;
import com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes;
import com.perl5.lang.perl.idea.formatter.PerlIndentProcessor;
import com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 07.03.2016.
 */
public class HTMLMasonIndentProcessor extends PerlIndentProcessor implements HTMLMasonElementTypes
{
	public static final HTMLMasonIndentProcessor INSTANCE = new HTMLMasonIndentProcessor();

	public static final TokenSet ABSOLUTE_UNINDENTABLE_TOKENS = TokenSet.orSet(
			PerlIndentProcessor.ABSOLUTE_UNINDENTABLE_TOKENS,
			TokenSet.create(
					HTML_MASON_LINE_OPENER
			));

	public static final TokenSet UNINDENTABLE_CONTAINERS = TokenSet.orSet(
			PerlIndentProcessor.UNINDENTABLE_CONTAINERS,
			TokenSet.create(
					HTML_MASON_METHOD_DEFINITION,
					HTML_MASON_SUBCOMPONENT_DEFINITION,
					HTMLMasonParserDefinition.FILE
			));

	public static final TokenSet UNINDENTABLE_TOKENS = TokenSet.orSet(
			PerlIndentProcessor.UNINDENTABLE_TOKENS,
			TokenSet.create(
					HTML_MASON_FLAGS_OPENER,
					HTML_MASON_FLAGS_CLOSER,

					HTML_MASON_TEMPLATE_BLOCK_HTML,

					HTML_MASON_INIT_OPENER,
					HTML_MASON_INIT_CLOSER,

					HTML_MASON_SHARED_OPENER,
					HTML_MASON_SHARED_CLOSER,

					HTML_MASON_CLEANUP_OPENER,
					HTML_MASON_CLEANUP_CLOSER,

					HTML_MASON_ARGS_OPENER,
					HTML_MASON_ARGS_CLOSER,

					HTML_MASON_ATTR_OPENER,
					HTML_MASON_ATTR_CLOSER,

					HTML_MASON_ONCE_OPENER,
					HTML_MASON_ONCE_CLOSER,

					HTML_MASON_PERL_OPENER,
					HTML_MASON_PERL_CLOSER
			));

	public static final TokenSet MASON_BLOCK_CONTAINERS = TokenSet.create(
			HTML_MASON_ABSTRACT_BLOCK,
			HTML_MASON_ARGS_BLOCK,
			HTML_MASON_ATTR_BLOCK

	);


	@Override
	public TokenSet getAbsoluteUnindentableTokens()
	{
		return ABSOLUTE_UNINDENTABLE_TOKENS;
	}

	@Override
	public TokenSet getUnindentableContainers()
	{
		return UNINDENTABLE_CONTAINERS;
	}

	@Override
	public TokenSet getUnindentableTokens()
	{
		return UNINDENTABLE_TOKENS;
	}

	@Override
	public Indent getNodeIndent(@NotNull ASTNode node, PerlCodeStyleSettings codeStyleSettings)
	{
		IElementType nodeType = node.getElementType();
		ASTNode parent = node.getTreeParent();
		ASTNode grandParent = parent != null ? parent.getTreeParent() : null;

		IElementType parentType = parent != null ? parent.getElementType() : null;
		IElementType grandParentType = grandParent != null ? grandParent.getElementType() : null;

		boolean isFirst = node.getTreePrev() == null;
		boolean isLast = node.getTreeNext() == null;

		if (nodeType == HTML_MASON_METHOD_DEFINITION)
		{
			return Indent.getNoneIndent();
		}


		if (MASON_BLOCK_CONTAINERS.contains(parentType))
		{
			if (isFirst || isLast)
			{
				return Indent.getNoneIndent();
			}
			else
			{
				return Indent.getNormalIndent();
			}
		}

		if (parentType == BLOCK && MASON_BLOCK_CONTAINERS.contains(nodeType))
		{
			return Indent.getNormalIndent();
		}

		if (parentType == COMMA_SEQUENCE_EXPR && grandParentType == HTML_MASON_ATTR_BLOCK)
		{
			return Indent.getNoneIndent();
		}

		return super.getNodeIndent(node, codeStyleSettings);
	}
}
