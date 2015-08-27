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

import com.perl5.lang.perl.extensions.packageprocessor.IPerlPackageOptionsProvider;
import com.perl5.lang.perl.psi.PerlUseStatement;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hurricup on 18.08.2015.
 */
public class ParentProcessor extends BaseProcessor implements IPerlPackageOptionsProvider
{
	protected static final Map<String, String> OPTIONS = new HashMap<String, String>();

	static
	{
		OPTIONS.put("-norequire", "suppresses attempt to require a package file");
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
		List<String> parents = super.getParentsList(useStatement);
		parents.removeAll(getOptions().keySet());
		return parents;
	}
}
