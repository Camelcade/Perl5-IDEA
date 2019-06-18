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
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.mojolicious.MojoIcons;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class MojoGeneratePluginAction extends MojoGenerateAction {
  public MojoGeneratePluginAction() {
    super("Mojo Plugin", MojoIcons.pluginIcon());
  }

  protected List<String> computeGenerationParameters(@NotNull AnActionEvent e, @NotNull VirtualFile mojoScript) {
    String pluginName = Messages.showInputDialog(getEventProject(e), "Plugin name", "Generate Plugin", MojoIcons.pluginIcon());
    return StringUtil.isEmpty(pluginName) ? null : Arrays.asList("plugin", pluginName);
  }
}
