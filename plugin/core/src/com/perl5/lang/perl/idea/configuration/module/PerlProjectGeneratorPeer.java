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

import com.intellij.openapi.options.UnnamedConfigurable;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.intellij.platform.GeneratorPeerImpl;
import com.perl5.lang.perl.idea.configuration.settings.sdk.Perl5SdkConfigurable;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class PerlProjectGeneratorPeer extends GeneratorPeerImpl<PerlProjectGenerationSettings> implements UnnamedConfigurable {
  @NotNull
  private final Perl5SdkConfigurable mySdkConfigurable;
  private final AtomicNotNullLazyValue<JComponent> myComponentProvider = AtomicNotNullLazyValue.createValue(
    () -> initializeComponent(super.getComponent()));

  public PerlProjectGeneratorPeer() {
    super(new PerlProjectGenerationSettings(), new JPanel(new VerticalFlowLayout()));
    mySdkConfigurable = new Perl5SdkConfigurable(getSettings(), null);
  }

  @NotNull
  @Override
  public final JComponent getComponent() {
    return myComponentProvider.getValue();
  }

  @Override
  public void disposeUIResources() {
    mySdkConfigurable.disposeUIResources();
  }

  @NotNull
  protected JComponent initializeComponent(@NotNull JComponent component) {
    component.add(mySdkConfigurable.createComponent());
    return component;
  }

  @NotNull
  @Override
  public final JComponent createComponent() {
    return getComponent();
  }

  @Override
  public boolean isModified() {
    throw new RuntimeException("NYI");
  }

  @Override
  public void apply() {
    mySdkConfigurable.apply();
  }

  @Override
  public void reset() {
    mySdkConfigurable.reset();
  }
}
