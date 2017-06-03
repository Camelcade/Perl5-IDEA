/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

package com.perl5.lang.embedded.idea.highlighting;

import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.ex.util.LayerDescriptor;
import com.intellij.openapi.editor.ex.util.LayeredLexerEditorHighlighter;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.embedded.psi.EmbeddedPerlElementTypes;
import com.perl5.lang.pod.filetypes.PodFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 19.05.2015.
 */
public class EmbeddedPerlHighlighter extends LayeredLexerEditorHighlighter implements EmbeddedPerlElementTypes {
  public EmbeddedPerlHighlighter(@Nullable final Project project,
                                 @Nullable final VirtualFile virtualFile,
                                 @NotNull final EditorColorsScheme colors) {
    super(new EmbeddedPerlSyntaxHighlighter(project), colors);
    registerLayer(EMBED_TEMPLATE_BLOCK_HTML, new LayerDescriptor(
      SyntaxHighlighterFactory.getSyntaxHighlighter(StdFileTypes.HTML, project, virtualFile), ""));
    registerLayer(POD, new LayerDescriptor(
      SyntaxHighlighterFactory.getSyntaxHighlighter(PodFileType.INSTANCE, project, virtualFile),
      ""
    ));
  }
}
