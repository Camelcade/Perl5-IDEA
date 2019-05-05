/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.completion.providers;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.idea.PerlElementPatterns;
import com.perl5.lang.perl.idea.completion.PerlInsertHandlers;
import com.perl5.lang.perl.idea.completion.util.PerlPackageCompletionUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 03.03.2016.
 */
public class PerlPackageCompletionProvider extends CompletionProvider<CompletionParameters> implements PerlElementPatterns {
  @Override
  protected void addCompletions(@NotNull CompletionParameters parameters,
                                @NotNull ProcessingContext context,
                                @NotNull CompletionResultSet result) {
    PsiElement element = parameters.getPosition();

    if (NAMESPACE_IN_DEFINITION_PATTERN.accepts(element)) // package Foo
    {
      PerlPackageCompletionUtil.fillWithNamespaceNameSuggestions(element, result);
    }
    else if (NAMESPACE_IN_VARIABLE_DECLARATION_PATTERN.accepts(element)) // my Foo::Bar
    {
      PerlPackageCompletionUtil.fillWithAllNamespacesNames(element, result);
    }
    else if (NAMESPACE_IN_ANNOTATION_PATTERN.accepts(element)) // #@returns / #@type
    {
      result.addElement(LookupElementBuilder.create("ArrayRef")
                          .withInsertHandler(PerlInsertHandlers.ARRAY_ELEMENT_INSERT_HANDLER)
                          .withTailText("[]"));
      result.addElement(LookupElementBuilder.create("HashRef")
                          .withInsertHandler(PerlInsertHandlers.ARRAY_ELEMENT_INSERT_HANDLER)
                          .withTailText("[]"));
      PerlPackageCompletionUtil.fillWithAllNamespacesNames(element, result);
    }
    else if (NAMESPACE_IN_USE_PATTERN.accepts(element)) // use/no/require
    {
      PerlPackageCompletionUtil.fillWithVersionNumbers(element, result);
      PerlPackageCompletionUtil.fillWithAllPackageFiles(element, result);
    }
    else // fallback
    {
      PerlPackageCompletionUtil.fillWithAllNamespacesNames(element, result);
    }
  }
}
