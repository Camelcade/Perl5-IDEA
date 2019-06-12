/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.configuration.module;

import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.perl5.lang.perl.idea.modules.PerlModuleType;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerlModuleBuilder extends ModuleBuilder {
  @NotNull
  private final AtomicNotNullLazyValue<PerlProjectGeneratorPeer> myPeerProvider =
    AtomicNotNullLazyValue.createValue(PerlProjectGeneratorPeer::new);

  @NotNull
  public PerlProjectGeneratorPeer getPeer() {
    return myPeerProvider.getValue();
  }

  @NotNull
  public PerlProjectGenerationSettings getSettings() {
    return getPeer().getSettings();
  }

  @Override
  public ModuleType getModuleType() {
    return PerlModuleType.getInstance();
  }

  @Override
  public boolean isSuitableSdkType(SdkTypeId sdkType) {
    return false;
  }

  @Override
  public void setupRootModel(@NotNull ModifiableRootModel modifiableRootModel) throws ConfigurationException {
    doAddContentEntry(modifiableRootModel);
  }

  @Nullable
  @Override
  public Project createProject(String name, String path) {
    Project project = super.createProject(name, path);
    if (project != null) {
      PerlProjectManager.getInstance(project).setProjectSdk(getSettings().getSdk());
    }
    return project;
  }

  @Override
  public ModuleWizardStep[] createWizardSteps(@NotNull WizardContext wizardContext, @NotNull ModulesProvider modulesProvider) {
    return new ModuleWizardStep[]{new PerlInterpreterSelectionStep(this, wizardContext, modulesProvider)};
  }
}
