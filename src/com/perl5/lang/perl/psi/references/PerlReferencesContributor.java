/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.perl.psi.references;

import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceRegistrar;
import com.perl5.lang.perl.idea.PerlElementPatterns;
import com.perl5.lang.perl.psi.references.providers.PerlSimpleSubReferenceProvider;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 24.01.2016.
 */
public class PerlReferencesContributor extends PsiReferenceContributor implements PerlElementPatterns
{
	@Override
	public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar)
	{
		registrar.registerReferenceProvider(
				EXPORT_ASSIGNED_STRING_CONTENT,
				new PerlSimpleSubReferenceProvider()
		);
	}
}
