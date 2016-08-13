/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.htmlmason.parser.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiElementProcessor;
import com.intellij.psi.stubs.Stub;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Processor;
import com.perl5.compat.PerlStubIndex;
import com.perl5.lang.htmlmason.HTMLMasonLanguage;
import com.perl5.lang.htmlmason.HTMLMasonUtil;
import com.perl5.lang.htmlmason.MasonCoreUtil;
import com.perl5.lang.htmlmason.idea.configuration.HTMLMasonSettings;
import com.perl5.lang.htmlmason.parser.psi.*;
import com.perl5.lang.htmlmason.parser.stubs.HTMLMasonArgsBlockStub;
import com.perl5.lang.htmlmason.parser.stubs.HTMLMasonFlagsStatementStub;
import com.perl5.lang.htmlmason.parser.stubs.HTMLMasonFlagsStubIndex;
import com.perl5.lang.htmlmason.parser.stubs.HTMLMasonMethodDefinitionStub;
import com.perl5.lang.htmlmason.parser.stubs.impl.HTMLMasonNamedElementStubBaseImpl;
import com.perl5.lang.perl.PerlScopes;
import com.perl5.lang.perl.psi.PerlCompositeElement;
import com.perl5.lang.perl.psi.PerlVariableDeclarationWrapper;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import gnu.trove.THashMap;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by hurricup on 05.03.2016.
 */
public class HTMLMasonFileImpl extends PerlFileImpl implements HTMLMasonFile
{
	protected List<PerlVariableDeclarationWrapper> myImplicitVariables = null;
	protected int myMasonChangeCounter;
	protected Map<Integer, Boolean> myPerlLinesMap = new THashMap<Integer, Boolean>();
	protected MyBlocksCache myBlocksCache;

	public HTMLMasonFileImpl(@NotNull FileViewProvider viewProvider)
	{
		super(viewProvider, HTMLMasonLanguage.INSTANCE);
		myBlocksCache = new MyBlocksCache(this);
	}

	// fixme this is a cut version from PerlScopeUtil to make implicit vars checking in the last order
	public static boolean processChildren(@NotNull PsiElement element,
										  @NotNull PsiScopeProcessor processor,
										  @NotNull ResolveState resolveState,
										  @Nullable PsiElement lastParent,
										  @NotNull PsiElement place)
	{
		PsiElement run = lastParent == null ? element.getLastChild() : lastParent.getPrevSibling();
		while (run != null)
		{
			if (run instanceof PerlCompositeElement &&
					!(run instanceof PerlLexicalScope) &&
					!run.processDeclarations(processor, resolveState, null, place)
					)
			{
				return false;
			}
			run = run.getPrevSibling();
		}

		return true;
	}

	@Nullable
	public VirtualFile getComponentRoot()
	{
		return HTMLMasonUtil.getComponentRoot(getProject(), getComponentVirtualFile());
	}

	public VirtualFile getComponentVirtualFile()
	{
		return MasonCoreUtil.getContainingVirtualFile(this);
	}

	/**
	 * Returns absolute path relative to the components root
	 *
	 * @return path
	 */
	@Nullable
	public String getAbsoluteComponentPath()
	{
		VirtualFile componentFile = getComponentVirtualFile();
		VirtualFile componentRoot = getComponentRoot();

		if (componentFile != null && componentRoot != null)
		{
			return '/' + VfsUtil.getRelativePath(componentFile, componentRoot);
		}
		return null;
	}

	/**
	 * Returns absolute containing dir path relative to the components root
	 *
	 * @return path
	 */
	@Nullable
	public String getAbsoluteComponentContainerPath()
	{
		VirtualFile componentFile = getComponentVirtualFile();
		VirtualFile componentRoot = getComponentRoot();

		if (componentFile != null && componentRoot != null)
		{
			return '/' + VfsUtil.getRelativePath(componentFile.getParent(), componentRoot);
		}
		return null;
	}

	public boolean isInPerlLine(PsiElement element)
	{
		if (element == null)
		{
			return false;
		}

		ASTNode node = element.getNode();
		int startOffset = node.getStartOffset();
//		int endOffset = startOffset + node.getTextLength();

		return isInPerlLine(startOffset); ///|| isInPerlLine(endOffset);
	}

