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

package com.perl5.lang.perl.idea.run;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationTypeBase;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import com.perl5.PerlIcons;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 16-Sep-15
 */
public class PerlConfigurationType extends ConfigurationTypeBase
{
	public PerlConfigurationType()
	{
		super("#PerlConfigurationType", "Perl", "", PerlIcons.PERL_LANGUAGE);

		addFactory(new ConfigurationFactory(this)
		{
			@Override
			public RunConfiguration createTemplateConfiguration(Project project)
			{
				return new PerlConfiguration(project, this, "Unnamed");
			}
		});
	}

	@NotNull
	public static PerlConfigurationType getInstance()
	{
		return CONFIGURATION_TYPE_EP.findExtension(PerlConfigurationType.class);
	}
}
