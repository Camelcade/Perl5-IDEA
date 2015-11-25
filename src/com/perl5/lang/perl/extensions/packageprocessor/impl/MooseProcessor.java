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

package com.perl5.lang.perl.extensions.packageprocessor.impl;

import com.perl5.lang.perl.extensions.packageprocessor.*;
import com.perl5.lang.perl.internals.PerlStrictMask;
import com.perl5.lang.perl.internals.PerlWarningsMask;
import com.perl5.lang.perl.psi.PerlUseStatement;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * Created by hurricup on 25.11.2015.
 */
public class MooseProcessor extends PerlPackageProcessorBase implements
		PerlStrictProvider,
		PerlWarningsProvider,
		PerlPackageParentsProvider,
		PerlPackageLoader
{
	public static final String MOOSE_OBJECT = "Moose::Object";
	protected static final List<String> LOADED_CLASSES = Collections.singletonList(MOOSE_OBJECT);
	protected static final List<String> PARENT_CLASSES = LOADED_CLASSES;

	@Override
	public List<String> getLoadedPackageNames(PerlUseStatement useStatement)
	{
		return getLoadedClasses();
	}

	@Override
	public void changeParentsList(@NotNull PerlUseStatement useStatement, @NotNull List<String> currentList)
	{
		currentList.clear();
		currentList.addAll(getParentClasses());
	}

	@Override
	public boolean hasPackageFilesOptions()
	{
		return false;
	}

	@Override
	public PerlStrictMask getStrictMask(PerlUseStatement useStatement, PerlStrictMask currentMask)
	{
		// fixme implement modification
		return currentMask.clone();
	}

	@Override
	public PerlWarningsMask getWarningMask(PerlUseStatement useStatement, PerlWarningsMask currentMask)
	{
		// fixme implement modification
		return currentMask.clone();
	}

	public List<String> getLoadedClasses()
	{
		return LOADED_CLASSES;
	}

	public List<String> getParentClasses()
	{
		return PARENT_CLASSES;
	}
}
