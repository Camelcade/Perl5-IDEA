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

import com.intellij.icons.AllIcons;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkType;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.JdkOrderEntry;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by hurricup on 28.08.2016.
 */
public class PerlDependencyWrapperSdk extends PerlDependencyWrapper<JdkOrderEntry>
{
	public PerlDependencyWrapperSdk(JdkOrderEntry orderEntry)
	{
		super(orderEntry);
	}

	@Override
	public String getType()
	{
		return "SDK";
	}

	@Nullable
	@Override
	Icon getIcon()
	{
		Sdk jdk = myOrderEntry.getJdk();
		if (jdk != null)
		{
			SdkTypeId sdkType = jdk.getSdkType();
			if (sdkType instanceof SdkType)
			{
				return ((SdkType) sdkType).getIcon();
			}
		}
		return AllIcons.Nodes.UnknownJdk;
	}

	@Override
	boolean isConfigurable()
	{
		return false;
	}
}
