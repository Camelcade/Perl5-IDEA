/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

package com.perl5.lang.tt2;

import com.intellij.openapi.util.IconLoader;
import com.intellij.ui.LayeredIcon;
import com.perl5.PerlIcons;

import javax.swing.*;

public final class TemplateToolkitIcons {
  public static final Icon TTK2_ICON = IconLoader.getIcon("/icons/template_toolkit_gutter_icon.png");
  public static final Icon TTK2_MODIFIER = IconLoader.getIcon("/icons/tt-modifier.png");
  public static final Icon TTK2_ROOT = new LayeredIcon(PerlIcons.TEMPLATE_ROOT, TTK2_MODIFIER);
  public static final Icon TTK2_BLOCK_ICON = IconLoader.getIcon("/icons/template_toolkit_block_gutter_icon.png");

  private TemplateToolkitIcons() {
  }


}
