/*
 * Copyright 2015-2017 Alexandr Evstigneev
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
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.idea.PerlElementPatterns;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 04.03.2016.
 */
public class PerlLabelCompletionProvider extends CompletionProvider<CompletionParameters> implements PerlElementPatterns {
  @Override
  protected void addCompletions(@NotNull CompletionParameters parameters,
                                @NotNull ProcessingContext context,
                                final @NotNull CompletionResultSet result) {
    final PsiElement element = parameters.getOriginalPosition();
    if (LABEL_DECLARATION_PATTERN.accepts(element)) {
      // unresolved labels should be here
    }
    else if (LABEL_IN_GOTO_PATTERN.accepts(element)) {
      PerlPsiUtil.processGotoLabelDeclarations(element, perlLabelDeclaration ->
      {
        if (perlLabelDeclaration != null && StringUtil.isNotEmpty(perlLabelDeclaration.getName())) {
          result.addElement(LookupElementBuilder.create(perlLabelDeclaration.getName()));
        }
        return true;
      });
    }
    else if (LABEL_IN_NEXT_LAST_REDO_PATTERN.accepts(element)) {
      PerlPsiUtil.processNextRedoLastLabelDeclarations(element, perlLabelDeclaration ->
      {
        if (perlLabelDeclaration != null && StringUtil.isNotEmpty(perlLabelDeclaration.getName())) {
          result.addElement(LookupElementBuilder.create(perlLabelDeclaration.getName()));
        }
        return true;
      });
    }
  }
}
