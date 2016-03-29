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
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.idea.PerlElementPatterns;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.PerlLexer;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.PerlSubNameElement;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.pod.parser.psi.PodCompositeElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URLDecoder;
import java.util.List;

/**
 * Created by hurricup on 26.03.2016.
 */
public class PerlDocumentationProvider extends AbstractDocumentationProvider implements PerlElementTypes, PerlElementPatterns
{
	private static final TokenSet myForceAsOp = TokenSet.create(
			RESERVED_Q,
			RESERVED_QQ,
			RESERVED_QX,
			RESERVED_QW,
			RESERVED_TR,
			RESERVED_Y,
			HEREDOC_OPENER,
			HEREDOC_END,
			HEREDOC,
			HEREDOC_QQ,
			HEREDOC_QX,

			RESERVED_S,
			RESERVED_M,
			RESERVED_QR
	);
	private static final TokenSet myForceAsFunc = TokenSet.create(
			BLOCK_NAME,
			TAG,
			OPERATOR_FILETEST
	);

	protected static boolean isFunc(PsiElement element)
	{
		IElementType elementType = element.getNode().getElementType();
		return myForceAsFunc.contains(elementType) ||
				!myForceAsOp.contains(elementType) && (
						PerlLexer.RESERVED_TOKENSET.contains(elementType) ||
								element instanceof PerlSubNameElement && ((PerlSubNameElement) element).isBuiltIn()
				);
	}

	protected static boolean isOp(PsiElement element)
	{
		IElementType elementType = element.getNode().getElementType();
		return myForceAsOp.contains(elementType) ||
				!myForceAsFunc.contains(elementType) && (
						PerlLexer.OPERATORS_TOKENSET.contains(elementType)
				);
	}

	@Override
	public String generateDoc(PsiElement element, @Nullable PsiElement originalElement)
	{
		if (element instanceof PodCompositeElement)
		{
			return PerlDocUtil.renderElement((PodCompositeElement) element);
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
		if (context instanceof PodCompositeElement)
		{
			try
			{
				return PerlDocUtil.resolveDocLink(URLDecoder.decode(link, "UTF-8"), context);
			} catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
		return super.getDocumentationElementForLink(psiManager, link, context);
	}

	@Nullable
	@Override
	public PsiElement getCustomDocumentationElement(@NotNull Editor editor, @NotNull PsiFile file, @Nullable PsiElement contextElement)
	{
		if (contextElement == null || contextElement.getLanguage() != PerlLanguage.INSTANCE)
			return null;

		IElementType elementType = contextElement.getNode().getElementType();

		if (contextElement instanceof PerlVariable)
		{
			return PerlDocUtil.getPerlVarDoc((PerlVariable) contextElement); // fixme try to search doc in package or declaration
		}
		if (elementType == REGEX_MODIFIER)
		{
			return PerlDocUtil.getRegexModifierDoc(contextElement);
		}
		else if (isFunc(contextElement))
		{
			return PerlDocUtil.getPerlFuncDoc(contextElement);
		}
		else if (isOp(contextElement))
		{
			return PerlDocUtil.getPerlOpDoc(contextElement);
		}
		else if (contextElement instanceof PerlSubNameElement)
		{
			String packageName = ((PerlSubNameElement) contextElement).getPackageName();
			String subName = ((PerlSubNameElement) contextElement).getName();
			if (StringUtil.isNotEmpty(packageName) && StringUtil.isNotEmpty(subName))
			{
				return PerlDocUtil.resolveDocLink(packageName + "/" + ((PerlSubNameElement) contextElement).getName(), contextElement);
			}
		}
		else if (contextElement instanceof PerlNamespaceElement)
		{
			String packageName = ((PerlNamespaceElement) contextElement).getCanonicalName();
			if (StringUtil.isNotEmpty(packageName))
			{
				return PerlDocUtil.resolveDocLink(packageName, contextElement);
			}
		}
		else if (!(contextElement instanceof PsiFile))
		{
			return getCustomDocumentationElement(editor, file, contextElement.getParent());
		}

		return super.getCustomDocumentationElement(editor, file, contextElement);
	}
}
