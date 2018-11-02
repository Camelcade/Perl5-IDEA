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

package com.perl5.lang.perl.idea.sdk.versionManager.perlbrew;

import com.intellij.execution.process.ProcessOutput;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.ObjectUtils;
import com.perl5.PerlBundle;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerData;
import org.jetbrains.annotations.NotNull;

public class PerlBrewCreateLibraryAction extends PerlBrewActionBase implements DumbAware {

  public PerlBrewCreateLibraryAction() {
    getTemplatePresentation().setText(PerlBundle.message("perl.vm.perlbrew.create.lib"));
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    Sdk perlSdk = getPerlSdk(e);
    PerlBrewData perlBrewData = ObjectUtils.tryCast(PerlVersionManagerData.from(perlSdk), PerlBrewData.class);
    if (perlBrewData == null) {
      return;
    }

    PerlHostData hostData = PerlHostData.from(perlSdk);
    if (hostData == null) {
      return;
    }

    final String perlVersionString = perlBrewData.getPerlVersionString();
    String libraryName = Messages.showInputDialog(getEventProject(e),
                                                  PerlBundle.message("perl.vm.perlbrew.create.lib.message"),
                                                  PerlBundle.message("perl.vm.perlbrew.create.lib.title",
                                                                     perlVersionString),
                                                  null);

    if (StringUtil.isEmpty(libraryName)) {
      return;
    }
    new Task.Backgroundable(getEventProject(e), PerlBundle.message("perl.vm.perlbrew.create.lib.progress", libraryName,
                                                                   perlVersionString)) {
      @Override
      public void run(@NotNull ProgressIndicator indicator) {
        PerlBrewAdapter perlBrewAdapter = new PerlBrewAdapter(perlBrewData.getVersionManagerPath(), hostData);
        ProcessOutput processOutput = perlBrewAdapter.createLibrary(perlVersionString, libraryName);
        if (processOutput == null) {
          return;
        }

        String distirbutionId = perlVersionString + "@" + libraryName;
        if (processOutput.getExitCode() == 0) {
          doCreateSdk(perlBrewAdapter, distirbutionId);
        }

        if (StringUtil.contains(processOutput.getStderr(), "is already there")) {
          Ref<Integer> resultRef = Ref.create(-1);
          ApplicationManager.getApplication().invokeAndWait(() -> resultRef.set(Messages.showYesNoDialog(
            PerlBundle.message("perl.vm.perlbrew.library.exists", distirbutionId),
            PerlBundle.message("perl.vm.perlbrew.title"),
            PerlIcons.PERLBREW_ICON)));
          if (Messages.YES == resultRef.get()) {
            doCreateSdk(perlBrewAdapter, distirbutionId);
          }
        }
      }

      // fixme we could avoid creating duplicated sdks here
      private void doCreateSdk(@NotNull PerlBrewAdapter perlBrewAdapter, @NotNull String distirbutionId) {
        PerlBrewHandler.getInstance().createInterpreter(
          distirbutionId, perlBrewAdapter, sdk -> ApplicationManager.getApplication().invokeLater(
            () -> {
              PerlBrewData perlBrewData = PerlBrewData.from(sdk);
              if (perlBrewData != null && Messages.showYesNoDialog(
                myProject,
                PerlBundle.message("perl.vm.perlbrew.create.lib.select", perlBrewData.getDistributionId()),
                PerlBundle.message("perl.vm.perlbrew.create.lib.title", perlBrewData.getPerlVersionString()), null) == Messages.YES) {
                PerlProjectManager.getInstance(myProject).setProjectSdk(sdk);
              }
            }));
      }
    }.queue();
  }
}
