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

package com.perl5.lang.tt2.idea.highlighting;

import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.ex.util.LayerDescriptor;
import com.intellij.openapi.editor.ex.util.LayeredLexerEditorHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.perl5.lang.tt2.TemplateToolkitFileViewProvider;
import com.perl5.lang.tt2.elementTypes.TemplateToolkitElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 05.06.2016.
 */
public class TemplateToolkitHighlighter extends LayeredLexerEditorHighlighter implements TemplateToolkitElementTypes {
  public TemplateToolkitHighlighter(
    @Nullable Project project,
    @Nullable final VirtualFile virtualFile,
    @NotNull EditorColorsScheme scheme
  ) {
    super(new TemplateToolkitSyntaxHighlighter(project), scheme);
    if (project != null) {
      registerLayer(TT2_HTML, new LayerDescriptor(
        SyntaxHighlighterFactory.getSyntaxHighlighter(
          TemplateToolkitFileViewProvider.calcTemplateLanguage(PsiManager.getInstance(project), virtualFile),
          project,
          virtualFile
        ),
        ""
      ));
    }
  }
}
