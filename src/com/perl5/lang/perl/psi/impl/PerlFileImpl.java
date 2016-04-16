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

package com.perl5.lang.perl.psi.impl;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.lang.Language;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.ObjectStubTree;
import com.intellij.psi.stubs.PsiFileStub;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubTreeLoader;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.extensions.PerlCodeGenerator;
import com.perl5.lang.perl.extensions.generation.PerlCodeGeneratorImpl;
import com.perl5.lang.perl.extensions.packageprocessor.PerlLibProvider;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageProcessor;
import com.perl5.lang.perl.fileTypes.PerlFileType;
import com.perl5.lang.perl.fileTypes.PerlFileTypePackage;
import com.perl5.lang.perl.idea.stubs.imports.PerlUseStatementStub;
import com.perl5.lang.perl.idea.stubs.imports.runtime.PerlRuntimeImportStub;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.mro.PerlMro;
import com.perl5.lang.perl.psi.mro.PerlMroC3;
import com.perl5.lang.perl.psi.mro.PerlMroDfs;
import com.perl5.lang.perl.psi.mro.PerlMroType;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import com.perl5.lang.perl.psi.references.scopes.PerlVariableDeclarationSearcher;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.psi.utils.PerlScopeUtil;
import com.perl5.lang.perl.util.*;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hurricup on 26.04.2015.
 */
public class PerlFileImpl extends PsiFileBase implements PerlFile
{
	private static final ArrayList<PerlNamespaceDefinition> EMPTY_LIST = new ArrayList<PerlNamespaceDefinition>();
	protected ConcurrentHashMap<PerlVariable, String> VARIABLE_TYPES_CACHE = new ConcurrentHashMap<PerlVariable, String>();
	protected ConcurrentHashMap<PerlMethod, String> METHODS_NAMESPACES_CACHE = new ConcurrentHashMap<PerlMethod, String>();
	protected GlobalSearchScope myElementsResolveScope;

	protected Map<Integer, Boolean> isNewLineFobiddenAtLine = new THashMap<Integer, Boolean>();

	public PerlFileImpl(@NotNull FileViewProvider viewProvider, Language language)
	{
		super(viewProvider, language);
	}

	public PerlFileImpl(@NotNull FileViewProvider viewProvider)
	{
		super(viewProvider, PerlLanguage.INSTANCE);
	}

	@NotNull
	@Override
	public FileType getFileType()
	{
		VirtualFile virtualFile = getVirtualFile();

		if (virtualFile != null)
		{
			return getVirtualFile().getFileType();
		}
		return getDefaultFileType();
	}

	protected FileType getDefaultFileType()
	{
		// fixme getViewProvider().getVirtualFile() should be here, but incompatible with IDEA14
		return PerlFileType.INSTANCE;
	}


	@Override
	public PerlLexicalScope getLexicalScope()
	{
		return null;
	}

	/**
	 * Returns package name for this psi file. Name built from filename and innermost root.
	 *
	 * @return canonical package name or null if it's not pm file or it's not in source root
	 */
	@Nullable
	public String getFilePackageName()
	{
		VirtualFile containingFile = getVirtualFile();

		if (containingFile != null && containingFile.getFileType() == PerlFileTypePackage.INSTANCE)
		{
			VirtualFile innermostSourceRoot = PerlUtil.getFileClassRoot(getProject(), containingFile);
			if (innermostSourceRoot != null)
			{
				String relativePath = VfsUtil.getRelativePath(containingFile, innermostSourceRoot);
				return PerlPackageUtil.getPackageNameByPath(relativePath);
			}
		}
		return null;
	}

	@Override
	public void subtreeChanged()
	{
		super.subtreeChanged();
		VARIABLE_TYPES_CACHE.clear();
		METHODS_NAMESPACES_CACHE.clear();
		myElementsResolveScope = null;
		isNewLineFobiddenAtLine.clear();
	}


	/**
	 * Searching for most recent lexically visible variable declaration
	 *
	 * @param currentVariable variable to search declaration for
	 * @return variable in declaration term or null if there is no such one
	 */
	public PerlVariableDeclarationWrapper getLexicalDeclaration(PerlVariable currentVariable)
	{
		PerlVariableDeclarationSearcher variableProcessor = new PerlVariableDeclarationSearcher(currentVariable);
		PerlScopeUtil.treeWalkUp(currentVariable, variableProcessor);
		return variableProcessor.getResult();
	}

