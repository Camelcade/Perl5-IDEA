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

package com.perl5.lang.perl;

/**
 * Created by hurricup on 12.04.2015.
 */

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.idea.stubs.PerlFileElementType;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.PerlLexerAdapter;
import com.perl5.lang.perl.parser.PerlParser;
import com.perl5.lang.perl.parser.PerlParserExtension;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import com.perl5.lang.perl.psi.impl.PerlParsableStringWrapperlImpl;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PerlParserDefinition implements ParserDefinition, PerlElementTypes
{
	public static final List<PerlParserExtension> PARSER_EXTENSIONS = new ArrayList<PerlParserExtension>();

	public static final TokenSet WHITE_SPACES = TokenSet.create(
			TokenType.WHITE_SPACE,
			TokenType.NEW_LINE_INDENT
	);
	public static final TokenSet COMMENTS = TokenSet.create(
			COMMENT_LINE, COMMENT_BLOCK,
			HEREDOC, HEREDOC_QQ, HEREDOC_QX, HEREDOC_END,

			// these should be in other place
			EMBED_MARKER, EMBED_MARKER_OPEN, EMBED_MARKER_CLOSE,
			TEMPLATE_BLOCK_HTML
	);

	public static final TokenSet WHITE_SPACE_AND_COMMENTS = TokenSet.orSet(WHITE_SPACES, COMMENTS);
	public static final TokenSet LITERALS = TokenSet.create(
			STRING_CONTENT,
			HEREDOC
	);
	public static final TokenSet IDENTIFIERS = TokenSet.create(
			SUB,
			PACKAGE,
			VARIABLE_NAME,
			IDENTIFIER,
			PACKAGE_IDENTIFIER
	);

	public static final IFileElementType FILE = new PerlFileElementType("Perl5", PerlLanguage.INSTANCE);

	@NotNull
	@Override
	public Lexer createLexer(Project project)
	{
		return new PerlLexerAdapter(project);
	}

	@NotNull
	public TokenSet getWhitespaceTokens()
	{
		return WHITE_SPACES;
	}

	@NotNull
	public TokenSet getCommentTokens()
	{
		return COMMENTS;
	}

	@NotNull
	public TokenSet getStringLiteralElements()
	{
		return LITERALS;
	}

	@NotNull
	public PsiParser createParser(final Project project)
	{
		return new PerlParser();
	}

	@Override
	public IFileElementType getFileNodeType()
	{
		return FILE;
	}

	public PsiFile createFile(FileViewProvider viewProvider)
	{
		return new PerlFileImpl(viewProvider);
	}

	public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right)
	{
		return SpaceRequirements.MAY;
	}

	@NotNull
	public PsiElement createElement(ASTNode node)
	{
		IElementType elementType = node.getElementType();

		for (PerlParserExtension extension : PARSER_EXTENSIONS)
		{
			PsiElement element = extension.createElement(node);
			if (element != null)
			{
				return element;
			}
		}

		if (elementType == HEREDOC_QQ || elementType == HEREDOC_QX || elementType == HEREDOC) // fixme instanceof is faster ?
		{
			return new PerlHeredocElementImpl(node);
		}
		else if (elementType == PARSABLE_STRING_USE_VARS)
		{
			return new PerlParsableStringWrapperlImpl(node);
		}
		return PerlElementTypes.Factory.createElement(node);
	}
}