package com.perl5.lang.perl.psi;

/**
 * Created by hurricup on 12.04.2015.
 */
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.perl5.lang.perl.PerlFileTypeScript;
import com.perl5.lang.perl.PerlLanguage;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class PsiFilePerl extends PsiFileBase {
	public PsiFilePerl(@NotNull FileViewProvider viewProvider) {
		super(viewProvider, PerlLanguage.INSTANCE);
	}

	@NotNull
	@Override
	public FileType getFileType() {
		return PerlFileTypeScript.INSTANCE;
	}

	@Override
	public String toString() {
		return "Perl file";
	}

	@Override
	public Icon getIcon(int flags) {
		return super.getIcon(flags);
	}
}