package com.perl5.lang.perl.idea;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.helpers.*;
import com.perl5.lang.perl.psi.impl.PerlFunctionDefinitionNamedImpl;
import com.perl5.lang.perl.psi.impl.PerlFunctionDefinitionNamedUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 25.04.2015.
 */
public class PerlCompletionContributor extends CompletionContributor
{
	// @todo implement some tree running for defined methods
	public PerlCompletionContributor() {
		extend(
				CompletionType.BASIC,
				PlatformPatterns.psiElement(PerlElementTypes.PERL_VARIABLE_ARRAY).withLanguage(PerlLanguage.INSTANCE),
				new CompletionProvider<CompletionParameters>() {
					public void addCompletions(@NotNull CompletionParameters parameters,
											   ProcessingContext context,
											   @NotNull CompletionResultSet resultSet) {

						for( String arrayName: PerlArray.BUILT_IN )
						{
							resultSet.addElement(LookupElementBuilder.create(arrayName));
							System.err.println(arrayName);
						}

					}
				}
		);
		extend(
				CompletionType.BASIC,
				PlatformPatterns.psiElement(PerlElementTypes.PERL_VARIABLE_SCALAR).withLanguage(PerlLanguage.INSTANCE),
				new CompletionProvider<CompletionParameters>() {
					public void addCompletions(@NotNull CompletionParameters parameters,
											   ProcessingContext context,
											   @NotNull CompletionResultSet resultSet) {

						for( String scalarName: PerlScalar.BUILT_IN )
						{
							resultSet.addElement(LookupElementBuilder.create(scalarName));
						}

					}
				}
		);
		extend(
				CompletionType.BASIC,
				PlatformPatterns.psiElement(PerlElementTypes.PERL_FUNCTION_USER).withLanguage(PerlLanguage.INSTANCE),
				new CompletionProvider<CompletionParameters>() {
					public void addCompletions(@NotNull CompletionParameters parameters,
											   ProcessingContext context,
											   @NotNull CompletionResultSet resultSet) {

						for( String functionName: PerlFunction.BUILT_IN )
						{
							resultSet.addElement(LookupElementBuilder.create(functionName));
						}
						for( String functionName: PerlFunction.IMPLEMENTED )
						{
							resultSet.addElement(LookupElementBuilder.create(functionName));
						}

						// append prevoiusly defined functions
						PsiElement currentPosition = parameters.getPosition();

						while( currentPosition != null )
						{
							if( currentPosition instanceof PerlFunctionDefinitionNamedImpl)
							{
								PsiElement functionName = ((PerlFunctionDefinitionNamedImpl)currentPosition).getPerlFunctionUser();

								if( functionName != null )
									resultSet.addElement(LookupElementBuilder.create(functionName.getText()));
							}


							PsiElement prevPosition = currentPosition.getPrevSibling();
							if( prevPosition == null )
							{
								prevPosition = currentPosition.getParent();
							}
							currentPosition = prevPosition;
						}

					}
				}
		);
		extend(
				CompletionType.BASIC,
				PlatformPatterns.psiElement(PerlElementTypes.PERL_VARIABLE_HASH).withLanguage(PerlLanguage.INSTANCE),
				new CompletionProvider<CompletionParameters>() {
					public void addCompletions(@NotNull CompletionParameters parameters,
											   ProcessingContext context,
											   @NotNull CompletionResultSet resultSet) {

						for( String hashName: PerlHash.BUILT_IN )
						{
							resultSet.addElement(LookupElementBuilder.create(hashName));
						}

					}
				}
		);
		extend(
				CompletionType.BASIC,
				PlatformPatterns.psiElement(PerlElementTypes.PERL_GLOB).withLanguage(PerlLanguage.INSTANCE),
				new CompletionProvider<CompletionParameters>() {
					public void addCompletions(@NotNull CompletionParameters parameters,
											   ProcessingContext context,
											   @NotNull CompletionResultSet resultSet) {

						for( String globName: PerlGlob.BUILT_IN )
						{
							resultSet.addElement(LookupElementBuilder.create(globName));
						}

					}
				}
		);
		extend(
				CompletionType.BASIC,
				PlatformPatterns.psiElement(PerlElementTypes.PERL_PACKAGE_USER).withLanguage(PerlLanguage.INSTANCE),
				new CompletionProvider<CompletionParameters>() {
					public void addCompletions(@NotNull CompletionParameters parameters,
											   ProcessingContext context,
											   @NotNull CompletionResultSet resultSet) {

						for( String packageName: PerlPackage.BUILT_IN )
						{
							resultSet.addElement(LookupElementBuilder.create(packageName));
						}
						for( String packageName: PerlPackage.BUILT_IN_PRAGMA )
						{
							resultSet.addElement(LookupElementBuilder.create(packageName));
						}
						for( String packageName: PerlPackage.BUILT_IN_DEPRECATED )
						{
							resultSet.addElement(LookupElementBuilder.create(packageName));
						}
					}
				}
		);
	}

	@Override
	public void fillCompletionVariants(CompletionParameters parameters, CompletionResultSet result)
	{
		super.fillCompletionVariants(parameters, result);
	}
}
