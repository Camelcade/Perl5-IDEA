/*
 * Copyright 2016 Alexandr Evstigneev
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

import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.fileTypes.EditorHighlighterProvider;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeEditorHighlighterProviders;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.fileTypes.ex.FileTypeIdentifiableByVirtualFile;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectLocator;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.newvfs.impl.StubVirtualFile;
import com.perl5.PerlIcons;
import com.perl5.lang.tt2.TemplateToolkitLanguage;
import com.perl5.lang.tt2.idea.highlighting.TemplateToolkitHighlighter;
import com.perl5.lang.tt2.idea.settings.TemplateToolkitSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by hurricup on 05.06.2016.
 */
public class TemplateToolkitFileType extends LanguageFileType implements FileTypeIdentifiableByVirtualFile
{
	public static final FileType INSTANCE = new TemplateToolkitFileType();

	public TemplateToolkitFileType()
	{
		super(TemplateToolkitLanguage.INSTANCE);
		FileTypeEditorHighlighterProviders.INSTANCE.addExplicitExtension(this, new EditorHighlighterProvider()
		{
			@Override
			public EditorHighlighter getEditorHighlighter(@Nullable Project project, @NotNull FileType fileType, @Nullable VirtualFile virtualFile, @NotNull EditorColorsScheme editorColorsScheme)
			{
				return new TemplateToolkitHighlighter(project, virtualFile, editorColorsScheme);
			}
		});

	}

	@NotNull
	@Override
	public String getName()
	{
		return "Template Toolkit";
	}

	@NotNull
	@Override
	public String getDescription()
	{
		return "Template Toolkit Template";
	}

	@NotNull
	@Override
	public String getDefaultExtension()
	{
		return "tt";
	}

	@Nullable
	@Override
	public Icon getIcon()
	{
		return PerlIcons.TTK2_ICON;
	}


	@Override
	public boolean isMyFileType(@NotNull VirtualFile virtualFile)
	{
		String extension = virtualFile.getExtension();
		if (TemplateToolkitFileTypeFactory.DEFAULT_EXTENSIONS.contains(extension))
		{
			return true;
		}

		if (virtualFile instanceof StubVirtualFile)
		{
			return false;
		}

		Project project = ProjectLocator.getInstance().guessProjectForFile(virtualFile);
		if (project != null)
		{
			TemplateToolkitSettings settings = TemplateToolkitSettings.getInstance(project);
			return settings.isVirtualFileNameMatches(virtualFile) && settings.isVirtualFileUnderRoot(virtualFile);
		}

		return false;
	}
}
