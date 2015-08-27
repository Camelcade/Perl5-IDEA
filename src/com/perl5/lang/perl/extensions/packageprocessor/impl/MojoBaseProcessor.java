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
import com.perl5.lang.perl.internals.PerlFeaturesTable;
import com.perl5.lang.perl.internals.PerlStrictMask;
import com.perl5.lang.perl.internals.PerlWarningsMask;
import com.perl5.lang.perl.psi.PerlUseStatement;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Created by hurricup on 25.08.2015.
 */
public class MojoBaseProcessor extends PerlPackageProcessorBase implements
		IPerlStrictProvider,
		IPerlUtfProvider,
		IPerlWarningsProvider,
		IPerlFeaturesProvider,
		IPerlPackageOptionsProvider,
		IPerlPackageParentsProvider,
		IPerlPackageLoader
{
	protected static final Map<String, String> OPTIONS = new HashMap<String, String>();

	static
	{
		OPTIONS.put("-strict", "strict,warnings,utf8,v5.10,IO::Handle");
		OPTIONS.put("-base", "strict,warnings,utf8,v5.10,IO::Handle,acts as parent");
	}

	@Override
	public PerlFeaturesTable getFeaturesTable(PerlUseStatement useStatement, PerlFeaturesTable currentFeaturesTable)
	{
		return currentFeaturesTable.clone();
	}

	@Override
	public List<String> getLoadedPackageNames(PerlUseStatement useStatement)
	{
		List<String> loadedPackages = new ArrayList<String>(Arrays.asList("IO::Handle"));
		List<String> parensts = getParentsList(useStatement);
		if (parensts.size() > 0 && !parensts.get(0).equals("Mojo::Base"))
			loadedPackages.add(parensts.get(0));
		return loadedPackages;
	}

	@NotNull
	@Override
	public Map<String, String> getOptions()
	{
		return OPTIONS;
	}

	@NotNull
	@Override
	public Map<String, String> getOptionsBundles()
	{
		return Collections.emptyMap();
	}

	@NotNull
	@Override
	public List<String> getParentsList(PerlUseStatement useStatement)
	{
		List<String> parents = new ArrayList<String>();

		List<String> allOptions = useStatement.getImportParameters();
		if (allOptions.contains("-base"))
			parents.add("Mojo::Base");
		else
		{
			allOptions.removeAll(getOptions().keySet());
			if (allOptions.size() > 0)
				parents.add(allOptions.get(0));
		}

		return parents;
	}

	@Override
	public boolean hasPackageFilesOptions()
	{
		return true;
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
}
