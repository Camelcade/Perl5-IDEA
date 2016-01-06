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
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.util.indexing.IndexingDataKeys;
import com.perl5.lang.mason.idea.configuration.MasonPerlSettings;
import com.perl5.lang.mason.psi.MasonNamespaceDefinition;
import com.perl5.lang.perl.idea.stubs.namespaces.PerlNamespaceDefinitionStub;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.impl.PsiPerlNamespaceDefinitionImpl;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

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
		MasonPerlSettings masonSettings = MasonPerlSettings.getInstance(getProject());
		VirtualFile originalFile = getContainingFile().getViewProvider().getVirtualFile();

		if (originalFile instanceof LightVirtualFile && getUserData(IndexingDataKeys.VIRTUAL_FILE) != null)
		{
			originalFile = getUserData(IndexingDataKeys.VIRTUAL_FILE);
		}

		for (String relativeRoot : masonSettings.componentRoots)
		{
			VirtualFile rootFile = VfsUtil.findRelativeFile(getProject().getBaseDir(), relativeRoot);
			if (rootFile != null && originalFile != null && VfsUtil.isAncestor(rootFile, originalFile, true))
			{
				String componentPath = VfsUtil.getRelativePath(originalFile, rootFile);

				if (componentPath != null)
				{
					return "MC0::" + componentPath.replaceAll("[^\\w\\/]", "_").replaceAll("" + VfsUtil.VFS_SEPARATOR_CHAR, PerlPackageUtil.PACKAGE_SEPARATOR);
				}
			}
		}
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

		// default
		if (parentsList.isEmpty())
		{
			parentsList.add(MASON_DEFAULT_COMPONENT_PARENT);
		}

		return parentsList;
	}

	@NotNull
	@Override
	public List<String> getParentNamespacesFromPsi()
	{
		// fixme not sure if we should use super method here
		List<String> parentsList = new ArrayList<String>();

		// check flags

		return parentsList;
	}
}
