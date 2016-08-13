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

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.*;
import com.intellij.openapi.vfs.pointers.VirtualFilePointer;
import com.intellij.openapi.vfs.pointers.VirtualFilePointerManager;
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
		private Key<VirtualFilePointer> KEY = Key.create(getClass().getName());

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

		@Override
		public void fileMoved(@NotNull VirtualFileMoveEvent event)
		{
			handleAfterOperation(event);
		}

		@Override
		public void beforeFileMovement(@NotNull VirtualFileMoveEvent event)
		{
			handleBeforeOperation(event);
		}

		@Override
		public void propertyChanged(@NotNull VirtualFilePropertyEvent event)
		{
			handleAfterOperation(event);
		}

		@Override
		public void beforePropertyChange(@NotNull VirtualFilePropertyEvent event)
		{
			handleBeforeOperation(event);
		}

		private void handleAfterOperation(VirtualFileEvent event)
		{
			VirtualFile file = event.getFile();
			VirtualFilePointer virtualFilePointer = file.getUserData(KEY);
			if (virtualFilePointer != null)
			{
				VirtualFile annotationsRoot = virtualFilePointer.getFile();
				if (annotationsRoot != null)
				{
					VirtualFile baseDir = myProject.getBaseDir();
					if (VfsUtil.isAncestor(baseDir, annotationsRoot, true))
					{
						mySettings.setAnnotationsPath(VfsUtil.getRelativePath(annotationsRoot, baseDir));
					}
					else
					{
						mySettings.setAnnotationsPath(null);
					}
				}
				file.putUserData(KEY, null);
			}
		}

		private void handleBeforeOperation(VirtualFileEvent event)
		{
			VirtualFile annotationsRoot = mySettings.getAnnotationsRoot();
			if (annotationsRoot == null)
			{
				return;
			}

			VirtualFile file = event.getFile();
			if (VfsUtil.isAncestor(file, annotationsRoot, false))
			{
				file.putUserData(KEY, VirtualFilePointerManager.getInstance().create(annotationsRoot, ApplicationManager.getApplication(), null));
			}
		}

	}
}
