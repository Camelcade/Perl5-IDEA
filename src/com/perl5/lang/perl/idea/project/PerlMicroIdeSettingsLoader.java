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

package com.perl5.lang.perl.idea.project;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.*;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.PlatformUtils;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.idea.modules.JpsPerlLibrarySourceRootType;
import com.perl5.lang.perl.idea.settings.Perl5Settings;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by hurricup on 30.08.2015.
 */
public class PerlMicroIdeSettingsLoader implements ProjectComponent
{
	protected Project myProject;
	protected Perl5Settings perl5Settings;

	public PerlMicroIdeSettingsLoader(Project project)
	{
		myProject = project;
		perl5Settings = Perl5Settings.getInstance(project);
	}

	// fixme make this non-static and request it from manager
	public static void applyClassPaths(ModifiableRootModel rootModel)
	{
		for (OrderEntry entry : rootModel.getOrderEntries())
		{
//			System.err.println("Checking " + entry);
			if (entry instanceof LibraryOrderEntry)
			{
//				System.err.println("Removing " + entry);
				rootModel.removeOrderEntry(entry);
			}
		}

		LibraryTable table = rootModel.getModuleLibraryTable();

		for (VirtualFile entry : rootModel.getSourceRoots(JpsPerlLibrarySourceRootType.INSTANCE))
		{
//			System.err.println("Adding " + entry);
			Library tableLibrary = table.createLibrary();
			Library.ModifiableModel modifiableModel = tableLibrary.getModifiableModel();
			modifiableModel.addRoot(entry, OrderRootType.CLASSES);
			modifiableModel.commit();
		}

		OrderEntry[] entries = rootModel.getOrderEntries();

		ContainerUtil.sort(entries, new Comparator<OrderEntry>()
		{
			@Override
			public int compare(OrderEntry orderEntry, OrderEntry t1)
			{
				int i1 = orderEntry instanceof LibraryOrderEntry ? 1 : 0;
				int i2 = t1 instanceof LibraryOrderEntry ? 1 : 0;
				return i2 - i1;
			}
		});
		rootModel.rearrangeOrderEntries(entries);
	}

	public void initComponent()
	{
		// TODO: insert component initialization logic here
	}

	public void disposeComponent()
	{
		// TODO: insert component disposal logic here
	}

	@NotNull
	public String getComponentName()
	{
		return "PerlMicroIdeSettingsLoader";
	}

	public void projectOpened()
	{
		// called when project is opened
		if (!PlatformUtils.isIntelliJ())
		{
			ApplicationManager.getApplication().runWriteAction(new Runnable()
			{
				@Override
				public void run()
				{
					ModifiableRootModel rootModel = ModuleRootManager.getInstance(ModuleManager.getInstance(myProject).getModules()[0]).getModifiableModel();
					ContentEntry entry = rootModel.getContentEntries()[0];
					Set<String> libPaths = new HashSet<String>(perl5Settings.getLibRoots());


					for (SourceFolder folder : entry.getSourceFolders())
					{
//						System.err.println("Before " + folder + " of " + folder.getRootType());
						if (libPaths.contains(folder.getUrl()))
							entry.removeSourceFolder(folder);
					}

					for (String path : libPaths)
						entry.addSourceFolder(path, JpsPerlLibrarySourceRootType.INSTANCE);

//					for(SourceFolder folder: entry.getSourceFolders())
//						System.err.println("After " + folder + " of " + folder.getRootType());

					applyClassPaths(rootModel);
					rootModel.commit();
				}
			});
		}

	}

	public void projectClosed()
	{
		// called when project is being closed
	}
}
