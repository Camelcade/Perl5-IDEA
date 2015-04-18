package com.perl5.lang.parser;

/**
 * Created by hurricup on 12.04.2015.
 */
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.PerlFileType;
import com.perl5.PerlLanguage;
import com.perl5.lang.lexer.PerlLexer;
import com.perl5.lang.lexer.PerlLexerPorted;
import com.perl5.lang.lexer.PerlTokenTypes;
import com.perl5.lang.psi.PerlFile;
import org.jetbrains.annotations.NotNull;
import com.intellij.psi.tree.IStubFileElementType;

import java.io.Reader;

public class PerlParserDefinition implements ParserDefinition{
	public static final TokenSet WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE);
	public static final TokenSet COMMENTS = TokenSet.create(PerlTokenTypes.PERL_COMMENT);
	public static final IStubFileElementType PERL_FILE = new IStubFileElementType(PerlFileType.PERL_LANGUAGE);

	public static final IFileElementType FILE = new IFileElementType(Language.<PerlLanguage>findInstance(PerlLanguage.class));

	@NotNull
	@Override
	public Lexer createLexer(Project project) {
		return new FlexAdapter(new PerlLexerPorted((Reader) null));
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
		return TokenSet.EMPTY;
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
		return new PerlFile(viewProvider);
	}

	public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
		return SpaceRequirements.MAY;
	}

	@NotNull
	public PsiElement createElement(ASTNode node) {
		return PerlPsiCreator.createElement(node);
	}
}