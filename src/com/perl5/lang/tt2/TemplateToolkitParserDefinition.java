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

package com.perl5.lang.tt2;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.parser.elementTypes.PsiElementProvider;
import com.perl5.lang.tt2.elementTypes.TemplateToolkitElementTypes;
import com.perl5.lang.tt2.lexer.TemplateToolkitLexerAdapter;
import com.perl5.lang.tt2.parser.TemplateToolkitParser;
import com.perl5.lang.tt2.psi.impl.TemplateToolkitFileImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 05.06.2016.
 */
public class TemplateToolkitParserDefinition implements ParserDefinition, TemplateToolkitElementTypes
{
	public static final TokenSet WHITE_SPACES = TokenSet.create(
			TokenType.WHITE_SPACE,
			TokenType.NEW_LINE_INDENT
	);
	public static final TokenSet COMMENTS = TokenSet.create(
			TT2_HTML,
			TT2_COMMENT
	);

	public static final TokenSet LITERALS = TokenSet.create(
	);

	public static final TokenSet IDENTIFIERS = TokenSet.create(
			TT2_IDENTIFIER
	);

	@NotNull
	@Override
	public Lexer createLexer(Project project)
	{
		return new TemplateToolkitLexerAdapter(project);
	}

	@Override
	public PsiParser createParser(Project project)
	{
		return new TemplateToolkitParser(project);
	}

	@Override
	public IFileElementType getFileNodeType()
	{
		return TT2_FILE;
	}

	@NotNull
	@Override
	public TokenSet getWhitespaceTokens()
	{
		return WHITE_SPACES;
	}

	@NotNull
	@Override
	public TokenSet getCommentTokens()
	{
		return COMMENTS;
	}

	@NotNull
	@Override
	public TokenSet getStringLiteralElements()
	{
		return LITERALS;
	}

	@NotNull
	@Override
	public PsiElement createElement(ASTNode node)
	{
		return ((PsiElementProvider) node.getElementType()).getPsiElement(node);
	}

	@Override
	public PsiFile createFile(FileViewProvider viewProvider)
	{
		return new TemplateToolkitFileImpl(viewProvider);
	}

	@Override
	public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right)
	{
		return null;
	}
}
