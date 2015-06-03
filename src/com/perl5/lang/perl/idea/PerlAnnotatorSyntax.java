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

package com.perl5.lang.perl.idea;

/**
 * Created by hurricup on 25.04.2015.
 */
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlStringContentElementImpl;
import com.perl5.lang.perl.psi.properties.PerlVariableNameElementContainer;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PerlAnnotatorSyntax implements Annotator, PerlElementTypes
{
	private void colorize(Annotation annotation, TextAttributesKey key, boolean builtin, boolean deprecated)
	{
		TextAttributes attributes = key.getDefaultAttributes();

		if( builtin )
			attributes = TextAttributes.merge(attributes, PerlSyntaxHighlighter.PERL_BUILT_IN.getDefaultAttributes());
		if( deprecated )
			attributes = TextAttributes.merge(attributes, CodeInsightColors.DEPRECATED_ATTRIBUTES.getDefaultAttributes());

		annotation.setEnforcedTextAttributes(attributes);
	}


	@Override
	public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {

		if( element instanceof PsiPerlScalarVariable || element instanceof PsiPerlArrayIndexVariable)
		{
			colorize(
					holder.createInfoAnnotation(element, null),
					PerlSyntaxHighlighter.PERL_SCALAR,
					((PerlVariableNameElementContainer)element).isBuiltIn(),
					false);
		}
		else if( element instanceof PsiPerlHashVariable)
		{
			colorize(
					holder.createInfoAnnotation(element, null),
					PerlSyntaxHighlighter.PERL_HASH,
					((PerlVariableNameElementContainer)element).isBuiltIn(),
					false);
		}
        else if( element instanceof PsiPerlArrayVariable)
        {
            colorize(
                    holder.createInfoAnnotation(element, null),
                    PerlSyntaxHighlighter.PERL_ARRAY,
					((PerlVariableNameElementContainer)element).isBuiltIn(),
                    false);
        }
		else if( element instanceof PsiPerlGlobVariable)
		{
			colorize(
					holder.createInfoAnnotation(element, null),
					PerlSyntaxHighlighter.PERL_GLOB,
					((PerlVariableNameElementContainer)element).isBuiltIn(),
					false);
		}
		else if( element instanceof PsiPerlAnnotation)
		{
			colorize(
					holder.createInfoAnnotation(element, null),
					PerlSyntaxHighlighter.PERL_ANNOTATION,
					false,
					false);
		}
//		if( elementType == PERL_PACKAGE )
//		{
//			String packageName = element.getText();
//			PerlPackageUtil.PACKAGE_TYPE packageType = PerlPackageUtil.getPackageType(packageName);
//
//			String message = packageType == PerlPackageUtil.PACKAGE_TYPE.DEPRECATED ?
//					"Package "+packageName+" is marked as deprecated and may be removed in future perl versions"
//					: null;
//
//			colorize(
//					holder.createInfoAnnotation(element, message),
//					packageType == PerlPackageUtil.PACKAGE_TYPE.PRAGMA ? PerlSyntaxHighlighter.PERL_PACKAGE_PRAGMA: PerlSyntaxHighlighter.PERL_PACKAGE,
//					packageType != null,
//					packageType == PerlPackageUtil.PACKAGE_TYPE.DEPRECATED);
//
//		}
		//else
		if( element instanceof PerlStringContentElementImpl)
		{
			PsiElement parent = element.getParent();

			Annotation annotation = holder.createInfoAnnotation(element, null);

			if(  parent instanceof PsiPerlStringDq ) // interpolated
			{
				annotation.setTextAttributes(PerlSyntaxHighlighter.PERL_DQ_STRING);
			}
			else if(  parent instanceof PsiPerlStringXq ) // executable
			{
				annotation.setTextAttributes(PerlSyntaxHighlighter.PERL_DX_STRING);
			}
			else
			{
				annotation.setTextAttributes(PerlSyntaxHighlighter.PERL_SQ_STRING);
			}
		}
		else if( element instanceof PerlSubNameElement)
		{
			List<PsiElement> subDefinitions = ((PerlSubNameElement) element).getSubDefinitions();

			if( subDefinitions.size() == 0)
				holder.createInfoAnnotation(element, "Unable to find sub definition");
			else if( subDefinitions.size() > 1)
				holder.createInfoAnnotation(element, "Multiple sub definitions found");
			else
			{
				PsiElement subDefinition = subDefinitions.get(0);

				if( subDefinition instanceof PerlSubDefinition)
				{
					PerlSubAnnotations subAnnotations = ((PerlSubDefinition) subDefinition).getAnnotations();

					if (subAnnotations.isDeprecated())
					{
						Annotation annotation = holder.createInfoAnnotation(element, "This sub is marked as deprecated");
						annotation.setTextAttributes(CodeInsightColors.DEPRECATED_ATTRIBUTES);
					}
				}
			}

		}
//		else if( elementType == PERL_METHOD)
//		{
//			colorize(
//					holder.createInfoAnnotation(element, null),
//					PerlSyntaxHighlighter.PERL_FUNCTION,
//					false,
//					false);
//		}
	}
}