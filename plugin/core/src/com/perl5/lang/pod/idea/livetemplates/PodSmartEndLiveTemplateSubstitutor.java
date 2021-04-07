/*
 * Copyright 2015-2021 Alexandr Evstigneev
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

package com.perl5.lang.pod.idea.livetemplates;

import com.intellij.codeInsight.template.TemplateSubstitutor;
import com.intellij.codeInsight.template.impl.TemplateImpl;
import com.intellij.codeInsight.template.impl.TemplateSubstitutionContext;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.pod.PodLanguage;
import com.perl5.lang.pod.parser.psi.PodSyntaxElements;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PodSmartEndLiveTemplateSubstitutor implements TemplateSubstitutor {
  private static final String POD_GROUP = "Perl5: POD";

  @Override
  public @Nullable TemplateImpl substituteTemplate(@NotNull TemplateSubstitutionContext substitutionContext,
                                                   @NotNull TemplateImpl template) {
    int caretOffset = substitutionContext.getOffset();
    if (!POD_GROUP.equals(template.getGroupName()) ||
        caretOffset > 0 &&
        PsiUtilCore.getLanguageAtOffset(substitutionContext.getPsiFile(), caretOffset - 1).isKindOf(PodLanguage.INSTANCE)) {
      return null;
    }
    TemplateImpl copy = template.copy();
    copy.setString(copy.getString() + "\n" + PodSyntaxElements.CUT_COMMAND + "\n");
    return copy;
  }
}
