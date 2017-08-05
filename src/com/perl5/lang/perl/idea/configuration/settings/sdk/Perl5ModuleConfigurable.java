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

package com.perl5.lang.perl.idea.configuration.settings.sdk;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.UnnamedConfigurable;
import com.intellij.openapi.util.Disposer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class Perl5ModuleConfigurable implements UnnamedConfigurable {
  private final Disposable myDisposable = Disposer.newDisposable(Perl5ModuleConfigurable.class.toString());
  @NotNull
  private final Module myModule;
  private PerlContentEntriesTreeEditor myPerlContentEntriesTreeEditor;

  public Perl5ModuleConfigurable(@NotNull Module module) {
    myModule = module;
  }

  @Nullable
  @Override
  public JComponent createComponent() {
    myPerlContentEntriesTreeEditor = new PerlContentEntriesTreeEditor(myModule, myDisposable);
    return myPerlContentEntriesTreeEditor.createComponent();
  }

  @Override
  public boolean isModified() {
    return myPerlContentEntriesTreeEditor.isModified();
  }

  @Override
  public void apply() throws ConfigurationException {
    myPerlContentEntriesTreeEditor.apply();
  }

  @Override
  public void reset() {
    myPerlContentEntriesTreeEditor.reset();
  }

  @Override
  public void disposeUIResources() {
    Disposer.dispose(myDisposable);
  }
}
