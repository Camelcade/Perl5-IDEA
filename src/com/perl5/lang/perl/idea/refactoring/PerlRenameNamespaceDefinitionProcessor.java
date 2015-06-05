package com.perl5.lang.perl.idea.refactoring;

import com.intellij.psi.PsiElement;
import com.intellij.refactoring.listeners.RefactoringElementListener;
import com.perl5.lang.perl.psi.PerlNamespaceDefinition;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 05.06.2015.
 *
 */
public class PerlRenameNamespaceDefinitionProcessor extends PerlRenameNamespaceProcessor
{
	public boolean canProcessElement(@NotNull PsiElement element)
	{
		return element instanceof PerlNamespaceDefinition;
	}

	@Nullable
	@Override
	public Runnable getPostRenameCallback(PsiElement element, String newName, RefactoringElementListener elementListener)
	{
		// todo here we should put file renaming and moving; do the same for file

//			Runnable newProcess = null;

		// namespace definition,
//			final PsiFile psiFile = getContainingFile();
//
//			if (psiFile instanceof PerlFileElementImpl)
//			{
//				final String packageName = ((PerlFileElementImpl) psiFile).getFilePackageName();
//				if (packageName != null && packageName.equals(getName()))
//				{
//					// ok, it's package with same name
//					final VirtualFile virtualFile = psiFile.getVirtualFile();
//					final Project project = getProject();
//					final String canonicalPackageName = PerlPackageUtil.getCanonicalPackageName(name);
////					final PsiElement requestor = this.getParent();
//
//					newProcess = new Runnable()
//					{
//						@Override
//						public void run()
//						{
//							VirtualFile newParent = PerlUtil.findInnermostSourceRoot(project, virtualFile);
//
//							List<String> packageDirs = Arrays.asList(canonicalPackageName.split(":+"));
//							String newFileName = packageDirs.get(packageDirs.size() - 1) + ".pm";
//
//							for (int i = 0; i < packageDirs.size() - 1; i++)
//							{
//								String dir = packageDirs.get(i);
//
//								VirtualFile subDir = newParent.findChild(dir);
//								try
//								{
//									newParent = (subDir != null) ? subDir : newParent.createChildDirectory(null, dir);
//								} catch (IOException e)
//								{
//									throw new IncorrectOperationException("Could not create subdirectory: " + newParent.getPath() + "/" + dir);
//								}
//
//							}
//
//							RenameRefactoringQueue queue = new RenameRefactoringQueue(project);
//
//							for (PsiReference inboundReference : ReferencesSearch.search(psiFile))
//							{
//								if (inboundReference.getElement() instanceof PerlNamespaceElement)
//									queue.addElement(inboundReference.getElement(), canonicalPackageName);
//							}
//
//							try
//							{
//								if (!newParent.getPath().equals(virtualFile.getParent().getPath()))
//								{
//									// we need to handle references ourselves
//									virtualFile.move(null, newParent);
//								}
//
//								virtualFile.rename(null, newFileName);
//							} catch (IOException e)
//							{
//								throw new IncorrectOperationException("Could not rename or move package file: " + e.getMessage());
//							}
//
//							queue.run();
//						}
//					};
//				}
//			}

		return super.getPostRenameCallback(element, newName, elementListener);
	}

}
