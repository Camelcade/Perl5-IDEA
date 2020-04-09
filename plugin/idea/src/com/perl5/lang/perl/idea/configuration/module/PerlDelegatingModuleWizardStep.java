/*
 * Copyright 2015-2020 Alexandr Evstigneev
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
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

class PerlDelegatingModuleWizardStep extends ModuleWizardStep {
  @NotNull
  private final PerlProjectGeneratorPeerBase<?> myPeer;

  public PerlDelegatingModuleWizardStep(@NotNull PerlProjectGeneratorPeerBase<?> peer) {
    myPeer = peer;
  }

  @Override
  public final JComponent getComponent() {
    return myPeer.getComponent();
  }

  @Override
  public final void updateStep() {
    myPeer.reset();
  }

  @Override
  public final void updateDataModel() {
    myPeer.apply();
  }

  @Override
  public final boolean isStepVisible() {
    return true;
  }

  @Override
  public final void disposeUIResources() {
    myPeer.disposeUIResources();
  }
}
