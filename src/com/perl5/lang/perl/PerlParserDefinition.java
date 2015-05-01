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
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.PerlLexerAdapter;
import com.perl5.lang.perl.parser.PerlParser;
import com.perl5.lang.perl.psi.PerlFilePackage;
import org.jetbrains.annotations.NotNull;
import com.intellij.psi.tree.IStubFileElementType;

import java.io.Reader;

public class PerlParserDefinition implements ParserDefinition, PerlElementTypes
{

	public static final TokenSet WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE, TokenType.NEW_LINE_INDENT);
	public static final TokenSet COMMENTS = TokenSet.create(PERL_COMMENT, PERL_COMMENT_BLOCK, PERL_POD, PERL_STRING_MULTILINE,PERL_STRING_MULTILINE_END);
	public static final TokenSet LITERALS = TokenSet.create(PERL_STRING);

	public static final IStubFileElementType PERL_FILE = new IStubFileElementType("Perl5", PerlLanguage.INSTANCE);

	public static final IFileElementType FILE = new IFileElementType("Perl5", PerlLanguage.INSTANCE);

	@NotNull
	@Override
	public Lexer createLexer(Project project) {
		return new PerlLexerAdapter();
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
		return new PerlParser();
	}

	@Override
	public IFileElementType getFileNodeType() {
		return FILE;
	}

	public PsiFile createFile(FileViewProvider viewProvider) {
		// @todo should we decide what kind of file to create? script/package
		return new PerlFilePackage(viewProvider);
	}

	public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
		return SpaceRequirements.MAY;
	}

	@NotNull
	public PsiElement createElement(ASTNode node) {
		return PerlElementTypes.Factory.createElement(node);
	}
}