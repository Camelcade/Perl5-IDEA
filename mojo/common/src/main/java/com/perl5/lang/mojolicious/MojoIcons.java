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

package com.perl5.lang.mojolicious;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.util.NotNullLazyValue;
import com.perl5.PerlIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public final class MojoIcons {
  public static final Icon MOJO_FILE = load("/icons/mojolicious.png");
  public static final Icon MOJO_LOGO = load("/icons/mojolicious_color.png");

  private static @NotNull Icon load(@NotNull String resourcePath) {
    return IconLoader.getIcon(resourcePath, MojoIcons.class);
  }

  public static final Icon MOJO_APP_ICON = MOJO_LOGO;
  public static final Icon MOJO_LITE_APP_ICON = MOJO_LOGO;
  private static final NotNullLazyValue<Icon> MOJO_ROOT = PerlIcons.createLazyIconWithModifier(PerlIcons.TEMPLATE_ROOT, MOJO_LOGO);
  private static final NotNullLazyValue<Icon> MOJO_PLUGIN_LOGO = PerlIcons.createLazyIconWithModifier(AllIcons.Nodes.Plugin, MOJO_LOGO);

  public static @NotNull Icon pluginIcon() {
    return MOJO_PLUGIN_LOGO.getValue();
  }

  public static @NotNull Icon rootIcon() {
    return MOJO_ROOT.getValue();
  }

  private MojoIcons() {
  }
}
