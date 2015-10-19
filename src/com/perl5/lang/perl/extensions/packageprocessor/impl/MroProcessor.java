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

import com.perl5.lang.perl.extensions.packageprocessor.IPerlMroProvider;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPragmaProcessorBase;
import com.perl5.lang.perl.psi.PerlUseStatement;
import com.perl5.lang.perl.psi.mro.PerlMroType;

import java.util.List;

/**
 * Created by evstigneev on 21.08.2015.
 */
public class MroProcessor extends PerlPragmaProcessorBase implements IPerlMroProvider
{
	@Override
	public PerlMroType getMroType(PerlUseStatement useStatement)
	{
		List<String> parameters = useStatement.getImportParameters();

		if (parameters != null && parameters.size() > 0 && "c3".equals(parameters.get(0)))
			return PerlMroType.C3;
		return PerlMroType.DFS;
	}
}
