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

import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.mojolicious.lexer.MojoliciousPerlLexerAdapter;
import com.perl5.lang.mojolicious.psi.impl.MojoliciousPerlFileImpl;
import com.perl5.lang.perl.PerlParserDefinition;
import com.perl5.lang.perl.idea.stubs.PerlFileElementType;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 21.07.2015.
 */
public class MojoliciousPerlParserDefinition extends PerlParserDefinition implements MojoliciousPerlElementTypes
{
	public static final IFileElementType FILE = new PerlFileElementType("Mojolicious Perl5 Template", MojoliciousPerlLanguage.INSTANCE);
	public static final TokenSet COMMENTS = TokenSet.orSet(PerlParserDefinition.COMMENTS,
			TokenSet.create(
					MOJO_TEMPLATE_BLOCK_HTML,
					EMBED_MARKER_OPEN,
					EMBED_MARKER_CLOSE,
					EMBED_MARKER,
					EMBED_MARKER_SEMICOLON
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
		return new MojoliciousPerlLexerAdapter(project);
	}

	@Override
	public IFileElementType getFileNodeType()
	{
		return FILE;
	}

	public PsiFile createFile(FileViewProvider viewProvider)
	{
		return new MojoliciousPerlFileImpl(viewProvider);
	}

}
