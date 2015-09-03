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
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings;
import com.perl5.lang.perl.lexer.PerlElementTypes;

/**
 * Created by hurricup on 03.09.2015.
 */
public class PerlIndentProcessor implements PerlElementTypes
{
	private final PerlCodeStyleSettings myCodeStyleSettings;

	public PerlIndentProcessor(PerlCodeStyleSettings codeStyleSettings)
	{
		this.myCodeStyleSettings = codeStyleSettings;
	}

	public Indent getChildIndent(ASTNode node, int binaryExpressionIndex)
	{
		// fixme wtf is this
		if (binaryExpressionIndex > 0) return com.intellij.formatting.Indent.getNormalIndent();
		IElementType nodeType = node.getElementType();

		// fixme not working
		if (nodeType == HEREDOC_END)
			return Indent.getAbsoluteNoneIndent();

		//	return com.intellij.formatting.Indent.getNormalIndent();
		return Indent.getNoneIndent();
	}
}

