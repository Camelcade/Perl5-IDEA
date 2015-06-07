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
import com.perl5.lang.perl.psi.impl.PerlFileElement;
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

		if (builtin)
			attributes = TextAttributes.merge(attributes, PerlSyntaxHighlighter.PERL_BUILT_IN.getDefaultAttributes());
		if (deprecated)
			attributes = TextAttributes.merge(attributes, CodeInsightColors.DEPRECATED_ATTRIBUTES.getDefaultAttributes());

		annotation.setEnforcedTextAttributes(attributes);
	}


	private void annotateVariable(PerlVariable element, AnnotationHolder holder, TextAttributesKey baseKey)
	{
		if (element.isBuiltIn())
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

			PsiElement parent = element.getParent();

			PerlVariable lexicalDeclaration = element.getLexicalDeclaration();

			boolean isGlobalDeclaration = parent instanceof PsiPerlVariableDeclarationGlobal;
			boolean isLexicalDeclaration = parent instanceof PsiPerlVariableDeclarationLexical;
			PerlNamespaceElement namespaceElement = element.getNamespaceElement();
			PerlVariableNameElement variableNameElement = element.getVariableNameElement();

			if (variableNameElement == null)
				return;

			boolean hasExplicitNamespace = namespaceElement != null;

			// todo we should annotate only variable name

			if (!hasExplicitNamespace)
			{
				if (isLexicalDeclaration && lexicalDeclaration != null)
					holder.createWarningAnnotation(variableNameElement, "Current lexical variable declaration shadows previous declaration of the same variable at line " + lexicalDeclaration.getLineNumber() + ". It's not a error, but not recommended.");
				else if (isGlobalDeclaration && lexicalDeclaration != null)
					holder.createWarningAnnotation(variableNameElement, "Current global variable declaration shadows previous declaration of the same variable at line " + lexicalDeclaration.getLineNumber() + ". It's not a error, but not recommended.");
				else if (lexicalDeclaration == null && !isGlobalDeclaration && !isLexicalDeclaration)
					holder.createWarningAnnotation(variableNameElement, "Unable to find variable declaration in the current file. Forgot to use our for package variable in the current file?");
			} else
			{
				List<PerlVariable> globalDeclarations = element.getGlobalDeclarations();
				List<PerlGlobVariable> relatedGlobs = element.getRelatedGlobs();

				if (globalDeclarations.size() == 0 && relatedGlobs.size() == 0)
					holder.createWarningAnnotation(variableNameElement, "Unable to find global variable declaration or typeglob aliasing for variable. It's not a error, but you should declare it using our() or typeglob alias to make refactoring work properly.");
				else if (globalDeclarations.size() > 0 && relatedGlobs.size() > 0)
					holder.createWarningAnnotation(variableNameElement, "Both global declaration and typeglob aliasing found for variable. It's not a error, but we are not recommend such practice to avoid mistakes.");
				// fixme not sure it's good idea, at least, should be optional
//				else if( relatedGlobs.size() > 1  )
//					holder.createWarningAnnotation(element, "Multiple typeglob aliasing found. It's not a error, but we are not recommend such practice to avoid mistakes.");
			}

			// todo annotate found variables here, not in the beginnig

		}
	}

	private void annotateNamespaceElement(PerlNamespaceElement namespaceElement, AnnotationHolder holder)
	{
		PsiElement parent = namespaceElement.getParent();

		if (parent instanceof GeneratedParserUtilBase.DummyBlock || parent instanceof PerlNamespaceDefinition)
			return;

		if (namespaceElement.isDeprecated())
		{
			Annotation annotation = holder.createWarningAnnotation(namespaceElement, "This sub is marked as deprecated");
			annotation.setEnforcedTextAttributes(
					TextAttributes.merge(
							PerlSyntaxHighlighter.PERL_BUILT_IN.getDefaultAttributes(),
							CodeInsightColors.DEPRECATED_ATTRIBUTES.getDefaultAttributes())
			);
		} else if (namespaceElement.isPragma())
		{
			Annotation annotation = holder.createInfoAnnotation(namespaceElement, null);
			annotation.setEnforcedTextAttributes(
					TextAttributes.merge(
							PerlSyntaxHighlighter.PERL_BUILT_IN.getDefaultAttributes(),
							PerlSyntaxHighlighter.PERL_PACKAGE_PRAGMA.getDefaultAttributes())
			);
		} else if (namespaceElement.isBuiltin())
		{
			Annotation annotation = holder.createInfoAnnotation(namespaceElement, null);
			annotation.setEnforcedTextAttributes(
					PerlSyntaxHighlighter.PERL_BUILT_IN.getDefaultAttributes()
			);
		} else if (parent instanceof PsiPerlRequireExpr || parent instanceof PsiPerlUseStatement)
		{
			List<PerlFileElement> namespaceFiles = namespaceElement.getNamespaceFiles();

			if (namespaceFiles.size() == 0)
				holder.createWarningAnnotation(namespaceElement, "Unable to find package file [if this is a module installed from CPAN, it's ok, just NYI]");
		} else
		{
			List<PerlNamespaceDefinition> namespaceDefinitions = namespaceElement.getNamespaceDefinitions();

			if (namespaceDefinitions.size() == 0)
				holder.createWarningAnnotation(namespaceElement, "Unable to find namespace definition [if this is a module installed from CPAN, it's ok, just NYI]");
		}
	}

	private void annotateSubNameElement(PerlSubNameElement perlSubNameElement, AnnotationHolder holder)
	{
		PsiElement parent = perlSubNameElement.getParent();

		if (parent instanceof GeneratedParserUtilBase.DummyBlock)
			return;

		PerlNamespaceElement methodNamespaceElement = null;
		if (parent instanceof PerlMethod)
			methodNamespaceElement = ((PerlMethod) parent).getNamespaceElement();

		// todo globs aliasing must be resolved
		List<PerlSubDefinition> subDefinitions = perlSubNameElement.getSubDefinitions();
		List<PerlSubDeclaration> subDeclarations = perlSubNameElement.getSubDeclarations();
		List<PerlGlobVariable> relatedGlobs = perlSubNameElement.getRelatedGlobs();

		boolean isDeclaration = parent instanceof PerlSubDeclaration;
		boolean isDefinition = parent instanceof PerlSubDefinition;


		if (subDefinitions.size() == 0 && subDeclarations.size() == 0 && relatedGlobs.size() == 0 && !isDeclaration && !isDefinition)
			holder.createWarningAnnotation(perlSubNameElement, "Unable to find sub definition, declaration or glob aliasing [if this is a sub from module installed from CPAN, imported sub, inherited sub or chained invocation, it's ok, just NYI]");
		else if (subDefinitions.size() > 1 || (subDefinitions.size() > 0 && isDefinition))
			holder.createWarningAnnotation(perlSubNameElement, "Multiple sub definitions found");
		else if (subDeclarations.size() > 1 || (subDeclarations.size() > 0 && isDeclaration))
			holder.createWarningAnnotation(perlSubNameElement, "Multiple sub declarations found");
//				else if (relatedGlobs.size() > 1)
//					holder.createWarningAnnotation(element, "Multiple typeglob aliases found");
		else if ((isDeclaration || isDefinition) && relatedGlobs.size() > 0)
			holder.createWarningAnnotation(perlSubNameElement, "Typeglob assignment may override current definition or declaration");
		else
		{
			PerlSubAnnotations subAnnotations = subDefinitions.size() > 0
					? subDefinitions.get(0).getSubAnnotations()
					: subDeclarations.size() > 0 ? subDeclarations.get(0).getSubAnnotations() : null;

			if (subAnnotations != null)
			{
				if (subAnnotations.isDeprecated())
				{
					Annotation annotation = holder.createWarningAnnotation(perlSubNameElement, "This sub is marked as deprecated");
					annotation.setTextAttributes(CodeInsightColors.DEPRECATED_ATTRIBUTES);
				}
			}

			// todo check that annotations are the same on definition and declaration
			// todo check that attributes are the same on definition and declaration
			// todo check that prototypes are the same on definition and declaration
			// todo check that arguments number suits prototype
			// todo check for override
		}
	}

	private void annotateStringContent(PerlStringContentElementImpl element, AnnotationHolder holder)
	{
		PsiElement parent = element.getParent();

		Annotation annotation = holder.createInfoAnnotation((PsiElement) element, null);

		if (parent instanceof PsiPerlStringDq) // interpolated
			annotation.setTextAttributes(PerlSyntaxHighlighter.PERL_DQ_STRING);
		else if (parent instanceof PsiPerlStringXq) // executable
			annotation.setTextAttributes(PerlSyntaxHighlighter.PERL_DX_STRING);
		else
			annotation.setTextAttributes(PerlSyntaxHighlighter.PERL_SQ_STRING);
	}


	@Override
	public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder)
	{

		if (element instanceof PsiPerlScalarVariable || element instanceof PsiPerlArrayIndexVariable)
			annotateVariable((PerlVariable) element, holder, PerlSyntaxHighlighter.PERL_SCALAR);
		else if (element instanceof PsiPerlHashVariable)
			annotateVariable((PerlVariable) element, holder, PerlSyntaxHighlighter.PERL_HASH);
		else if (element instanceof PsiPerlArrayVariable)
			annotateVariable((PerlVariable) element, holder, PerlSyntaxHighlighter.PERL_ARRAY);
		else if (element instanceof PsiPerlGlobVariable)
			decorateElement(
					holder.createInfoAnnotation(element, null),
					PerlSyntaxHighlighter.PERL_GLOB,
					((PerlVariableNameElementContainer) element).isBuiltIn(),
					false);
		else if (element instanceof PsiPerlAnnotation)
			decorateElement(
					holder.createInfoAnnotation(element, null),
					PerlSyntaxHighlighter.PERL_ANNOTATION,
					false,
					false);
		else if (element instanceof PerlNamespaceElement)
			annotateNamespaceElement((PerlNamespaceElement) element, holder);
		else if (element instanceof PerlStringContentElementImpl)
			annotateStringContent((PerlStringContentElementImpl) element, holder);
		else if (element instanceof PerlSubNameElement)
			annotateSubNameElement((PerlSubNameElement) element, holder);
	}
}