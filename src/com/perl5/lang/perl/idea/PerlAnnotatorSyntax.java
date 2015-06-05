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
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlFileElementImpl;
import com.perl5.lang.perl.psi.impl.PerlStringContentElementImpl;
import com.perl5.lang.perl.psi.properties.PerlVariableNameElementContainer;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PerlAnnotatorSyntax implements Annotator, PerlElementTypes
{
	private void decorateElement(Annotation annotation, TextAttributesKey key, boolean builtin, boolean deprecated)
	{
		TextAttributes attributes = key.getDefaultAttributes();

		if( builtin )
			attributes = TextAttributes.merge(attributes, PerlSyntaxHighlighter.PERL_BUILT_IN.getDefaultAttributes());
		if( deprecated )
			attributes = TextAttributes.merge(attributes, CodeInsightColors.DEPRECATED_ATTRIBUTES.getDefaultAttributes());

		annotation.setEnforcedTextAttributes(attributes);
	}


	private void annotateVariable(PerlVariable element, AnnotationHolder holder, TextAttributesKey baseKey)
	{
		if( element.isBuiltIn() )
			decorateElement(
					holder.createInfoAnnotation(element, null),
					baseKey,
					true,
					element.isDeprecated());
		else
		{
			// not built-in variable
			Annotation annotation = holder.createInfoAnnotation(element, null);
			annotation.setEnforcedTextAttributes(baseKey.getDefaultAttributes());

//			List<PerlNamespaceDefinition> namespaceDefinitions = ((PerlNamespaceElement) element).getNamespaceDefinitions();
//
//			if( namespaceDefinitions.size() == 0)
//				holder.createWarningAnnotation(element, "Unable to find namespace definition [if this is a module installed from CPAN, it's ok, just NYI]");

		}
	}


	@Override
	public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {

		if( element instanceof PsiPerlScalarVariable || element instanceof PsiPerlArrayIndexVariable)
			annotateVariable((PerlVariable)element, holder, PerlSyntaxHighlighter.PERL_SCALAR);
		else if( element instanceof PsiPerlHashVariable)
			annotateVariable((PerlVariable)element, holder, PerlSyntaxHighlighter.PERL_HASH);
        else if( element instanceof PsiPerlArrayVariable)
			annotateVariable((PerlVariable)element, holder, PerlSyntaxHighlighter.PERL_ARRAY);
		else if( element instanceof PsiPerlGlobVariable)
			decorateElement(
					holder.createInfoAnnotation(element, null),
					PerlSyntaxHighlighter.PERL_GLOB,
					((PerlVariableNameElementContainer) element).isBuiltIn(),
					false);
		else if( element instanceof PsiPerlAnnotation)
			decorateElement(
					holder.createInfoAnnotation(element, null),
					PerlSyntaxHighlighter.PERL_ANNOTATION,
					false,
					false);
		else if( element instanceof PerlNamespaceElement )
		{
			PsiElement parent = element.getParent();

			if( parent instanceof GeneratedParserUtilBase.DummyBlock || parent instanceof PerlNamespaceDefinition )
				return;

			if( ((PerlNamespaceElement) element).isDeprecated())
			{
				Annotation annotation = holder.createWarningAnnotation(element, "This sub is marked as deprecated");
				annotation.setEnforcedTextAttributes(
						TextAttributes.merge(
								PerlSyntaxHighlighter.PERL_BUILT_IN.getDefaultAttributes(),
								CodeInsightColors.DEPRECATED_ATTRIBUTES.getDefaultAttributes())
				);
			}
			else if( ((PerlNamespaceElement) element).isPragma())
			{
				Annotation annotation = holder.createInfoAnnotation(element, null);
				annotation.setEnforcedTextAttributes(
						TextAttributes.merge(
								PerlSyntaxHighlighter.PERL_BUILT_IN.getDefaultAttributes(),
								PerlSyntaxHighlighter.PERL_PACKAGE_PRAGMA.getDefaultAttributes())
				);
			}
			else if( ((PerlNamespaceElement) element).isBuiltin())
			{
				Annotation annotation = holder.createInfoAnnotation(element, null);
				annotation.setEnforcedTextAttributes(
						PerlSyntaxHighlighter.PERL_BUILT_IN.getDefaultAttributes()
				);
			}
			else if( parent instanceof PsiPerlRequireExpr || parent instanceof PsiPerlUseStatement)
			{
				List<PerlFileElementImpl> namespaceFiles = ((PerlNamespaceElement) element).getNamespaceFiles();

				if( namespaceFiles.size() == 0)
					holder.createWarningAnnotation(element, "Unable to find package file [if this is a module installed from CPAN, it's ok, just NYI]");
			}
			else
			{
				List<PerlNamespaceDefinition> namespaceDefinitions = ((PerlNamespaceElement) element).getNamespaceDefinitions();

				if( namespaceDefinitions.size() == 0)
					holder.createWarningAnnotation(element, "Unable to find namespace definition [if this is a module installed from CPAN, it's ok, just NYI]");
			}
		}
		else if( element instanceof PerlStringContentElementImpl)
		{
			PsiElement parent = element.getParent();

			Annotation annotation = holder.createInfoAnnotation(element, null);

			if(  parent instanceof PsiPerlStringDq ) // interpolated
				annotation.setTextAttributes(PerlSyntaxHighlighter.PERL_DQ_STRING);
			else if(  parent instanceof PsiPerlStringXq ) // executable
				annotation.setTextAttributes(PerlSyntaxHighlighter.PERL_DX_STRING);
			else
				annotation.setTextAttributes(PerlSyntaxHighlighter.PERL_SQ_STRING);
		}
		else if( element instanceof PerlSubNameElement)
		{
			PsiElement parent = element.getParent();

			if( parent instanceof GeneratedParserUtilBase.DummyBlock || parent instanceof PerlSubDefinition || parent instanceof PerlSubDeclaration)
				return;

			PerlNamespaceElement methodNamespaceElement = null;
			if( parent instanceof PerlMethod)
				methodNamespaceElement = ((PerlMethod) parent).getNamespaceElement();

			if( methodNamespaceElement != null && methodNamespaceElement.isBuiltin())
			{
				// todo after implementinting and scanning sdk we may remove this. Atm no control for built-in packages methods
			}
			else
			{
				// todo check for declarations and globs aliasing
				// todo globs aliasing must be resolved
				List<PsiElement> subDefinitions = ((PerlSubNameElement) element).getSubDefinitions();

				if (subDefinitions.size() == 0)
					holder.createWarningAnnotation(element, "Unable to find sub definition [if this is a sub from module installed from CPAN, imported sub, inherited sub or chained invocation, it's ok, just NYI]");
//				else if (subDefinitions.size() > 1)
//					holder.createWarningAnnotation(element, "Multiple sub definitions found");
				else
				{
					for( PsiElement subDefinition: subDefinitions )
					{
						if (subDefinition instanceof PerlSubDefinition)
						{
							PerlSubAnnotations subAnnotations = ((PerlSubDefinition) subDefinition).getSubAnnotations();

							if (subAnnotations.isDeprecated())
							{
								Annotation annotation = holder.createWarningAnnotation(element, "This sub is marked as deprecated");
								annotation.setTextAttributes(CodeInsightColors.DEPRECATED_ATTRIBUTES);
								break;
							}
						}
						// todo globs handling here
						// todo check that annotations are the same on multiple definitions
						// todo check that parameters are the same on multiple definitions
						// todo check that attributes are the same on multiple definitions
						// todo check that prototypes are the same on multiple definitions
						// todo check that arguments number suits prototype
					}
				}
			}
		}
	}
}