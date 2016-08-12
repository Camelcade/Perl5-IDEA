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

package com.perl5.lang.ea.idea.annotators;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.PerlBundle;
import com.perl5.lang.ea.psi.PerlExternalAnnotationDeclaration;
import com.perl5.lang.ea.psi.PerlExternalAnnotationNamespace;
import com.perl5.lang.ea.psi.PerlExternalAnnotationsElementTypes;
import com.perl5.lang.perl.idea.annotators.PerlAnnotator;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.util.PerlAnnotationsUtil;
import com.perl5.lang.perl.util.PerlExternalAnnotationsLevels;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Created by hurricup on 10.08.2016.
 */
public class PerlExternalAnnotationsAnnotator extends PerlAnnotator implements PerlExternalAnnotationsElementTypes, PerlExternalAnnotationsLevels
{
	@Override
	public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder)
	{
		IElementType elementType = PsiUtilCore.getElementType(element);
		if (elementType == PSEUDO_SUB_NAME)
		{
			holder.createInfoAnnotation(element, null).setTextAttributes(PerlSyntaxHighlighter.PERL_SUB_DECLARATION);
		}
		else if (elementType == PSEUDO_NAMESPACE)
		{
			processNamespace((PerlExternalAnnotationNamespace) element, holder);
		}
		else if (elementType == PSEUDO_DECLARATION)
		{
			processDeclaration((PerlExternalAnnotationDeclaration) element, holder);
		}
	}

	// fixme this should be in inspection, but they not working on library files
	private boolean processNamespace(@NotNull PerlExternalAnnotationNamespace o, @NotNull AnnotationHolder holder)
	{
		String packageName = o.getPackageName();
		if (StringUtil.isEmpty(packageName))
		{
			return false;
		}

		int psiElementLevel = PerlAnnotationsUtil.getPsiElementLevel(o);
		if (psiElementLevel == UNKNOWN_LEVEL)
		{
			return false;
		}

		Collection<PerlExternalAnnotationNamespace> externalAnnotationsNamespaces = PerlAnnotationsUtil.getExternalAnnotationsNamespaces(o.getProject(), packageName, psiElementLevel);
		if (externalAnnotationsNamespaces == null)
		{
			return false;
		}

		if (externalAnnotationsNamespaces.size() < 2)
		{
			return false;
		}

		PerlNamespaceElement nameIdentifier = o.getNameIdentifier();
		if (nameIdentifier == null)
		{
			return false;
		}

		holder.createWarningAnnotation(nameIdentifier, PerlBundle.message("perl.ea.multiple.namespace.annotations"));
		return true;
	}

	// fixme this should be in inspection, but they not working on library files
	private boolean processDeclaration(@NotNull PerlExternalAnnotationDeclaration o, @NotNull AnnotationHolder holder)
	{
		String canonicalName = o.getCanonicalName();
		if (StringUtil.isEmpty(canonicalName))
		{
			return false;
		}

		int psiElementLevel = PerlAnnotationsUtil.getPsiElementLevel(o);
		if (psiElementLevel == UNKNOWN_LEVEL)
		{
			return false;
		}

		Collection<PerlExternalAnnotationDeclaration> externalAnnotationsSubDeclarations = PerlAnnotationsUtil.getExternalAnnotationsSubDeclarations(o.getProject(), canonicalName, psiElementLevel);
		if (externalAnnotationsSubDeclarations == null)
		{
			return false;
		}

		if (externalAnnotationsSubDeclarations.size() < 2)
		{
			return false;
		}

		PsiElement nameIdentifier = o.getNameIdentifier();
		if (nameIdentifier == null)
		{
			return false;
		}

		holder.createWarningAnnotation(nameIdentifier, PerlBundle.message("perl.ea.multiple.subs.annotations"));
		return true;
	}

}
