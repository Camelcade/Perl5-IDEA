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

package com.perl5.lang.perl.idea.sdk.versionManager;

import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.text.VersionComparatorUtil;
import com.perl5.PerlBundle;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.actions.PerlActionBase;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

public abstract class PerlInstallAction extends PerlActionBase implements DumbAware {
  public PerlInstallAction() {
    Presentation presentation = getTemplatePresentation();
    presentation.setText(PerlBundle.message("perl.vm.install.perl"));
  }

  @NotNull
  protected abstract String doCleanDistributionItem(@NotNull String rawItem);

  protected abstract boolean doIsInstalled(@NotNull String rawItem);

  @Nullable
  protected abstract PerlRealVersionManagerData getData(@Nullable Sdk sdk);

  @NotNull
  protected abstract PerlVersionManagerAdapter createAdapter(@NotNull String vmPath, @NotNull PerlHostData hostData);

  @NotNull
  protected abstract PerlInstallFormOptions createOptionsForm();

  protected int doCompareVersions(String a, String b) {
    return VersionComparatorUtil.compare(b, a);
  }

  @Nullable
  public Icon doGetIcon(@NotNull String distribution) {
    return PerlIcons.PERL_LANGUAGE_ICON;
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    Sdk perlSdk = getPerlSdk(e);
    PerlHostData hostData = PerlHostData.from(perlSdk);
    PerlRealVersionManagerData versionManagerData = getData(perlSdk);
    if (versionManagerData == null || hostData == null) {
      return;
    }

    new Task.Modal(getEventProject(e), PerlBundle.message("perl.vm.fetching.available.perls"), false) {
      @Override
      public void run(@NotNull ProgressIndicator indicator) {
        indicator.setIndeterminate(true);
        PerlVersionManagerAdapter vmAdapter = createAdapter(versionManagerData.getVersionManagerPath(), hostData);
        List<String> distributionsList = vmAdapter.getAvailableDistributionsList();
        if (distributionsList == null) {
          return;
        }
        ApplicationManager.getApplication().invokeLater(() -> {
          PerlInstallFormOptions optionsForm = createOptionsForm();
          MyDialog dialog = new MyDialog(getEventProject(e), optionsForm, distributionsList);
          if (dialog.showAndGet()) {
            PerlInstallForm installForm = dialog.getForm();

            boolean createInterpreter = installForm.isAddInstalledPerl();
            boolean chooseInterpreter = installForm.isChooseInstalledPerl();

            String distributionId = installForm.getSelectedDistributionId();
            String targetName = optionsForm.getTargetName(distributionId);

            vmAdapter.installPerl(myProject, distributionId, optionsForm.buildParametersList(), new ProcessAdapter() {
              @Override
              public void processTerminated(@NotNull ProcessEvent event) {
                if (!createInterpreter || event.getExitCode() != 0) {
                  return;
                }
                ((PerlRealVersionManagerHandler)versionManagerData.getHandler()).createInterpreter(
                  targetName, vmAdapter, sdk -> {
                    if (chooseInterpreter) {
                      ApplicationManager.getApplication().invokeLater(
                        () -> PerlProjectManager.getInstance(myProject).setProjectSdk((Sdk)sdk));
                    }
                  });
              }
            });
          }
        });
      }
    }.queue();
  }

  private class MyDialog extends DialogWrapper implements PerlInstallForm.InstallFormHelper {
    @NotNull
    private PerlInstallForm myForm;

    public MyDialog(@Nullable Project project, @Nullable PerlInstallFormOptions optionsPanel, @NotNull List<String> distributions) {
      super(project, true, IdeModalityType.PROJECT);
      myForm = new PerlInstallForm(optionsPanel, this, distributions);

      setOKButtonText(PerlBundle.message("perl.vm.perlbrew.install.form.button"));
      setResizable(false);
      setButtonsAlignment(SwingConstants.CENTER);
      setTitle(PerlBundle.message("perl.vm.install.form.title"));
      init();
    }

    @Override
    public void changed(@NotNull PerlInstallForm form) {
      getOKAction().setEnabled(StringUtil.isNotEmpty(form.getSelectedDistributionId()));
    }

    @Override
    public int compare(String o1, String o2) {
      return doCompareVersions(o1, o2);
    }

    @Nullable
    @Override
    public Icon getIcon(@NotNull String distribution) {
      return doGetIcon(distribution);
    }

    @Override
    public String cleanDistributionItem(@NotNull String rawItem) {
      return doCleanDistributionItem(rawItem);
    }

    @Override
    public boolean isInstalled(@NotNull String rawItem) {
      return doIsInstalled(rawItem);
    }

    @NotNull
    public PerlInstallForm getForm() {
      return myForm;
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
      return myForm.getRootPanel();
    }
  }
}
