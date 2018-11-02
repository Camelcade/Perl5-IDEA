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

package com.perl5.lang.perl.adapters;

import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessListener;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.text.StringUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

public class CpanAdapter extends PackageManagerAdapter {
  private static final String PACKAGE_NAME = "CPAN";
  public static final String SCRIPT_NAME = "cpan";

  public CpanAdapter(@NotNull Sdk sdk, @NotNull Project project) {
    super(sdk, project);
  }

  @NotNull
  @Override
  public String getPresentableName() {
    return "cpan";
  }

  @NotNull
  @Override
  protected String getManagerScriptName() {
    return SCRIPT_NAME;
  }

  @NotNull
  @Override
  protected String getManagerPackageName() {
    return PACKAGE_NAME;
  }

  @NotNull
  public static AnAction createInstallAction(@NotNull Sdk sdk,
                                             @NotNull Project project,
                                             @NotNull Collection<String> libraryNames,
                                             @Nullable Runnable actionCallback) {
    return new DumbAwareAction(PerlBundle.message("perl.quickfix.install.family", SCRIPT_NAME)) {
      @Override
      public void actionPerformed(@NotNull AnActionEvent e) {
        new CpanAdapter(sdk, project).install(libraryNames);
        if (actionCallback != null) {
          actionCallback.run();
        }
      }
    };
  }

  @Nullable
  @Override
  protected ProcessListener getAdditionalListener() {
    return new ProcessAdapter() {
      @Override
      public void onTextAvailable(@NotNull ProcessEvent event, @NotNull Key outputType) {
        if (StringUtil.startsWith(event.getText(), "Would you like to configure as much as possible automatically")) {
          try {
            OutputStream processInput = event.getProcessHandler().getProcessInput();
            if (processInput != null) {
              processInput.write(new byte[]{13, 13, 13, 13});
            }
          }
          catch (IOException ignore) {
          }
        }
      }
    };
  }

  @Contract("null->null")
  @Nullable
  public static CpanAdapter create(@Nullable Project project) {
    Sdk sdk = PerlProjectManager.getSdk(project);
    return sdk == null ? null : new CpanAdapter(sdk, project);
  }
}
