/*
 * Copyright 2015 Alexandr Evstigneev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.perl5.lang.perl.psi.mixins;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.idea.refactoring.RenameRefactoringQueue;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.PsiPerlNamespaceDefinition;
import com.perl5.lang.perl.psi.impl.PerlFileElementImpl;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceDefinitionStub;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hurricup on 28.05.2015.
 */
public abstract class PerlNamespaceDefinitionImplMixin extends StubBasedPsiElementBase<PerlNamespaceDefinitionStub> implements PsiPerlNamespaceDefinition
{
	public PerlNamespaceDefinitionImplMixin(@NotNull ASTNode node)
	{
		super(node);
	}

	public PerlNamespaceDefinitionImplMixin(@NotNull PerlNamespaceDefinitionStub stub, @NotNull IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	@Override
	public String getName()
	{
		return getPackageName();
	}

	@Override
	public PerlNamespaceElement getNamespaceElement()
	{
		return findChildByClass(PerlNamespaceElement.class);
	}

	@Nullable
	@Override
	public PsiElement getNameIdentifier()
	{
		return getNamespaceElement();
	}

	@Override
	public PsiElement setName(@NotNull String name) throws IncorrectOperationException
	{
		PerlNamespaceElement namespaceElement = getNamespaceElement();

		if (namespaceElement != null)
		{
			Runnable newProcess = null;

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

			namespaceElement.setName(name);

			if (newProcess != null)
				newProcess.run();


		}
		return this;
	}

	@Override
	public String getPackageName()
	{
		PerlNamespaceDefinitionStub stub = getStub();
		if (stub != null)
			return stub.getPackageName();

		PerlNamespaceElement namespaceElement = getNamespaceElement();
		if (namespaceElement != null)
			return namespaceElement.getName();

		return null;
	}
}
