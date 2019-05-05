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

package com.perl5.lang.perl.fileTypes;

import com.perl5.PerlBundle;
import com.perl5.PerlIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public class PerlFileTypeScript extends PurePerlFileType {
  public static final PerlFileType INSTANCE = new PerlFileTypeScript();

  public static final String EXTENSION_PL = "pl";
  public static final String EXTENSION_PH = "ph";
  public static final String EXTENSION_AL = "al";
  public static final String EXTENSION_CGI = "cgi";

  @NotNull
  @Override
  public String getDefaultExtension() {
    return EXTENSION_PL;
  }

  @NotNull
  @Override
  public String getName() {
    return "Perl5 script";
  }

  @NotNull
  @Override
  public String getDescription() {
    return PerlBundle.message("perl.filetype.script.description");
  }


  @Nullable
  @Override
  public Icon getIcon() {
    return PerlIcons.PERL_SCRIPT_FILE_ICON;
  }
}
