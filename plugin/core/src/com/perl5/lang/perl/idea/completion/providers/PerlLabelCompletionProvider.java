/*
 * Copyright 2015-2020 Alexandr Evstigneev
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
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.idea.completion.providers.processors.PerlCompletionProvider;
import com.perl5.lang.perl.idea.completion.providers.processors.PerlSimpleCompletionProcessor;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.idea.PerlElementPatterns.*;


public class PerlLabelCompletionProvider extends PerlCompletionProvider {
  @Override
  protected void addCompletions(@NotNull CompletionParameters parameters,
                                @NotNull ProcessingContext context,
                                final @NotNull CompletionResultSet result) {
    final PsiElement element = parameters.getOriginalPosition();
    if (element == null) {
      return;
    }
    PerlSimpleCompletionProcessor completionProcessor = new PerlSimpleCompletionProcessor(result, element);
    if (LABEL_DECLARATION_PATTERN.accepts(element)) {
      // unresolved labels should be here
    }
    else if (LABEL_IN_GOTO_PATTERN.accepts(element)) {
      PerlPsiUtil.processGotoLabelDeclarations(element, perlLabelDeclaration -> {
        if (perlLabelDeclaration != null) {
          String labelName = perlLabelDeclaration.getName();
          if (StringUtil.isNotEmpty(labelName) && completionProcessor.matches(labelName)) {
            return completionProcessor.process(LookupElementBuilder.create(perlLabelDeclaration, labelName));
          }
        }
        return completionProcessor.result();
      });
    }
    else if (LABEL_IN_NEXT_LAST_REDO_PATTERN.accepts(element)) {
      PerlPsiUtil.processNextRedoLastLabelDeclarations(element, perlLabelDeclaration -> {
        if (perlLabelDeclaration != null) {
          String labelName = perlLabelDeclaration.getName();
          if (StringUtil.isNotEmpty(labelName) && completionProcessor.matches(labelName)) {
            return completionProcessor.process(LookupElementBuilder.create(perlLabelDeclaration, labelName));
          }
        }
        return completionProcessor.result();
      });
    }
    completionProcessor.logStatus(getClass());
  }
}
