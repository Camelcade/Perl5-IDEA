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
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.*;
import com.intellij.openapi.vfs.pointers.VirtualFilePointer;
import com.intellij.openapi.vfs.pointers.VirtualFilePointerManager;
import com.perl5.lang.perl.idea.configuration.settings.PerlApplicationSettings;
import com.perl5.lang.perl.util.PerlLibUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created by hurricup on 13.08.2016.
 */
public class PerlExternalAnnotationsApplicationRootListener extends VirtualFileAdapter implements ApplicationComponent
{
	private Key<VirtualFilePointer> KEY = Key.create(getClass().getName());
	private final PerlApplicationSettings mySettings = PerlApplicationSettings.getInstance();

	@Override
	public void initComponent()
	{
		VirtualFileManager.getInstance().addVirtualFileListener(this);
	}

	@Override
	public void disposeComponent()
	{
		VirtualFileManager.getInstance().removeVirtualFileListener(this);
	}

	@NotNull
	@Override
	public String getComponentName()
	{
		return getClass().getName();
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
				mySettings.setAnnotationsPath(annotationsRoot.getPath());
			}
			file.putUserData(KEY, null);
		}
	}

	private void handleBeforeOperation(VirtualFileEvent event)
	{
		VirtualFile annotationsRoot = PerlApplicationSettings.getInstance().getAnnotationsRoot();
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

	@Override
	public void fileDeleted(@NotNull VirtualFileEvent event)
	{
		VirtualFile annotationsRoot = mySettings.getAnnotationsRoot();
		if (annotationsRoot == null)
		{
			return;
		}

		VirtualFile deletingFile = event.getFile();
		if (VfsUtilCore.isAncestor(deletingFile, annotationsRoot, false))
		{
			mySettings.setDefaultAnnotationsPath();
			try
			{
				VfsUtil.createDirectories(mySettings.getAnnotationsPath());
			}
			catch (IOException ignore)
			{
			}
			PerlLibUtil.applyClassPathsForAllProjects();
		}
	}

}
