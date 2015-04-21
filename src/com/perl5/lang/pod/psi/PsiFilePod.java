package com.perl5.lang.pod.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.perl5.lang.pod.PodFileType;
import com.perl5.lang.pod.PodLanguage;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created by hurricup on 21.04.2015.
 */
public class PsiFilePod extends PsiFileBase
{
	public PsiFilePod(@NotNull FileViewProvider viewProvider) {
		super(viewProvider, PodLanguage.INSTANCE);
	}

	@NotNull
	@Override
	public FileType getFileType() {
		return PodFileType.INSTANCE;
	}

	@Override
	public String toString() {
		return "POD file";
	}

	@Override
	public Icon getIcon(int flags) {
		return super.getIcon(flags);
	}
}