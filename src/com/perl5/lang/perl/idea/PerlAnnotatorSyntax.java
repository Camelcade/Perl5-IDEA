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
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlStringContentElementImpl;
import org.jetbrains.annotations.NotNull;

public class PerlAnnotatorSyntax implements Annotator, PerlElementTypes
{
	private void colorize(Annotation annotation, TextAttributesKey key, boolean builtin, boolean deprecated)
	{
		TextAttributes attributes = key.getDefaultAttributes();

		if( builtin )
			attributes = TextAttributes.merge(attributes, PerlSyntaxHighlighter.PERL_BUILT_IN.getDefaultAttributes());
		if( deprecated )
			attributes = TextAttributes.merge(attributes, PerlSyntaxHighlighter.PERL_DEPRECATED.getDefaultAttributes());

		annotation.setEnforcedTextAttributes(attributes);
	}


	@Override
	public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {

		if( element instanceof PsiPerlScalarVariable || element instanceof PsiPerlArrayIndex)
		{
			colorize(
					holder.createInfoAnnotation(element, null),
					PerlSyntaxHighlighter.PERL_SCALAR,
                    false,
					false);
		}
		else if( element instanceof PsiPerlHashVariable)
		{
			colorize(
					holder.createInfoAnnotation(element, null),
					PerlSyntaxHighlighter.PERL_HASH,
                    false,
					false);
		}
        else if( element instanceof PsiPerlArrayVariable)
        {
            colorize(
                    holder.createInfoAnnotation(element, null),
                    PerlSyntaxHighlighter.PERL_ARRAY,
                    false,
                    false);
        }
        else if( element instanceof PsiPerlGlobVariable)
        {
            colorize(
                    holder.createInfoAnnotation(element, null),
                    PerlSyntaxHighlighter.PERL_GLOB,
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
//		else if( elementType == PERL_FUNCTION)
//		{
//			boolean isBuiltIn = PerlFunctionUtil.isBuiltIn(element.getText());
//			colorize(
//					holder.createInfoAnnotation(element, null),
//					isBuiltIn ? PerlSyntaxHighlighter.PERL_OPERATOR :PerlSyntaxHighlighter.PERL_FUNCTION,
//					isBuiltIn,
//					false);
//		}
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