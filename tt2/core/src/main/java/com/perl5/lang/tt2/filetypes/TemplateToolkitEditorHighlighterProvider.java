/*
 * Copyright 2015-2023 Alexandr Evstigneev
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

import com.intellij.openapi.editor.ex.util.LayerDescriptor;
import com.intellij.openapi.editor.ex.util.LayeredLexerEditorHighlighter;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.idea.highlighter.PerlEditorHighlighterProviderBase;
import com.perl5.lang.tt2.TemplateToolkitFileViewProvider;
import com.perl5.lang.tt2.elementTypes.TemplateToolkitElementTypes;
import com.perl5.lang.tt2.idea.highlighting.TemplateToolkitSyntaxHighlighter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.tt2.lexer.TemplateToolkitElementTypesGenerated.TT2_HTML;

public class TemplateToolkitEditorHighlighterProvider extends PerlEditorHighlighterProviderBase {
  @Override
  protected @NotNull EditorHighlighter registerAdditionalLayers(@NotNull LayeredLexerEditorHighlighter highlighter,
                                                                @Nullable Project project,
                                                                @NotNull FileType fileType,
                                                                @Nullable VirtualFile virtualFile) {
    highlighter.registerLayer(TT2_HTML, new LayerDescriptor(
      SyntaxHighlighterFactory.getSyntaxHighlighter(
        TemplateToolkitFileViewProvider.calcTemplateLanguage(project, virtualFile),
        project,
        virtualFile
      ),
      ""
    ));
    var perlLayerDescriptor = new LayerDescriptor(
      SyntaxHighlighterFactory.getSyntaxHighlighter(PerlLanguage.INSTANCE, project, virtualFile), "");
    highlighter.registerLayer(TemplateToolkitElementTypes.TT2_PERL_CODE, perlLayerDescriptor);
    highlighter.registerLayer(TemplateToolkitElementTypes.TT2_RAWPERL_CODE, perlLayerDescriptor);
    return highlighter;
  }

  @Override
  protected @NotNull SyntaxHighlighter createBaseSyntaxHighlighter(@Nullable Project project) {
    return new TemplateToolkitSyntaxHighlighter(project);
  }
}
