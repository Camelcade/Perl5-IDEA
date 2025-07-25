/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.formatter;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.codeStyle.PreFormatProcessor;
import com.perl5.lang.perl.PerlLanguage;
import org.jetbrains.annotations.NotNull;


public class PerlPreFormatProcessor implements PreFormatProcessor {
  @Override
  public @NotNull TextRange process(@NotNull ASTNode element, @NotNull TextRange range) {
    PsiElement psiElement = element.getPsi();
    if (psiElement == null) {
      return range;
    }

    if (!canProcess(psiElement, range)) {
      return range;
    }

    PsiFile file = psiElement.isValid() ? psiElement.getContainingFile() : null;
    if (file == null) {
      return range;
    }

    Project project = psiElement.getProject();

    return new PerlPreFormatter(project).process(psiElement, range);
  }

  protected boolean canProcess(PsiElement element, TextRange range) {
    return element.getLanguage().is(PerlLanguage.INSTANCE);
  }
}
