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

package com.perl5.lang.tt2.filetypes;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.ex.FileTypeIdentifiableByVirtualFile;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.perl.fileTypes.PerlFileTypeService;
import com.perl5.lang.perl.fileTypes.PerlPluginBaseFileType;
import com.perl5.lang.tt2.TemplateToolkitBundle;
import com.perl5.lang.tt2.TemplateToolkitIcons;
import com.perl5.lang.tt2.TemplateToolkitLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public class TemplateToolkitFileType extends PerlPluginBaseFileType implements FileTypeIdentifiableByVirtualFile {
  public static final FileType INSTANCE = new TemplateToolkitFileType();

  public TemplateToolkitFileType() {
    super(TemplateToolkitLanguage.INSTANCE);
  }

  @Override
  public @NotNull String getName() {
    return "Template Toolkit";
  }

  @Override
  public @NotNull String getDescription() {
    return TemplateToolkitBundle.message("label.template.toolkit.template");
  }

  @Override
  public @NotNull String getDefaultExtension() {
    return "tt";
  }

  @Override
  public @Nullable Icon getIcon() {
    return TemplateToolkitIcons.TTK2_ICON;
  }


  @Override
  public boolean isMyFileType(@NotNull VirtualFile virtualFile) {
    return PerlFileTypeService.getFileType(virtualFile) == INSTANCE;
  }
}
