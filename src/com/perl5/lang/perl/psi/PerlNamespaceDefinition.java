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
import com.perl5.lang.perl.psi.mixins.PerlNamespaceDefinitionImplMixin;
import com.perl5.lang.perl.psi.properties.PerlNamedElement;
import com.perl5.lang.perl.psi.properties.PerlNamespaceElementContainer;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceDefinitionStub;
import com.perl5.lang.perl.psi.utils.PerlNamespaceAnnotations;
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
	 * Collects, cached and returns exporter arrays and hashes
	 *
	 * @return exporter info
	 */
	@NotNull
	PerlNamespaceDefinitionImplMixin.ExporterInfo getExporterInfo();


	/**
	 * Returns list of parent namespace names from stub or psi
	 *
	 * @return list of names
	 */
	@NotNull
	List<String> getParentNamepsacesNames();

	@Nullable
	String getName();

	/**
	 * Returns stubbed, local or external namespace annotations
	 *
	 * @return annotations or null
	 */
	@Nullable
	PerlNamespaceAnnotations getAnnotations();

	/**
	 * Returns local namespace annotations if any
	 *
	 * @return annotations object or null
	 */
	@Nullable
	PerlNamespaceAnnotations getLocalAnnotations();


	/**
	 * Returns local or stubbed annotations objec
	 *
	 * @return annotations or null
	 */
	@Nullable
	PerlNamespaceAnnotations getStubbedOrLocalAnnotations();

	/**
	 * Returns external namespace annotations if any
	 *
	 * @return annotations object or null
	 */
	@Nullable
	PerlNamespaceAnnotations getExternalAnnotations();


}