	protected boolean isInPerlLine(int offset)
	{
		Document document = PsiDocumentManager.getInstance(getProject()).getDocument(this);
		if (document != null)
		{
			int lineNumber = document.getLineNumber(offset);

			Boolean result = myPerlLinesMap.get(lineNumber);

			if (result != null)
			{
				return result;
			}

			PsiElement firstElement = findElementAt(document.getLineStartOffset(lineNumber));

			result = firstElement != null && firstElement.getNode().getElementType() == HTML_MASON_LINE_OPENER;

			myPerlLinesMap.put(lineNumber, result);
			return result;
		}
		return false;
	}

	@NotNull
	@Override
	public List<PerlVariableDeclarationWrapper> getImplicitVariables()
	{

		HTMLMasonSettings settings = HTMLMasonSettings.getInstance(getProject());
		if (myImplicitVariables == null || myMasonChangeCounter != settings.getChangeCounter())
		{
			myImplicitVariables = buildImplicitVariables(settings);
			myMasonChangeCounter = settings.getChangeCounter();
		}
		return myImplicitVariables;
	}

	protected List<PerlVariableDeclarationWrapper> buildImplicitVariables(HTMLMasonSettings settings)
	{
		List<PerlVariableDeclarationWrapper> newImplicitVariables = new ArrayList<PerlVariableDeclarationWrapper>();

		if (isValid())
		{
			MasonCoreUtil.fillVariablesList(this, newImplicitVariables, settings.globalVariables);
		}
		return newImplicitVariables;
	}

	@Nullable
	public HTMLMasonFileImpl getParentComponent()
	{
		String parentComponentPath = getParentComponentPath();
		HTMLMasonSettings settings = HTMLMasonSettings.getInstance(getProject());
		VirtualFile parentFile = null;

		if (parentComponentPath == null) // autohandler
		{
			VirtualFile containingFile = getComponentVirtualFile();
			if (containingFile != null)
			{
				VirtualFile startDir = containingFile.getParent();
				if (StringUtil.equals(containingFile.getName(), settings.autoHandlerName))
				{
					startDir = startDir.getParent();
				}

				VirtualFile componentRoot = HTMLMasonUtil.getComponentRoot(getProject(), startDir);
				if (componentRoot != null)
				{
					while (VfsUtil.isAncestor(componentRoot, startDir, false))
					{
						if ((parentFile = startDir.findFileByRelativePath(settings.autoHandlerName)) != null)
						{
							break;
						}
						startDir = startDir.getParent();
					}
				}
			}
		}
		else if (!StringUtil.equals(parentComponentPath, HTMLMasonFlagsStatement.UNDEF_RESULT)) // Specific component
		{
			if (StringUtil.startsWith(parentComponentPath, "/")) // absolute path
			{
				parentComponentPath = parentComponentPath.substring(1);
				for (VirtualFile root : settings.getComponentsRootsVirtualFiles())
				{
					if ((parentFile = root.findFileByRelativePath(parentComponentPath)) != null)
					{
						break;
					}

				}
			}
			else // relative path
			{
				VirtualFile containingVirtualFile = getComponentVirtualFile();
				if (containingVirtualFile != null)
				{
					VirtualFile containingDir = containingVirtualFile.getParent();
					if (containingDir != null)
					{
						parentFile = containingDir.findFileByRelativePath(parentComponentPath);
					}
				}
			}
		}

		if (parentFile != null)
		{
			PsiFile file = PsiManager.getInstance(getProject()).findFile(parentFile);
			if (file instanceof HTMLMasonFileImpl)
			{
				return (HTMLMasonFileImpl) file;
			}
		}

		return null;
	}

	@NotNull
	public List<HTMLMasonFileImpl> getChildComponents()
	{
		final List<HTMLMasonFileImpl> result = new ArrayList<HTMLMasonFileImpl>();
		VirtualFile containingFile = getComponentVirtualFile();

		if (containingFile != null)
		{
			VirtualFile componentRoot = getComponentRoot();

			if (componentRoot != null)
			{
				final String relativePath = VfsUtil.VFS_SEPARATOR_CHAR + VfsUtil.getRelativePath(containingFile, componentRoot);
				final Project project = getProject();
				final GlobalSearchScope scope = PerlScopes.getProjectAndLibrariesScope(project);
				final HTMLMasonFileImpl currentFile = this;
				HTMLMasonSettings settings = HTMLMasonSettings.getInstance(project);

				// indexed children
				for (String parentPath : PerlStubIndex.getInstance().getAllKeys(HTMLMasonFlagsStubIndex.KEY, project))
				{
					boolean isEquals = StringUtil.equals(relativePath, parentPath);
					boolean isRelative = parentPath.length() == 0 || parentPath.charAt(0) != VfsUtil.VFS_SEPARATOR_CHAR;

					for (HTMLMasonFlagsStatement statement : PerlStubIndex.getElements(
							HTMLMasonFlagsStubIndex.KEY,
							parentPath,
							project,
							scope,
							HTMLMasonFlagsStatement.class
					))
					{
						PsiFile file = statement.getContainingFile();
						if (file instanceof HTMLMasonFileImpl)
						{
							if (isEquals || isRelative && currentFile.equals(((HTMLMasonFileImpl) file).getParentComponent()))
							{
								result.add((HTMLMasonFileImpl) file);
							}
						}
					}
				}

				// implicit autohandled children
				if (StringUtil.equals(containingFile.getName(), settings.autoHandlerName))
				{
					collectAuthoHandledFiles(PsiManager.getInstance(project), containingFile.getParent(), result, settings.autoHandlerName, null);
				}
			}
		}

		return result;
	}

