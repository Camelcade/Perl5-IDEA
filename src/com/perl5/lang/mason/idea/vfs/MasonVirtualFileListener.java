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

package com.perl5.lang.mason.idea.vfs;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.*;
import com.intellij.util.indexing.FileBasedIndex;
import com.perl5.lang.mason.MasonUtils;
import com.perl5.lang.mason.filetypes.MasonPurePerlComponentFileType;
import com.perl5.lang.mason.idea.configuration.MasonSettings;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

/**
 * Created by hurricup on 09.01.2016.
 */
public class MasonVirtualFileListener extends VirtualFileAdapter
{
	public static final Key<Boolean> FORCE_REINDEX = new Key<Boolean>("Force re-indexing");

	private final Project myProject;

	public MasonVirtualFileListener(Project project)
	{
		myProject = project;
	}

	private static boolean containsAtLeastOneFile(VirtualFile root, List<VirtualFile> files)
	{
		for (VirtualFile file : files)
		{
			if (VfsUtil.isAncestor(root, file, false))
			{
				return true;
			}

		}
		return false;
	}

	/**
	 * Fired when a virtual file is renamed from within IDEA, or its writable status is changed.
	 * For files renamed externally, {@link #fileCreated} and {@link #fileDeleted} events will be fired.
	 *
	 * @param event the event object containing information about the change.
	 */
	@Override
	public void propertyChanged(@NotNull VirtualFilePropertyEvent event)
	{
		super.propertyChanged(event);
	}

	/**
	 * Fired when a virtual file is moved from within IDEA.
	 *
	 * @param event the event object containing information about the change.
	 */
	@Override
	public void fileMoved(@NotNull VirtualFileMoveEvent event)
	{
		MasonSettings masonSettings = MasonSettings.getInstance(getProject());
		List<VirtualFile> componentsRoots = masonSettings.getComponentsRootsVirtualFiles();
		if (componentsRoots.size() == 0)
			return;

		VirtualFile movedFile = event.getFile();

		Set<VirtualFile> rootsSet = new THashSet<VirtualFile>(componentsRoots);
		if (movedFile.isDirectory())
		{
			if (movedFile.getUserData(FORCE_REINDEX) != null ||
					VfsUtil.isUnder(movedFile, rootsSet) ||        // moved to component root
					containsAtLeastOneFile(movedFile, componentsRoots)
					)
			{
				MasonUtils.reindexProjectFile(getProject(), movedFile);
			}
		}
		else if (movedFile.getFileType() instanceof MasonPurePerlComponentFileType)    // Mason file has been moved
		{
			if (movedFile.getUserData(FORCE_REINDEX) != null ||
					VfsUtil.isUnder(movedFile, rootsSet)
					)
			{
				FileBasedIndex.getInstance().requestReindex(movedFile);
			}
		}
	}

	/**
	 * Fired before the change of a name or writable status of a file is processed.
	 *
	 * @param event the event object containing information about the change.
	 */
	@Override
	public void beforePropertyChange(@NotNull VirtualFilePropertyEvent event)
	{
		super.beforePropertyChange(event);
	}

	/**
	 * Fired before the movement of a file is processed.
	 *
	 * @param event the event object containing information about the change.
	 */
	@Override
	public void beforeFileMovement(@NotNull VirtualFileMoveEvent event)
	{
		MasonSettings masonSettings = MasonSettings.getInstance(getProject());
		List<VirtualFile> componentsRoots = masonSettings.getComponentsRootsVirtualFiles();
		if (componentsRoots.size() == 0)
			return;

		VirtualFile movedFile = event.getFile();

		Set<VirtualFile> rootsSet = new THashSet<VirtualFile>(componentsRoots);
		if (movedFile.isDirectory())
		{
			if (VfsUtil.isUnder(movedFile, rootsSet) ||    // moved from component root
					containsAtLeastOneFile(movedFile, componentsRoots) // contains component root
					)
			{
				movedFile.putUserData(FORCE_REINDEX, true);
			}
		}
		else if (movedFile.getFileType() instanceof MasonPurePerlComponentFileType)    // Mason file has been moved
		{
			if (VfsUtil.isUnder(movedFile, rootsSet))
			{
				movedFile.putUserData(FORCE_REINDEX, true);
			}
		}
	}

	public Project getProject()
	{
		return myProject;
	}
}
