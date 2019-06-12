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

import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.perl5.lang.perl.idea.configuration.settings.sdk.Perl5SdkConfigurable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class PerlInterpreterSelectionStep extends ModuleWizardStep {
  @NotNull
  private final PerlModuleBuilder myBuilder;
  @NotNull
  private final WizardContext myWizardContext;
  @NotNull
  private final ModulesProvider myModulesProvider;
  @NotNull
  private final Perl5SdkConfigurable mySdkConfigurable;
  @Nullable
  private Sdk mySdk;

  public PerlInterpreterSelectionStep(@NotNull PerlModuleBuilder builder,
                                      @NotNull WizardContext wizardContext,
                                      @NotNull ModulesProvider modulesProvider) {
    myBuilder = builder;
    myWizardContext = wizardContext;
    myModulesProvider = modulesProvider;
    mySdkConfigurable = new Perl5SdkConfigurable(myBuilder, myWizardContext.getProject());
  }

  @Override
  public JComponent getComponent() {
    JPanel panel = new JPanel(new VerticalFlowLayout());
    panel.add(mySdkConfigurable.createComponent());
    return panel;
  }

  @Override
  public void updateStep() {
    mySdkConfigurable.reset();
  }

  @Override
  public boolean isStepVisible() {
    return myWizardContext.getProject() == null;
  }

  @Override
  public void disposeUIResources() {
    mySdkConfigurable.disposeUIResources();
  }

  @Override
  public void updateDataModel() {
    mySdkConfigurable.apply();
  }
}
