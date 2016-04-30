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

import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.PsiElementProcessor;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.mojolicious.psi.MojoliciousElementPatterns;
import com.perl5.lang.mojolicious.psi.impl.MojoliciousHelperDeclarationImpl;
import com.perl5.lang.perl.idea.configuration.settings.Perl5Settings;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlCompositeElementImpl;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import com.perl5.lang.perl.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 24.05.2015.
 */
public abstract class PerlVariableImplMixin extends PerlCompositeElementImpl implements PerlElementTypes, PerlVariable
{
	public PerlVariableImplMixin(ASTNode node)
	{
		super(node);
	}

	@Override
	public PerlLexicalScope getLexicalScope()
	{
		return PsiTreeUtil.getParentOfType(this, PerlLexicalScope.class);
	}

	@NotNull
	@Override
	public String getPackageName()
	{
		String namespace = getExplicitPackageName();

		if (namespace == null)
			namespace = getContextPackageName();

		return namespace;
	}

	@NotNull
	@Override
	public String getContextPackageName()
	{
		return PerlPackageUtil.getContextPackageName(this);
	}

	@Override
	public String getExplicitPackageName()
	{
		PerlNamespaceElement namespaceElement = getNamespaceElement();
		return namespaceElement != null ? namespaceElement.getCanonicalName() : null;
	}

	@Override
	public PerlNamespaceElement getNamespaceElement()
	{
		return findChildByClass(PerlNamespaceElement.class);
	}

	@Nullable
	@Override
	public PerlVariableNameElement getVariableNameElement()
	{
		return findChildByClass(PerlVariableNameElement.class);
	}

	@Nullable
	@Override
	public String guessVariableType()
	{
		PsiFile file = getContainingFile();
		if (file instanceof PerlFileImpl)
			return ((PerlFileImpl) file).getVariableType(this);
		return getVariableTypeHeavy();
	}


	@Nullable
	@Override
	public String getVariableTypeHeavy()
	{
		if (this instanceof PsiPerlScalarVariable)
		{
//			System.err.println("Guessing type for " + getText() + " at " + getTextOffset());

			PerlVariableNameElement variableNameElement = getVariableNameElement();

			if (variableNameElement != null)
			{
				if (isSelf())
				{
					// fixme this is hacky, we should generalize this in some way, see #1062
					if (MojoliciousElementPatterns.MOJO_ELEMENT_IN_HELPER.accepts(this))
					{
						return MojoliciousHelperDeclarationImpl.HELPER_NAMESPACE_NAME;
					}
					return PerlPackageUtil.getContextPackageName(this);
				}

				// find lexicaly visible declaration and check type
				final PerlVariableDeclarationWrapper declarationWrapper = getLexicalDeclaration();
				if (declarationWrapper != null)
				{
					if (declarationWrapper.isInvocantDeclaration())
					{
						return PerlPackageUtil.getContextPackageName(this);
					}

					// check explicit type in declaration
					String declarationPackageName = declarationWrapper.getDeclaredType();
					if (declarationPackageName != null)
					{
						assert !declarationPackageName.equals("");
						return declarationPackageName;
					}

					// check assignment around declaration
					PerlVariableDeclaration declaration = PsiTreeUtil.getParentOfType(declarationWrapper, PerlVariableDeclaration.class);
					if (declaration != null)
					{
						if (declaration.getParent() instanceof PsiPerlAssignExpr)
						{
							PsiPerlAssignExpr assignmentExpression = (PsiPerlAssignExpr) declaration.getParent();
							List<PsiPerlExpr> assignmentElements = assignmentExpression.getExprList();

							if (assignmentElements.size() > 0)
							{
								PsiPerlExpr lastExpression = assignmentElements.get(assignmentElements.size() - 1);

								if (lastExpression != declaration)
								{
									// source element is on the left side
									if (lastExpression instanceof PerlMethodContainer)
									{
										return PerlSubUtil.getMethodReturnValue((PerlMethodContainer) lastExpression);
									}
									if (lastExpression instanceof PerlDerefExpression)
									{
										return ((PerlDerefExpression) lastExpression).guessType();
									}
								}
							}
						}
					}

					// fixme this is bad, because my $var1 && print $var1 will be valid, but it's not
					PerlLexicalScope perlLexicalScope = PsiTreeUtil.getParentOfType(declarationWrapper, PerlLexicalScope.class);
					assert perlLexicalScope != null : "Got error at " + declarationWrapper.getTextOffset() + " in " + declarationWrapper.getContainingFile().getVirtualFile();

					final String[] guessResult = new String[]{null};

					int startOffset = declarationWrapper.getTextRange().getEndOffset();
					int endOffset = getTextRange().getStartOffset();

					if (startOffset < endOffset)
					{
						PerlPsiUtil.processElementsInRange(
								perlLexicalScope,
								new TextRange(startOffset, endOffset),
								new PsiElementProcessor<PsiElement>()
								{
									@Override
									public boolean execute(@NotNull PsiElement element)
									{
										if (element != PerlVariableImplMixin.this &&
												element instanceof PsiPerlScalarVariable &&
												element.getParent() instanceof PsiPerlAssignExpr
												)
										{
											PsiElement variableNameElement = ((PsiPerlScalarVariable) element).getVariableNameElement();

											if (variableNameElement != null &&
													variableNameElement.getReference() != null &&
													variableNameElement.getReference().isReferenceTo(declarationWrapper)
													)
											{
												// found variable assignment
												PsiPerlAssignExpr assignmentExpression = (PsiPerlAssignExpr) element.getParent();
												List<PsiPerlExpr> assignmentElements = assignmentExpression.getExprList();

												if (assignmentElements.size() > 0)
												{
													PsiPerlExpr lastExpression = assignmentElements.get(assignmentElements.size() - 1);

													if (lastExpression != element && lastExpression.getTextOffset() < getTextOffset())
													{
														// source element is on the left side
														// fixme implement variables assignment support. Need to build kinda visitor with recursion control
														String returnValue = null;
														if (lastExpression instanceof PerlMethodContainer)
														{
															returnValue = PerlSubUtil.getMethodReturnValue((PerlMethodContainer) lastExpression);
														}
														if (lastExpression instanceof PerlDerefExpression)
														{
															returnValue = ((PerlDerefExpression) lastExpression).guessType();
														}
														if (StringUtil.isNotEmpty(returnValue))
														{
															guessResult[0] = returnValue;
															return false;
														}
													}
												}
											}
										}
										return true;
									}
								}
						);
					}

					if (guessResult[0] != null)
					{
						return guessResult[0];
					}
				}

				// checking global declarations with explicit types
				for (PerlVariableDeclarationWrapper declaration : getGlobalDeclarations())
					if (declaration.getDeclaredType() != null)
						return declaration.getDeclaredType();
			}
		}

		return null;
	}

