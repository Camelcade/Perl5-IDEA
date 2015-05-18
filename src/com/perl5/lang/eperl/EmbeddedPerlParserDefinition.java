package com.perl5.lang.eperl;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.templateLanguages.TemplateDataElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.eperl.lexer.EmbeddedPerlElementTypes;
import com.perl5.lang.eperl.parser.EmbeddedPerlParser;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 18.05.2015.
 */
public class EmbeddedPerlParserDefinition implements ParserDefinition
{
	public static final TokenSet WHITE_SPACES = TokenSet.EMPTY;
	public static final TokenSet COMMENTS = TokenSet.EMPTY;
	public static final TokenSet LITERALS = TokenSet.EMPTY;
	public static final IFileElementType FILE = new IFileElementType("Embedded perl", EmbeddedPerlLanguage.INSTANCE);

	

	@NotNull
	@Override
	public Lexer createLexer(Project project) {
		return new EmbeddedPerlLexerAdapter();
	}

	@NotNull
	public TokenSet getWhitespaceTokens() {
		return WHITE_SPACES;
	}

	@NotNull
	public TokenSet getCommentTokens() {
		return COMMENTS;
	}

	@NotNull
	public TokenSet getStringLiteralElements() {
		return LITERALS;
	}

	@NotNull
	public PsiParser createParser(final Project project) {
		return new EmbeddedPerlParser();
	}

	@Override
	public IFileElementType getFileNodeType() {
		return FILE;
	}

	public PsiFile createFile(FileViewProvider viewProvider) {
		return new EmbeddedPerlPsiFile(viewProvider);
	}

	public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
		return SpaceRequirements.MAY;
	}


	@NotNull
	public PsiElement createElement(ASTNode node) {
		return EmbeddedPerlElementTypes.Factory.createElement(node);
	}
}
