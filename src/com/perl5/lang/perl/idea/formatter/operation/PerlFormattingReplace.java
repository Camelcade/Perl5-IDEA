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

package com.perl5.lang.perl.idea.formatter.operation;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 15.11.2015.
 */
public class PerlFormattingReplace implements PerlFormattingOperation
{
	private final PsiElement mySourceFromElement;
	private final PsiElement mySourceToElement;
	private final PsiElement myTargetFromElement;
	private final PsiElement myTargetToElement;

	public PerlFormattingReplace(@NotNull PsiElement sourceElement, @NotNull PsiElement targetElement)
	{
		this(sourceElement, sourceElement, targetElement, targetElement);
	}

	public PerlFormattingReplace(@NotNull PsiElement mySourceFromElement, @NotNull PsiElement mySourceToElement, @NotNull PsiElement myTargetFromElement, @NotNull PsiElement myTargetToElement)
	{
		assert mySourceFromElement.getParent() == mySourceToElement.getParent() : "Source PSI elements range must be under same parent";
		assert mySourceFromElement.getStartOffsetInParent() <= mySourceToElement.getStartOffsetInParent() : "Source from element must come first";
		this.mySourceFromElement = mySourceFromElement;
		this.mySourceToElement = mySourceToElement;

		assert myTargetFromElement.getParent() == myTargetToElement.getParent() : "Target PSI elements range must be under same parent";
		assert myTargetFromElement.getStartOffsetInParent() <= myTargetToElement.getStartOffsetInParent() : "Targtet from element must come first";
		this.myTargetFromElement = myTargetFromElement;
		this.myTargetToElement = myTargetToElement;
	}

	@Override
	public int apply()
	{
		if (!mySourceFromElement.isValid() || !mySourceToElement.isValid()) // something changed above
		{
			return 0;
		}

		assert myTargetFromElement.isValid() : "Start target element for removal became invalid";
		assert myTargetToElement.isValid() : "End target element for removal became invalid";

		int result = 0;
		PsiElement currentSourceElement = mySourceFromElement;
		PsiElement currentTargetElement = myTargetFromElement;

		PsiElement nextSourceElement = currentSourceElement.getNextSibling();
		result += currentTargetElement.getNode().getTextLength() - currentSourceElement.getNode().getTextLength();

		PsiElement additionAnchor = currentSourceElement.replace(currentTargetElement);

		if (mySourceFromElement != mySourceToElement)
		{
			// removing the rest of sources
			currentSourceElement = nextSourceElement;
			while (true)
			{
				nextSourceElement = currentSourceElement.getNextSibling();
				result -= currentSourceElement.getNode().getTextLength();
				boolean isLast = currentSourceElement == mySourceToElement;
				currentSourceElement.delete();
				if (isLast || nextSourceElement == null)
				{
					break;
				}
				currentSourceElement = nextSourceElement;
				assert currentSourceElement.isValid();
			}
		}

		if (myTargetFromElement != myTargetToElement)
		{
			PsiElement targetContainer = additionAnchor.getParent();
			assert targetContainer != null;

			// adding the rest of targets
			while (currentTargetElement != null && currentTargetElement != myTargetToElement)
			{
				currentTargetElement = currentTargetElement.getNextSibling();
				assert currentTargetElement != null && currentTargetElement.isValid();
				result += currentTargetElement.getNode().getTextLength();
				additionAnchor = targetContainer.addAfter(currentTargetElement, additionAnchor);
			}
		}

		return result;
	}
}
