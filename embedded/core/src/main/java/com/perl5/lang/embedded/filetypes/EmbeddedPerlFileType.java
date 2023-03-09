/*
 * Copyright 2015-2023 Alexandr Evstigneev
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

package com.perl5.lang.embedded.filetypes;

import com.perl5.lang.embedded.EmbeddedPerlIcons;
import com.perl5.lang.embedded.EmbeddedPerlLanguage;
import com.perl5.lang.perl.fileTypes.PerlFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public class EmbeddedPerlFileType extends PerlFileType {
  public static final EmbeddedPerlFileType INSTANCE = new EmbeddedPerlFileType();
  public static final String DEFAULT_EXTENSION = "thtml";

  public EmbeddedPerlFileType() {
    super(EmbeddedPerlLanguage.INSTANCE);
  }

  @Override
  public @NotNull String getName() {
    return "Embedded Perl";
  }

  @Override
  public @NotNull String getDescription() {
    return "Embedded perl file";
  }

  @Override
  public @NotNull String getDefaultExtension() {
    return DEFAULT_EXTENSION;
  }

  @Override
  public @Nullable Icon getIcon() {
    return EmbeddedPerlIcons.EMBEDDED_PERL_FILE;
  }

  @Override
  public boolean checkStrictPragma() {
    return false;
  }

  @Override
  public boolean checkWarningsPragma() {
    return false;
  }
}
