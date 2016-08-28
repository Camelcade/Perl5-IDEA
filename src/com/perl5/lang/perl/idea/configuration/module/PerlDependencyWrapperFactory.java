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

import com.intellij.openapi.roots.*;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 28.08.2016.
 */
public class PerlDependencyWrapperFactory
{
	@NotNull
	public static PerlDependencyWrapper getWrapper(OrderEntry orderEntry)
	{
		if (orderEntry instanceof LibraryOrderEntry)
		{
			return new PerlDependencyWrapperLibrary((LibraryOrderEntry) orderEntry);
		}
		else if (orderEntry instanceof ModuleOrderEntry)
		{
			return new PerlDependencyWrapperModule((ModuleOrderEntry) orderEntry);
		}
		else if (orderEntry instanceof JdkOrderEntry)
		{
			return new PerlDependencyWrapperSdk((JdkOrderEntry) orderEntry);
		}
		else if (orderEntry instanceof ModuleSourceOrderEntry)
		{
			return new PerlDependencyWrapperModuleSource((ModuleSourceOrderEntry) orderEntry);
		}
		// fixme place for the EP to handle custom order entries

		return new PerlDependencyWrapperUnknown(orderEntry);
	}
}
