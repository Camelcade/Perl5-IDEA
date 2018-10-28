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

import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.text.StringUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

public class PerlBrewInstallPerlAction extends PerlBrewAction {

  public PerlBrewInstallPerlAction() {
    Presentation presentation = getTemplatePresentation();
    presentation.setText(PerlBundle.message("perl.vm.install.perl"));
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    Sdk perlSdk = getPerlSdk(e);
    PerlHostData hostData = PerlHostData.from(perlSdk);
    PerlBrewData perlBrewData = PerlBrewData.from(perlSdk);
    if (perlBrewData == null || hostData == null) {
      return;
    }

    new Task.Modal(getEventProject(e), PerlBundle.message("perl.vm.perlbrew.fetching.available.perls"), false) {
      @Override
      public void run(@NotNull ProgressIndicator indicator) {
        indicator.setIndeterminate(true);
        PerlBrewAdapter perlBrewAdapter = new PerlBrewAdapter(perlBrewData.getVersionManagerPath(), hostData);
        List<String> distributionsList = perlBrewAdapter.getAvailableDistributionsList();
        if (distributionsList == null) {
          return;
        }
        ApplicationManager.getApplication().invokeLater(() -> {
          MyDialog dialog = new MyDialog(getEventProject(e), distributionsList);
          if (dialog.showAndGet()) {
            PerlBrewInstallPerlForm form = dialog.getForm();

            boolean createInterpreter = form.getAddInstalledPerl5ToCheckBox().isSelected();
            boolean chooseInterpreter = form.getChooseForCurrentProjectCheckBox().isSelected();

            String targetName = form.getTargetName();
            String distributionId = form.getSelectedDistributionId();

            perlBrewAdapter.installPerl(myProject, distributionId, form.buildParametersList(), new ProcessAdapter() {
              @Override
              public void processTerminated(@NotNull ProcessEvent event) {
                if (!createInterpreter || event.getExitCode() != 0) {
                  return;
                }
                PerlBrewHandler.getInstance().createInterpreter(
                  StringUtil.isEmpty(targetName) ? distributionId : targetName,
                  perlBrewAdapter,
                  sdk -> {
                    if (chooseInterpreter) {
                      ApplicationManager.getApplication().invokeLater(() -> PerlProjectManager.getInstance(myProject).setProjectSdk(sdk));
                    }
                  });
              }
            });
          }
        });
      }
    }.queue();
  }

  private static class MyDialog extends DialogWrapper implements PerlBrewInstallPerlForm.ChangeListener {
    @NotNull
    private PerlBrewInstallPerlForm myForm;

    public MyDialog(@Nullable Project project, @NotNull List<String> distibutions) {
      super(project, true, IdeModalityType.PROJECT);
      myForm = new PerlBrewInstallPerlForm(this, distibutions);
      setOKButtonText(PerlBundle.message("perl.vm.perlbrew.install.form.button"));
      setResizable(false);
      setButtonsAlignment(SwingConstants.CENTER);
      setTitle(PerlBundle.message("perl.vm.perlbrew.install.form.title"));
      init();
    }

    @Override
    public void changed(@NotNull PerlBrewInstallPerlForm form) {
      getOKAction().setEnabled(form.getDistributionsComboBox().getSelectedItem() != null);
    }

    @NotNull
    public PerlBrewInstallPerlForm getForm() {
      return myForm;
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
      return myForm.getRootPanel();
    }
  }
}
