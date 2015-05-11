package com.perl5.lang.perl.idea;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.completion.impl.CamelHumpMatcher;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.util.*;
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
				PlatformPatterns.psiElement(PerlElementTypes.ARRAY_VARIABLE).withLanguage(PerlLanguage.INSTANCE),
				new CompletionProvider<CompletionParameters>() {
					public void addCompletions(@NotNull CompletionParameters parameters,
											   ProcessingContext context,
											   @NotNull CompletionResultSet resultSet) {

						resultSet = resultSet.withPrefixMatcher("@"+resultSet.getPrefixMatcher().getPrefix());
						for( String arrayName: PerlArrayUtil.BUILT_IN )
						{
							resultSet.addElement(LookupElementBuilder.create(arrayName));
						}
						resultSet.withPrefixMatcher("@");

					}
				}
		);
		//CamelHumpMatcher
		extend(
				CompletionType.BASIC,
				PlatformPatterns.psiElement(PerlElementTypes.SCALAR_VARIABLE).withLanguage(PerlLanguage.INSTANCE),
				new CompletionProvider<CompletionParameters>() {
					public void addCompletions(@NotNull CompletionParameters parameters,
											   ProcessingContext context,
											   @NotNull CompletionResultSet resultSet) {

						for( String scalarName: PerlScalarUtil.BUILT_IN )
						{
							resultSet.addElement(LookupElementBuilder.create(scalarName));
						}

					}
				}
		);
		extend(
				CompletionType.BASIC,
				PlatformPatterns.psiElement(PerlElementTypes.PERL_FUNCTION).withLanguage(PerlLanguage.INSTANCE),
				new CompletionProvider<CompletionParameters>() {
					public void addCompletions(@NotNull CompletionParameters parameters,
											   ProcessingContext context,
											   @NotNull CompletionResultSet resultSet) {

						for( String functionName: PerlFunctionUtil.BUILT_IN )
						{
							resultSet.addElement(LookupElementBuilder.create(functionName));
						}

						// append prevoiusly defined functions;
						// @todo we should check all included files for the current package and check functions in there
/*
						PsiElement currentPosition = parameters.getPosition();

						while( currentPosition != null )
						{
							if( currentPosition instanceof PerlFunctionDefinitionNamedImpl)
							{
								PsiElement functionName = ((PerlFunctionDefinitionNamedImpl)currentPosition).getFunction();

								resultSet.addElement(LookupElementBuilder.create(functionName.getText()));
							}


							PsiElement prevPosition = currentPosition.getPrevSibling();
							if( prevPosition == null )
							{
								prevPosition = currentPosition.getParent();
							}
							currentPosition = prevPosition;
						}
*/

					}
				}
		);
		extend(
				CompletionType.BASIC,
				PlatformPatterns.psiElement(PerlElementTypes.HASH_VARIABLE).withLanguage(PerlLanguage.INSTANCE),
				new CompletionProvider<CompletionParameters>() {
					public void addCompletions(@NotNull CompletionParameters parameters,
											   ProcessingContext context,
											   @NotNull CompletionResultSet resultSet) {

						resultSet = resultSet.withPrefixMatcher("%"+resultSet.getPrefixMatcher().getPrefix());
						for( String hashName: PerlHashUtil.BUILT_IN )
						{
							resultSet.addElement(LookupElementBuilder.create(hashName));
						}
						resultSet.withPrefixMatcher("%");
					}
				}
		);
/*
		extend(
				CompletionType.BASIC,
				PlatformPatterns.psiElement(PerlElementTypes.PERL_PACKAGE).withLanguage(PerlLanguage.INSTANCE),
				new CompletionProvider<CompletionParameters>() {
					public void addCompletions(@NotNull CompletionParameters parameters,
											   ProcessingContext context,
											   @NotNull CompletionResultSet resultSet) {

						for( String packageName: PerlPackageUtil.BUILT_IN )
						{
							resultSet.addElement(LookupElementBuilder.create(packageName));
						}
						for( String packageName: PerlPackageUtil.BUILT_IN_PRAGMA )
						{
							resultSet.addElement(LookupElementBuilder.create(packageName));
						}
						for( String packageName: PerlPackageUtil.BUILT_IN_DEPRECATED )
						{
							resultSet.addElement(LookupElementBuilder.create(packageName));
						}
					}
				}
		);
*/
	}

	@Override
	public void fillCompletionVariants(CompletionParameters parameters, CompletionResultSet result)
	{
		super.fillCompletionVariants(parameters, result);
	}
}
