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

package com.perl5.lang.perl.idea.configuration.module;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ModuleRootModel;
import com.intellij.openapi.roots.OrderEnumerationHandler;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.PlatformUtils;
import com.perl5.lang.perl.idea.sdk.PerlSdkType;
import com.perl5.lang.perl.xsubs.PerlXSubsState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Created by hurricup on 28.08.2016.
 */
public class PerlIdeaOrderEnumeratorHandler extends OrderEnumerationHandler
{
	private static final PerlIdeaOrderEnumeratorHandler INSTANCE = new PerlIdeaOrderEnumeratorHandler();

	public static class Factory extends OrderEnumerationHandler.Factory
	{
		@Override
		public boolean isApplicable(@NotNull Project project)
		{
			return true;
		}

		@Override
		public boolean isApplicable(@NotNull Module module)
		{

			if (!PlatformUtils.isIntelliJ())
			{
				return false;
			}

			Sdk moduleSdk = ModuleRootManager.getInstance(module).getSdk();
			return moduleSdk != null && moduleSdk.getSdkType() == PerlSdkType.getInstance();
		}

		@Override
		public OrderEnumerationHandler createHandler(@Nullable Module module)
		{
			return INSTANCE;
		}
	}

	@Override
	public boolean addCustomModuleRoots(
			@NotNull OrderRootType type,
			@NotNull ModuleRootModel rootModel,
			@NotNull Collection<String> result,
			boolean includeProduction,
			boolean includeTests)
	{
		if (!type.equals(OrderRootType.CLASSES))
		{
			return false;
		}
		Module module = rootModel.getModule();
		Project project = module.getProject();

		VirtualFile xSubsFile = PerlXSubsState.getXSubsFile(project);
		if (xSubsFile != null)
		{
			result.add(xSubsFile.getUrl());
		}
		return true;
	}
}

