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

package com.perl5.lang.tt2.idea.highlighting;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.tt2.elementTypes.TemplateToolkitElementTypes;
import com.perl5.lang.tt2.lexer.TemplateToolkitLexerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 05.06.2016.
 */
public class TemplateToolkitSyntaxHighlighter extends SyntaxHighlighterBase implements TemplateToolkitElementTypes
{
	private final Project myProject;
	private final TokenSet myMarkers = TokenSet.create(
			TT2_OPEN_TAG,
			TT2_CLOSE_TAG,
			TT2_OUTLINE_TAG
	);

	public TemplateToolkitSyntaxHighlighter(Project project)
	{
		myProject = project;
	}

	@NotNull
	@Override
	public Lexer getHighlightingLexer()
	{
		return new TemplateToolkitLexerAdapter(myProject);
	}

	@NotNull
	@Override
	public TextAttributesKey[] getTokenHighlights(IElementType tokenType)
	{
		if (myMarkers.contains(tokenType))
		{
			return PerlSyntaxHighlighter.EMBED_MARKER_KEYS;
		}
		else if (tokenType == TT2_NUMBER || tokenType == TT2_NUMBER_SIMPLE)
		{
			return PerlSyntaxHighlighter.ATTRIBUTES_MAP.get(PerlElementTypes.NUMBER);
		}
		else if (tokenType == LINE_COMMENT)
		{
			return PerlSyntaxHighlighter.ATTRIBUTES_MAP.get(PerlElementTypes.COMMENT_LINE);
		}
		return new TextAttributesKey[0];
	}
}
