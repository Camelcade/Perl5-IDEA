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

package com.perl5.lang.mojolicious.idea.actions;

import com.intellij.openapi.ui.InputValidator;
import com.perl5.lang.mojolicious.MojoBundle;
import com.perl5.lang.mojolicious.MojoIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class MojoGenerateLiteAppAction extends MojoGenerateAction {

  public MojoGenerateLiteAppAction() {
    super(MojoBundle.message("mojo.action.generate.lite.app"));
  }

  @Nullable
  @Override
  protected InputValidator getNameValidator() {
    return null;
  }

  @NotNull
  @Override
  protected Icon getPromptIcon() {
    return MojoIcons.MOJO_LITE_APP_ICON;
  }

  @NotNull
  @Override
  protected String getPromptTitle() {
    return MojoBundle.message("mojo.action.generate.lite.app.prompt.title");
  }

  @NotNull
  @Override
  protected String getPromptMessage() {
    return MojoBundle.message("mojo.action.generate.lite.app.prompt.message");
  }

  @NotNull
  @Override
  protected String getGenerateCommand() {
    return "lite_app";
  }

  @NotNull
  @Override
  protected String getDefaultName() {
    return "myapp.pl";
  }
}
