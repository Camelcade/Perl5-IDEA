/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package com.perl5.lang.mojolicious.idea.actions;

import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.util.NlsContexts;
import com.perl5.lang.mojolicious.MojoBundle;
import com.perl5.lang.mojolicious.MojoIcons;
import com.perl5.lang.mojolicious.model.MojoApp;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class MojoGenerateAppAction extends MojoGenerateAction {

  public MojoGenerateAppAction() {
    super(MojoBundle.message("mojo.action.generate.app"));
  }

  @Override
  protected @Nullable InputValidator getNameValidator() {
    return new MojoApp.NameValidator();
  }

  @Override
  protected @NotNull Icon getPromptIcon() {
    return MojoIcons.MOJO_LOGO;
  }

  @Override
  protected @NlsContexts.DialogTitle @NotNull String getPromptTitle() {
    return MojoBundle.message("mojo.action.generate.app.prompt.title");
  }

  @Override
  protected @NotNull String getPromptMessage() {
    return MojoBundle.message("mojo.action.generate.app.prompt.message");
  }

  @Override
  protected @NotNull String getGenerateCommand() {
    return "app";
  }

  @Override
  protected @NotNull String getDefaultName() {
    return "MyApp";
  }
}
