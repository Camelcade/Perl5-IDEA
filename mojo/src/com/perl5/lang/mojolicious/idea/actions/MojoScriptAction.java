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

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import com.perl5.lang.mojolicious.MojoIcons;
import com.perl5.lang.mojolicious.MojoUtil;
import com.perl5.lang.perl.idea.actions.PerlActionBase;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public abstract class MojoScriptAction extends PerlActionBase implements DumbAware {

  public MojoScriptAction(@Nls(capitalization = Nls.Capitalization.Title) @Nullable String text) {
    this(text, MojoIcons.MOJO_LOGO);
  }

  public MojoScriptAction(@Nls(capitalization = Nls.Capitalization.Title) @Nullable String text, @Nullable Icon icon) {
    super(text, icon);
  }

  @Override
  protected boolean isEnabled(@NotNull AnActionEvent event) {
    return MojoUtil.isMojoAvailable(event);
  }
}
