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

package com.perl5.lang.htmlmason.idea.livetemplates;

import com.intellij.lang.Language;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.htmlmason.HTMLMasonLanguage;
import com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes;
import com.perl5.lang.htmlmason.parser.psi.impl.HTMLMasonFileImpl;
import com.perl5.lang.perl.idea.livetemplates.AbstractOutlineLiveTemplateProcessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 07.07.2016.
 */
public class HTMLMasonLiveTemplateProcessor extends AbstractOutlineLiveTemplateProcessor
{
	@Override
	protected boolean isMyFile(PsiFile file)
	{
		return file instanceof HTMLMasonFileImpl;
	}

	@Override
	@NotNull
	protected Language getMyLanguage()
	{
		return HTMLMasonLanguage.INSTANCE;
	}

	@Override
	@NotNull
	protected String getOutlineMarker()
	{
		return "% ";
	}

	@Override
	protected boolean shouldAddMarkerAtLineStartingAtOffset(CharSequence buffer, int offset)
	{
		int bufferEnd = buffer.length();
		if (offset >= bufferEnd)
		{
			return false;
		}

		char currentChar = buffer.charAt(offset);
		if (currentChar == '%')
		{
			return false;
		}

		while (Character.isWhitespace(currentChar))
		{
			if (currentChar == '\n' || ++offset >= bufferEnd)
			{
				return false;
			}
			currentChar = buffer.charAt(offset);
		}

		return true;
	}

	@Nullable
	@Override
	protected PsiElement getOutlineElement(PsiElement firstElement)
	{
		return PsiUtilCore.getElementType(firstElement) == HTMLMasonElementTypes.HTML_MASON_LINE_OPENER ? firstElement : null;
	}
}