	@Override
	public String getPackageName()
	{
		return PerlPackageUtil.MAIN_PACKAGE;
	}

	@Override
	public List<PerlNamespaceDefinition> getParentNamespaceDefinitions()
	{
		return EMPTY_LIST;
	}

	@NotNull
	@Override
	public Collection<PerlNamespaceDefinition> getChildNamespaceDefinitions()
	{
		return Collections.emptyList();
	}

	@Override
	public PerlMroType getMroType()
	{
		return PerlMroType.DFS;
	}

	@Override
	public PerlMro getMro()
	{
		if (getMroType() == PerlMroType.DFS)
			return PerlMroDfs.INSTANCE;
		else
			return PerlMroC3.INSTANCE;
	}

	@Override
	public String getVariableType(PerlVariable element)
	{
		if (VARIABLE_TYPES_CACHE.containsKey(element))
		{
//			System.err.println("Read from cache " + element.getText());
			String type = VARIABLE_TYPES_CACHE.get(element);
			return type.isEmpty() ? null : type;
		}

		String type = element.getVariableTypeHeavy();
//		System.err.println("Cached " + element.getText() + type);
		VARIABLE_TYPES_CACHE.put(element, type == null ? "" : type);
		return getVariableType(element);
	}

	@Override
	public String getMethodNamespace(PerlMethod element)
	{
		if (METHODS_NAMESPACES_CACHE.containsKey(element))
		{
//			System.err.println("Got cached type for method " + element.getText() + " at " + element.getTextOffset());
			String type = METHODS_NAMESPACES_CACHE.get(element);
			return type.isEmpty() ? null : type;
		}

		String type = element.getContextPackageNameHeavy();
		METHODS_NAMESPACES_CACHE.put(element, type == null ? "" : type);
		return getMethodNamespace(element);
	}

	@Nullable
	@Override
	public Map<String, Set<String>> getImportedSubsNames()
	{
		return PerlSubUtil.getImportedSubs(getProject(), PerlPackageUtil.MAIN_PACKAGE, this);
	}

	@Nullable
	@Override
	public Map<String, Set<String>> getImportedScalarNames()
	{
		return PerlScalarUtil.getImportedScalars(getProject(), PerlPackageUtil.MAIN_PACKAGE, this);
	}

	@Nullable
	@Override
	public Map<String, Set<String>> getImportedArrayNames()
	{
		return PerlArrayUtil.getImportedArrays(getProject(), PerlPackageUtil.MAIN_PACKAGE, this);
	}

	@Nullable
	@Override
	public Map<String, Set<String>> getImportedHashNames()
	{
		return PerlHashUtil.getImportedHashes(getProject(), PerlPackageUtil.MAIN_PACKAGE, this);
	}

	@NotNull
	@Override
	public List<VirtualFile> getLibPaths()
	{
		List<VirtualFile> result = new ArrayList<VirtualFile>();

		// libdirs providers
		for (PerlUseStatement useStatement : PsiTreeUtil.findChildrenOfType(this, PerlUseStatement.class))
		{
			PerlPackageProcessor packageProcessor = useStatement.getPackageProcessor();
			if (packageProcessor instanceof PerlLibProvider)
			{
				((PerlLibProvider) packageProcessor).addLibDirs(useStatement, result);
			}
		}

		// classpath
		result.addAll(Arrays.asList(ProjectRootManager.getInstance(getProject()).orderEntries().getClassesRoots()));

		// current dir
		VirtualFile virtualFile = getVirtualFile();
		if (virtualFile != null)
		{
			result.add(virtualFile.getParent());
		}

		return result;
	}

//	@Override
//	@NotNull
//	public GlobalSearchScope getElementsResolveScope()
//	{
//		if (myElementsResolveScope != null)
//			return myElementsResolveScope;
//
//		long t = System.currentTimeMillis();
//		Set<VirtualFile> filesToSearch = new THashSet<VirtualFile>();
//		collectIncludedFiles(filesToSearch);
//		myElementsResolveScope = GlobalSearchScope.filesScope(getProject(), filesToSearch);
//
//		System.err.println("Collected in ms: " + (System.currentTimeMillis() - t) / 1000);
//
//		return myElementsResolveScope;
//	}

