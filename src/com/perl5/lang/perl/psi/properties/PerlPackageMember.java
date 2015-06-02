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

package com.perl5.lang.perl.psi.properties;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.util.PsiTreeUtil;

/**
 * Created by hurricup on 25.05.2015.
 *
 */
public interface PerlPackageMember extends PsiElement
{
	/**
	 * Method for checking explicit package name for current element
	 * @return package name or null if n/a
	 */
	public String getExplicitPackageName();

	/**
	 * Trying to detect package name by traversing parents
	 * @return package name or main if not found
	 */
	public String getContextPackageName();

	/**
	 * Trying to get the package name from explicit specification or by traversing
	 * @return package name for current element
	 */
	public String getPackageName();

}
