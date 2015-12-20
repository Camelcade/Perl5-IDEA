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

package com.perl5.lang.mojolicious.filetypes;

import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.fileTypes.EditorHighlighterProvider;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeEditorHighlighterProviders;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.PerlIcons;
import com.perl5.lang.mojolicious.MojoliciousPerlLanguage;
import com.perl5.lang.mojolicious.idea.highlighter.MojoliciousPerlHighlighter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by hurricup on 21.07.2015.
 */
public class MojoliciousPerlFileType extends LanguageFileType
{
	public static final MojoliciousPerlFileType INSTANCE = new MojoliciousPerlFileType();

	public MojoliciousPerlFileType()
	{
		super(MojoliciousPerlLanguage.INSTANCE);
		FileTypeEditorHighlighterProviders.INSTANCE.addExplicitExtension(this, new EditorHighlighterProvider()
		{
			@Override
			public EditorHighlighter getEditorHighlighter(@Nullable Project project, @NotNull FileType fileType, @Nullable VirtualFile virtualFile, @NotNull EditorColorsScheme editorColorsScheme)
			{
				return new MojoliciousPerlHighlighter(project, virtualFile, editorColorsScheme);
			}
		});
	}

	@NotNull
	@Override
	public String getName()
	{
		return "Mojolicious Perl5 Template";
	}

	@NotNull
	@Override
	public String getDescription()
	{
		return "Mojolicious Perl5 Template";
	}

	@NotNull
	@Override
	public String getDefaultExtension()
	{
		return "ep";
	}

	@Nullable
	@Override
	public Icon getIcon()
	{
		return PerlIcons.MOJO_FILE;
	}

}
