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

import com.intellij.openapi.actionSystem.CustomShortcutSet;
import com.intellij.openapi.roots.ui.configuration.ModuleSourceRootEditHandler;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.model.JpsDummyElement;
import org.jetbrains.jps.model.module.JpsModuleSourceRootType;

import javax.swing.*;
import java.awt.*;

public abstract class PerlSourceRootEditHandler extends ModuleSourceRootEditHandler<JpsDummyElement> {
  public PerlSourceRootEditHandler(JpsModuleSourceRootType<JpsDummyElement> rootType) {
    super(rootType);
  }

  @Nullable
  @Override
  public Icon getFolderUnderRootIcon() {
    return null;
  }

  @Nullable
  @Override
  public CustomShortcutSet getMarkRootShortcutSet() {
    return null;
  }


  @NotNull
  @Override
  public Color getRootsGroupColor() {
    return new JBColor(new Color(76, 94, 133), new Color(76, 94, 133));
  }

}
