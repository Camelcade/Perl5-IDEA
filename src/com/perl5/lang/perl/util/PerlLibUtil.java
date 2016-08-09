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

package com.perl5.lang.perl.util;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.*;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.PlatformUtils;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.idea.configuration.settings.PerlLocalSettings;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.idea.modules.JpsPerlLibrarySourceRootType;
import com.perl5.lang.perl.idea.sdk.PerlSdkType;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by hurricup on 09.08.2016.
 */
public class PerlLibUtil
{
	public static void applyClassPaths(final Project project)
	{
		if (project == null)
		{
			return;
		}

		ApplicationManager.getApplication().runWriteAction(new Runnable()
		{
			@Override
			public void run()
			{
				for (Module module : ModuleManager.getInstance(project).getModules())
				{
					// fixme we could fix modules here. Any for micro-ide and perl for perl
					ModifiableRootModel modifiableModel = ModuleRootManager.getInstance(module).getModifiableModel();
					applyClassPaths(modifiableModel);
					modifiableModel.commit();
				}
			}
		});
	}

	/**
	 * Scans ModifiableRootModel and update shared settings with new list
	 *
	 * @param model module modifiable model
	 */
	public static void updatePerlLibsForModel(ModifiableRootModel model)
	{
		List<String> libRoots = new ArrayList<String>();

		for (VirtualFile entry : model.getSourceRoots(JpsPerlLibrarySourceRootType.INSTANCE))
		{
			libRoots.add(entry.getUrl());
		}

		PerlSharedSettings.getInstance(model.getProject()).setLibRootUrlsForModule(model.getModule(), libRoots);

		applyClassPaths(model);
	}

	public static void applyClassPaths(ModifiableRootModel rootModel)
	{
		for (OrderEntry entry : rootModel.getOrderEntries())
		{
//			System.err.println("Checking " + entry + " of " + entry.getClass());
			if (entry instanceof LibraryOrderEntry)
			{
//				System.err.println("Removing " + entry);
				rootModel.removeOrderEntry(entry);
			}
		}

		LibraryTable table = rootModel.getModuleLibraryTable();

		for (VirtualFile virtualFile : rootModel.getSourceRoots(JpsPerlLibrarySourceRootType.INSTANCE))
		{
			addClassRootLibrary(table, virtualFile, true);
		}

		// add external annotations coming with plugin
		VirtualFile pluginAnnotationsRootVirtualFile = PerlAnnotationsUtil.getPluginAnnotationsRoot();
		if (pluginAnnotationsRootVirtualFile != null)
		{
			addClassRootLibrary(table, pluginAnnotationsRootVirtualFile, false);
		}

		// add application-level external annotations
		VirtualFile applicationAnnotationsRoot = PerlAnnotationsUtil.getApplicationAnnotationsRoot();
		if (applicationAnnotationsRoot != null)
		{
			addClassRootLibrary(table, applicationAnnotationsRoot, false);
		}

		// Add project-level external annotations
		VirtualFile projectAnnotationsRoot = PerlAnnotationsUtil.getProjectAnnotationsRoot(rootModel.getProject());
		if (projectAnnotationsRoot != null)
		{
			addClassRootLibrary(table, projectAnnotationsRoot, false);
		}

		if (!PlatformUtils.isIntelliJ())
		{
			// add perl @inc to the end of libs
			PerlLocalSettings settings = PerlLocalSettings.getInstance(rootModel.getProject());
			if (!settings.PERL_PATH.isEmpty())
			{
				for (String incPath : PerlSdkType.getInstance().getINCPaths(settings.PERL_PATH))
				{
					VirtualFile incFile = LocalFileSystem.getInstance().findFileByIoFile(new File(incPath));
					if (incFile != null)
					{
						addClassRootLibrary(table, incFile, true);
					}
				}
			}
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

	private static void addClassRootLibrary(LibraryTable table, VirtualFile virtualFile, boolean addSource)
	{
		Library tableLibrary = table.createLibrary();
		Library.ModifiableModel modifiableModel = tableLibrary.getModifiableModel();
		modifiableModel.addRoot(virtualFile, OrderRootType.CLASSES);
		if (addSource)
		{
			modifiableModel.addRoot(virtualFile, OrderRootType.SOURCES);
		}
		modifiableModel.commit();
	}
}
