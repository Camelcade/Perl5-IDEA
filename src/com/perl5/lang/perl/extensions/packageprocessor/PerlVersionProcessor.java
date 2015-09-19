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

package com.perl5.lang.perl.extensions.packageprocessor;

import com.perl5.lang.perl.internals.PerlFeaturesTable;
import com.perl5.lang.perl.internals.PerlVersion;
import com.perl5.lang.perl.psi.PerlUseStatement;
import com.perl5.lang.perl.psi.PerlVersionElement;

import java.util.Collections;
import java.util.List;

/**
 * Created by hurricup on 09.09.2015.
 */
public class PerlVersionProcessor implements PerlPackageProcessor, IPerlFeaturesProvider
{
	protected static final PerlVersionProcessor INSTANCE = new PerlVersionProcessor();

	protected PerlVersionProcessor()
	{
	}

	public static PerlVersionProcessor getProcessor(PerlUseStatement useStatement)
	{
		PerlVersionElement versionElement = useStatement.getVersionElement();
		assert versionElement != null;

		if (versionElement.getPerlVersion().lesserThan(PerlVersion.V5_12))
			return INSTANCE;

		return PerlVersionProcessor512.INSTANCE;
	}

	@Override
	public boolean isPragma()
	{
		return false;
	}

	@Override
	public void use(PerlUseStatement useStatement)
	{

	}

	@Override
	public void no(PerlUseStatement noStatement)
	{

	}

	@Override
	public List<String> getImportedSubs(PerlUseStatement useStatement)
	{
		return Collections.emptyList();
	}

	@Override
	public PerlFeaturesTable getFeaturesTable(PerlUseStatement useStatement, PerlFeaturesTable currentFeaturesTable)
	{
		// fixme implement modification
		return currentFeaturesTable == null ? new PerlFeaturesTable() : currentFeaturesTable.clone();
	}
}
