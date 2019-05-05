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

package com.perl5.lang.tt2.filetypes;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeEditorHighlighterProviders;
import com.intellij.openapi.fileTypes.ex.FileTypeIdentifiableByVirtualFile;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.perl.fileTypes.PerlFileTypeService;
import com.perl5.lang.perl.fileTypes.PerlPluginBaseFileType;
import com.perl5.lang.tt2.TemplateToolkitIcons;
import com.perl5.lang.tt2.TemplateToolkitLanguage;
import com.perl5.lang.tt2.idea.highlighting.TemplateToolkitHighlighter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public class TemplateToolkitFileType extends PerlPluginBaseFileType implements FileTypeIdentifiableByVirtualFile {
  public static final FileType INSTANCE = new TemplateToolkitFileType();

  public TemplateToolkitFileType() {
    super(TemplateToolkitLanguage.INSTANCE);
    FileTypeEditorHighlighterProviders.INSTANCE.addExplicitExtension(this,
                                                                     (project, fileType, virtualFile, editorColorsScheme) -> new TemplateToolkitHighlighter(
                                                                       project, virtualFile, editorColorsScheme));
  }

  @NotNull
  @Override
  public String getName() {
    return "Template Toolkit";
  }

  @NotNull
  @Override
  public String getDescription() {
    return "Template Toolkit Template";
  }

  @NotNull
  @Override
  public String getDefaultExtension() {
    return "tt";
  }

  @Nullable
  @Override
  public Icon getIcon() {
    return TemplateToolkitIcons.TTK2_ICON;
  }


  @Override
  public boolean isMyFileType(@NotNull VirtualFile virtualFile) {
    return PerlFileTypeService.getFileType(virtualFile) == INSTANCE;
  }
}
