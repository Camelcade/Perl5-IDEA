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
import com.intellij.openapi.util.Disposer;
import com.intellij.util.ui.JBUI;
import com.perl5.lang.perl.idea.configuration.settings.sdk.wrappers.Perl5ParentSdkWrapper;
import com.perl5.lang.perl.idea.configuration.settings.sdk.wrappers.Perl5SdkWrapper;
import com.perl5.lang.perl.idea.modules.JpsPerlLibrarySourceRootType;
import org.apache.batik.ext.swing.GridBagConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class Perl5ModuleConfigurable extends Perl5StructureConfigurable {
  private final Perl5SdkWrapper myUseProjectSdkItem;
  private final Disposable myDisposable = Disposer.newDisposable(Perl5ModuleConfigurable.class.toString());

  @NotNull
  private final Module myModule;

  private PerlContentEntriesTreeEditor myPerlContentEntriesTreeEditor;

  public Perl5ModuleConfigurable(@NotNull Module module, @Nullable Perl5ProjectConfigurable projectConfigurable) {
    myModule = module;
    myUseProjectSdkItem = projectConfigurable == null ? NOT_SELECTED_ITEM : new Perl5ParentSdkWrapper(projectConfigurable);
  }

  @Override
  protected void initPanel() {
    super.initPanel();
    Perl5StructurePanel perlPanel = getPanel();
    JPanel mainPanel = perlPanel.getAdditionalPanel();
    perlPanel.getSdkPanel().setVisible(false);
    myPerlContentEntriesTreeEditor = new PerlContentEntriesTreeEditor(myModule, myDisposable, JpsPerlLibrarySourceRootType.INSTANCE);
    mainPanel.add(myPerlContentEntriesTreeEditor.createComponent(),
                  new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstants.BOTH, JBUI.emptyInsets(), 0, 0));
  }

  @Override
  public boolean isModified() {
    return super.isModified() || myPerlContentEntriesTreeEditor.isModified();
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
  protected List<Perl5SdkWrapper> getSdkItems() {
    return Collections.singletonList(myUseProjectSdkItem);
  }

  @Nullable
  @Override
  protected Perl5SdkWrapper getDefaultSelectedItem() {
    return myUseProjectSdkItem;
  }

  @Override
  public void disposeUIResources() {
    super.disposeUIResources();
    Disposer.dispose(myDisposable);
  }
}
