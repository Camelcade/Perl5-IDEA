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

import com.intellij.ide.projectView.actions.MarkRootActionBase;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ContentFolder;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class PerlSourceRootAction extends MarkRootActionBase {
  private static final Logger LOG = Logger.getInstance(PerlSourceRootAction.class);

  @Override
  protected final boolean isEnabled(@NotNull RootsSelection selection, @NotNull Module module) {
    return isEnabled(getFilesFromSelection(selection), module);
  }

  @NotNull
  protected final List<VirtualFile> getFilesFromSelection(@NotNull RootsSelection selection) {
    List<VirtualFile> roots = new ArrayList<>();
    roots.addAll(selection.mySelectedDirectories);
    roots.addAll(ContainerUtil.map(selection.mySelectedRoots, ContentFolder::getFile));
    return roots;
  }

  protected abstract boolean isEnabled(@NotNull List<VirtualFile> files, @NotNull Module module);

  @Override
  protected void modifyRoots(VirtualFile file, ContentEntry entry) {
    throw new IncorrectOperationException();
  }

}
