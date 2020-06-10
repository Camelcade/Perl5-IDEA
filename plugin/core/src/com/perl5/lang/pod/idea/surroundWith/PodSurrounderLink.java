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

package com.perl5.lang.pod.idea.surroundWith;

import com.intellij.codeInsight.AutoPopupController;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import com.perl5.PerlBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class PodSurrounderLink extends PodSurrounder {
  @Override
  protected char getFormatterLetter() {
    return 'L';
  }

  @Override
  protected @NotNull String adjustContent(@NotNull String originalContent) {
    return super.adjustContent(originalContent) + "|";
  }

  @Override
  public @Nullable TextRange surroundElements(@NotNull Project project, @NotNull Editor editor, @NotNull PsiElement[] elements)
    throws IncorrectOperationException {
    TextRange range = super.surroundElements(project, editor, elements);
    AutoPopupController.getInstance(project).scheduleAutoPopup(editor);
    return range;
  }

  @Override
  protected @NotNull String getFormatterDescription() {
    return PerlBundle.message("pod.intention.wrap.description.l");
  }
}
