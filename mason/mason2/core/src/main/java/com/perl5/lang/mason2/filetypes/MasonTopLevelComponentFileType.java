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

package com.perl5.lang.mason2.filetypes;

import com.perl5.lang.mason2.Mason2Bundle;
import com.perl5.lang.mason2.Mason2Icons;
import com.perl5.lang.mason2.Mason2TemplatingLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public class MasonTopLevelComponentFileType extends MasonPurePerlComponentFileType {
  public static final MasonTopLevelComponentFileType INSTANCE = new MasonTopLevelComponentFileType();
  public static final String TOP_LEVEL_COMPONENT_EXTENSION = "mc";

  public MasonTopLevelComponentFileType() {
    super(Mason2TemplatingLanguage.INSTANCE);
  }

  @Override
  public @NotNull String getName() {
    return "Mason2 Top-Level Component";
  }

  @Override
  public @NotNull String getDescription() {
    return Mason2Bundle.message("label.mason2.top.level.component");
  }

  @Override
  public @NotNull String getDefaultExtension() {
    return TOP_LEVEL_COMPONENT_EXTENSION;
  }

  @Override
  public @Nullable Icon getIcon() {
    return Mason2Icons.MASON_TOP_LEVEL_COMPONENT_ICON;
  }
}
