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

package com.perl5.lang.tt2.idea.settings;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.perl5.lang.perl.idea.PerlPathMacros;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 05.06.2016.
 */
@State(
		name = "TemplateToolkitSettings",
		storages = {
				@Storage(id = "default", file = StoragePathMacros.PROJECT_FILE),
				@Storage(id = "dir", file = PerlPathMacros.PERL5_PROJECT_SHARED_SETTINGS_FILE, scheme = StorageScheme.DIRECTORY_BASED)
		}
)

public class TemplateToolkitSettings implements PersistentStateComponent<TemplateToolkitSettings>
{
	public String START_TAG = "[%";
	public String END_TAG = "%]";
	public String OUTLINE_TAG = "%%";
	public boolean ENABLE_ANYCASE = false;
	public boolean ENABLE_RELATIVE = false;
	public List<String> TEMPLATE_DIRS = new ArrayList<String>();

	public static TemplateToolkitSettings getInstance(@NotNull Project project)
	{
		TemplateToolkitSettings persisted = ServiceManager.getService(project, TemplateToolkitSettings.class);
		return persisted != null ? persisted : new TemplateToolkitSettings();
	}

	@Nullable
	@Override
	public TemplateToolkitSettings getState()
	{
		return this;
	}

	@Override
	public void loadState(TemplateToolkitSettings state)
	{
		XmlSerializerUtil.copyBean(state, this);
	}
}
