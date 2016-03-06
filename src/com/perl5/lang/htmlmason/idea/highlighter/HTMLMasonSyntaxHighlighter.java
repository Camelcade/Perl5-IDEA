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

package com.perl5.lang.htmlmason.idea.highlighter;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes;
import com.perl5.lang.htmlmason.lexer.HTMLMasonLexerAdapter;
import com.perl5.lang.perl.idea.highlighter.PerlHighlightningLexer;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighterEmbedded;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 06.03.2016.
 */
public class HTMLMasonSyntaxHighlighter extends PerlSyntaxHighlighterEmbedded implements HTMLMasonElementTypes
{
	public static final TokenSet MARKER_TOKENS = TokenSet.create(
			HTML_MASON_LINE_OPENER,
			HTML_MASON_EXPR_FILTER_PIPE,
			HTML_MASON_TAG_CLOSER,

			HTML_MASON_BLOCK_OPENER, HTML_MASON_BLOCK_CLOSER,
			HTML_MASON_CALL_OPENER, HTML_MASON_CALL_CLOSER,
			HTML_MASON_METHOD_OPENER, HTML_MASON_METHOD_CLOSER,
			HTML_MASON_DEF_OPENER, HTML_MASON_DEF_CLOSER,
			HTML_MASON_DOC_OPENER, HTML_MASON_DOC_CLOSER,
			HTML_MASON_ATTR_OPENER, HTML_MASON_ATTR_CLOSER,
			HTML_MASON_ARGS_OPENER, HTML_MASON_ARGS_CLOSER,
			HTML_MASON_FLAGS_OPENER, HTML_MASON_FLAGS_CLOSER,
			HTML_MASON_INIT_OPENER, HTML_MASON_INIT_CLOSER,
			HTML_MASON_CLEANUP_OPENER, HTML_MASON_CLEANUP_CLOSER,
			HTML_MASON_SHARED_OPENER, HTML_MASON_SHARED_CLOSER,
			HTML_MASON_PERL_OPENER, HTML_MASON_PERL_CLOSER,
			HTML_MASON_TEXT_OPENER, HTML_MASON_TEXT_CLOSER,
			HTML_MASON_FILTER_OPENER, HTML_MASON_FILTER_CLOSER,
			HTML_MASON_ONCE_OPENER, HTML_MASON_ONCE_CLOSER
	);

	public HTMLMasonSyntaxHighlighter(Project project)
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
		return new PerlHighlightningLexer(myProject, new HTMLMasonLexerAdapter(myProject));
	}


}
