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

import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.psi.mro.PerlMro;
import com.perl5.lang.perl.psi.mro.PerlMroType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by hurricup on 09.08.2015.
 * This interface shows that element wraps the namespace.
 */
public interface PerlNamespaceContainer extends PsiElement
{
	/**
	 * Returns package name
	 *
	 * @return canonical package name
	 */
	String getPackageName();

	/**
	 * Returns parent namespaces definitions
	 *
	 * @return list of packages specified in parent
	 */
	List<PerlNamespaceDefinition> getParentNamespaceDefinitions();

	/**
	 * Returns child namespace definitions
	 *
	 * @return list of packages derived from current
	 */
	@NotNull
	Collection<PerlNamespaceDefinition> getChildNamespaceDefinitions();

	/**
	 * Get mro type for current package
	 *
	 * @return mro type
	 */
	PerlMroType getMroType();

	/**
	 * Returns MRO instance for current package
	 *
	 * @return mro class instance
	 */
	PerlMro getMro();

	/**
	 * Returns map of imported subs as map package => qw(subs)
	 *
	 * @return map of imports
	 */
	public Map<String, Set<String>> getImportedSubsNames();

	/**
	 * Returns map of imported scalars as map package => qw(scalars)
	 *
	 * @return map of imports
	 */
	public Map<String, Set<String>> getImportedScalarNames();

	/**
	 * Returns map of imported arrays as map package => qw(arrays)
	 *
	 * @return map of imports
	 */
	public Map<String, Set<String>> getImportedArrayNames();

	/**
	 * Returns map of imported hashes as map package => qw(hashes)
	 *
	 * @return map of imports
	 */
	public Map<String, Set<String>> getImportedHashNames();
}