	protected void collectAuthoHandledFiles(
			PsiManager manager,
			VirtualFile dir,
			List<HTMLMasonFileImpl> result,
			String autoHandlerName,
			@Nullable Set<VirtualFile> recursionMap
	)
	{
		if (dir != null)
		{
			if (recursionMap == null) // first iteration
			{
				recursionMap = new THashSet<VirtualFile>();
			}
			else // non-first iteration
			{
				VirtualFile autohandlerVirtualFile = dir.findChild(autoHandlerName);
				if (autohandlerVirtualFile != null)
				{
					PsiFile autohandlerPsiFile = manager.findFile(autohandlerVirtualFile);
					if (autohandlerPsiFile instanceof HTMLMasonFileImpl && this.equals(((HTMLMasonFileImpl) autohandlerPsiFile).getParentComponent()))
					{
						result.add((HTMLMasonFileImpl) autohandlerPsiFile);
					}
					return;
				}
			}

			recursionMap.add(dir);

			// all iterations
			for (VirtualFile file : dir.getChildren())
			{
				if (file.isDirectory() && !recursionMap.contains(file))
				{
					collectAuthoHandledFiles(manager, file, result, autoHandlerName, recursionMap);
				}
				else if (!StringUtil.equals(file.getName(), autoHandlerName)) // non-autohandler file
				{
					PsiFile psiFile = manager.findFile(file);
					if (psiFile instanceof HTMLMasonFileImpl && this.equals(((HTMLMasonFileImpl) psiFile).getParentComponent()))
					{
						result.add((HTMLMasonFileImpl) psiFile);
					}
				}
			}
		}
	}

	@Nullable
	protected String getParentComponentPath()
	{
		HTMLMasonFlagsStatement statement = getFlagsStatement();
		return statement == null ? null : statement.getParentComponentPath();
	}

	@Nullable
	public HTMLMasonFlagsStatement getFlagsStatement()
	{
		StubElement stub = getStub();
		FlagsStatementSeeker seeker = null;

		if (stub != null)
		{
			seeker = new FlagsStatementStubSeeker();
			//noinspection unchecked
			PerlPsiUtil.processElementsFromStubs(stub, seeker, null);
		}
		else
		{
			seeker = new FlagsStatementPsiSeeker();
			//noinspection unchecked
			PerlPsiUtil.processElementsFromPsi(this, seeker, null);
		}
		return seeker.getResult();
	}

