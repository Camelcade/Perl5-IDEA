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

package com.perl5.lang.htmlmason.idea.editor;

import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.ex.util.LayerDescriptor;
import com.intellij.openapi.editor.ex.util.LayeredLexerEditorHighlighter;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes;
import com.perl5.lang.htmlmason.idea.highlighter.HTMLMasonSyntaxHighlighter;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.pod.filetypes.PodFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 06.03.2016.
 */
public class HTMLMasonHighlighter extends LayeredLexerEditorHighlighter implements HTMLMasonElementTypes {
  public HTMLMasonHighlighter(@Nullable final Project project,
                              @Nullable final VirtualFile virtualFile,
                              @NotNull final EditorColorsScheme colors) {
    super(new HTMLMasonSyntaxHighlighter(project), colors);
    registerLayer(HTML_MASON_TEMPLATE_BLOCK_HTML, new LayerDescriptor(
      SyntaxHighlighterFactory.getSyntaxHighlighter(StdFileTypes.HTML, project, virtualFile), ""));
    registerLayer(PerlElementTypes.POD, new LayerDescriptor(
      SyntaxHighlighterFactory.getSyntaxHighlighter(PodFileType.INSTANCE, project, virtualFile),
      ""
    ));
  }
}
