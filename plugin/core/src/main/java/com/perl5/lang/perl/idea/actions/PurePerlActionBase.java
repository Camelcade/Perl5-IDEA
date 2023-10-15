/*
 * Copyright 2015-2023 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.PsiFile;
import com.perl5.lang.perl.fileTypes.PurePerlFileType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Actions applicable to files with pure perl syntax
 */
public abstract class PurePerlActionBase extends PerlActionBase {

  public PurePerlActionBase(@Nls(capitalization = Nls.Capitalization.Title) @Nullable String text) {
    super(text);
  }

  @Override
  protected boolean isEnabled(@NotNull AnActionEvent event) {
    return super.isEnabled(event) && isMyFile(PerlActionBase.getPsiFile(event));
  }

  @Contract("null -> false")
  protected boolean isMyFile(@Nullable PsiFile file) {
    if (file == null || !file.isPhysical()) {
      return false;
    }
    FileType fileType = file.getFileType();
    return fileType instanceof PurePerlFileType;
  }
}
