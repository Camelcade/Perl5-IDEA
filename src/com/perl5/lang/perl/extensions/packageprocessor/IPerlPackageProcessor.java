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

import com.perl5.lang.perl.psi.PerlUseStatement;

import java.util.List;

/**
 * Created by hurricup on 18.08.2015.
 * implement this interface to provide a package processor
 */
public interface IPerlPackageProcessor
{
	/**
	 * Returns true if package is pragma, false otherwise
	 *
	 * @return result
	 */
	public boolean isPragma();

	/**
	 * Can do additional work for import
	 *
	 * @param useStatement psi element of use statement
	 */
	public void use(PerlUseStatement useStatement);

	/**
	 * Can do additional work for unimport
	 *
	 * @param noStatement psi element of no statement
	 */
	public void no(PerlUseStatement noStatement);

	/**
	 * Retuns list of subs imported by current statement; Null means @EXPORT, empty list means suppressing @EXPORT
	 *
	 * @param useStatement use statement psi element
	 * @return list of subs imported by current statement with current options
	 */
	public List<String> getImports(PerlUseStatement useStatement);

}
