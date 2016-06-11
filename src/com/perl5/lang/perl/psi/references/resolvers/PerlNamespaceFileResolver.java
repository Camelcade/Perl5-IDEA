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

package com.perl5.lang.perl.psi.references.resolvers;

import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiFile;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.references.PerlNamespaceFileReference;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 27.09.2015.
 */
public class PerlNamespaceFileResolver implements ResolveCache.PolyVariantResolver<PerlNamespaceFileReference>
{
	@NotNull
	@Override
	public ResolveResult[] resolve(@NotNull PerlNamespaceFileReference perlNamespaceFileReference, boolean incompleteCode)
	{
		PerlNamespaceElement myElement = perlNamespaceFileReference.getElement();
		PsiFile file = myElement.getContainingFile();
		PsiFile targetFile = null;

		targetFile = PerlPackageUtil.resolvePackageNameToPsi(file, perlNamespaceFileReference.getPackageName());

		return targetFile == null
				? ResolveResult.EMPTY_ARRAY
				: new ResolveResult[]{new PsiElementResolveResult(targetFile)};
	}
}
