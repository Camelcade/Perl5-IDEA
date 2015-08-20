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
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.perl.extensions.packageprocessor.IPerlPackageProcessor;
import com.perl5.lang.perl.idea.stubs.imports.PerlUseStatementStub;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.PerlStringContentElement;
import com.perl5.lang.perl.psi.PsiPerlExpr;
import com.perl5.lang.perl.psi.PsiPerlUseStatement;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 31.05.2015.
 */
public abstract class PerlUseStatementImplMixin extends StubBasedPsiElementBase<PerlUseStatementStub> implements PsiPerlUseStatement
{
	public PerlUseStatementImplMixin(ASTNode node)
	{
		super(node);
	}

	public PerlUseStatementImplMixin(PerlUseStatementStub stub, IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	@Override
	public boolean isPragma()
	{
		IPerlPackageProcessor packageProcessor = getPackageProcessor();
		return packageProcessor != null && packageProcessor.isPragma();
	}

	@Override
	public boolean isVersion()
	{
		return getNamespaceElement() == null && getVersionElement() != null;
	}

	@Override
	public boolean isPragmaOrVersion()
	{
		return isPragma() || isVersion();
	}

	@Override
	public String getPackageName()
	{
		PerlNamespaceElement ns = getNamespaceElement();
		if (ns != null)
			return ns.getName();
		return null;
	}

	@Override
	public PerlNamespaceElement getNamespaceElement()
	{
		return findChildByClass(PerlNamespaceElement.class);
	}

	@Override
	public PsiElement getVersionElement()
	{
		return findChildByType(PerlElementTypes.NUMBER_VERSION);
	}

	@Override
	public List<String> getImportParameters()
	{
		ArrayList<String> stringParameters = new ArrayList<String>();
		for (PerlStringContentElement stringContentElement : PerlPsiUtil.findStringElments(getNamespaceElement().getNextSibling()))
			stringParameters.add(stringContentElement.getText());
		return stringParameters;
	}

	@Override
	public IPerlPackageProcessor getPackageProcessor()
	{
		if (getNamespaceElement() != null)
			return getNamespaceElement().getPackageProcessor();
		return null;
	}

	@Override
	public String getOuterPackageName()
	{
		return PerlPackageUtil.getContextPackageName(this);
	}

	@Override
	@Nullable
	public PsiPerlExpr getExpr()
	{
		return findChildByClass(PsiPerlExpr.class);
	}
}
