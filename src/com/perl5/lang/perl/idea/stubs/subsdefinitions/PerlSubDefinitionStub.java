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

package com.perl5.lang.perl.idea.stubs.subsdefinitions;

import com.perl5.lang.perl.idea.stubs.PerlSubBaseStub;
import com.perl5.lang.perl.psi.PsiPerlSubDefinition;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;

import java.util.List;

/**
 * Created by hurricup on 25.05.2015.
 */
public interface PerlSubDefinitionStub extends PerlSubBaseStub<PsiPerlSubDefinition>
{
	/**
	 * Returns list of accepted arguments
	 *
	 * @return list of accepted arguments
	 */
	public List<PerlSubArgument> getSubArgumentsList();
}
