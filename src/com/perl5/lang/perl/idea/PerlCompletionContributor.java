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
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.*;
import com.perl5.lang.perl.util.*;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

/**
 * Created by hurricup on 25.04.2015.
 *
 */
public class PerlCompletionContributor extends CompletionContributor implements PerlElementTypes
{

    private void fillScalarCompletions(@NotNull CompletionParameters parameters,
                                       ProcessingContext context,
                                       @NotNull CompletionResultSet resultSet)
    {

        for( String name: PerlScalarUtil.BUILT_IN )
        {
            resultSet.addElement(LookupElementBuilder.create(name.substring(1)));
        }
        for( String name: PerlArrayUtil.BUILT_IN )
        {
            resultSet.addElement(LookupElementBuilder.create(name.substring(1) + "[]"));
        }
        for( String name: PerlHashUtil.BUILT_IN )
        {
            resultSet.addElement(LookupElementBuilder.create(name.substring(1) + "{}"));
        }
    }
    private void fillArrayCompletions(@NotNull CompletionParameters parameters,
                                       ProcessingContext context,
                                       @NotNull CompletionResultSet resultSet)
    {
        // built in arrays
        for( String name: PerlArrayUtil.BUILT_IN )
        {
            resultSet.addElement(LookupElementBuilder.create(name.substring(1)));
        }
        for( String name: PerlHashUtil.BUILT_IN )
        {
            resultSet.addElement(LookupElementBuilder.create(name.substring(1) + "{}"));
        }

    }
    private void fillHashCompletions(@NotNull CompletionParameters parameters,
                                       ProcessingContext context,
                                       @NotNull CompletionResultSet resultSet)
    {
        // built-in hashes
        for( String name: PerlHashUtil.BUILT_IN )
        {
            resultSet.addElement(LookupElementBuilder.create(name.substring(1)));
        }

    }
    private void fillGlobCompletions(@NotNull CompletionParameters parameters,
                                       ProcessingContext context,
                                       @NotNull CompletionResultSet resultSet)
    {
        // built-in globs
        for( String name: PerlGlobUtil.BUILT_IN )
        {
            resultSet.addElement(LookupElementBuilder.create(name.substring(1)));
        }
    }


