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

package com.perl5.lang.mason.idea.configuration;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hurricup on 03.01.2016.
 */
@State(
		name = "Perl5MasonSettings",
		storages = {
				@Storage(file = StoragePathMacros.PROJECT_FILE),
				@Storage(file = StoragePathMacros.PROJECT_CONFIG_DIR + "/perl5.xml", scheme = StorageScheme.DIRECTORY_BASED)
		}
)


public class MasonPerlSettings implements PersistentStateComponent<MasonPerlSettings>
{
	public List<String> componentRoots = new ArrayList<String>();
	public List<String> autobaseNames = new ArrayList<String>(Arrays.asList("Base.mp", "Base.mc"));

	public static MasonPerlSettings getInstance(@NotNull Project project)
	{
		MasonPerlSettings persisted = ServiceManager.getService(project, MasonPerlSettings.class);
		return persisted != null ? persisted : new MasonPerlSettings();
	}

	@Nullable
	@Override
	public MasonPerlSettings getState()
	{
		return this;
	}

	@Override
	public void loadState(MasonPerlSettings state)
	{
		XmlSerializerUtil.copyBean(state, this);
	}
}
