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

package com.perl5.lang.perl.util;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 26.04.2016.
 */
public class PerlActionUtil {
  @Nullable
  public static PsiFile getPsiFileFromEvent(AnActionEvent event) {
    final DataContext context = event.getDataContext();
    final Project project = CommonDataKeys.PROJECT.getData(context);
    if (project == null) {
      return null;
    }

    final Editor editor = CommonDataKeys.EDITOR.getData(context);
    if (editor != null) {
      return PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
    }
    else {
      VirtualFile virtualFile = CommonDataKeys.VIRTUAL_FILE.getData(context);
      if (virtualFile != null) {
        return PsiManager.getInstance(project).findFile(virtualFile);
      }
    }
    return null;
  }
}
