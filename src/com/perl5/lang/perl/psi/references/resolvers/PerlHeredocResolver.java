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

package com.perl5.lang.perl.psi.references.resolvers;

import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.perl5.lang.perl.psi.PerlHeredocOpener;
import com.perl5.lang.perl.psi.PerlHeredocTerminatorElement;
import com.perl5.lang.perl.psi.references.PerlHeredocReference;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 02.03.2016.
 */
public class PerlHeredocResolver implements ResolveCache.AbstractResolver<PerlHeredocReference, PerlHeredocOpener>
{
	@Override
	public PerlHeredocOpener resolve(@NotNull PerlHeredocReference perlHeredocReference, boolean incompleteCode)
	{
		PerlHeredocTerminatorElement element = perlHeredocReference.getElement();
		return PerlPsiUtil.findHeredocOpenerByOffset(element.getContainingFile(), element.getText(), element.getTextOffset());
	}
}
