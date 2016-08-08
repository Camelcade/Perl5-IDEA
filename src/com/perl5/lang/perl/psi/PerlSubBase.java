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
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.stubs.StubElement;
import com.perl5.lang.perl.psi.properties.PerlLabelScope;
import com.perl5.lang.perl.psi.properties.PerlNamedElement;
import com.perl5.lang.perl.psi.properties.PerlPackageMember;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by hurricup on 05.06.2015.
 */
public interface PerlSubBase<Stub extends StubElement> extends
		StubBasedPsiElement<Stub>,
		PerlPackageMember,
		PerlNamedElement,
		PerlDeprecatable,
		PerlLabelScope
{
	/**
	 * Returns function name for current function definition
	 *
	 * @return function name or null
	 */
	String getSubName();

	/**
	 * Returns PsiElement containing sub name
	 *
	 * @return name container
	 */
	PsiElement getSubNameElement();

	/**
	 * Checks if sub defined as method
	 *
	 * @return result
	 */
	boolean isMethod();


	/**
	 * Checks if sub defined as static, default implementation returns !isMethod(), but may be different for constants for example
	 *
	 * @return true if sub is static
	 */
	boolean isStatic();

	/**
	 * Checks if current declaration/definition is XSub
	 *
	 * @return true if sub located in deparsed file
	 */
	boolean isXSub();

	/**
	 * Returns stubbed, local or external sub annotations
	 *
	 * @return PerlSubAnnotation object
	 */
	@Nullable
	PerlSubAnnotations getAnnotations();

	/**
	 * Returns local sub annotations if any
	 *
	 * @return annotations object or null
	 */
	@Nullable
	PerlSubAnnotations getLocalAnnotations();


	/**
	 * Returns list of sub annotations elements
	 *
	 * @return list
	 */
	@NotNull
	List<PerlAnnotation> getAnnotationList();

	/**
	 * Used to re-set stubs
	 */
	void subtreeChanged();

	/**
	 * Returns return value for this sub
	 */
	@Nullable
	String getReturns();
}
