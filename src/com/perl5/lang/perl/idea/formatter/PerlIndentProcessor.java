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

import com.intellij.formatting.Indent;
import com.intellij.lang.ASTNode;
import com.intellij.psi.formatter.FormatterUtil;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.PerlParserDefinition;
import com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings;
import com.perl5.lang.perl.lexer.PerlElementTypes;

/**
 * Created by hurricup on 03.09.2015.
 */
public class PerlIndentProcessor implements PerlElementTypes
{
	// containers which has none indentation
	public static final TokenSet UNINDENTABLE_CONTAINERS = TokenSet.create(
			NAMESPACE_DEFINITION,
			NAMESPACE_CONTENT,
			SUB_DEFINITION,
			METHOD_DEFINITION,
			FUNC_DEFINITION,
			IF_COMPOUND,
			FOR_COMPOUND,
			FOREACH_COMPOUND,
			CONDITIONAL_BLOCK,
			CONDITIONAL_BLOCK_WHILE,
			CONTINUE_BLOCK,
			UNCONDITIONAL_BLOCK,

			DO_EXPR,
			EVAL_EXPR,
			PerlParserDefinition.FILE
	);

	public static final TokenSet UNINDENTED_TOKENS = TokenSet.create(
			COMMA_SEQUENCE_EXPR,
			CALL_ARGUMENTS
	);

	public static final TokenSet COMMA_LIKE_SEQUENCES = TokenSet.create(
			COMMA_SEQUENCE_EXPR,
			SUB_SIGNATURE_CONTENT,
			METHOD_SIGNATURE_CONTENT,
			FUNC_SIGNATURE_CONTENT
	);

	/**
	 * Tokens that must be suppressed for indentation
	 */
	public static final TokenSet ABSOLUTE_UNINDENTED_TOKENS = TokenSet.create(
			HEREDOC,
			HEREDOC_QQ,
			HEREDOC_QX,
			HEREDOC_END,
			POD,
			FORMAT,
			FORMAT_TERMINATOR
	);
	private final PerlCodeStyleSettings myCodeStyleSettings;

	public PerlIndentProcessor(PerlCodeStyleSettings codeStyleSettings)
	{
		this.myCodeStyleSettings = codeStyleSettings;
	}

	public Indent getNodeIndent(ASTNode node)
	{
		IElementType nodeType = node.getElementType();
		ASTNode parent = node.getTreeParent();
		ASTNode grandParent = parent != null ? parent.getTreeParent() : null;

		IElementType parentType = parent != null ? parent.getElementType() : null;
		IElementType grandParentType = grandParent != null ? grandParent.getElementType() : null;

		ASTNode prevSibling = FormatterUtil.getPreviousNonWhitespaceSibling(node);
		IElementType prevSiblingElementType = prevSibling != null ? prevSibling.getElementType() : null;

		ASTNode nextSibling = FormatterUtil.getNextNonWhitespaceSibling(node);
		IElementType nextSiblingElementType = nextSibling != null ? nextSibling.getElementType() : null;

		// defined by node
		if (ABSOLUTE_UNINDENTED_TOKENS.contains(nodeType) || parent == null || grandParent == null)
		{
			return Indent.getAbsoluteNoneIndent();
		}

		if (UNINDENTED_TOKENS.contains(nodeType))
		{
			return Indent.getNoneIndent();
		}

		// defined by parent
		if (UNINDENTABLE_CONTAINERS.contains(parentType))
		{
			// a little magic for sub attributes
			if (parentType == SUB_DEFINITION)
			{
				if (nodeType == COLON && nextSiblingElementType == ATTRIBUTE ||
						nodeType == ATTRIBUTE && prevSiblingElementType != COLON
						)
				{
					return Indent.getContinuationIndent();
				}
			}

			return Indent.getNoneIndent();
		}

		if (COMMA_LIKE_SEQUENCES.contains(parentType))
		{
			return Indent.getIndent(Indent.Type.CONTINUATION, false, true);
		}

		return Indent.getContinuationWithoutFirstIndent();
	}
}

