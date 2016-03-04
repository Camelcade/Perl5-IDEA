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

package com.perl5.lang.mojolicious;

import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.mojolicious.lexer.MojoliciousLexerAdapter;
import com.perl5.lang.mojolicious.psi.impl.MojoliciousFileImpl;
import com.perl5.lang.perl.PerlParserDefinition;
import com.perl5.lang.perl.idea.stubs.PerlFileElementType;
import com.perl5.lang.perl.parser.MojoliciousParser;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 21.07.2015.
 */
public class MojoliciousParserDefinition extends PerlParserDefinition implements MojoliciousElementTypes
{
	public static final IFileElementType FILE = new PerlFileElementType("Mojolicious Perl5 Template", MojoliciousLanguage.INSTANCE);
	public static final TokenSet COMMENTS = TokenSet.orSet(PerlParserDefinition.COMMENTS,
			TokenSet.create(
					MOJO_TEMPLATE_BLOCK_HTML,

					MOJO_BLOCK_OPENER,
					MOJO_BLOCK_CLOSER,
					MOJO_BLOCK_NOSPACE_CLOSER,

					MOJO_LINE_OPENER,
					MOJO_LINE_EXPR_OPENER,
					MOJO_LINE_EXPR_ESCAPED_OPENER,

					MOJO_BLOCK_EXPR_OPENER,
					MOJO_BLOCK_EXPR_ESCAPED_OPENER,

					MOJO_BLOCK_OPENER_TAG,
					MOJO_LINE_OPENER_TAG
			));

	@NotNull
	@Override
	public TokenSet getCommentTokens()
	{
		return COMMENTS;
	}

	@NotNull
	@Override
	public Lexer createLexer(Project project)
	{
		return new MojoliciousLexerAdapter(project);
	}

	@Override
	public IFileElementType getFileNodeType()
	{
		return FILE;
	}

	public PsiFile createFile(FileViewProvider viewProvider)
	{
		return new MojoliciousFileImpl(viewProvider);
	}

	@NotNull
	@Override
	public PsiParser createParser(Project project)
	{
		return new MojoliciousParser();
	}
}
