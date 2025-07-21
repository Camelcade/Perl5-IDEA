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

package com.perl5.lang.perl.idea.highlighter;

import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.ex.util.LayerDescriptor;
import com.intellij.openapi.editor.ex.util.LayeredLexerEditorHighlighter;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.fileTypes.EditorHighlighterProvider;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.pod.filetypes.PodFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.POD;

public abstract class PerlEditorHighlighterProviderBase implements EditorHighlighterProvider {
  @Override
  public final EditorHighlighter getEditorHighlighter(@Nullable Project project,
                                                      @NotNull FileType fileType,
                                                      @Nullable VirtualFile virtualFile,
                                                      @NotNull EditorColorsScheme colors) {
    LayeredLexerEditorHighlighter highlighter = new LayeredLexerEditorHighlighter(createBaseSyntaxHighlighter(project), colors);
    highlighter.registerLayer(POD, new LayerDescriptor(
      Objects.requireNonNull(SyntaxHighlighterFactory.getSyntaxHighlighter(PodFileType.INSTANCE, project, virtualFile)),
      ""
    ));
    return registerAdditionalLayers(highlighter, project, fileType, virtualFile);
  }

  protected @NotNull EditorHighlighter registerAdditionalLayers(@NotNull LayeredLexerEditorHighlighter highlighter,
                                                                @Nullable Project project,
                                                                @NotNull FileType fileType,
                                                                @Nullable VirtualFile virtualFile) {
    return highlighter;
  }

  protected abstract @NotNull SyntaxHighlighter createBaseSyntaxHighlighter(@Nullable Project project);
}
