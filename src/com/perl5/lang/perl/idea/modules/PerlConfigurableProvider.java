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

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurableProvider;
import com.intellij.openapi.project.Project;
import com.intellij.util.PlatformUtils;
import com.perl5.lang.perl.idea.small.PerlPlatformContentEntriesConfigurable;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 29.08.2015.
 */
public class PerlConfigurableProvider extends ConfigurableProvider
{
	Project project;

	public PerlConfigurableProvider(Project project)
	{
		this.project = project;
	}

	@Nullable
	@Override
	public Configurable createConfigurable()
	{
		return new PerlPlatformContentEntriesConfigurable(project);
	}

	@Override
	public boolean canCreateConfigurable()
	{
		return !PlatformUtils.isIntelliJ();
	}
}
