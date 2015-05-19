package com.perl5.lang.embedded;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.FileViewProviderFactory;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 18.05.2015.
 */
public class EmbeddedPerlFileViewProviderFactory implements FileViewProviderFactory
{
	public FileViewProvider createFileViewProvider(@NotNull VirtualFile file,
												   com.intellij.lang.Language language,
												   @NotNull PsiManager manager,
												   boolean eventSystemEnabled) {
		return new EmbeddedPerlFileViewProvider(manager, file, eventSystemEnabled);
	}
}
