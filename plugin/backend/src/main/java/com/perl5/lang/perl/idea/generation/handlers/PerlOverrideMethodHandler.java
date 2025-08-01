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

package com.perl5.lang.perl.idea.generation.handlers;

import com.intellij.lang.LanguageCodeInsightActionHandler;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.perl5.lang.perl.extensions.PerlCodeGenerator;
import com.perl5.lang.perl.psi.PerlFile;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import org.jetbrains.annotations.NotNull;


public class PerlOverrideMethodHandler extends GeneratePerlClassMemberHandlerBase implements LanguageCodeInsightActionHandler {
  @Override
  protected void generateAfterElement(PsiElement anchor, Editor editor, PsiFile file) {
    PerlCodeGenerator.getInstance(((PerlFile)file)).generateOverrideMethod(anchor, editor);
  }

  @Override
  public boolean isValidFor(Editor editor, PsiFile file) {
    return file instanceof PerlFileImpl;
  }

  @Override
  public void invoke(final @NotNull Project project, final @NotNull Editor editor, final @NotNull PsiFile file) {
    PerlOverrideMethodHandler.super.invoke(project, editor, file);
  }
}
