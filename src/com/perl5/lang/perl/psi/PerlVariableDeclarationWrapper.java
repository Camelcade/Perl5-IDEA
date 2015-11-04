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
import com.perl5.lang.perl.idea.stubs.variables.PerlVariableStub;
import com.perl5.lang.perl.psi.properties.PerlNamedElement;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 29.09.2015.
 */
public interface PerlVariableDeclarationWrapper extends StubBasedPsiElement<PerlVariableStub>, PerlNamedElement
{
	/**
	 * Returns declared variable object
	 *
	 * @return variable object
	 */
	PerlVariable getVariable();

	/**
	 * Returns declaration type if variable is in declaration
	 *
	 * @return type string or null
	 */
	@Nullable
	String getDeclaredType();

	/**
	 * Trying to get the package name from explicit specification or by traversing
	 *
	 * @return package name for current element
	 */
	public String getPackageName();

	/**
	 * Guessing actual variable type from context
	 *
	 * @return variable type
	 */
	PerlVariableType getActualType();

	/**
	 * Checks if this declaration is lexical. IMPORTANT: builds PSI
	 *
	 * @return checking result
	 */
	boolean isLexicalDeclaration();

	/**
	 * Checks if this declaration is local. IMPORTANT: builds PSI
	 *
	 * @return checking result
	 */
	boolean isLocalDeclaration();

	/**
	 * Checks if this declaration is global. IMPORTANT: builds PSI
	 *
	 * @return checking result
	 */
	boolean isGlobalDeclaration();
}
