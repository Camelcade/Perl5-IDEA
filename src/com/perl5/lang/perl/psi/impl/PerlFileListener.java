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

package com.perl5.lang.perl.psi.impl;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.*;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 29.05.2015.
 */
public class PerlFileListener implements VirtualFileListener
{
	Project myProject;
	ProjectFileIndex myProjectFileIndex;

	public PerlFileListener(Project project)
	{
		myProject = project;
		myProjectFileIndex = ProjectRootManager.getInstance(project).getFileIndex();
	}

	@Override
	public void propertyChanged(VirtualFilePropertyEvent event)
	{

	}

	@Override
	public void contentsChanged(VirtualFileEvent event)
	{

	}

	@Override
	public void fileCreated(VirtualFileEvent event)
	{

	}

	@Override
	public void fileDeleted(VirtualFileEvent event)
	{

	}

	@Override
	public void fileMoved(@NotNull VirtualFileMoveEvent event)
	{
		if( myProjectFileIndex.isInSource(event.getNewParent()) )
		{
			VirtualFile movedFile = event.getFile();

			if( "pm".equals(movedFile.getExtension()) )
			{
				PerlPackageUtil.adjustMovedFileNamespaces(myProject, event);
			}
		}
	}

	@Override
	public void fileCopied(VirtualFileCopyEvent event)
	{

	}

	@Override
	public void beforePropertyChange(VirtualFilePropertyEvent event)
	{

	}

	@Override
	public void beforeContentsChange(VirtualFileEvent event)
	{

	}

	@Override
	public void beforeFileDeletion(VirtualFileEvent event)
	{

	}

	@Override
	public void beforeFileMovement(VirtualFileMoveEvent event)
	{

	}
}
