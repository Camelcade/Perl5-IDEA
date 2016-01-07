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

package com.perl5.lang.mason.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.mason.MasonUtils;
import com.perl5.lang.mason.idea.configuration.MasonSettings;
import com.perl5.lang.mason.psi.MasonFlagsStatement;
import com.perl5.lang.mason.psi.MasonNamespaceDefinition;
import com.perl5.lang.perl.idea.stubs.namespaces.PerlNamespaceDefinitionStub;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.impl.PsiPerlNamespaceDefinitionImpl;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 05.01.2016.
 */
public class MasonNamespaceDefinitionImpl extends PsiPerlNamespaceDefinitionImpl implements MasonNamespaceDefinition
{
	public MasonNamespaceDefinitionImpl(ASTNode node)
	{
		super(node);
	}

	public MasonNamespaceDefinitionImpl(PerlNamespaceDefinitionStub stub, IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	@Override
	public PerlNamespaceElement getNamespaceElement()
	{
		return null;
	}

	@Override
	protected String getPackageNameHeavy()
	{
		VirtualFile containingFile = getContainingFile().getRealContainingFile();
		VirtualFile componentRoot = getContainingFile().getComponentRoot();

		if (containingFile != null && componentRoot != null)
		{
			String componentPath = VfsUtil.getRelativePath(containingFile, componentRoot);

			if (componentPath != null)
			{
				return MasonUtils.getClassnameFromPath(componentPath);
			}
		}

		// fixme shouldn't we just use full path?
		return PerlPackageUtil.MAIN_PACKAGE;
	}


	@Override
	public List<String> getParentNamespaces()
	{
		List<String> result = null;
		PerlNamespaceDefinitionStub stub = getStub();
		if (stub != null)
		{
			result = stub.getParentNamespaces();
		}
		else
		{
			result = getParentNamespacesFromPsi();
		}

		if (result.isEmpty())
		{
			return getParentNamespacesFromAutobase();
		}
		else
		{
			return result;
		}
	}

	@NotNull
	protected List<String> getParentNamespacesFromAutobase()
	{
		List<String> parentsList = new ArrayList<String>();

		// autobase
		VirtualFile componentRoot = getContainingFile().getComponentRoot();
		VirtualFile containingFile = getContainingFile().getRealContainingFile();

		if (componentRoot != null && containingFile != null)
		{
			VirtualFile parentComponentFile = getParentComponentFile(componentRoot, containingFile.getParent(), containingFile);
			if (parentComponentFile != null) // found autobase class
			{
				String componentPath = VfsUtil.getRelativePath(parentComponentFile, componentRoot);

				if (componentPath != null)
				{
					parentsList.add(MasonUtils.getClassnameFromPath(componentPath));
				}
			}
		}

		// default
		if (parentsList.isEmpty())
		{
			parentsList.add(MASON_DEFAULT_COMPONENT_PARENT);
		}

		return parentsList;
	}

	/**
	 * Recursively traversing paths and looking for autobase
	 *
	 * @param componentRoot    component root we are search in
	 * @param currentDirectory directory we are currently in	 *
	 * @param childFile        current file (just to speed things up)
	 * @return parent component virtual file or null if not found
	 */
	@Nullable
	private VirtualFile getParentComponentFile(VirtualFile componentRoot, VirtualFile currentDirectory, VirtualFile childFile)
	{
		// check in current dir
		List<String> autobaseNames = new ArrayList<String>(MasonSettings.getInstance(getProject()).autobaseNames);

		if (childFile.getParent().equals(currentDirectory) && autobaseNames.contains(childFile.getName())) // avoid cyclic inheritance
		{
			autobaseNames = autobaseNames.subList(0, autobaseNames.lastIndexOf(childFile.getName()));
		}

		for (int i = autobaseNames.size() - 1; i >= 0; i--)
		{
			VirtualFile potentialParent = VfsUtil.findRelativeFile(currentDirectory, autobaseNames.get(i));
			if (potentialParent != null && potentialParent.exists() && !potentialParent.equals(childFile))
			{
				return potentialParent;
			}
		}

		// move up or exit
		if (!componentRoot.equals(currentDirectory))
		{
			return getParentComponentFile(componentRoot, currentDirectory.getParent(), childFile);
		}
		return null;
	}

	@NotNull
	@Override
	public List<String> getParentNamespacesFromPsi()
	{
		// fixme not sure if we should use super method here
		List<String> parentsList = new ArrayList<String>();

		// check flags
		MasonFlagsStatement flagsStatement = PsiTreeUtil.findChildOfType(this, MasonFlagsStatement.class);

		if (flagsStatement != null)
		{
			String parentNamespace = flagsStatement.getParentNamespace();
			if (parentNamespace != null)
			{
				parentsList.add(parentNamespace);
			}
		}

		return parentsList;
	}

	@Override
	public String getPresentableName()
	{
		VirtualFile componentRoot = getContainingFile().getComponentRoot();
		VirtualFile containingFile = getContainingFile().getRealContainingFile();

		if (componentRoot != null && containingFile != null)
		{
			String componentPath = VfsUtil.getRelativePath(containingFile, componentRoot);

			if (componentPath != null)
			{
				return VfsUtil.VFS_SEPARATOR_CHAR + componentPath;
			}
		}

		return super.getPresentableName();
	}

	@NotNull
	@Override
	public MasonFileImpl getContainingFile()
	{
		return (MasonFileImpl) super.getContainingFile();
	}
}
