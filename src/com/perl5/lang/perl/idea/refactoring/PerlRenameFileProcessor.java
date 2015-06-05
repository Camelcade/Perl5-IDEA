package com.perl5.lang.perl.idea.refactoring;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.listeners.RefactoringElementListener;
import com.intellij.refactoring.rename.RenamePsiFileProcessor;
import com.perl5.lang.perl.psi.PsiPerlNamespaceDefinition;
import com.perl5.lang.perl.psi.impl.PerlFileElementImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * Created by hurricup on 05.06.2015.
 *
 */
public class PerlRenameFileProcessor extends RenamePsiFileProcessor
{
	@Override
	public boolean canProcessElement(@NotNull PsiElement element)
	{
		return element instanceof PerlFileElementImpl && "pm".equals(element.getContainingFile().getVirtualFile().getExtension());
	}

	@Nullable
	@Override
	public Runnable getPostRenameCallback(PsiElement element, String newName, RefactoringElementListener elementListener)
	{
		Runnable postProcessor = null;

		if (newName.endsWith(".pm"))
		{
			assert element instanceof PerlFileElementImpl;
			final Project project = element.getProject();
			VirtualFile currentVirtualFile = ((PerlFileElementImpl) element).getVirtualFile();
			final String currentPacakgeName = ((PerlFileElementImpl) element).getFilePackageName();

			if (currentPacakgeName != null)
			{
				String currentFileName = currentVirtualFile.getNameWithoutExtension();
				String newFileName = newName.replaceFirst("\\.pm$", "");

				final String newFilePath = currentVirtualFile.getPath().replaceFirst(currentVirtualFile.getName() + "$", newName);
				final String newPackageName = currentPacakgeName.replaceFirst(currentFileName + "$", newFileName);

				postProcessor = new Runnable()
				{
					@Override
					public void run()
					{
						VirtualFile newVirtualFile = LocalFileSystem.getInstance().findFileByIoFile(new File(newFilePath));

						if (newVirtualFile != null)
						{
							PsiFile newPsiFile = PsiManager.getInstance(project).findFile(newVirtualFile);

							if (newPsiFile != null)
							{
								RenameRefactoringQueue queue = new RenameRefactoringQueue(project);

								for (PsiPerlNamespaceDefinition namespaceDefinition : PsiTreeUtil.findChildrenOfType(newPsiFile, PsiPerlNamespaceDefinition.class))
									if (currentPacakgeName.equals(namespaceDefinition.getPackageName()))
										queue.addElement(namespaceDefinition, newPackageName);

								queue.run();
							}
						}
					}
				};
			}
		}

		return postProcessor;
	}
}
