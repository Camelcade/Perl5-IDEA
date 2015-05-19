package com.perl5.lang.embedded;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.perl5.lang.perl.PerlParserDefinition;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 18.05.2015.
 */
public class EmbeddedPerlParserDefinition extends PerlParserDefinition
{
	public static final IFileElementType FILE = new IFileElementType("Embedded perl", EmbeddedPerlLanguage.INSTANCE);

	@NotNull
	@Override
	public Lexer createLexer(Project project) {
		return new EmbeddedPerlLexerAdapter();
	}

	@Override
	public IFileElementType getFileNodeType() {
		return FILE;
	}

	public PsiFile createFile(FileViewProvider viewProvider) {
		return new EmbeddedPerlPsiFile(viewProvider);
	}

}
