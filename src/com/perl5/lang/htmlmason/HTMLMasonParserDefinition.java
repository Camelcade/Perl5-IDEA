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

package com.perl5.lang.htmlmason;

import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes;
import com.perl5.lang.htmlmason.elementType.HTMLMasonFileElementType;
import com.perl5.lang.htmlmason.lexer.HTMLMasonLexerAdapter;
import com.perl5.lang.htmlmason.parser.psi.impl.HTMLMasonFileImpl;
import com.perl5.lang.perl.PerlParserDefinition;
import com.perl5.lang.perl.parser.HTMLMasonParserImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 05.03.2016.
 */
public class HTMLMasonParserDefinition extends PerlParserDefinition implements HTMLMasonElementTypes
{
	public static final IFileElementType FILE = new HTMLMasonFileElementType("HTML::Mason component", HTMLMasonLanguage.INSTANCE);

	public static final TokenSet COMMENTS = TokenSet.orSet(PerlParserDefinition.COMMENTS,
			TokenSet.create(
					HTML_MASON_LINE_OPENER,
					HTML_MASON_PERL_OPENER,
					HTML_MASON_PERL_CLOSER,
					HTML_MASON_TEMPLATE_BLOCK_HTML
			));

	@NotNull
	@Override
	public Lexer createLexer(Project project)
	{
		return new HTMLMasonLexerAdapter(project);
	}

	@Override
	public IFileElementType getFileNodeType()
	{
		return FILE;
	}

	@Override
	public PsiFile createFile(FileViewProvider viewProvider)
	{
		return new HTMLMasonFileImpl(viewProvider);
	}

	@NotNull
	public TokenSet getCommentTokens()
	{
		return COMMENTS;
	}

	@NotNull
	@Override
	public PsiParser createParser(Project project)
	{
		return new HTMLMasonParserImpl();
	}

}
