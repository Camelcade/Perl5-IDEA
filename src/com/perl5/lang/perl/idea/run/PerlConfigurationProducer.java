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

import com.intellij.execution.Location;
import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.actions.RunConfigurationProducer;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.perl.fileTypes.PerlFileTypeScript;
import com.perl5.lang.perl.fileTypes.PerlFileTypeTest;
import com.perl5.lang.perl.idea.run.debugger.PerlRemoteFileSystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author VISTALL
 * @since 19-Sep-15
 */
public class PerlConfigurationProducer extends RunConfigurationProducer<PerlConfiguration>
{
	public PerlConfigurationProducer()
	{
		super(PerlConfigurationType.getInstance().getConfigurationFactories()[0]);
	}

	public static boolean isExecutableFile(@NotNull VirtualFile virtualFile)
	{
		if (virtualFile instanceof PerlRemoteFileSystem.PerlRemoteVirtualFile)
		{
			return false;
		}

		FileType fileType = virtualFile.getFileType();
		return fileType == PerlFileTypeScript.INSTANCE || fileType == PerlFileTypeTest.INSTANCE;
	}

	@Nullable
	public VirtualFile findPerlFile(ConfigurationContext configurationContext)
	{
		Location location = configurationContext.getLocation();
		VirtualFile virtualFile = location == null ? null : location.getVirtualFile();
		return virtualFile != null && isExecutableFile(virtualFile) ? virtualFile : null;
	}

	@Override
	public boolean isConfigurationFromContext(PerlConfiguration runConfiguration, ConfigurationContext configurationContext)
	{
		VirtualFile perlFile = findPerlFile(configurationContext);
		return perlFile != null && Comparing.equal(runConfiguration.getScriptFile(), perlFile);
	}

	@Override
	protected boolean setupConfigurationFromContext(PerlConfiguration runConfiguration, ConfigurationContext configurationContext, Ref ref)
	{
		VirtualFile perlFile = findPerlFile(configurationContext);
		if (perlFile != null)
		{
			runConfiguration.setScriptPath(perlFile.getPath());
			runConfiguration.setConsoleCharset(perlFile.getCharset().displayName());
			runConfiguration.setGeneratedName();
			return true;
		}
		return false;
	}
}
