package com.perl5.lang.perl.idea;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlSubDeclaration;
import com.perl5.lang.perl.psi.PerlSubDefinition;
import com.perl5.lang.perl.psi.impl.PerlSubDeclarationImpl;
import com.perl5.lang.perl.psi.impl.PerlSubDefinitionImpl;
import com.perl5.lang.perl.util.*;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Created by hurricup on 25.04.2015.
 */
public class PerlCompletionContributor extends CompletionContributor implements PerlElementTypes
{

	// @todo implement some tree running for defined methods
	public PerlCompletionContributor() {
        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement(PerlElementTypes.PERL_ARRAY).withLanguage(PerlLanguage.INSTANCE),
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
				PlatformPatterns.psiElement(PerlElementTypes.PERL_SCALAR).withLanguage(PerlLanguage.INSTANCE),
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

        // current file
        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement(PerlElementTypes.PERL_FUNCTION).withLanguage(PerlLanguage.INSTANCE),
                new CompletionProvider<CompletionParameters>() {
                    public void addCompletions(@NotNull final CompletionParameters parameters,
                                               ProcessingContext context,
                                               @NotNull final CompletionResultSet resultSet) {

                        ApplicationManager.getApplication().runReadAction(new Runnable() {
                            @Override
                            public void run() {
                                PsiFile file = parameters.getOriginalFile();

                                for( PerlSubDeclarationImpl sub : PsiTreeUtil.findChildrenOfType(file, PerlSubDeclarationImpl.class))
                                {
                                    resultSet.addElement(LookupElementBuilder.create(sub.getMethod().getText()));
                                }

                                for( PerlSubDefinitionImpl sub : PsiTreeUtil.findChildrenOfType(file, PerlSubDefinitionImpl.class))
                                {
                                    resultSet.addElement(LookupElementBuilder.create(sub.getMethod().getText()));
                                }
                            }
                        });

                        // append prevoiusly defined functions;
                        // @todo we should check all included files for the current package and check functions in there

                    }
                }
        );

        // built in
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
                    }
                }
        );
		extend(
				CompletionType.BASIC,
				PlatformPatterns.psiElement(PerlElementTypes.PERL_HASH).withLanguage(PerlLanguage.INSTANCE),
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
