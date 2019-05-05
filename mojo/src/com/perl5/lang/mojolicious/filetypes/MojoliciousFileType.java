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

package com.perl5.lang.mojolicious.filetypes;

import com.intellij.openapi.fileTypes.FileTypeEditorHighlighterProviders;
import com.perl5.lang.mojolicious.MojoIcons;
import com.perl5.lang.mojolicious.MojoliciousLanguage;
import com.perl5.lang.mojolicious.idea.highlighter.MojoliciousHighlighter;
import com.perl5.lang.perl.fileTypes.PerlFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public class MojoliciousFileType extends PerlFileType {
  public static final MojoliciousFileType INSTANCE = new MojoliciousFileType();
  public static final String MOJO_DEFAULT_EXTENSION = "ep";

  public MojoliciousFileType() {
    super(MojoliciousLanguage.INSTANCE);
    FileTypeEditorHighlighterProviders.INSTANCE.addExplicitExtension(this,
                                                                     (project, fileType, virtualFile, editorColorsScheme) -> new MojoliciousHighlighter(
                                                                       project, virtualFile, editorColorsScheme));
  }

  @NotNull
  @Override
  public String getName() {
    return "Mojolicious Perl5 Template";
  }

  @NotNull
  @Override
  public String getDescription() {
    return "Mojolicious Perl5 Template";
  }

  @NotNull
  @Override
  public String getDefaultExtension() {
    return MOJO_DEFAULT_EXTENSION;
  }

  @Nullable
  @Override
  public Icon getIcon() {
    return MojoIcons.MOJO_FILE;
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
