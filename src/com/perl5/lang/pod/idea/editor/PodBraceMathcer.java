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

package com.perl5.lang.pod.idea.editor;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.pod.lexer.PodElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 03.04.2016.
 */
public class PodBraceMathcer implements PairedBraceMatcher, PodElementTypes
{
	private static final BracePair[] PAIRS = new BracePair[]{
			new BracePair(POD_BEGIN, POD_END, true),
			new BracePair(POD_OVER, POD_BACK, true),
			new BracePair(POD_ANGLE_LEFT, POD_ANGLE_RIGHT, false),
			new BracePair(POD_PAREN_LEFT, POD_PAREN_RIGHT, false),
			new BracePair(POD_BRACE_LEFT, POD_BRACE_RIGHT, false),
			new BracePair(POD_BRACKET_LEFT, POD_BRACKET_RIGHT, false),
	};

	@Override
	public BracePair[] getPairs()
	{
		return PAIRS;
	}

	@Override
	public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType lbraceType, @Nullable IElementType contextType)
	{
		return true;
	}

	@Override
	public int getCodeConstructStart(PsiFile file, int openingBraceOffset)
	{
		return openingBraceOffset;
	}


}
