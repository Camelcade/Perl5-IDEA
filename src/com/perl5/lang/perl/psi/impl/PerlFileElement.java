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
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PerlVariableDeclaration;
import com.perl5.lang.perl.psi.PsiPerlStatement;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import com.perl5.lang.perl.psi.utils.PerlLexicalDeclaration;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Created by hurricup on 26.04.2015.
 */
public class PerlFileElement extends PsiFileBase implements PerlLexicalScope
{
	List<PerlLexicalDeclaration> declaredScalars = new ArrayList<>();
	List<PerlLexicalDeclaration> declaredArrays = new ArrayList<>();
	List<PerlLexicalDeclaration> declaredHashes = new ArrayList<>();
	List<PerlLexicalDeclaration> declaredVariables = new ArrayList<>();

	boolean lexicalCacheInvalid = true;

	public PerlFileElement(@NotNull FileViewProvider viewProvider, Language language)
	{
		super(viewProvider, language);
	}

	public PerlFileElement(@NotNull FileViewProvider viewProvider)
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
	}

	/**
	 * Creates new lexicalVariables scanner or notifies it to restart scanning
	 */
	private synchronized void rescanLexicalVariables()
	{
		if (lexicalCacheInvalid)
		{
			List<PerlLexicalDeclaration> declaredScalars = new ArrayList<>();
			List<PerlLexicalDeclaration> declaredArrays = new ArrayList<>();
			List<PerlLexicalDeclaration> declaredHashes = new ArrayList<>();
			List<PerlLexicalDeclaration> declaredVariables = new ArrayList<>();

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
		PsiPerlStatement currentStatement = PsiTreeUtil.getParentOfType(currentVariable, PsiPerlStatement.class);
		PerlVariableType variableType = currentVariable.getActualType();

		if (currentStatement == null)
			throw new RuntimeException("Unable to find current variable statement");

		int currentStatementOffset = currentStatement.getTextOffset();

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

		HashMap<String, PerlVariable> declarationsHash = new HashMap<>();

		PerlLexicalScope currentScope = PsiTreeUtil.getParentOfType(currentElement, PerlLexicalScope.class);
		assert currentScope != null;

		PsiPerlStatement currentStatement = PsiTreeUtil.getParentOfType(currentElement, PsiPerlStatement.class);

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
}
