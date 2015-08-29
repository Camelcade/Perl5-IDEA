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

package com.perl5.lang.perl.idea.modules;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.LibraryOrderEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.OrderEntry;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.roots.ui.configuration.CommonContentEntriesEditor;
import com.intellij.openapi.roots.ui.configuration.ModuleConfigurationState;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.jps.model.java.JavaSourceRootType;

import java.util.Comparator;

/**
 * Created by hurricup on 07.06.2015.
 */
public class PerlContentEntriesEditor extends CommonContentEntriesEditor
{
	public PerlContentEntriesEditor(String moduleName, ModuleConfigurationState state)
	{
		super(moduleName, state, JavaSourceRootType.SOURCE, JavaSourceRootType.TEST_SOURCE, JpsPerlLibrarySourceRootType.INSTANCE);
	}

	@Override
	public void apply() throws ConfigurationException
	{
		ModifiableRootModel rootModel = getModel();
		for (OrderEntry entry : rootModel.getOrderEntries())
			if (entry instanceof LibraryOrderEntry)
				rootModel.removeOrderEntry(entry);

		LibraryTable table = rootModel.getModuleLibraryTable();

		for (VirtualFile entry : rootModel.getSourceRoots(JpsPerlLibrarySourceRootType.INSTANCE))
		{
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
}
