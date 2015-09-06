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
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.PerlFileType;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.psi.PerlFile;
import com.perl5.lang.perl.psi.PerlMethod;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PerlVariableDeclaration;
import com.perl5.lang.perl.psi.mro.PerlMro;
import com.perl5.lang.perl.psi.mro.PerlMroC3;
import com.perl5.lang.perl.psi.mro.PerlMroDfs;
import com.perl5.lang.perl.psi.mro.PerlMroType;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import com.perl5.lang.perl.psi.utils.PerlLexicalDeclaration;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import com.perl5.lang.perl.util.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hurricup on 26.04.2015.
 */
public class PerlFileImpl extends PsiFileBase implements PerlFile
{
	private static final ArrayList<String> EMPTY_LIST = new ArrayList<String>();
	protected ConcurrentHashMap<PerlVariable, String> VARIABLE_TYPES_CACHE = new ConcurrentHashMap<PerlVariable, String>();
	protected ConcurrentHashMap<PerlMethod, String> METHODS_NAMESAPCES_CACHE = new ConcurrentHashMap<PerlMethod, String>();
	List<PerlLexicalDeclaration> declaredScalars = new ArrayList<PerlLexicalDeclaration>();
	List<PerlLexicalDeclaration> declaredArrays = new ArrayList<PerlLexicalDeclaration>();
	List<PerlLexicalDeclaration> declaredHashes = new ArrayList<PerlLexicalDeclaration>();
	List<PerlLexicalDeclaration> declaredVariables = new ArrayList<PerlLexicalDeclaration>();
	boolean lexicalCacheInvalid = true;

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
	public String getFilePackageName()
	{
		VirtualFile containingFile = getVirtualFile();

		if ("pm".equals(containingFile.getExtension()))
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
		lexicalCacheInvalid = true;
		VARIABLE_TYPES_CACHE.clear();
		METHODS_NAMESAPCES_CACHE.clear();
	}

	/**
	 * Creates new lexicalVariables scanner or notifies it to restart scanning
	 */
	private synchronized void rescanLexicalVariables()
	{
		if (lexicalCacheInvalid)
		{
//			System.err.println("Started scanning declarations");
			List<PerlLexicalDeclaration> declaredScalars = new ArrayList<PerlLexicalDeclaration>();
			List<PerlLexicalDeclaration> declaredArrays = new ArrayList<PerlLexicalDeclaration>();
			List<PerlLexicalDeclaration> declaredHashes = new ArrayList<PerlLexicalDeclaration>();
			List<PerlLexicalDeclaration> declaredVariables = new ArrayList<PerlLexicalDeclaration>();

			Collection<PerlVariableDeclaration> declarations = PsiTreeUtil.findChildrenOfType(this, PerlVariableDeclaration.class);

			for (PerlVariableDeclaration declaration : declarations)
			{
				// lexically ok
				PerlLexicalScope declarationScope = declaration.getLexicalScope();
				assert declarationScope != null;

				for (PsiElement var : declaration.getScalarVariableList())
				{
					assert var instanceof PerlVariable;
					PerlLexicalDeclaration variableDeclaration = new PerlLexicalDeclaration((PerlVariable) var, declarationScope);
					declaredScalars.add(variableDeclaration);
					declaredVariables.add(variableDeclaration);
				}
				for (PsiElement var : declaration.getArrayVariableList())
				{
					assert var instanceof PerlVariable;
					PerlLexicalDeclaration variableDeclaration = new PerlLexicalDeclaration((PerlVariable) var, declarationScope);
					declaredArrays.add(variableDeclaration);
					declaredVariables.add(variableDeclaration);
				}
				for (PsiElement var : declaration.getHashVariableList())
				{
					assert var instanceof PerlVariable;
					PerlLexicalDeclaration variableDeclaration = new PerlLexicalDeclaration((PerlVariable) var, declarationScope);
					declaredHashes.add(variableDeclaration);
					declaredVariables.add(variableDeclaration);
				}
			}

			this.declaredScalars = declaredScalars;
			this.declaredArrays = declaredArrays;
			this.declaredHashes = declaredHashes;
			this.declaredVariables = declaredVariables;
			lexicalCacheInvalid = false;
//			System.err.println("Finished scanning declarations");
		}
	}

