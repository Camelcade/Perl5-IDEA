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

package com.perl5.lang.tt2.idea.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.tt2.psi.TemplateToolkitNamedBlock;
import com.perl5.lang.tt2.psi.TemplateToolkitString;
import com.perl5.lang.tt2.psi.mixins.TemplateToolkitStringMixin;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 16.06.2016.
 */
public class TemplateToolkitBlocksCompletionProvider extends CompletionProvider<CompletionParameters> {
  @Override
  protected void addCompletions(@NotNull CompletionParameters parameters,
                                ProcessingContext context,
                                @NotNull final CompletionResultSet result) {
    PsiElement element = parameters.getOriginalPosition();

    if (element == null) {
      return;
    }

    PsiElement stringElement = element.getParent();
    if (!(stringElement instanceof TemplateToolkitString)) {
      return;
    }

    PsiElement container = stringElement.getParent();
    if (!TemplateToolkitStringMixin.BLOCK_NAME_TARGETED_CONTAINERS.contains(PsiUtilCore.getElementType(container))) {
      return;
    }

    PsiTreeUtil.processElements(element.getContainingFile(), element1 -> {
      if (element1 instanceof TemplateToolkitNamedBlock) {
        String blockName = ((TemplateToolkitNamedBlock)element1).getName();
        if (StringUtil.isNotEmpty(blockName)) {
          result.addElement(
            LookupElementBuilder.create(blockName)
              .withTypeText("BLOCK", true)
              .withIcon(element1.getIcon(0))
          );
        }
      }
      return true;
    });
  }
}
