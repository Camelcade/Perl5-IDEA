package com.perl5.lang.perl.psi;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.perl5.lang.perl.files.PerlFileTypePackage;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created by hurricup on 26.04.2015.
 */
public class PerlFilePackage extends PerlFile
{
	public PerlFilePackage(@NotNull FileViewProvider viewProvider) {
		super(viewProvider);
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
