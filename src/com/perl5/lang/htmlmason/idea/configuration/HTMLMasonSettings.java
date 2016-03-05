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

package com.perl5.lang.htmlmason.idea.configuration;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.perl5.lang.mason2.idea.configuration.VariableDescription;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 05.03.2016.
 */
@State(
		name = "HTMLMasonSettings",
		storages = {
				@Storage(id = "default", file = StoragePathMacros.PROJECT_FILE),
				@Storage(id = "dir", file = StoragePathMacros.PROJECT_CONFIG_DIR + "/perl5.xml", scheme = StorageScheme.DIRECTORY_BASED)
		}
)

public class HTMLMasonSettings extends AbstractMasonSettings<HTMLMasonSettings>
{
	public HTMLMasonSettings()
	{
		globalVariables.add(new VariableDescription("$m", "HTML::Mason::Request"));
		globalVariables.add(new VariableDescription("$r", "Apache::Request"));
		changeCounter++;
	}

	public static HTMLMasonSettings getInstance(@NotNull Project project)
	{
		HTMLMasonSettings persisted = ServiceManager.getService(project, HTMLMasonSettings.class);
		if (persisted == null)
			persisted = new HTMLMasonSettings();

		return (HTMLMasonSettings) persisted.setProject(project);
	}

	@Nullable
	@Override
	public HTMLMasonSettings getState()
	{
		return this;
	}

	@Override
	public void loadState(HTMLMasonSettings state)
	{
		XmlSerializerUtil.copyBean(state, this);
		changeCounter++;
	}

}
