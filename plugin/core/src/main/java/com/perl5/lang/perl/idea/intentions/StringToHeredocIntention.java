/*
 * Copyright 2015-2022 Alexandr Evstigneev
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

import com.intellij.codeInsight.intention.FileModifier;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.TestDialog;
import com.intellij.openapi.ui.TestDialogManager;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import com.perl5.PerlBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;


public class StringToHeredocIntention extends StringToLastHeredocIntention {

  @Override
  public @Nullable FileModifier getFileModifierForPreview(@NotNull PsiFile target) {
    return new StringToHeredocIntention() {
      @Override
      public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement element) throws IncorrectOperationException {
        doInvoke(project, editor, element);
      }
    };
  }

  @Override
  public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement element) throws IncorrectOperationException {
    ApplicationManager.getApplication().invokeLater(() -> doInvoke(project, editor, element));
  }

  private void doInvoke(@NotNull Project project, Editor editor, @NotNull PsiElement element) {
    String markerText = Messages.showInputDialog(
      project,
      PerlBundle.message("perl.intention.heredoc.dialog.prompt"),
      PerlBundle.message("perl.intention.heredoc.dialog.title"),
      Messages.getQuestionIcon(),
      ourMarker,
      null);
    if (StringUtil.isEmpty(markerText)) {
      Messages.showErrorDialog(
        project,
        PerlBundle.message("perl.intention.heredoc.error.message"),
        PerlBundle.message("perl.intention.heredoc.error.title")
      );
      return;
    }
    ourMarker = markerText;
    if (ApplicationManager.getApplication().isWriteThread()) {
      WriteCommandAction.runWriteCommandAction(project, () -> super.invoke(project, editor, element));
    }
    else {
      super.invoke(project, editor, element);
    }
  }

  @Override
  public @NotNull String getText() {
    return PerlBundle.message("perl.intention.heredoc.title");
  }

  @TestOnly
  public static void doWithMarker(@NlsSafe @NotNull String marker, @NotNull Runnable runnable) {
    var currentMarker = ourMarker;
    TestDialogManager.setTestInputDialog(message -> marker);
    TestDialogManager.setTestDialog(TestDialog.YES);
    try {
      runnable.run();
    }
    finally {
      ourMarker = currentMarker;
    }
  }
}
