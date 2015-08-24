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

import com.perl5.lang.perl.extensions.packageprocessor.IPerlFeaturesProvider;
import com.perl5.lang.perl.extensions.packageprocessor.IPerlPackageOptionsProvider;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPragmaProcessorBase;
import com.perl5.lang.perl.internals.PerlFeaturesTable;
import com.perl5.lang.perl.psi.PerlUseStatement;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by evstigneev on 19.08.2015.
 */
public class FeatureProcessor extends PerlPragmaProcessorBase implements IPerlPackageOptionsProvider, IPerlFeaturesProvider
{
	protected static final HashMap<String, String> OPTIONS = new HashMap<String, String>();
	protected static final HashMap<String, String> OPTIONS_BUNDLES = new HashMap<String, String>();

	static
	{
		OPTIONS.putAll(PerlFeaturesTable.AVAILABLE_FEATURES);

	}

	static
	{
		for (Map.Entry<String, List<String>> option : PerlFeaturesTable.AVAILABLE_FEATURES_BUNDLES.entrySet())
			OPTIONS_BUNDLES.put(":" + option.getKey(), StringUtils.join(option.getValue(), " "));
	}


	@NotNull
	@Override
	public HashMap<String, String> getOptions()
	{
		return OPTIONS;
	}

	@Nullable
	@Override
	public HashMap<String, String> getOptionsBundles()
	{
		return OPTIONS_BUNDLES;
	}

	@Override
	public PerlFeaturesTable getFeaturesTable(PerlUseStatement useStatement, PerlFeaturesTable currentFeaturesTable)
	{
		// fixme implement modification
		return currentFeaturesTable == null ? new PerlFeaturesTable() : currentFeaturesTable.clone();
	}
}
