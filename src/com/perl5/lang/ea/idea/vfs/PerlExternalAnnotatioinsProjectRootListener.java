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

package com.perl5.lang.ea.idea.vfs;

import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.*;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.util.PerlLibUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 13.08.2016.
 */
public class PerlExternalAnnotatioinsProjectRootListener extends AbstractProjectComponent
{
	private PerlSharedSettings mySettings = PerlSharedSettings.getInstance(myProject);
	private final VirtualFileListener myFileListener = new MyListener();

	public PerlExternalAnnotatioinsProjectRootListener(Project project)
	{
		super(project);
	}

	@Override
	public void projectOpened()
	{
		VirtualFileManager.getInstance().addVirtualFileListener(myFileListener);
	}

	@Override
	public void projectClosed()
	{
		VirtualFileManager.getInstance().removeVirtualFileListener(myFileListener);
	}

	@NotNull
	@Override
	public String getComponentName()
	{
		return getClass().getName();
	}

	private class MyListener extends VirtualFileAdapter
	{
		@Override
		public void fileDeleted(@NotNull VirtualFileEvent event)
		{
			VirtualFile annotationsRoot = mySettings.getAnnotationsRoot();
			if (annotationsRoot == null)
			{
				return;
			}
			VirtualFile file = event.getFile();
			if (VfsUtil.isAncestor(file, annotationsRoot, false))
			{
				mySettings.setAnnotationsPath(null);
				PerlLibUtil.applyClassPaths(myProject);
			}
		}
	}
}
