package com.perl5.lang.embedded.idea;

import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.ex.util.LayerDescriptor;
import com.intellij.openapi.editor.ex.util.LayeredLexerEditorHighlighter;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 19.05.2015.
 */
public class EmbeddedPerlHighlighter extends LayeredLexerEditorHighlighter
{
	public EmbeddedPerlHighlighter(@Nullable final Project project,
								   @Nullable final VirtualFile virtualFile,
								   @NotNull final EditorColorsScheme colors)
	{
		super(new EmbeddedPerlSyntaxHighlighter(), colors);
		registerLayer(PerlElementTypes.TEMPLATE_BLOCK_HTML, new LayerDescriptor(
				SyntaxHighlighterFactory.getSyntaxHighlighter(StdFileTypes.HTML, project, virtualFile), ""));
	}
}
