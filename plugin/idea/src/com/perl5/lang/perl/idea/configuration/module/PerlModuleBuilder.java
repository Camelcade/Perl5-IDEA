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
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.perl5.lang.perl.idea.modules.PerlModuleType;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class PerlModuleBuilder extends ModuleBuilder {
  @NotNull
  private final AtomicNotNullLazyValue<PerlProjectGeneratorPeer> myPeerProvider =
    AtomicNotNullLazyValue.createValue(() -> getGenerator().createPeer());

  @NotNull
  public final PerlProjectGeneratorPeer getPeer() {
    return myPeerProvider.getValue();
  }

  @NotNull
  public final PerlProjectGenerationSettings getSettings() {
    return getPeer().getSettings();
  }

  @Override
  public final Icon getNodeIcon() {
    return getGenerator().getLogo();
  }

  @Nls(capitalization = Nls.Capitalization.Title)
  @Override
  public final String getPresentableName() {
    return getGenerator().getName();
  }

  /**
   * @return generator paired with this builder. All work is delegated to the generator and it's peer. This builder is just a wrapper
   * fixme this should just seek for existing generator, but seem they gonna be disabled for now
   */
  @NotNull
  protected PerlProjectGenerator getGenerator() {
    return new PerlProjectGenerator();
  }

  @Override
  public ModuleType getModuleType() {
    return PerlModuleType.getInstance();
  }

  @Override
  public final boolean isSuitableSdkType(SdkTypeId sdkType) {
    return false;
  }

  @Override
  public final void setupRootModel(@NotNull ModifiableRootModel modifiableRootModel) throws ConfigurationException {
    doAddContentEntry(modifiableRootModel);
  }

  @Override
  protected final void setProjectType(Module module) {
    getGenerator().configureModule(module, getSettings());
  }

  @Override
  public final ModuleWizardStep[] createWizardSteps(@NotNull WizardContext wizardContext, @NotNull ModulesProvider modulesProvider) {
    if (!isStepAvailable(wizardContext, modulesProvider)) {
      return ModuleWizardStep.EMPTY_ARRAY;
    }
    getSettings().setProject(wizardContext.getProject());
    return new ModuleWizardStep[]{new PerlDelegatingModuleWizardStep(getPeer())};
  }

  /**
   * @return true iff we should add a peer-based step
   */
  protected boolean isStepAvailable(@NotNull WizardContext wizardContext, @NotNull ModulesProvider modulesProvider) {
    return wizardContext.getProject() == null;
  }
}
