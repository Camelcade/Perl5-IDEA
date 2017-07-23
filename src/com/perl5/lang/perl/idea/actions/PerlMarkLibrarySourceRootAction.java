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

package com.perl5.lang.perl.idea.actions;

import com.intellij.ide.projectView.actions.MarkSourceRootAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.impl.PerlModuleExtension;
import com.intellij.openapi.roots.ContentFolder;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.idea.modules.JpsPerlLibrarySourceRootType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 29.08.2015.
 */
public class PerlMarkLibrarySourceRootAction extends MarkSourceRootAction {
  public PerlMarkLibrarySourceRootAction() {
    super(JpsPerlLibrarySourceRootType.INSTANCE);
  }

  @Override
  protected final boolean isEnabled(@NotNull RootsSelection selection, @NotNull Module module) {
    List<VirtualFile> rootsInQuestion = new ArrayList<>();
    rootsInQuestion.addAll(selection.mySelectedDirectories);
    rootsInQuestion.addAll(ContainerUtil.map(selection.mySelectedRoots, ContentFolder::getFile));

    return isEnabled(rootsInQuestion, module);
  }

  protected boolean isEnabled(@NotNull List<VirtualFile> files, @NotNull Module module) {
    if (files.isEmpty()) {
      return false;
    }
    return !JpsPerlLibrarySourceRootType.INSTANCE.equals(PerlModuleExtension.getInstance(module).getRootType(files.get(0)));
  }

  @Override
  protected void modifyRoots(@NotNull AnActionEvent e, @NotNull Module module, @NotNull VirtualFile[] files) {
    PerlModuleExtension modifiableModel = (PerlModuleExtension)PerlModuleExtension.getInstance(module).getModifiableModel(true);
    for (VirtualFile virtualFile : files) {
      modifiableModel.setRoot(virtualFile, JpsPerlLibrarySourceRootType.INSTANCE);
    }
    modifiableModel.commit();
  }
}
