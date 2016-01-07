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

package com.perl5.lang.mason.idea.highlighter;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.mason.elementType.MasonElementTypes;
import com.perl5.lang.mason.lexer.MasonLexerAdapter;
import com.perl5.lang.perl.idea.highlighter.PerlHighlightningLexer;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighterEmbedded;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 20.12.2015.
 */
public class MasonSyntaxHighlighter extends PerlSyntaxHighlighterEmbedded implements MasonElementTypes
{
	public static final TokenSet MARKER_TOKENS = TokenSet.create(
			MASON_LINE_OPENER,
			MASON_EXPR_FILTER_PIPE,
			MASON_TAG_CLOSER,

			MASON_BLOCK_OPENER, MASON_BLOCK_CLOSER,
			MASON_CALL_OPENER, MASON_CALL_CLOSER,
			MASON_METHOD_OPENER, MASON_METHOD_CLOSER,
			MASON_CLASS_OPENER, MASON_CLASS_CLOSER,
			MASON_DOC_OPENER, MASON_DOC_CLOSER,
			MASON_FLAGS_OPENER, MASON_FLAGS_CLOSER,
			MASON_INIT_OPENER, MASON_INIT_CLOSER,
			MASON_PERL_OPENER, MASON_PERL_CLOSER,
			MASON_TEXT_OPENER, MASON_TEXT_CLOSER,
			MASON_FILTER_OPENER, MASON_FILTER_CLOSER,
			MASON_AFTER_OPENER, MASON_AFTER_CLOSER,
			MASON_AUGMENT_OPENER, MASON_AUGMENT_CLOSER,
			MASON_AROUND_OPENER, MASON_AROUND_CLOSER,
			MASON_BEFORE_OPENER, MASON_BEFORE_CLOSER,
			MASON_OVERRIDE_OPENER, MASON_OVERRIDE_CLOSER
	);

	public MasonSyntaxHighlighter(Project project)
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
		return new PerlHighlightningLexer(myProject, new MasonLexerAdapter(myProject));
	}

}
