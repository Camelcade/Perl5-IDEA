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
import com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings;
import com.perl5.lang.perl.lexer.PerlElementTypes;

/**
 * Created by hurricup on 03.09.2015.
 */
public class PerlIndentProcessor implements PerlElementTypes
{
	public static final TokenSet UNINDENTED_TOKENS = TokenSet.create(
			HEREDOC,
			HEREDOC_QQ,
			HEREDOC_QX,
			HEREDOC_END
	);
	public static final TokenSet BLOCK_LIKE_TOKENS = TokenSet.create(
			BLOCK,
			PARENTHESISED_EXPR,
			ANON_ARRAY,
			ANON_HASH
	);
	private final PerlCodeStyleSettings myCodeStyleSettings;

	public PerlIndentProcessor(PerlCodeStyleSettings codeStyleSettings)
	{
		this.myCodeStyleSettings = codeStyleSettings;
	}

	public Indent getChildIndent(ASTNode node, int binaryExpressionIndex)
	{
		// fixme wtf is this
		if (binaryExpressionIndex > 0)
			return Indent.getNormalIndent();

		IElementType nodeType = node.getElementType();
		ASTNode parent = node.getTreeParent();
		IElementType parentType = parent != null ? parent.getElementType() : null;
		ASTNode grandParent = parent != null ? parent.getTreeParent() : null;
		IElementType grandParentType = grandParent != null ? grandParent.getElementType() : null;
		ASTNode prevSibling = FormatterUtil.getPreviousNonWhitespaceSibling(node);
		IElementType prevSiblingElementType = prevSibling != null ? prevSibling.getElementType() : null;
		ASTNode nextSibling = FormatterUtil.getNextNonWhitespaceSibling(node);
		IElementType nextSiblingElementType = nextSibling != null ? nextSibling.getElementType() : null;

		if (UNINDENTED_TOKENS.contains(nodeType) || parent == null || grandParent == null)
			return Indent.getAbsoluteNoneIndent();

		if (BLOCK_LIKE_TOKENS.contains(parentType) && nextSibling != null && prevSibling != null)
			return Indent.getNormalIndent();

		return Indent.getNoneIndent();
	}
}

