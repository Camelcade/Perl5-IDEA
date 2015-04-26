package com.perl5.lang.perl.psi;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.perl5.lang.perl.files.PerlFileTypePackage;
import com.perl5.lang.perl.files.PerlFileTypeScript;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created by hurricup on 26.04.2015.
 */
public class PerlFileScript extends PerlFile
{
	public PerlFileScript(@NotNull FileViewProvider viewProvider) {
		super(viewProvider);
	}

	@NotNull
	@Override
	public FileType getFileType() {
		return PerlFileTypeScript.INSTANCE;
	}

	@Override
	public String toString() {
		return "Perl Script File";
	}

	@Override
	public Icon getIcon(int flags) {
		return super.getIcon(flags);
	}

}