	@Override
	public void collectIncludedFiles(Set<VirtualFile> includedVirtualFiles)
	{
		if (!includedVirtualFiles.contains(getVirtualFile()))
		{
			includedVirtualFiles.add(getVirtualFile());

			StubElement fileStub = getStub();

			if (fileStub == null)
			{
//				System.err.println("Collecting from psi for " + getVirtualFile());
				collectRequiresFromPsi(this, includedVirtualFiles);
			}
			else
			{
//				System.err.println("Collecting from stubs for " + getVirtualFile());
				collectRequiresFromStub(fileStub, includedVirtualFiles);
			}
		}
	}

	protected void collectRequiresFromVirtualFile(VirtualFile virtualFile, Set<VirtualFile> includedVirtualFiles)
	{
		if (!includedVirtualFiles.contains(virtualFile))
		{
			includedVirtualFiles.add(virtualFile);

			ObjectStubTree objectStubTree = StubTreeLoader.getInstance().readOrBuild(getProject(), virtualFile, null);
			if (objectStubTree != null)
			{
//				System.err.println("Collecting from stub for " + virtualFile);
				collectRequiresFromStub((PsiFileStub) objectStubTree.getRoot(), includedVirtualFiles);
			}
			else
			{
//				System.err.println("Collecting from psi for " + virtualFile);
				PsiFile targetPsiFile = PsiManager.getInstance(getProject()).findFile(virtualFile);
				if (targetPsiFile != null)
					collectRequiresFromPsi(targetPsiFile, includedVirtualFiles);
			}
		}
	}

	protected void collectRequiresFromStub(@NotNull StubElement currentStub, Set<VirtualFile> includedVirtualFiles)
	{
		VirtualFile virtualFile = null;
		if (currentStub instanceof PerlUseStatementStub)
		{
			String packageName = ((PerlUseStatementStub) currentStub).getPackageName();
			if (packageName != null)
			{
				virtualFile = resolvePackageNameToVirtualFile(packageName);
			}

		}
		if (currentStub instanceof PerlRuntimeImportStub)
		{
			String importPath = ((PerlRuntimeImportStub) currentStub).getImportPath();
			if (importPath != null)
			{
				virtualFile = resolveRelativePathToVirtualFile(importPath);
			}
		}

		if (virtualFile != null)
		{
			collectRequiresFromVirtualFile(virtualFile, includedVirtualFiles);
		}

		for (Object childStub : currentStub.getChildrenStubs())
		{
			assert childStub instanceof StubElement : childStub.getClass();
			collectRequiresFromStub((StubElement) childStub, includedVirtualFiles);
		}
	}


	/**
	 * Collects required files from psi structure, used in currently modified document
	 *
	 * @param includedVirtualFiles set of already collected virtual files
	 */
	protected void collectRequiresFromPsi(PsiFile psiFile, Set<VirtualFile> includedVirtualFiles)
	{
		for (PsiElement importStatement : PsiTreeUtil.<PsiElement>findChildrenOfAnyType(psiFile, PerlUseStatement.class, PerlDoExpr.class))
		{
			VirtualFile virtualFile = null;
			if (importStatement instanceof PerlUseStatement)
			{
				String packageName = ((PerlUseStatement) importStatement).getPackageName();
				if (packageName != null)
				{
					virtualFile = resolvePackageNameToVirtualFile(packageName);
				}
			}
			else if (importStatement instanceof PerlDoExpr)
			{
				String importPath = ((PerlDoExpr) importStatement).getImportPath();

				if (importPath != null)
				{
					virtualFile = resolveRelativePathToVirtualFile(((PerlDoExpr) importStatement).getImportPath());
				}
			}

			if (virtualFile != null)
			{
				collectRequiresFromVirtualFile(virtualFile, includedVirtualFiles);
			}
		}
	}

