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

package com.perl5.lang.pod.filetypes;

import com.perl5.PerlIcons;
import com.perl5.lang.perl.fileTypes.PerlPluginBaseFileType;
import com.perl5.lang.pod.PodLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public class PodFileType extends PerlPluginBaseFileType {
  public static final String EXTENSION = "pod";
  public static final PodFileType INSTANCE = new PodFileType();

  private PodFileType() {
    super(PodLanguage.INSTANCE);
  }

  @NotNull
  @Override
  public String getName() {
    return "POD file";
  }

  @NotNull
  @Override
  public String getDescription() {
    return "Perl5 Documentation File";
  }

  @NotNull
  @Override
  public String getDefaultExtension() {
    return EXTENSION;
  }

  @Nullable
  @Override
  public Icon getIcon() {
    return PerlIcons.POD_FILE;
  }
}
