/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessListener;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.text.VersionComparatorUtil;
import com.perl5.PerlBundle;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

public abstract class InstallPerlHandler {
  private final @NotNull String myVersionManagerPath;
  private final @NotNull PerlRealVersionManagerHandler<?, ?> myVersionManageHandler;

  public InstallPerlHandler(@NotNull String versionManagerPath,
                            @NotNull PerlRealVersionManagerHandler<?, ?> versionManageHandler) {
    myVersionManagerPath = versionManagerPath;
    myVersionManageHandler = versionManageHandler;
  }

  void install(@NotNull PerlHostData<?, ?> hostData, @Nullable Project project) {
    new Task.Modal(project, PerlBundle.message("perl.vm.installing.perl"), false) {
      @Override
      public void run(@NotNull ProgressIndicator indicator) {
        indicator.setIndeterminate(true);
        doInstall(hostData, project);
      }
    }.queue();
  }

  void doInstall(@NotNull PerlHostData<?, ?> hostData, @Nullable Project project) {
    if (ApplicationManager.getApplication().isDispatchThread()) {
      throw new RuntimeException("Should not be invoked on EDT");
    }
    PerlVersionManagerAdapter vmAdapter = createAdapter(myVersionManagerPath, hostData);
    PerlRunUtil.setProgressText(PerlBundle.message("perl.vm.fetching.available.perls"));
    List<String> distributionsList = vmAdapter.getInstallableDistributionsList();
    if (distributionsList == null) {
      return;
    }
    ApplicationManager.getApplication().invokeLater(() -> {
      PerlInstallFormOptions optionsForm = createOptionsForm();
      MyDialog dialog = new MyDialog(project, optionsForm, distributionsList);
      if (dialog.showAndGet()) {
        PerlInstallForm installForm = dialog.getForm();

        boolean createInterpreter = installForm.isAddInstalledPerl();
        boolean chooseInterpreter = installForm.isChooseInstalledPerl();

        String distributionId = installForm.getSelectedDistributionId();
        String targetName = optionsForm.getTargetName(distributionId);

        vmAdapter.installPerl(project, distributionId, optionsForm.buildParametersList(), new ProcessListener() {
          @Override
          public void processTerminated(@NotNull ProcessEvent event) {
            if (!createInterpreter || event.getExitCode() != 0) {
              return;
            }
            myVersionManageHandler.createInterpreter(
              targetName, vmAdapter, sdk -> {
                if (chooseInterpreter && project != null) {
                  ApplicationManager.getApplication().invokeLater(
                    () -> PerlProjectManager.getInstance(project).setProjectSdk(sdk));
                }
              }, project);
          }
        });
      }
    });
  }

  public @NotNull PerlRealVersionManagerHandler<?, ?> getVersionManageHandler() {
    return myVersionManageHandler;
  }

  protected abstract @NotNull String doCleanDistributionItem(@NotNull String rawItem);

  protected abstract boolean doIsInstalled(@NotNull String rawItem);

  protected abstract @NotNull PerlVersionManagerAdapter createAdapter(@NotNull String vmPath, @NotNull PerlHostData<?, ?> hostData);

  protected abstract @NotNull PerlInstallFormOptions createOptionsForm();

  protected int doCompareVersions(String a, String b) {
    return VersionComparatorUtil.compare(b, a);
  }

  public @Nullable Icon doGetIcon(@NotNull String distribution) {
    return PerlIcons.PERL_LANGUAGE_ICON;
  }

  private class MyDialog extends DialogWrapper implements PerlInstallForm.InstallFormHelper {
    private final @NotNull PerlInstallForm myForm;

    public MyDialog(@Nullable Project project, @Nullable PerlInstallFormOptions optionsPanel, @NotNull List<String> distributions) {
      super(project, true, IdeModalityType.IDE);
      myForm = new PerlInstallForm(optionsPanel, this, distributions);
      if (project == null) {
        myForm.disableChooseCheckbox();
      }

      setOKButtonText(PerlBundle.message("perl.vm.perlbrew.install.form.button"));
      setResizable(false);
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

    @Override
    public @Nullable Icon getIcon(@NotNull String distribution) {
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

    public @NotNull PerlInstallForm getForm() {
      return myForm;
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
      return myForm.getRootPanel();
    }
  }
}
