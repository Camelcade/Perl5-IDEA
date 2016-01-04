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

package com.perl5.lang.embedded.idea;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.embedded.lexer.EmbeddedPerlLexerAdapter;
import com.perl5.lang.embedded.psi.EmbeddedPerlElementTypes;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighterEmbedded;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 19.05.2015.
 */
public class EmbeddedPerlSyntaxHighlighter extends PerlSyntaxHighlighterEmbedded implements EmbeddedPerlElementTypes
{
	public static final TokenSet MARKER_TOKENS = TokenSet.create(
			EMBED_MARKER_OPEN,
			EMBED_MARKER_CLOSE
	);

	public EmbeddedPerlSyntaxHighlighter(Project project)
	{
		super(project);
	}

	@Override
	public TokenSet getMarkersTokenSet()
	{
		return MARKER_TOKENS;
	}

	@NotNull
	@Override
	public Lexer getHighlightingLexer()
	{
		return new EmbeddedPerlLexerAdapter(myProject);
	}
}
