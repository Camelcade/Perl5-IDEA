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

package com.perl5.lang.htmlmason.filetypes;

import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.fileTypes.EditorHighlighterProvider;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeEditorHighlighterProviders;
import com.intellij.openapi.fileTypes.ex.FileTypeIdentifiableByVirtualFile;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectLocator;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.htmlmason.HTMLMasonIcons;
import com.perl5.lang.htmlmason.HTMLMasonLanguage;
import com.perl5.lang.htmlmason.HTMLMasonUtils;
import com.perl5.lang.htmlmason.idea.configuration.HTMLMasonSettings;
import com.perl5.lang.htmlmason.idea.editor.HTMLMasonHighlighter;
import com.perl5.lang.perl.fileTypes.PerlFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by hurricup on 05.03.2016.
 */
public class HTMLMasonFileType extends PerlFileType implements FileTypeIdentifiableByVirtualFile
{
	public static final HTMLMasonFileType INSTANCE = new HTMLMasonFileType();

	public HTMLMasonFileType()
	{
		super(HTMLMasonLanguage.INSTANCE);
		FileTypeEditorHighlighterProviders.INSTANCE.addExplicitExtension(this, new EditorHighlighterProvider()
		{
			@Override
			public EditorHighlighter getEditorHighlighter(@Nullable Project project, @NotNull FileType fileType, @Nullable VirtualFile virtualFile, @NotNull EditorColorsScheme editorColorsScheme)
			{
				return new HTMLMasonHighlighter(project, virtualFile, editorColorsScheme);
			}
		});
	}

	@NotNull
	@Override
	public String getName()
	{
		return "HTML::Mason component";
	}

	@NotNull
	@Override
	public String getDescription()
	{
		return "HTML::Mason component";
	}

	@NotNull
	@Override
	public String getDefaultExtension()
	{
		return "mas";
	}

	@Nullable
	@Override
	public Icon getIcon()
	{
		return HTMLMasonIcons.HTML_MASON_COMPONENT_ICON;
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

	@Override
	public boolean isMyFileType(@NotNull VirtualFile file)
	{
		Project project = ProjectLocator.getInstance().guessProjectForFile(file);
		if (project != null)
		{
			HTMLMasonSettings settings = HTMLMasonSettings.getInstance(project);
			if (settings != null &&
					(StringUtil.equals(settings.autoHandlerName, file.getName()) || StringUtil.equals(settings.defaultHandlerName, file.getName())) &&
					HTMLMasonUtils.getComponentRoot(project, file) != null)
			{
				return true;
			}
		}
		return false;
	}
}