	@Override
	public PerlVariableType getActualType()
	{
		PsiElement variableContainer = this.getParent();

		if (this instanceof PsiPerlCodeVariable)
			return PerlVariableType.SCALAR;
		else if (
				variableContainer instanceof PsiPerlScalarHashElement
						|| variableContainer instanceof PsiPerlArrayHashSlice
						|| this instanceof PsiPerlHashVariable
				)
			return PerlVariableType.HASH;
		else if (
				variableContainer instanceof PsiPerlArrayArraySlice
						|| variableContainer instanceof PsiPerlScalarArrayElement
						|| this instanceof PsiPerlArrayIndexVariable
						|| this instanceof PsiPerlArrayVariable
				)
			return PerlVariableType.ARRAY;
		else if (
				variableContainer instanceof PsiPerlDerefExpr
						|| this instanceof PsiPerlScalarVariable
				)
			return PerlVariableType.SCALAR;
		else
			throw new RuntimeException("Can't be: could not detect actual type of myVariable: " + getText());

	}


	@Override
	public boolean isBuiltIn()
	{
		if (getNamespaceElement() != null)
			return false;

		if (getVariableNameElement() == null)
			return false;

		PerlVariableType variableType = getActualType();

		// tood make getter for this
		String variableName = getVariableNameElement().getName();

		if (variableType == PerlVariableType.SCALAR)
			return variableName.matches("^\\d+$") || PerlScalarUtil.BUILT_IN.contains(variableName);
		if (variableType == PerlVariableType.ARRAY)
			return PerlArrayUtil.BUILT_IN.contains(variableName);

		return variableType == PerlVariableType.HASH && PerlHashUtil.BUILT_IN.contains(variableName);
	}

	@Override
	public boolean isDeprecated()
	{
		return false;
	}

	@Override
	public String getName()
	{
		PerlVariableNameElement variableNameElement = getVariableNameElement();
		if (variableNameElement != null)
			return variableNameElement.getName();

		return super.getName();
	}

	@NotNull
	@Override
	public String getCanonicalName()
	{
		return getPackageName() + PerlPackageUtil.PACKAGE_SEPARATOR + getName();
	}

	@Override
	public PerlVariableDeclarationWrapper getLexicalDeclaration()
	{
		if (getNamespaceElement() != null)
			return null;

		PsiFile myFile = getContainingFile();
		if (myFile instanceof PerlFileImpl)
			return ((PerlFileImpl) myFile).getLexicalDeclaration(this);

		return null;
	}

	@Override
	public List<PerlVariableDeclarationWrapper> getGlobalDeclarations()
	{
		List<PerlVariableDeclarationWrapper> result = new ArrayList<PerlVariableDeclarationWrapper>();
		PerlVariableType myType = getActualType();

		PsiElement parent = getParent(); // wrapper if any

		if (myType == PerlVariableType.SCALAR)
		{
			for (PerlVariableDeclarationWrapper variable : PerlScalarUtil.getGlobalScalarDefinitions(getProject(), getCanonicalName()))
				if (!variable.equals(parent))
					result.add(variable);
		}
		else if (myType == PerlVariableType.ARRAY)
		{
			for (PerlVariableDeclarationWrapper variable : PerlArrayUtil.getGlobalArrayDefinitions(getProject(), getCanonicalName()))
				if (!variable.equals(parent))
					result.add(variable);
		}
		else if (myType == PerlVariableType.HASH)
		{
			for (PerlVariableDeclarationWrapper variable : PerlHashUtil.getGlobalHashDefinitions(getProject(), getCanonicalName()))
				if (!variable.equals(parent))
					result.add(variable);
		}

		return result;
	}

	@Override
	public List<PerlGlobVariable> getRelatedGlobs()
	{
		List<PerlGlobVariable> result = new ArrayList<PerlGlobVariable>();

		for (PsiPerlGlobVariable glob : PerlGlobUtil.getGlobsDefinitions(getProject(), getCanonicalName()))
			result.add(glob);

		return result;
	}

	@Override
	public int getLineNumber()
	{
		Document document = PsiDocumentManager.getInstance(getProject()).getCachedDocument(getContainingFile());
		return document == null ? 0 : document.getLineNumber(getTextOffset()) + 1;
	}

	@Override
	public boolean isSelf()
	{
		return getActualType() == PerlVariableType.SCALAR && Perl5Settings.getInstance(getProject()).isSelfName(getName());
	}
}