	/**
	 * Searching for most recent lexically visible variable declaration
	 *
	 * @param currentVariable variable to search declaration for
	 * @return variable in declaration term or null if there is no such one
	 */
	public PerlVariable getLexicalDeclaration(PerlVariable currentVariable)
	{
		if (lexicalCacheInvalid)
			rescanLexicalVariables();

		String currentVariableName = currentVariable.getName();
		if (currentVariableName == null)
			return null;

		PerlLexicalScope currentScope = currentVariable.getLexicalScope();
		PerlVariableType variableType = currentVariable.getActualType();

		int currentStatementOffset;

		PsiElement currentStatement = PerlPsiUtil.getElementStatement(currentVariable);

		if (currentStatement == null)
			return null;
//			throw new RuntimeException("Unable to find current variable statement: " + currentVariableName); // atm happens on bad recovery

		currentStatementOffset = currentStatement.getTextOffset();

		List<PerlLexicalDeclaration> knownDeclarations;

		if (variableType == PerlVariableType.SCALAR)
			knownDeclarations = declaredScalars;
		else if (variableType == PerlVariableType.ARRAY)
			knownDeclarations = declaredArrays;
		else if (variableType == PerlVariableType.HASH)
			knownDeclarations = declaredHashes;
		else
			throw new RuntimeException("Unable to find declarations for variable type " + variableType);

		ListIterator<PerlLexicalDeclaration> iterator = knownDeclarations.listIterator(knownDeclarations.size());
		while (iterator.hasPrevious())
		{
			PerlLexicalDeclaration declaration = iterator.previous();
			if (declaration.getTextOffset() < currentStatementOffset
					&& currentVariableName.equals(declaration.getVariable().getName())
					&& PsiTreeUtil.isAncestor(declaration.getScope(), currentScope, false))
				return declaration.getVariable();
		}

		return null;
	}

	/**
	 * Searches for lexically visible variables declarations relatively to the current element
	 *
	 * @return list of visible variables
	 */
	public Collection<PerlVariable> getVisibleLexicalVariables(PsiElement currentElement)
	{
		if (lexicalCacheInvalid)
			rescanLexicalVariables();

		HashMap<String, PerlVariable> declarationsHash = new HashMap<String, PerlVariable>();

		PerlLexicalScope currentScope = PsiTreeUtil.getParentOfType(currentElement, PerlLexicalScope.class);
		assert currentScope != null;

		PsiElement currentStatement = PerlPsiUtil.getElementStatement(currentElement);

		if (currentStatement == null)
			throw new RuntimeException("Unable to find current element statement");

		int currentStatementOffset = currentStatement.getTextOffset();

		ListIterator<PerlLexicalDeclaration> iterator = declaredVariables.listIterator(declaredVariables.size());
		while (iterator.hasPrevious())
		{
			PerlLexicalDeclaration declaration = iterator.previous();
			if (declaration.getTextOffset() < currentStatementOffset)
			{
				// todo actually, we should use variable as a key or canonical variable name WITHOUT package and possible braces
				String variableName = declaration.getVariable().getText();

				if (declarationsHash.get(variableName) == null
						&& PsiTreeUtil.isAncestor(declaration.getScope(), currentScope, false))
					declarationsHash.put(variableName, declaration.getVariable());
			}
		}

		return declarationsHash.values();
	}

	@Override
	public String getPackageName()
	{
		return "main";
	}

	@Override
	public List<String> getParentNamespaces()
	{
		return EMPTY_LIST;
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
			String type = VARIABLE_TYPES_CACHE.get(element);
			return type.isEmpty() ? null : type;
		}

		String type = element.guessVariableTypeHeavy();
		VARIABLE_TYPES_CACHE.put(element, type == null ? "" : type);
		return getVariableType(element);
	}

	@Override
	public String getMethodNamespace(PerlMethod element)
	{
		if (METHODS_NAMESAPCES_CACHE.containsKey(element))
		{
//			System.err.println("Got cached type for method " + element.getText() + " at " + element.getTextOffset());
			String type = METHODS_NAMESAPCES_CACHE.get(element);
			return type.isEmpty() ? null : type;
		}

		String type = element.getContextPackageNameHeavy();
		METHODS_NAMESAPCES_CACHE.put(element, type == null ? "" : type);
		return getMethodNamespace(element);
	}

	@Override
	public Map<String, Set<String>> getImportedSubsNames()
	{
		return PerlSubUtil.getImportedSubs(getProject(), "main", this);
	}

	@Override
	public Map<String, Set<String>> getImportedScalarNames()
	{
		return PerlScalarUtil.getImportedScalars(getProject(), "main", this);
	}

	@Override
	public Map<String, Set<String>> getImportedArrayNames()
	{
		return PerlArrayUtil.getImportedArrays(getProject(), "main", this);
	}

	@Override
	public Map<String, Set<String>> getImportedHashNames()
	{
		return PerlHashUtil.getImportedHashes(getProject(), "main", this);
	}
}
