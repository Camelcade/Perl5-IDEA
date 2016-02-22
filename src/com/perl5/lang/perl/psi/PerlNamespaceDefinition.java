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

import com.intellij.psi.StubBasedPsiElement;
import com.perl5.lang.perl.idea.PerlElementPatterns;
import com.perl5.lang.perl.idea.stubs.namespaces.PerlNamespaceDefinitionStub;
import com.perl5.lang.perl.psi.properties.PerlNamedElement;
import com.perl5.lang.perl.psi.properties.PerlNamespaceElementContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by hurricup on 31.05.2015.
 */
public interface PerlNamespaceDefinition extends
		StubBasedPsiElement<PerlNamespaceDefinitionStub>,
		PerlNamespaceElementContainer,
		PerlNamedElement,
		PerlNamespaceContainer,
		PerlDeprecatable,
		PerlElementPatterns
{
	/**
	 * Retuns block or namespace content with statements
	 *
	 * @return PsiElement
	 */
	@Nullable
	PsiPerlBlock getBlock();

	/**
	 * Populates result with linear ISA according to the namespace MRO
	 *
	 * @param recursionMap recursion map
	 * @param result       array to populate
	 */
	void getLinearISA(HashSet<String> recursionMap, ArrayList<String> result);

	/**
	 * Retuns list of exports from this module
	 *
	 * @return list of @EXPORTs
	 */
	@NotNull
	List<String> getEXPORT();

	/**
	 * .
	 * Returns list of optional exports from this module
	 *
	 * @return list of @EXPORT_OKs
	 */
	@NotNull
	List<String> getEXPORT_OK();

	/**
	 * Returns map of exported tags
	 *
	 * @return map of %EXPORT_TAGS
	 */
	@NotNull
	Map<String, List<String>> getEXPORT_TAGS();

	/**
	 * Returns deprecated annotation if any
	 *
	 * @return deprecated annotation
	 */
	@Nullable
	PsiPerlAnnotationDeprecated getAnnotationDeprecated();


	/**
	 * Returns list of parent namespace names from stub or psi
	 *
	 * @return list of names
	 */
	@NotNull
	List<String> getParentNamepsacesNames();

	@Nullable
	String getName();
}
