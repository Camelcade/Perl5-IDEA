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

package com.perl5.lang.perl.idea.configuration.settings;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by hurricup on 30.08.2015.
 */
@State(
		name = "Perl5Settings",
		storages = {
				@Storage(id = "default", file = StoragePathMacros.PROJECT_FILE),
				@Storage(id = "dir", file = StoragePathMacros.PROJECT_CONFIG_DIR + "/perl5.xml", scheme = StorageScheme.DIRECTORY_BASED)
		}
)

public class Perl5Settings implements PersistentStateComponent<Perl5Settings>
{
	public List<String> libRootUrls = new ArrayList<String>();
	public List<String> selfNames = new ArrayList<String>(Arrays.asList("self", "this", "class", "proto"));
	public String perlPath = "";
	public boolean SIMPLE_MAIN_RESOLUTION = true;
	public boolean AUTOMATIC_HEREDOC_INJECTIONS = true;
	public boolean ALLOW_INJECTIONS_WITH_INTERPOLATION = false;

	@Transient
	private Set<String> SELF_NAMES_SET = null;

	public static Perl5Settings getInstance(@NotNull Project project)
	{
		Perl5Settings persisted = ServiceManager.getService(project, Perl5Settings.class);
		return persisted != null ? persisted : new Perl5Settings();
	}

	@Nullable
	@Override
	public Perl5Settings getState()
	{
		return this;
	}

	@Override
	public void loadState(Perl5Settings state)
	{
		XmlSerializerUtil.copyBean(state, this);
	}

	public void settingsUpdated()
	{
		SELF_NAMES_SET = null;
	}

	public boolean isSelfName(String name)
	{
		if (SELF_NAMES_SET == null)
		{
			SELF_NAMES_SET = new THashSet<String>(selfNames);
		}
		return SELF_NAMES_SET.contains(name);
	}

}