	public PerlCompletionContributor()
    {

        // router built-in
        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement(PerlElementTypes.PERL_VARIABLE_NAME).withLanguage(PerlLanguage.INSTANCE),
                new CompletionProvider<CompletionParameters>()
                {
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet)
                    {

                        PsiElement position = parameters.getOriginalPosition();
                        assert position != null;
                        PsiElement parent = position.getParent();

                        if (parent instanceof PerlPerlScalarImpl)
                            fillScalarCompletions(parameters, context, resultSet);
                        else if (parent instanceof PerlPerlArrayImpl)
                            fillArrayCompletions(parameters, context, resultSet);
                        else if (parent instanceof PerlPerlHashImpl)
                            fillHashCompletions(parameters, context, resultSet);
                        else if (parent instanceof PerlPerlGlobImpl)
                            fillGlobCompletions(parameters, context, resultSet);
                    }
                }
        );

        // router defined
        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement(PerlElementTypes.PERL_VARIABLE_NAME).withLanguage(PerlLanguage.INSTANCE),
                new CompletionProvider<CompletionParameters>()
                {
                    public void addCompletions(@NotNull final CompletionParameters parameters,
                                               ProcessingContext context,
                                               @NotNull final CompletionResultSet resultSet)
                    {

                        final PsiFile file = parameters.getOriginalFile();
                        final PsiElement position = parameters.getOriginalPosition();
                        assert position != null;
                        final PsiElement parent = position.getParent();

                        if (parent instanceof PerlPerlScalarImpl)
                            ApplicationManager.getApplication().runReadAction(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    String currentText = position.getText();

                                    Collection<PerlVariableDeclarationGlobalImpl> globalDeclarations = PsiTreeUtil.findChildrenOfType(file, PerlVariableDeclarationGlobalImpl.class);

                                    for (PerlVariableDeclarationGlobalImpl decl : globalDeclarations)
                                    {
                                        for (PerlPerlScalar variable : decl.getPerlScalarList())
                                        {
                                            assert variable instanceof PerlPerlScalarImpl;
                                            String variableName = ((PerlPerlScalarImpl) variable).getName();
                                            if (variableName != null)
                                                resultSet.addElement(LookupElementBuilder.create(variableName));
                                        }
                                        for (PerlPerlArray variable : decl.getPerlArrayList())
                                        {
                                            assert variable instanceof PerlPerlArrayImpl;
                                            String variableName = ((PerlPerlArrayImpl) variable).getName();
                                            if (variableName != null)
                                                resultSet.addElement(LookupElementBuilder.create(variableName + "[]"));
                                        }
                                        for (PerlPerlHash variable : decl.getPerlHashList())
                                        {
                                            assert variable instanceof PerlPerlHashImpl;
                                            String variableName = ((PerlPerlHashImpl) variable).getName();
                                            if (variableName != null)
                                                resultSet.addElement(LookupElementBuilder.create(variableName + "{}"));
                                        }
                                    }

                                    Collection<PerlVariableDeclarationLexicalImpl> lexicalDeclarations = PsiTreeUtil.findChildrenOfType(file, PerlVariableDeclarationLexicalImpl.class);

                                    for (PerlVariableDeclarationLexicalImpl decl : lexicalDeclarations)
                                    {
                                        for (PerlPerlScalar variable : decl.getPerlScalarList())
                                        {
                                            assert variable instanceof PerlPerlScalarImpl;
                                            String variableName = ((PerlPerlScalarImpl) variable).getName();
                                            if (variableName != null)
                                                resultSet.addElement(LookupElementBuilder.create(variableName));
                                        }
                                        for (PerlPerlArray variable : decl.getPerlArrayList())
                                        {
                                            assert variable instanceof PerlPerlArrayImpl;
                                            String variableName = ((PerlPerlArrayImpl) variable).getName();
                                            if (variableName != null)
                                                resultSet.addElement(LookupElementBuilder.create(variableName + "[]"));
                                        }
                                        for (PerlPerlHash variable : decl.getPerlHashList())
                                        {
                                            assert variable instanceof PerlPerlHashImpl;
                                            String variableName = ((PerlPerlHashImpl) variable).getName();
                                            if (variableName != null)
                                                resultSet.addElement(LookupElementBuilder.create(variableName + "{}"));
                                        }
                                    }
                                }
                            });
                        else if (parent instanceof PerlPerlArrayImpl)
                            ApplicationManager.getApplication().runReadAction(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    String currentText = position.getText();

                                    Collection<PerlVariableDeclarationGlobalImpl> globalDeclarations = PsiTreeUtil.findChildrenOfType(file, PerlVariableDeclarationGlobalImpl.class);

                                    for (PerlVariableDeclarationGlobalImpl decl : globalDeclarations)
                                    {
                                        for (PerlPerlArray variable : decl.getPerlArrayList())
                                        {
                                            assert variable instanceof PerlPerlArrayImpl;
                                            String variableName = ((PerlPerlArrayImpl) variable).getName();
                                            if (variableName != null)
                                                resultSet.addElement(LookupElementBuilder.create(variableName));
                                        }
                                        for (PerlPerlHash variable : decl.getPerlHashList())
                                        {
                                            assert variable instanceof PerlPerlHashImpl;
                                            String variableName = ((PerlPerlHashImpl) variable).getName();
                                            if (variableName != null)
                                                resultSet.addElement(LookupElementBuilder.create(variableName + "{}"));
                                        }
                                    }

                                    Collection<PerlVariableDeclarationLexicalImpl> lexicalDeclarations = PsiTreeUtil.findChildrenOfType(file, PerlVariableDeclarationLexicalImpl.class);

                                    for (PerlVariableDeclarationLexicalImpl decl : lexicalDeclarations)
                                    {
                                        for (PerlPerlArray variable : decl.getPerlArrayList())
                                        {
                                            assert variable instanceof PerlPerlArrayImpl;
                                            String variableName = ((PerlPerlArrayImpl) variable).getName();
                                            if (variableName != null)
                                                resultSet.addElement(LookupElementBuilder.create(variableName));
                                        }
                                        for (PerlPerlHash variable : decl.getPerlHashList())
                                        {
                                            assert variable instanceof PerlPerlHashImpl;
                                            String variableName = ((PerlPerlHashImpl) variable).getName();
                                            if (variableName != null)
                                                resultSet.addElement(LookupElementBuilder.create(variableName + "{}"));
                                        }
                                    }

                                    // it's dereference
                                    if (parent.getText().contains("$"))
                                    {
                                        populateScalars(globalDeclarations, lexicalDeclarations, resultSet);
                                    }
                                }
                            });
                        else if (parent instanceof PerlPerlHashImpl)
                            ApplicationManager.getApplication().runReadAction(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    String currentText = position.getText();

                                    Collection<PerlVariableDeclarationGlobalImpl> globalDeclarations = PsiTreeUtil.findChildrenOfType(file, PerlVariableDeclarationGlobalImpl.class);

                                    for (PerlVariableDeclarationGlobalImpl decl : globalDeclarations)
                                    {
                                        for (PerlPerlHash variable : decl.getPerlHashList())
                                        {
                                            assert variable instanceof PerlPerlHashImpl;
                                            String variableName = ((PerlPerlHashImpl) variable).getName();
                                            if (variableName != null)
                                                resultSet.addElement(LookupElementBuilder.create(variableName + "{}"));
                                        }
                                    }

                                    Collection<PerlVariableDeclarationLexicalImpl> lexicalDeclarations = PsiTreeUtil.findChildrenOfType(file, PerlVariableDeclarationLexicalImpl.class);

                                    for (PerlVariableDeclarationLexicalImpl decl : lexicalDeclarations)
                                    {
                                        for (PerlPerlHash variable : decl.getPerlHashList())
                                        {
                                            assert variable instanceof PerlPerlHashImpl;
                                            String variableName = ((PerlPerlHashImpl) variable).getName();
                                            if (variableName != null)
                                                resultSet.addElement(LookupElementBuilder.create(variableName + "{}"));
                                        }
                                    }

                                    // it's dereference
                                    if (parent.getText().contains("$"))
                                    {
                                        populateScalars(globalDeclarations, lexicalDeclarations, resultSet);
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
                new CompletionProvider<CompletionParameters>()
                {
                    public void addCompletions(@NotNull final CompletionParameters parameters,
                                               ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet)
                    {

                        final String prefix = resultSet.getPrefixMatcher().getPrefix();
                        final CompletionResultSet finalResultSet = resultSet;// = resultSet.withPrefixMatcher("");
                        String packageName = null;

                        PsiElement parent = parameters.getPosition().getParent();
                        if( parent != null && parent instanceof PerlElementInContext )
                            packageName = ((PerlElementInContext) parent).getPackageName();

                        final String finalPackageName = packageName == null ? null: packageName + "::";

                        ApplicationManager.getApplication().runReadAction(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                List<String> definedSubs = PerlFunctionUtil.getDefinedSubsNames(parameters.getPosition().getProject());

                                for (String subname : definedSubs )
                                {
                                    if( finalPackageName != null && subname.startsWith(finalPackageName) )
                                        subname = subname.substring(finalPackageName.length());
                                    finalResultSet.addElement(LookupElementBuilder.create(subname));

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
                new CompletionProvider<CompletionParameters>()
                {
                    public void addCompletions(@NotNull final CompletionParameters parameters,
                                               ProcessingContext context,
                                               @NotNull final CompletionResultSet resultSet)
                    {

                        ApplicationManager.getApplication().runReadAction(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                PsiFile file = parameters.getOriginalFile();

                                for (PerlUseStatementImpl use : PsiTreeUtil.findChildrenOfType(file, PerlUseStatementImpl.class))
                                {
                                    PerlNamespace namespace = use.getNamespace();

                                    if (namespace != null)
                                        resultSet.addElement(LookupElementBuilder.create(namespace.getText()));
                                }

                                for (PerlRequireTermImpl use : PsiTreeUtil.findChildrenOfType(file, PerlRequireTermImpl.class))
                                {
                                    PerlNamespace namespace = use.getNamespace();

                                    if (namespace != null)
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
                new CompletionProvider<CompletionParameters>()
                {
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet)
                    {

                        for (String functionName : PerlFunctionUtil.BUILT_IN)
                        {
                            resultSet.addElement(LookupElementBuilder.create(functionName));
                        }
                    }
                }
        );
    }


    private void populateScalars(
                @NotNull Collection<PerlVariableDeclarationGlobalImpl> globalDeclarations,
                @NotNull Collection<PerlVariableDeclarationLexicalImpl> lexicalDeclarations,
                @NotNull CompletionResultSet resultSet
    )
    {
        for (PerlVariableDeclarationGlobalImpl decl : globalDeclarations)
        {
            for (PerlPerlScalar variable : decl.getPerlScalarList())
            {
                assert variable instanceof PerlPerlScalarImpl;
                String variableName = ((PerlPerlScalarImpl) variable).getName();
                if( variableName != null)
                    resultSet.addElement(LookupElementBuilder.create(variableName));
            }
        }

        for (PerlVariableDeclarationLexicalImpl decl : lexicalDeclarations)
        {
            for (PerlPerlScalar variable : decl.getPerlScalarList())
            {
                assert variable instanceof PerlPerlScalarImpl;
                String variableName = ((PerlPerlScalarImpl) variable).getName();
                if( variableName != null)
                    resultSet.addElement(LookupElementBuilder.create(variableName));
            }
        }
    }


	@Override
	public void fillCompletionVariants(CompletionParameters parameters, CompletionResultSet result)
	{
		super.fillCompletionVariants(parameters, result);
	}
}
