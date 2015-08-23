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
import com.perl5.lang.perl.extensions.packageprocessor.IPerlWarningsProvider;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPragmaProcessorBase;
import com.perl5.lang.perl.internals.PerlWarningsMask;
import com.perl5.lang.perl.internals.warnings.PerlWarningTree;
import com.perl5.lang.perl.internals.warnings.PerlWarningTreeLeaf;
import com.perl5.lang.perl.internals.warnings.PerlWarningTreeNode;
import com.perl5.lang.perl.psi.PerlUseStatement;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hurricup on 18.08.2015.
 */
public class WarningsProcessor extends PerlPragmaProcessorBase implements IPerlPackageOptionsProvider, IPerlWarningsProvider
{
	protected static final HashMap<String, String> OPTIONS = new HashMap<String, String>();
	protected static final HashMap<String, String> OPTIONS_BUNDLES = new HashMap<String, String>();

	static
	{
		OPTIONS.put("FATAL", "FATALITY!");

		for (Map.Entry<String, PerlWarningTreeLeaf> option : PerlWarningTree.LEAF_OPTIONS.entrySet())
			OPTIONS.put(option.getKey(), option.getValue().getMinVersion().toString());
	}

	static
	{
		for (Map.Entry<String, PerlWarningTreeNode> option : PerlWarningTree.NODE_OPTIONS.entrySet())
		{
			List<String> subElements = new ArrayList<String>();
			for (PerlWarningTreeLeaf leaf : option.getValue().collectChildLeafs())
				subElements.add(leaf.getStringIdentifier() + "(" + leaf.getMinVersion().toString() + ")");

			OPTIONS_BUNDLES.put(option.getKey(),
					option.getValue().getMinVersion().toString()
							+ ", "
							+ StringUtils.join(subElements, " ")
			);
		}
	}

	@Nullable
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
	public PerlWarningsMask getWarningMask(PerlUseStatement useStatement, PerlWarningsMask currentMask)
	{
		// fixme implement modification
		return currentMask == null ? new PerlWarningsMask() : currentMask.clone();
	}
}
