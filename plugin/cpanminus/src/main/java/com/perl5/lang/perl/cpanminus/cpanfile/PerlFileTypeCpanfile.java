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

package com.perl5.lang.perl.cpanminus.cpanfile;

import com.intellij.openapi.util.NlsSafe;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.fileTypes.PerlFileTypeScript;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class PerlFileTypeCpanfile extends PerlFileTypeScript {
  public static final @NlsSafe String CPANFILE = "cpanfile";
  public static final PerlFileTypeCpanfile INSTANCE = new PerlFileTypeCpanfile();

  @Override
  public @NlsSafe @NotNull String getName() {
    return "Cpanfile";
  }

  @Override
  public @Nullable Icon getIcon() {
    return PerlIcons.METACPAN;
  }

  @Override
  public boolean checkStrictPragma() {
    return false;
  }

  @Override
  public boolean checkWarningsPragma() {
    return false;
  }

  @Override
  public @NotNull String getDescription() {
    return getName();
  }
}
