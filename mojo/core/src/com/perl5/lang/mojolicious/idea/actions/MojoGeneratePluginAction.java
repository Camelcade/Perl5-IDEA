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

package com.perl5.lang.mojolicious.idea.actions;

import com.perl5.lang.mojolicious.MojoBundle;
import com.perl5.lang.mojolicious.MojoIcons;
import com.perl5.lang.mojolicious.model.MojoPlugin;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class MojoGeneratePluginAction extends MojoGenerateAction {
  public MojoGeneratePluginAction() {
    super(MojoBundle.message("mojo.action.generate.plugin"), MojoIcons.pluginIcon());
  }

  @Override
  protected @NotNull MojoPlugin.NameValidator getNameValidator() {
    return new MojoPlugin.NameValidator();
  }

  @Override
  protected @NotNull Icon getPromptIcon() {
    return MojoIcons.pluginIcon();
  }

  @Override
  protected @NotNull String getPromptTitle() {
    return MojoBundle.message("mojo.action.generate.plugin.prompt.title");
  }

  @Override
  protected @NotNull String getPromptMessage() {
    return MojoBundle.message("mojo.action.generate.plugin.prompt.message");
  }

  @Override
  protected @NotNull String getGenerateCommand() {
    return "plugin";
  }

  @Override
  protected @NotNull String getDefaultName() {
    return "MyPlugin";
  }
}
