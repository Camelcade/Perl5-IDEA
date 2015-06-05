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

package com.perl5.lang.perl.psi.stubs;

import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.StubElement;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;

/**
 * Created by hurricup on 05.06.2015.
 */
public interface PerlSubBaseStub<Stub extends PsiElement> extends StubElement<Stub>
{
	/**
	 * Returns package name for current function
	 * @return canonical package name from declaration or context
	 */
	public String getPackageName();

	/**
	 * Returns function name for current function definition
	 * @return function name or null
	 */
	public String getSubName();

	/**
	 * Checks if sub defined as method (accepts one of the PerlThisNames as first argument)
	 * @return result
	 */
	public boolean isMethod();

	/**
	 * Checks PSI tree before a sub definition for annotations and builds annotations object
	 * @return PerlSubAnnotation object
	 */
	public PerlSubAnnotations getSubAnnotations();

	/**
	 * Returns canonical name PackageName::SubName
	 * @return name
	 */
	public String getCanonicalName();
}
