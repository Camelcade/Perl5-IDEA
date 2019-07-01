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

package com.perl5.lang.mojolicious.idea.ide;

import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.perl5.lang.mojolicious.MojoBundle;
import com.perl5.lang.mojolicious.MojoIcons;
import com.perl5.lang.mojolicious.MojoUtil;
import com.perl5.lang.mojolicious.filetypes.MojoliciousFileType;
import org.jetbrains.annotations.NotNull;

public class MojoCreateFileFromTemplateAction extends CreateFileFromTemplateAction implements DumbAware {
  public MojoCreateFileFromTemplateAction() {
    super(MojoBundle.message("action.new.file.title"), MojoBundle.message("action.new.file.description"), MojoIcons.MOJO_FILE);
  }

  @Override
  protected void buildDialog(Project project, PsiDirectory directory, CreateFileFromTemplateDialog.Builder builder) {
    builder.setTitle(MojoBundle.message("action.new.file.builder.title"));
    builder.addKind(MojoliciousFileType.INSTANCE.getDescription(), MojoIcons.MOJO_FILE, MojoBundle.message("perl.file.kind.mojo"));
  }

  @Override
  protected String getActionName(PsiDirectory directory, @NotNull String newName, String templateName) {
    return MojoBundle.message("action.new.file.action.name", newName);
  }

  @Override
  protected boolean isAvailable(DataContext dataContext) {
    return super.isAvailable(dataContext) && MojoUtil.isMojoAvailable(dataContext);
  }

}
