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
import com.intellij.openapi.roots.LibraryOrderEntry;
import com.perl5.PerlBundle;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by hurricup on 28.08.2016.
 */
public class PerlDependencyWrapperLibrary extends PerlDependencyWrapper<LibraryOrderEntry>
{
	public PerlDependencyWrapperLibrary(LibraryOrderEntry libraryOrderEntry)
	{
		super(libraryOrderEntry);
	}

	@Override
	public String getType()
	{
		return PerlBundle.message("perl.settings.dependency.type.library");
	}

	@Nullable
	@Override
	Icon getIcon()
	{
		return AllIcons.Modules.Library;
	}
}
