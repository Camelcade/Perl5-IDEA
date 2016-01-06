/*
 * Copyright 2015 Alexandr Evstigneev
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

package com.perl5.lang.embedded.filetypes;

import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.fileTypes.EditorHighlighterProvider;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeEditorHighlighterProviders;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.PerlIcons;
import com.perl5.lang.embedded.EmbeddedPerlLanguage;
import com.perl5.lang.embedded.idea.highlighting.EmbeddedPerlHighlighter;
import com.perl5.lang.perl.filetypes.PerlFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by hurricup on 18.05.2015.
 */
public class EmbeddedPerlFileType extends PerlFileType
{
	public static final EmbeddedPerlFileType INSTANCE = new EmbeddedPerlFileType();

	public EmbeddedPerlFileType()
	{

		super(EmbeddedPerlLanguage.INSTANCE);
		FileTypeEditorHighlighterProviders.INSTANCE.addExplicitExtension(this, new EditorHighlighterProvider()
		{
			@Override
			public EditorHighlighter getEditorHighlighter(@Nullable Project project, @NotNull FileType fileType, @Nullable VirtualFile virtualFile, @NotNull EditorColorsScheme editorColorsScheme)
			{
				return new EmbeddedPerlHighlighter(project, virtualFile, editorColorsScheme);
			}
		});
	}

	@NotNull
	@Override
	public String getName()
	{
		return "Embedded perl";
	}

	@NotNull
	@Override
	public String getDescription()
	{
		return "Embedded perl file";
	}

	@NotNull
	@Override
	public String getDefaultExtension()
	{
		return "thtml";
	}

	@Nullable
	@Override
	public Icon getIcon()
	{
		return PerlIcons.EMBEDDED_PERL_FILE;
	}

	@Override
	public boolean checkStrictPragma()
	{
		return false;
	}

	@Override
	public boolean checkWarningsPragma()
	{
		return false;
	}
}
