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

package com.perl5.lang.perl.psi;

import com.perl5.lang.perl.psi.impl.PerlFileElement;
import com.perl5.lang.perl.psi.properties.PerlNamedElement;

import java.util.List;

/**
 * Created by hurricup on 31.05.2015.
 */
public interface PerlNamespaceElement extends PerlNamedElement
{
	/**
	 * Checks if package is from Core list
	 * @return result
	 */
	public boolean isBuiltin();

	/**
	 * Checks if package is pragma
	 * @return result
	 */
	public boolean isPragma();

	/**
	 * Checks if package is deprecated
	 * @return result
	 */
	public boolean isDeprecated();

	/**
	 * Returns list of definitions of current namespace
	 * @return list of PerlNameSpaceDefitions
	 */
	public List<PerlNamespaceDefinition> getNamespaceDefinitions();

	/**
	 * Returns list of files suitable for this namespace, works only if namespace is in use or require statement
	 * @return list of PerlNameSpaceDefitions
	 */
	public List<PerlFileElement> getNamespaceFiles();

	/**
	 * Returns canonical namespace name
	 * @return canonical name
	 */
	public String getCanonicalName();
}
