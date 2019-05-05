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

package com.perl5.lang.perl.idea.intentions;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import com.perl5.PerlBundle;
import org.jetbrains.annotations.NotNull;


public class StringToHeredocIntention extends StringToLastHeredocIntention {

  @Override
  public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement element) throws IncorrectOperationException {
    ApplicationManager.getApplication().invokeLater(() -> {
      String markerText = Messages.showInputDialog(
        project,
        PerlBundle.message("perl.intention.heredoc.dialog.prompt"),
        PerlBundle.message("perl.intention.heredoc.dialog.title"),
        Messages.getQuestionIcon(),
        HEREDOC_MARKER,
        null);
      if (markerText != null) {
        if (markerText.isEmpty()) {
          Messages.showErrorDialog(
            project,
            PerlBundle.message("perl.intention.heredoc.error.message"),
            PerlBundle.message("perl.intention.heredoc.error.title")
          );
        }
        else    // converting
        {
          HEREDOC_MARKER = markerText;
          WriteCommandAction.runWriteCommandAction(project, () -> super.invoke(project, editor, element));
        }
      }
    });
  }

  @NotNull
  @Override
  public String getText() {
    return PerlBundle.message("perl.intention.heredoc.title");
  }
}
