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

package com.perl5.lang.htmlmason.filetypes;

import com.intellij.openapi.fileTypes.ex.FileTypeIdentifiableByVirtualFile;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.htmlmason.HTMLMasonIcons;
import com.perl5.lang.htmlmason.HTMLMasonLanguage;
import com.perl5.lang.htmlmason.HtmlMasonBundle;
import com.perl5.lang.perl.fileTypes.PerlFileType;
import com.perl5.lang.perl.fileTypes.PerlFileTypeService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public class HTMLMasonFileType extends PerlFileType implements FileTypeIdentifiableByVirtualFile {
  public static final HTMLMasonFileType INSTANCE = new HTMLMasonFileType();
  public static final String DEFAULT_EXTENSION = "mas";

  public HTMLMasonFileType() {
    super(HTMLMasonLanguage.INSTANCE);
  }

  @Override
  public @NotNull String getName() {
    return "HTML::Mason Component";
  }

  @Override
  public @NotNull String getDescription() {
    return HtmlMasonBundle.message("label.html.mason.component");
  }

  @Override
  public @NotNull String getDefaultExtension() {
    return DEFAULT_EXTENSION;
  }

  @Override
  public @Nullable Icon getIcon() {
    return HTMLMasonIcons.HTML_MASON_COMPONENT_ICON;
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
  public boolean isMyFileType(@NotNull VirtualFile file) {
    return PerlFileTypeService.getFileType(file) == INSTANCE;
  }
}
