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

import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.pod.lexer.PodElementTypes;

/**
 * Created by hurricup on 16.04.2016.
 */
public class PodEditorUtils implements PodElementTypes
{
	public static int getRightParentOffset(HighlighterIterator iterator)
	{
		int lastRbraceOffset = -1;
		int bracesCounter = 0;
		for (; !iterator.atEnd(); iterator.advance())
		{
			final IElementType tokenType = iterator.getTokenType();

			if (tokenType == POD_ANGLE_RIGHT)
			{
				if (bracesCounter > 0)
				{
					bracesCounter--;
				}
				else
				{
					return iterator.getStart();
				}
			}
			else if (tokenType == POD_ANGLE_LEFT)
			{
				bracesCounter++;
			}
		}

		return lastRbraceOffset;
	}

}
