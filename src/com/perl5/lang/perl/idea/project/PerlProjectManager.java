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

package com.perl5.lang.perl.idea.project;

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.ide.projectView.ProjectView;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.ProjectJdkTable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.impl.PerlSdkTable;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.roots.ex.ProjectRootManagerEx;
import com.intellij.openapi.util.AtomicNullableLazyValue;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiManager;
import com.perl5.lang.perl.idea.configuration.settings.PerlLocalSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerlProjectManager {
  @NotNull
  private final Project myProject;
  private final PerlLocalSettings myPerlSettings;
  private AtomicNullableLazyValue<Sdk> mySdkProvider;

  public PerlProjectManager(@NotNull Project project) {
    myProject = project;
    myPerlSettings = PerlLocalSettings.getInstance(project);
    resetProjectSdk();
    myProject.getMessageBus().connect().subscribe(PerlSdkTable.PERL_TABLE_TOPIC, new ProjectJdkTable.Listener() {
      @Override
      public void jdkAdded(@NotNull Sdk jdk) {

      }

      @Override
      public void jdkRemoved(@NotNull Sdk sdk) {
        if (StringUtil.equals(sdk.getName(), myPerlSettings.getPerlInterpreter())) {
          setProjectSdk(null);
        }
      }

      @Override
      public void jdkNameChanged(@NotNull Sdk jdk, @NotNull String previousName) {
        if (StringUtil.equals(myPerlSettings.getPerlInterpreter(), previousName)) {
          setProjectSdk(jdk);
        }
      }
    });
  }

  private void resetProjectSdk() {
    mySdkProvider = new AtomicNullableLazyValue<Sdk>() {
      @Nullable
      @Override
      protected Sdk compute() {
        return PerlSdkTable.getInstance().findJdk(myPerlSettings.getPerlInterpreter());
      }
    };
  }

  public Sdk getProjectSdk() {
    return mySdkProvider.getValue();
  }

  public void setProjectSdk(@Nullable Sdk sdk) {
    myPerlSettings.setPerlInterpreter(sdk == null ? null : sdk.getName());
    resetProjectSdk();


    ((ProjectRootManagerEx)ProjectRootManager.getInstance(myProject)).clearScopesCachesForModules();
    PsiManager.getInstance(myProject).dropResolveCaches();
    DaemonCodeAnalyzer.getInstance(myProject).restart();
    ProjectView.getInstance(myProject).refresh();
  }

  public static PerlProjectManager getInstance(@NotNull Project project) {
    return ServiceManager.getService(project, PerlProjectManager.class);
  }
}
