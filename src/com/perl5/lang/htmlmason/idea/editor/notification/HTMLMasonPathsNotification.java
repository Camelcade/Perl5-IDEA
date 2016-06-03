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

package com.perl5.lang.htmlmason.idea.editor.notification;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.ui.EditorNotificationPanel;
import com.intellij.ui.EditorNotifications;
import com.perl5.lang.htmlmason.filetypes.HTMLMasonFileType;
import com.perl5.lang.htmlmason.idea.configuration.HTMLMasonSettings;
import com.perl5.lang.htmlmason.idea.configuration.HTMLMasonSettingsConfigurable;
import com.perl5.lang.htmlmason.parser.psi.impl.HTMLMasonFileImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 05.03.2016.
 */
public class HTMLMasonPathsNotification extends EditorNotifications.Provider<EditorNotificationPanel> implements DumbAware
{
	private static final Key<EditorNotificationPanel> KEY = Key.create("perl.htmlmason.roots.not.set.panel");
	private final Project myProject;

	public HTMLMasonPathsNotification(Project myProject)
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
	public EditorNotificationPanel createNotificationPanel(@NotNull final VirtualFile file, @NotNull FileEditor fileEditor)
	{
		if (file.getFileType() == HTMLMasonFileType.INSTANCE)
		{
			String message = null;

			if (HTMLMasonSettings.getInstance(myProject).componentRoots.isEmpty())
			{
				message = "HTML::Mason components roots are not configured";
			}
			else
			{
				PsiFile psiFile = PsiManager.getInstance(myProject).findFile(file);
				if (psiFile instanceof HTMLMasonFileImpl && ((HTMLMasonFileImpl) psiFile).getComponentRoot() == null)
				{
					message = "Component is not under one of configured roots";
				}
			}

			if (message != null)
			{
				EditorNotificationPanel panel = new EditorNotificationPanel();
				panel.setText(message);
				panel.createActionLabel("Configure", new Runnable()
				{
					@Override
					public void run()
					{
						ShowSettingsUtil.getInstance().editConfigurable(myProject, new HTMLMasonSettingsConfigurable(myProject, "HTML::Mason Settings"));
					}
				});
				return panel;
			}
		}

		return null;
	}


}
