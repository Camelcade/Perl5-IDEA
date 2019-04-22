/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.editor.smartkeys;

import com.intellij.codeInsight.AutoPopupController;
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public abstract class PerlTypedHandlerDelegate extends TypedHandlerDelegate {
  @NotNull
  @Override
  public Result checkAutoPopup(char charTyped, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
    int currentOffset = editor.getCaretModel().getOffset();
    if (currentOffset > 0) {
      currentOffset--;
    }
    PsiElement element = file.findElementAt(currentOffset);
    if (element != null && shouldShowPopup(charTyped, project, editor, element)) {
      AutoPopupController.getInstance(project).scheduleAutoPopup(editor);
      return Result.STOP;
    }

    return super.checkAutoPopup(charTyped, project, editor, file);
  }

  protected abstract boolean shouldShowPopup(char typedChar,
                                             @NotNull Project project,
                                             @NotNull Editor editor,
                                             @NotNull PsiElement element);
}
