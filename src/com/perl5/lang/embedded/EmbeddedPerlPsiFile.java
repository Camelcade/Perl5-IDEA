package com.perl5.lang.embedded;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
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
		return EmbeddedPerlFileType.INSTANCE;
	}

	@Override
	public String toString() {
		return "Embedded Perl file";
	}

	@Override
	public Icon getIcon(int flags) {
		return super.getIcon(flags);
	}

}
