/*
 * Copyright 2015-2021 Alexandr Evstigneev
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

package com.perl5.lang.embedded;

import com.intellij.openapi.util.IconLoader;
import com.intellij.ui.LayeredIcon;
import com.perl5.PerlIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public final class EmbeddedPerlIcons {
  public static final Icon EMBEDDED_PERL_FILE = load("/icons/embeddedperl.png");
  public static final Icon EMBEDDED_MODIFIER = load("/icons/embedded-modifier.png");

  private static @NotNull Icon load(@NotNull String resourcePath) {
    return IconLoader.getIcon(resourcePath, EmbeddedPerlIcons.class);
  }

  public static final Icon EMBEDDED_ROOT = new LayeredIcon(PerlIcons.TEMPLATE_ROOT, EMBEDDED_MODIFIER);

  private EmbeddedPerlIcons() {
  }
}
