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

package com.perl5.lang.perl.documentation;

import com.intellij.lang.documentation.AbstractDocumentationProvider;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.lexer.PerlLexer;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PerlVariableNameElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by hurricup on 26.03.2016.
 */
public class PerlDocumentationProvider extends AbstractDocumentationProvider
{
	// fixme guess we should create specific token class for this
	protected static boolean isReserved(PsiElement element)
	{
		return PerlLexer.RESERVED_TOKENSET.contains(element.getNode().getElementType());
	}

	@Override
	public String generateDoc(PsiElement element, @Nullable PsiElement originalElement)
	{
		if (element instanceof PerlVariable)
		{
			return PerlDocUtil.getVariableDoc((PerlVariable) element);
		}
		else if (isReserved(element))
		{
			return PerlDocUtil.getReservedDoc(element);
		}

		return super.generateDoc(element, originalElement);
	}

	@Nullable
	@Override
	public String getQuickNavigateInfo(PsiElement element, PsiElement originalElement)
	{
		return super.getQuickNavigateInfo(element, originalElement);
	}

	@Override
	public List<String> getUrlFor(PsiElement element, PsiElement originalElement)
	{
		return super.getUrlFor(element, originalElement);
	}

	@Override
	public PsiElement getDocumentationElementForLookupItem(PsiManager psiManager, Object object, PsiElement element)
	{
		return super.getDocumentationElementForLookupItem(psiManager, object, element);
	}

	@Override
	public PsiElement getDocumentationElementForLink(PsiManager psiManager, String link, PsiElement context)
	{
		return super.getDocumentationElementForLink(psiManager, link, context);
	}

	@Nullable
	@Override
	public PsiElement getCustomDocumentationElement(@NotNull Editor editor, @NotNull PsiFile file, @Nullable PsiElement contextElement)
	{
		if (contextElement == null)
			return null;

		if (contextElement instanceof PerlVariableNameElement)
		{
			return PsiTreeUtil.getParentOfType(contextElement, PerlVariable.class);
		}
		else if (isReserved(contextElement))
		{
			return contextElement;
		}
		return super.getCustomDocumentationElement(editor, file, contextElement);
	}

}
