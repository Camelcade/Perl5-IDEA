package com.perl5.lang.embedded;

import com.intellij.lang.Language;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.fileTypes.EditorHighlighterProvider;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeEditorHighlighterProviders;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.PerlIcons;
import com.perl5.lang.embedded.idea.EmbeddedPerlHighlighter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by hurricup on 18.05.2015.
 */
public class EmbeddedPerlFileType extends LanguageFileType
{
	public static final EmbeddedPerlFileType INSTANCE = new EmbeddedPerlFileType();
	public static final Language LANGUAGE = INSTANCE.getLanguage();

	public EmbeddedPerlFileType(){

		super(EmbeddedPerlLanguage.INSTANCE);
		FileTypeEditorHighlighterProviders.INSTANCE.addExplicitExtension(this, new EditorHighlighterProvider()
		{
			@Override
			public EditorHighlighter getEditorHighlighter(@Nullable Project project, @NotNull FileType fileType, @Nullable VirtualFile virtualFile, @NotNull EditorColorsScheme editorColorsScheme)
			{
				return new EmbeddedPerlHighlighter(project,virtualFile,editorColorsScheme);
			}
		});
	}

	@NotNull
	@Override
	public String getName() {
		return "Embedded perl";
	}

	@NotNull
	@Override
	public String getDescription() {
		return "Embedded perl file";
	}

	@NotNull
	@Override
	public String getDefaultExtension() {
		return "thtml";
	}

	@Nullable
	@Override
	public Icon getIcon() {
		return PerlIcons.EMBEDDED_PERL_FILE;
	}

}