	@Nullable
	@Override
	public PsiFile resolvePackageNameToPsi(String canonicalPackageName)
	{
		// resolves to a psi file
		return resolveRelativePathToPsi(PerlPackageUtil.getPackagePathByName(canonicalPackageName));
	}

	@Nullable
	@Override
	public VirtualFile resolvePackageNameToVirtualFile(String canonicalPackageName)
	{
		// resolves to a psi file
		return resolveRelativePathToVirtualFile(PerlPackageUtil.getPackagePathByName(canonicalPackageName));
	}

	@Nullable
	@Override
	public PsiFile resolveRelativePathToPsi(String relativePath)
	{
		VirtualFile targetFile = resolveRelativePathToVirtualFile(relativePath);

		if (targetFile != null && targetFile.exists())
		{
			PsiFile targetPsiFile = PsiManager.getInstance(getProject()).findFile(targetFile);
			if (targetPsiFile != null)
				return targetPsiFile;
		}

		return null;
	}

	@Override
	public VirtualFile resolveRelativePathToVirtualFile(String relativePath)
	{
		if (relativePath != null)
		{
			for (VirtualFile classRoot : getLibPaths())
			{
				if (classRoot != null)
				{
					VirtualFile targetFile = classRoot.findFileByRelativePath(relativePath);
					if (targetFile != null)
					{
						return targetFile;
					}
				}
			}
		}

		return null;
	}

	@Override
	public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place)
	{
		return PerlScopeUtil.processChildren(
				this,
				processor,
				state,
				lastParent,
				place
		);
	}

	public boolean isNewLineForbiddenAt(PsiElement element)
	{
		Document document = PsiDocumentManager.getInstance(getProject()).getDocument(this);
		if (document != null)
		{
			int lineNumber = document.getLineNumber(element.getTextRange().getEndOffset());

			if (isNewLineFobiddenAtLine.containsKey(lineNumber))
				return isNewLineFobiddenAtLine.get(lineNumber);

			int lineEndOffset = document.getLineEndOffset(lineNumber);

			boolean result = PerlPsiUtil.isHeredocAhead(element, lineEndOffset + 1);

			isNewLineFobiddenAtLine.put(lineNumber, result);

			return result;
		}
		return false;
	}

	@Override
	public PerlCodeGenerator getCodeGenerator()
	{
		return PerlCodeGeneratorImpl.INSTANCE;
	}

/* This method is to get ElementTypes stats from PsiFile using PSIViewer
	public String getTokensStats()
	{
		final Map<IElementType, Integer> TOKENS_STATS = new THashMap<IElementType, Integer>();

		accept(new PerlRecursiveVisitor(){

			@Override
			public void visitElement(PsiElement element)
			{
				IElementType elementType = element.getNode().getElementType();
				if( elementType instanceof PerlElementType)
				{
					if (!TOKENS_STATS.containsKey(elementType))
					{
						TOKENS_STATS.put(elementType, 1);
					}
					else
					{
						TOKENS_STATS.put(elementType, TOKENS_STATS.get(elementType) + 1);
					}
				}
				super.visitElement(element);
			}
		});

		for (IElementType type: TOKENS_STATS.keySet())
		{
			System.err.println(type+ ";" + TOKENS_STATS.get(type));
		}

		return "";
	}
*/

	@Override
	public ItemPresentation getPresentation()
	{
		return this;
	}

	@Nullable
	@Override
	public String getPresentableText()
	{
		String result = getFilePackageName();
		return result == null ? getName() : result;
	}

	@Nullable
	@Override
	public String getLocationString()
	{
		final PsiDirectory psiDirectory = getParent();
		if (psiDirectory != null)
		{
			return psiDirectory.getVirtualFile().getPresentableUrl();
		}
		return null;
	}

	@Nullable
	@Override
	public Icon getIcon(boolean unused)
	{
		return getFileType().getIcon();
	}

	@Nullable
	@Override
	public String getPodLink()
	{
		return getFilePackageName();
	}

	@Nullable
	@Override
	public String getPodLinkText()
	{
		return getPodLink();
	}


	@Override
	public byte[] getPerlContentInBytes()
	{
		return getText().getBytes(getVirtualFile().getCharset());
	}
}
