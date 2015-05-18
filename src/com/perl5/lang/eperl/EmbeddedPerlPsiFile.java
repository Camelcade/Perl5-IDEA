package com.perl5.lang.eperl;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiFile;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.files.PerlFileTypePackage;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created by hurricup on 18.05.2015.
 */
public class EmbeddedPerlPsiFile extends PsiFileBase
{
	public EmbeddedPerlPsiFile (@NotNull FileViewProvider viewProvider) {
		super(viewProvider, EmbeddedPerlLanguage.INSTANCE);
	}

	@NotNull
	@Override
	public FileType getFileType() {
		return PerlFileTypePackage.INSTANCE;
	}

	@Override
	public String toString() {
		return "Perl Package File";
	}

	@Override
	public Icon getIcon(int flags) {
		return super.getIcon(flags);
	}

}
