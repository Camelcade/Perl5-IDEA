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

import com.intellij.ide.util.projectWizard.SettingsStep;
import com.intellij.openapi.options.UnnamedConfigurable;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.platform.GeneratorPeerImpl;
import com.perl5.lang.perl.idea.configuration.settings.sdk.Perl5SdkConfigurable;
import com.perl5.lang.perl.idea.configuration.settings.sdk.Perl5SdkPanel;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public abstract class PerlProjectGeneratorPeerBase<Settings extends PerlProjectGenerationSettings> extends GeneratorPeerImpl<Settings>
  implements UnnamedConfigurable {
  @NotNull
  private final Perl5SdkConfigurable mySdkConfigurable;

  protected PerlProjectGeneratorPeerBase(@NotNull Settings settings) {
    super(settings, new JPanel(new VerticalFlowLayout()));
    mySdkConfigurable = new Perl5SdkConfigurable(getSettings(), null);
  }

  @NotNull
  @Override
  public final JComponent getComponent() {
    return new JPanel();
  }

  @Override
  public final void disposeUIResources() {
    mySdkConfigurable.disposeUIResources();
    disposeAdditionalResources();
  }

  protected void disposeAdditionalResources() {}

  @Override
  public void buildUI(@NotNull SettingsStep settingsStep) {
    Perl5SdkPanel perl5SdkPanel = mySdkConfigurable.createComponent();
    settingsStep.addSettingsField(perl5SdkPanel.getLabel().getText(), perl5SdkPanel.getComponent());
    mySdkConfigurable.setEnabled(PerlProjectManager.getSdk(getSettings().getProject()) == null);
  }

  @NotNull
  @Override
  public final JComponent createComponent() {
    return getComponent();
  }

  @Override
  public final boolean isModified() {
    throw new RuntimeException("Should not be invoked");
  }

  @Override
  public void apply() {
    mySdkConfigurable.apply();
  }

  @Override
  public void reset() {
    mySdkConfigurable.reset();
  }

  /**
   * @return suggested project name according to current settings. Or null if none
   */
  @Nullable
  public String suggestProjectName() {
    return null;
  }
}