	@Override
	public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place)
	{
		boolean checkShared = false;
		boolean checkArgs = false;
		boolean checkInit = false;
		boolean checkCode = false;
		boolean checkCleanup = false;

		PsiElement onceAnchor = null;
		PsiElement sharedAnchor = null;
		PsiElement argsAnchor = null;
		PsiElement initAnchor = null;
		PsiElement cleanupAnchor = null;

		if (lastParent instanceof HTMLMasonSharedBlockImpl)
		{
			checkShared = true;
			sharedAnchor = lastParent;
		}
		else if (lastParent instanceof HTMLMasonArgsBlockImpl)
		{
			checkShared = true;
			checkArgs = true;
			argsAnchor = lastParent;
		}
		else if (lastParent instanceof HTMLMasonInitBlockImpl)
		{
			checkArgs = true;
			checkShared = true;
			checkInit = true;
			initAnchor = lastParent;
		}
		else if (lastParent instanceof HTMLMasonFilterBlockImpl)
		{
			checkArgs = true;
			checkShared = true;
		}
		else if (lastParent instanceof HTMLMasonOnceBlock)
		{
			onceAnchor = lastParent;
		}
		else if (!(lastParent instanceof HTMLMasonSubcomponentDefitnitionImpl || lastParent instanceof HTMLMasonMethodDefinitionImpl))
		{
			checkArgs = true;
			checkShared = true;
			checkInit = true;
			checkCode = true;

			if (lastParent instanceof HTMLMasonCleanupBlockImpl) // change nature flow
			{
				checkCleanup = true;
				cleanupAnchor = lastParent;
				lastParent = null;
			}
		}

		if (checkCode)
		{
			if (!processChildren(this, processor, state, lastParent, place))
			{
				return false;
			}
		}
		if (checkCleanup)
		{
			if (!checkSubblocks(processor, state, place, HTMLMasonCleanupBlock.class, cleanupAnchor))
			{
				return false;
			}
		}
		if (checkInit)
		{
			if (!checkSubblocks(processor, state, place, HTMLMasonInitBlock.class, initAnchor))
			{
				return false;
			}
		}
		if (checkArgs)
		{
			if (!checkSubblocks(processor, state, place, HTMLMasonArgsBlock.class, argsAnchor))
			{
				return false;
			}
		}
		if (checkShared)
		{
			if (!checkSubblocks(processor, state, place, HTMLMasonSharedBlock.class, sharedAnchor))
			{
				return false;
			}
		}

		if (!checkSubblocks(processor, state, place, HTMLMasonOnceBlock.class, onceAnchor))
		{
			return false;
		}

		// implicit variables
		for (PerlVariableDeclarationWrapper wrapper : getImplicitVariables())
		{
			if (!processor.execute(wrapper, state))
			{
				return false;
			}
		}

		return false;
	}

	@SuppressWarnings("Duplicates")
	protected boolean checkSubblocks(
			@NotNull PsiScopeProcessor processor,
			@NotNull ResolveState state,
			@NotNull PsiElement place,
			@NotNull Class<? extends HTMLMasonCompositeElement> clazz,
			@Nullable PsiElement anchor
	)
	{
		List<HTMLMasonCompositeElement> elements = myBlocksCache.getValue().get(clazz);

		for (int i = elements.size() - 1; i >= 0; i--)
		{
			HTMLMasonCompositeElement element = elements.get(i);
			if (anchor == null && !element.processDeclarationsForReal(processor, state, null, place))
			{
				return false;
			}
			else if (anchor != null && anchor.equals(element))
			{
				anchor = null;
			}
		}

		return true;
	}

	@Override
	public void subtreeChanged()
	{
		super.subtreeChanged();
		myBlocksCache = new MyBlocksCache(this);
	}

	public List<HTMLMasonCompositeElement> getSubComponentsDefinitions()
	{
		return myBlocksCache.getValue().get(HTMLMasonSubcomponentDefitnition.class);
	}


	/**
	 * Recursively looking for method in child components
	 *
	 * @param name method name
	 * @return list of child components
	 */
	@NotNull
	public List<HTMLMasonMethodDefinition> findMethodDefinitionByNameInChildComponents(String name)
	{
		List<HTMLMasonMethodDefinition> result = new ArrayList<HTMLMasonMethodDefinition>();
		Set<HTMLMasonFileImpl> recursionSet = new THashSet<HTMLMasonFileImpl>();

		collectMethodDefinitionByNameInChildComponents(name, result, recursionSet);

		return result;
	}

	protected void collectMethodDefinitionByNameInChildComponents(String name, List<HTMLMasonMethodDefinition> result, Set<HTMLMasonFileImpl> recursionSet)
	{
		for (HTMLMasonFileImpl childComponent : getChildComponents())
		{
			if (!recursionSet.contains(childComponent))
			{
				recursionSet.add(childComponent);
				HTMLMasonMethodDefinition methodDefinition = childComponent.getMethodDefinitionByName(name);
				if (methodDefinition != null)
				{
					result.add(methodDefinition);
				}
				else
				{
					childComponent.collectMethodDefinitionByNameInChildComponents(name, result, recursionSet);
				}
			}
		}
	}

	/**
	 * Recursively looking for method in parent components
	 *
	 * @param name method name
	 * @return method definition or null
	 */
	@Nullable
	public HTMLMasonMethodDefinition findMethodDefinitionByNameInParents(String name)
	{
		HTMLMasonFileImpl parentComponent = getParentComponent();
		return parentComponent == null ? null : parentComponent.findMethodDefinitionByNameInThisOrParents(name);
	}

	/**
	 * Recursively looking for method in current or parent components
	 *
	 * @param name method name
	 * @return method definition or null
	 */
	@Nullable
	public HTMLMasonMethodDefinition findMethodDefinitionByNameInThisOrParents(String name)
	{
		HTMLMasonMethodDefinitionSeeker seeker = new HTMLMasonMethodDefinitionSeeker(name);
		processMethodDefinitionsInThisOrParents(seeker);
		return seeker.getResult();
	}

	public boolean processMethodDefinitionsInThisOrParents(Processor<HTMLMasonMethodDefinition> processor)
	{
		return processMethodDefinitionsInThisOrParents(processor, new THashSet<HTMLMasonFileImpl>());
	}


	protected boolean processMethodDefinitionsInThisOrParents(Processor<HTMLMasonMethodDefinition> processor, Set<HTMLMasonFileImpl> recursionSet)
	{
		if (recursionSet.contains(this))
		{
			return false;
		}
		recursionSet.add(this);

		if (!processMethodDefinitions(processor))
		{
			return false;
		}

		HTMLMasonFileImpl parentComponent = getParentComponent();

		return parentComponent != null && parentComponent.processMethodDefinitionsInThisOrParents(processor, recursionSet);

	}

	@Nullable
	public HTMLMasonMethodDefinition getMethodDefinitionByName(String name)
	{
		HTMLMasonMethodDefinitionSeeker seeker = new HTMLMasonMethodDefinitionSeeker(name);
		processMethodDefinitions(seeker);
		return seeker.getResult();
	}

	protected boolean processMethodDefinitions(Processor<HTMLMasonMethodDefinition> processor)
	{
		for (HTMLMasonCompositeElement methodDefinition : getMethodsDefinitions())
		{
			assert methodDefinition instanceof HTMLMasonMethodDefinition : "got " + methodDefinition + " instead of method definition";
			if (!processor.process((HTMLMasonMethodDefinition) methodDefinition))
			{
				return false;
			}
		}
		return true;
	}

	public List<HTMLMasonCompositeElement> getMethodsDefinitions()
	{
		StubElement stub = getStub();
		if (stub != null)
		{
			final List<HTMLMasonCompositeElement> result = new ArrayList<HTMLMasonCompositeElement>();
			PerlPsiUtil.processElementsFromStubs(stub, new Processor<Stub>()
			{
				@Override
				public boolean process(Stub stub)
				{
					if (stub instanceof HTMLMasonMethodDefinitionStub)
					{
						result.add(((HTMLMasonMethodDefinitionStub) stub).getPsi());
					}
					return true;
				}
			}, null);

			return result;
		}
		return myBlocksCache.getValue().get(HTMLMasonMethodDefinition.class);
	}

	@NotNull
	@Override
	public List<HTMLMasonCompositeElement> getArgsBlocks()
	{
		StubElement stub = getStub();

		//noinspection Duplicates in HTMLMasonStubBasedNamedElementImpl
		if (stub != null)
		{
			final List<HTMLMasonCompositeElement> result = new ArrayList<HTMLMasonCompositeElement>();

			PerlPsiUtil.processElementsFromStubs(
					stub,
					new Processor<Stub>()
					{
						@Override
						public boolean process(Stub stub)
						{
							if (stub instanceof HTMLMasonArgsBlockStub)
							{
								result.add(((HTMLMasonArgsBlockStub) stub).getPsi());
							}
							return true;
						}
					},
					HTMLMasonNamedElementStubBaseImpl.class
			);
			return result;
		}

		return myBlocksCache.getValue().get(HTMLMasonArgsBlock.class);
	}

	@Override
	public byte[] getPerlContentInBytes()
	{
		return null;
	}


	protected abstract static class FlagsStatementSeeker<T> implements Processor<T>
	{
		protected HTMLMasonFlagsStatement myResult = null;

		public HTMLMasonFlagsStatement getResult()
		{
			return myResult;
		}
	}

	protected static class FlagsStatementStubSeeker extends FlagsStatementSeeker<Stub>
	{
		@Override
		public boolean process(Stub stub)
		{
			if (stub instanceof HTMLMasonFlagsStatementStub)
			{
				myResult = ((HTMLMasonFlagsStatementStub) stub).getPsi();
				return false;
			}
			return true;
		}
	}

	protected static class FlagsStatementPsiSeeker extends FlagsStatementSeeker<PsiElement>
	{
		@Override
		public boolean process(PsiElement element)
		{
			if (element instanceof HTMLMasonFlagsStatement)
			{
				myResult = (HTMLMasonFlagsStatement) element;
				return false;
			}
			return true;
		}
	}

	protected static class MyBlocksCache extends AtomicNotNullLazyValue<Map<Class<? extends HTMLMasonCompositeElement>, List<HTMLMasonCompositeElement>>>
	{
		private final HTMLMasonFileImpl myFile;

		public MyBlocksCache(HTMLMasonFileImpl myFile)
		{
			this.myFile = myFile;
		}

		@NotNull
		@Override
		protected Map<Class<? extends HTMLMasonCompositeElement>, List<HTMLMasonCompositeElement>> compute()
		{
			Map<Class<? extends HTMLMasonCompositeElement>, List<HTMLMasonCompositeElement>> result = new THashMap<Class<? extends HTMLMasonCompositeElement>, List<HTMLMasonCompositeElement>>();

			final List<HTMLMasonCompositeElement> cleanupResult = new ArrayList<HTMLMasonCompositeElement>();
			final List<HTMLMasonCompositeElement> initResult = new ArrayList<HTMLMasonCompositeElement>();
			final List<HTMLMasonCompositeElement> argsResult = new ArrayList<HTMLMasonCompositeElement>();
			final List<HTMLMasonCompositeElement> sharedResult = new ArrayList<HTMLMasonCompositeElement>();
			final List<HTMLMasonCompositeElement> onceResult = new ArrayList<HTMLMasonCompositeElement>();
			final List<HTMLMasonCompositeElement> methodsResult = new ArrayList<HTMLMasonCompositeElement>();
			final List<HTMLMasonCompositeElement> subComponentsResult = new ArrayList<HTMLMasonCompositeElement>();

			result.put(HTMLMasonOnceBlock.class, onceResult);
			result.put(HTMLMasonSharedBlock.class, sharedResult);
			result.put(HTMLMasonInitBlock.class, initResult);
			result.put(HTMLMasonArgsBlock.class, argsResult);
			result.put(HTMLMasonCleanupBlock.class, cleanupResult);
			result.put(HTMLMasonMethodDefinition.class, methodsResult);
			result.put(HTMLMasonSubcomponentDefitnition.class, subComponentsResult);

			PsiTreeUtil.processElements(myFile, new PsiElementProcessor()
			{
				@Override
				public boolean execute(@NotNull PsiElement element)
				{
					if (element instanceof HTMLMasonOnceBlock)
					{
						onceResult.add((HTMLMasonCompositeElement) element);
					}
					else if (element instanceof HTMLMasonSharedBlock)
					{
						sharedResult.add((HTMLMasonCompositeElement) element);
					}
					else if (element instanceof HTMLMasonCleanupBlock)
					{
						cleanupResult.add((HTMLMasonCompositeElement) element);
					}
					else if (element instanceof HTMLMasonInitBlock && myFile.equals(PsiTreeUtil.getParentOfType(element, HTMLMasonArgsContainer.class)))
					{
						initResult.add((HTMLMasonCompositeElement) element);
					}
					else if (element instanceof HTMLMasonArgsBlock && myFile.equals(PsiTreeUtil.getParentOfType(element, HTMLMasonArgsContainer.class)))
					{
						argsResult.add((HTMLMasonCompositeElement) element);
					}
					else if (element instanceof HTMLMasonMethodDefinition)
					{
						methodsResult.add((HTMLMasonCompositeElement) element);
					}
					else if (element instanceof HTMLMasonSubcomponentDefitnition)
					{
						subComponentsResult.add((HTMLMasonCompositeElement) element);
					}

					return true;
				}
			});

			return result;
		}
	}

	protected static class HTMLMasonMethodDefinitionSeeker implements Processor<HTMLMasonMethodDefinition>
	{
		private final String myName;
		private HTMLMasonMethodDefinition myResult;

		public HTMLMasonMethodDefinitionSeeker(String myName)
		{
			this.myName = myName;
		}

		@Override
		public boolean process(HTMLMasonMethodDefinition htmlMasonMethodDefinition)
		{
			if (StringUtil.equals(myName, htmlMasonMethodDefinition.getName()))
			{
				myResult = htmlMasonMethodDefinition;
				return false;
			}
			return true;
		}

		public HTMLMasonMethodDefinition getResult()
		{
			return myResult;
		}
	}

}
