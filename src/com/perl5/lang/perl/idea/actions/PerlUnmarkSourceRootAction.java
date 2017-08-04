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

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.impl.PerlModuleExtension;
import com.intellij.openapi.roots.ui.configuration.ModuleSourceRootEditHandler;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.LayeredIcon;
import com.perl5.PerlBundle;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.modules.PerlSourceRootType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PerlUnmarkSourceRootAction extends PerlSourceRootAction {

  @Override
  public void update(AnActionEvent e) {
    Presentation presentation = e.getPresentation();
    RootsSelection selection = getSelection(e);
    if (selection.myModule == null) {
      presentation.setEnabledAndVisible(false);
      return;
    }
    boolean enabled = isEnabled(selection, selection.myModule);
    presentation.setEnabledAndVisible(enabled);
    if (!enabled) {
      return;
    }
    List<VirtualFile> virtualFiles = getFilesFromSelection(selection);
    PerlModuleExtension perlModuleExtension = PerlModuleExtension.getInstance(selection.myModule);
    if (perlModuleExtension == null || virtualFiles.isEmpty()) {
      return;
    }

    if (virtualFiles.size() == 1) {
      PerlSourceRootType type = perlModuleExtension.getRootType(virtualFiles.get(0));
      if (type == null) {
        return;
      }

      ModuleSourceRootEditHandler<?> handler = type.getEditHandler();
      presentation.setText(handler.getUnmarkRootButtonText());
      presentation.setIcon(new LayeredIcon(handler.getRootIcon(), AllIcons.RunConfigurations.InvalidConfigurationLayer));
    }
    else {
      presentation.setText(PerlBundle.message("perl.action.unmark.multi"));
      presentation.setIcon(new LayeredIcon(PerlIcons.PERL_LANGUAGE_ICON, AllIcons.RunConfigurations.InvalidConfigurationLayer));
    }
  }

  @Override
  public void actionPerformed(AnActionEvent e) {
    RootsSelection selection = getSelection(e);
    PerlModuleExtension perlModuleExtension =
      (PerlModuleExtension)PerlModuleExtension.getInstance(selection.myModule).getModifiableModel(true);
    for (VirtualFile virtualFile : getFilesFromSelection(selection)) {
      perlModuleExtension.removeRoot(virtualFile);
    }
    perlModuleExtension.commit();
  }

  @Override
  protected boolean isEnabled(@NotNull List<VirtualFile> files, @NotNull Module module) {
    PerlModuleExtension perlModuleExtension = PerlModuleExtension.getInstance(module);
    for (VirtualFile file : files) {
      if (perlModuleExtension.getRootType(file) != null) {
        return true;
      }
    }
    return false;
  }
}
