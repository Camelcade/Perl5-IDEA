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

package com.perl5.lang.perl.parser.builder;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 23.04.2016.
 */
public class PerlStringWrapper
{
	private final IElementType myTargetElementType;

	public PerlStringWrapper(@NotNull IElementType targetElementType)
	{
		myTargetElementType = targetElementType;
	}

	public boolean canProcess()
	{
		return true;
	}

	protected void processed()
	{

	}

	@Nullable
	public PsiBuilder.Marker wrapMarker(@NotNull PsiBuilder.Marker marker)
	{
		if (!canProcess())
		{
			return null;
		}

		PsiBuilder.Marker m = marker.precede();
		m.done(myTargetElementType);

		processed();

		return m;
	}

	public PsiBuilder.Marker wrapNextToken(@NotNull PerlBuilder b)
	{
		if (!canProcess())
		{
			return null;
		}

		PsiBuilder.Marker m = b.mark();
		b.advanceLexer();
		m.done(myTargetElementType);

		processed();

		return m;
	}

}
