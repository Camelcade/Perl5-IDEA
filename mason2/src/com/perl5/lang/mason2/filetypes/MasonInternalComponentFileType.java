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

package com.perl5.lang.mason2.filetypes;

import com.perl5.lang.mason2.Mason2Icons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public class MasonInternalComponentFileType extends MasonTopLevelComponentFileType {
  public static final MasonInternalComponentFileType INSTANCE = new MasonInternalComponentFileType();
  public static final String INTERNAL_COMPONENT_EXTENSION = "mi";

  @Override
  public @NotNull String getName() {
    return "Mason2 Internal Component";
  }

  @Override
  public @NotNull String getDescription() {
    return "Mason2 internal component";
  }

  @Override
  public @NotNull String getDefaultExtension() {
    return INTERNAL_COMPONENT_EXTENSION;
  }

  @Override
  public @Nullable Icon getIcon() {
    return Mason2Icons.MASON_INTERNAL_COMPONENT_ICON;
  }
}
