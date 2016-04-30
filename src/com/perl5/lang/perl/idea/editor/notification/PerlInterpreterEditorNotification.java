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

package com.perl5.lang.perl.idea.editor.notification;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.EditorNotificationPanel;
import com.intellij.ui.EditorNotifications;
import com.intellij.util.PlatformUtils;
import com.perl5.lang.perl.fileTypes.PerlFileType;
import com.perl5.lang.perl.idea.configuration.settings.Perl5Settings;
import com.perl5.lang.perl.idea.configuration.settings.PerlSettingsConfigurable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 30.04.2016.
 */
public class PerlInterpreterEditorNotification extends EditorNotifications.Provider<EditorNotificationPanel> implements DumbAware
{
	private static final Key<EditorNotificationPanel> KEY = Key.create("perl.interpreter.not.choosen");
	private final Project myProject;

	public PerlInterpreterEditorNotification(Project myProject)
	{
		this.myProject = myProject;
	}

	@NotNull
	@Override
	public Key<EditorNotificationPanel> getKey()
	{
		return KEY;
	}

	@Nullable
	@Override
	public EditorNotificationPanel createNotificationPanel(@NotNull VirtualFile file, @NotNull FileEditor fileEditor)
	{
		if (!PlatformUtils.isIntelliJ() && file.getFileType() instanceof PerlFileType && StringUtil.isEmpty(Perl5Settings.getInstance(myProject).perlPath))
		{
			EditorNotificationPanel panel = new EditorNotificationPanel();
			panel.setText("Perl5 interpreter is not configured");
			panel.createActionLabel("Configure", new Runnable()
			{
				@Override
				public void run()
				{
					ShowSettingsUtil.getInstance().editConfigurable(myProject, new PerlSettingsConfigurable(myProject));
				}
			});
			return panel;
		}
		return null;
	}
}
