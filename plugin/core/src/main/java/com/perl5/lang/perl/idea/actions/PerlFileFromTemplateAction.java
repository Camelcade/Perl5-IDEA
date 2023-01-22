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

import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.fileTypes.ex.FileTypeManagerEx;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.perl5.PerlBundle;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.filetemplates.PerlCreateFileFromTemplateHandler;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class PerlFileFromTemplateAction extends CreateFileFromTemplateAction implements DumbAware {

  public PerlFileFromTemplateAction() {
    super(PerlBundle.message("action.perl5.new.file.text"), PerlBundle.message("action.perl5.new.file.description"),
          PerlIcons.PERL_LANGUAGE_ICON);
  }

  @Override
  protected void buildDialog(@NotNull Project project, @NotNull PsiDirectory directory, CreateFileFromTemplateDialog.Builder builder) {
    builder
      .setTitle(PerlBundle.message("dialog.title.new.perl5.file"))
      .addKind(PerlBundle.message("list.item.package"), PerlIcons.PM_FILE, "Perl5 package")
      .addKind(PerlBundle.message("list.item.script"), PerlIcons.PERL_SCRIPT_FILE_ICON, "Perl5 script")
      .addKind(PerlBundle.message("list.item.test"), PerlIcons.TEST_FILE, "Perl5 test")
      .addKind(PerlBundle.message("list.item.pod.file"), PerlIcons.POD_FILE, "Perl5 pod")
    ;

    FileTypeManagerEx fileTypeManager = FileTypeManagerEx.getInstanceEx();
    for (FileTemplate fileTemplate : FileTemplateManager.getInstance(project).getAllTemplates()) {
      if (PerlCreateFileFromTemplateHandler.INSTANCE.handlesTemplate(fileTemplate)) {
        builder.addKind(fileTemplate.getName(), fileTypeManager.getFileTypeByExtension(fileTemplate.getExtension()).getIcon(),
                        fileTemplate.getName());
      }
    }
  }

  @Override
  protected boolean isAvailable(DataContext dataContext) {
    return PerlProjectManager.isPerlEnabled(dataContext) && super.isAvailable(dataContext);
  }

  @Override
  protected String getActionName(PsiDirectory directory, @NotNull String newName, String templateName) {
    return PerlBundle.message("command.name.create.perl5.file", newName);
  }

  @Override
  protected PsiFile createFileFromTemplate(String name, FileTemplate template, PsiDirectory dir) {
    final List<String> pathChunks = new ArrayList<>();

    if (StringUtil.contains(name, PerlPackageUtil.NAMESPACE_SEPARATOR)) {
      pathChunks.addAll(StringUtil.split(name, PerlPackageUtil.NAMESPACE_SEPARATOR));
    }
    else if (StringUtil.contains(name, "/")) {
      pathChunks.addAll(StringUtil.split(name, "/"));
    }
    else {
      pathChunks.add(name);
    }
    name = pathChunks.remove(pathChunks.size() - 1);

    for (String pathChunk : pathChunks) {
      if (StringUtil.isNotEmpty(pathChunk)) {
        final PsiDirectory sub = dir.findSubdirectory(pathChunk);
        dir = sub == null ? dir.createSubdirectory(pathChunk) : sub;
      }
    }
    return super.createFileFromTemplate(name, template, dir);
  }
}
