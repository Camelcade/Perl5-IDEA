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

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlLexicalVariable;
import com.perl5.lang.perl.psi.PerlNamespace;
import com.perl5.lang.perl.psi.impl.*;
import com.perl5.lang.perl.util.*;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Created by hurricup on 25.04.2015.
 */
public class PerlCompletionContributor extends CompletionContributor implements PerlElementTypes
{

	// built in arrays
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

        // current file defined hashes
        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement(PerlElementTypes.PERL_ARRAY).withLanguage(PerlLanguage.INSTANCE),
                new CompletionProvider<CompletionParameters>() {
                    public void addCompletions(@NotNull final CompletionParameters parameters,
                                               ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {

                        resultSet = resultSet.withPrefixMatcher("@"+resultSet.getPrefixMatcher().getPrefix());

                        final CompletionResultSet finalResultSet = resultSet;

                        ApplicationManager.getApplication().runReadAction(new Runnable() {
                            @Override
                            public void run() {
                                PsiFile file = parameters.getOriginalFile();
                                PsiElement position = parameters.getOriginalPosition();
                                assert position != null;
                                String currentText = parameters.getOriginalPosition().getText();

                                Collection<PerlVariableDeclarationGlobalImpl> globalDeclarations = PsiTreeUtil.findChildrenOfType(file, PerlVariableDeclarationGlobalImpl.class);

                                for( PerlVariableDeclarationGlobalImpl decl : globalDeclarations)
                                {
                                    for( PerlLexicalVariable variable: decl.getLexicalVariableList())
                                    {
                                        IElementType variableType = variable.getFirstChild().getNode().getElementType();
                                        if( variableType == PERL_ARRAY )
                                            finalResultSet.addElement(LookupElementBuilder.create(variable.getText()));
                                        else if( variableType == PERL_HASH) // hash slice
                                            finalResultSet.addElement(LookupElementBuilder.create("@"+variable.getText().substring(1)+"{}"));
                                    }
                                }

                                Collection<PerlVariableDeclarationLexicalImpl> lexicalDeclarations = PsiTreeUtil.findChildrenOfType(file, PerlVariableDeclarationLexicalImpl.class);

                                for( PerlVariableDeclarationLexicalImpl decl : lexicalDeclarations)
                                {
                                    for( PerlLexicalVariable variable: decl.getLexicalVariableList())
                                    {
                                        IElementType variableType = variable.getFirstChild().getNode().getElementType();
                                        if( variableType == PERL_ARRAY )
                                            finalResultSet.addElement(LookupElementBuilder.create(variable.getText()));
                                        else if( variableType == PERL_HASH) // hash slice
                                            finalResultSet.addElement(LookupElementBuilder.create("@"+variable.getText().substring(1)+"{}"));
                                    }
                                }

                                // it's dereference
                                if( currentText.contains("$"))
                                {
                                    for( PerlVariableDeclarationGlobalImpl decl : globalDeclarations)
                                    {
                                        for( PerlLexicalVariable variable: decl.getLexicalVariableList())
                                        {
                                            IElementType variableType = variable.getFirstChild().getNode().getElementType();
                                            if( variableType == PERL_SCALAR )
                                                finalResultSet.addElement(LookupElementBuilder.create("@" + variable.getText()));
                                        }
                                    }

                                    for( PerlVariableDeclarationLexicalImpl decl : lexicalDeclarations)
                                    {
                                        for( PerlLexicalVariable variable: decl.getLexicalVariableList())
                                        {
                                            IElementType variableType = variable.getFirstChild().getNode().getElementType();
                                            if( variableType == PERL_SCALAR )
                                                finalResultSet.addElement(LookupElementBuilder.create("@" + variable.getText()));
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
        );


        // built in scalars
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

        // current file defined scalars
        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement(PerlElementTypes.PERL_SCALAR).withLanguage(PerlLanguage.INSTANCE),
                new CompletionProvider<CompletionParameters>() {
                    public void addCompletions(@NotNull final CompletionParameters parameters,
                                               ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {

                        final CompletionResultSet finalResultSet = resultSet;

                        ApplicationManager.getApplication().runReadAction(new Runnable() {
                            @Override
                            public void run() {
                                PsiFile file = parameters.getOriginalFile();
                                PsiElement position = parameters.getOriginalPosition();
                                assert position != null;
                                String currentText = parameters.getOriginalPosition().getText();

                                Collection<PerlVariableDeclarationGlobalImpl> globalDeclarations = PsiTreeUtil.findChildrenOfType(file, PerlVariableDeclarationGlobalImpl.class);

                                for( PerlVariableDeclarationGlobalImpl decl : globalDeclarations)
                                {
                                    for( PerlLexicalVariable variable: decl.getLexicalVariableList())
                                    {
                                        IElementType variableType = variable.getFirstChild().getNode().getElementType();
                                        if( variableType == PERL_SCALAR )
                                            finalResultSet.addElement(LookupElementBuilder.create(variable.getText()));
                                        else if( variableType == PERL_ARRAY) // array element
                                            finalResultSet.addElement(LookupElementBuilder.create("$"+variable.getText().substring(1)+"[]"));
                                        else if( variableType == PERL_HASH) // hash element
                                            finalResultSet.addElement(LookupElementBuilder.create("$"+variable.getText().substring(1)+"{}"));
                                    }
                                }

                                Collection<PerlVariableDeclarationLexicalImpl> lexicalDeclarations = PsiTreeUtil.findChildrenOfType(file, PerlVariableDeclarationLexicalImpl.class);

                                for( PerlVariableDeclarationLexicalImpl decl : lexicalDeclarations)
                                {
                                    for( PerlLexicalVariable variable: decl.getLexicalVariableList())
                                    {
                                        IElementType variableType = variable.getFirstChild().getNode().getElementType();
                                        if( variableType == PERL_SCALAR )
                                            finalResultSet.addElement(LookupElementBuilder.create(variable.getText()));
                                        else if( variableType == PERL_ARRAY) // array element
                                            finalResultSet.addElement(LookupElementBuilder.create("$"+variable.getText().substring(1)+"[]"));
                                        else if( variableType == PERL_HASH) // hash element
                                            finalResultSet.addElement(LookupElementBuilder.create("$"+variable.getText().substring(1)+"{}"));
                                    }
                                }
                            }
                        });
                    }
                }
        );


        // current file defined subs
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
                    }
                }
        );

        // used packages
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

								for( PerlUseStatementImpl use : PsiTreeUtil.findChildrenOfType(file, PerlUseStatementImpl.class))
								{
									PerlNamespace namespace = use.getNamespace();

									if( namespace != null)
										resultSet.addElement(LookupElementBuilder.create(namespace.getText()));
								}

								for( PerlRequireTermImpl use : PsiTreeUtil.findChildrenOfType(file, PerlRequireTermImpl.class))
								{
									PerlNamespace namespace = use.getNamespace();

									if( namespace != null)
										resultSet.addElement(LookupElementBuilder.create(namespace.getText()));
								}
                            }
                        });
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

        // built-in hashes
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
					}
				}
		);

        // current file defined hashes
        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement(PerlElementTypes.PERL_HASH).withLanguage(PerlLanguage.INSTANCE),
                new CompletionProvider<CompletionParameters>() {
                    public void addCompletions(@NotNull final CompletionParameters parameters,
                                               ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {

                        resultSet = resultSet.withPrefixMatcher("%"+resultSet.getPrefixMatcher().getPrefix());

                        final CompletionResultSet finalResultSet = resultSet;

                        ApplicationManager.getApplication().runReadAction(new Runnable() {
                            @Override
                            public void run() {
                                PsiFile file = parameters.getOriginalFile();
                                PsiElement position = parameters.getOriginalPosition();
                                assert position != null;
                                String currentText = parameters.getOriginalPosition().getText();

                                Collection<PerlVariableDeclarationGlobalImpl> globalDeclarations = PsiTreeUtil.findChildrenOfType(file, PerlVariableDeclarationGlobalImpl.class);

                                for( PerlVariableDeclarationGlobalImpl decl : globalDeclarations)
                                {
                                    for( PerlLexicalVariable variable: decl.getLexicalVariableList())
                                    {
                                        IElementType variableType = variable.getFirstChild().getNode().getElementType();
                                        if( variableType == PERL_HASH )
                                            finalResultSet.addElement(LookupElementBuilder.create(variable.getText()));
                                    }
                                }

                                Collection<PerlVariableDeclarationLexicalImpl> lexicalDeclarations = PsiTreeUtil.findChildrenOfType(file, PerlVariableDeclarationLexicalImpl.class);

                                for( PerlVariableDeclarationLexicalImpl decl : lexicalDeclarations)
                                {
                                    for( PerlLexicalVariable variable: decl.getLexicalVariableList())
                                    {
                                        IElementType variableType = variable.getFirstChild().getNode().getElementType();
                                        if( variableType == PERL_HASH )
                                            finalResultSet.addElement(LookupElementBuilder.create(variable.getText()));
                                    }
                                }

                                // it's dereference
                                if( currentText.contains("$"))
                                {
                                    for( PerlVariableDeclarationGlobalImpl decl : globalDeclarations)
                                    {
                                        for( PerlLexicalVariable variable: decl.getLexicalVariableList())
                                        {
                                            IElementType variableType = variable.getFirstChild().getNode().getElementType();
                                            if( variableType == PERL_SCALAR )
                                                finalResultSet.addElement(LookupElementBuilder.create("%" + variable.getText()));
                                        }
                                    }

                                    for( PerlVariableDeclarationLexicalImpl decl : lexicalDeclarations)
                                    {
                                        for( PerlLexicalVariable variable: decl.getLexicalVariableList())
                                        {
                                            IElementType variableType = variable.getFirstChild().getNode().getElementType();
                                            if( variableType == PERL_SCALAR )
                                                finalResultSet.addElement(LookupElementBuilder.create("%" + variable.getText()));
                                        }
                                    }
                                }
                            }
                        });
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
