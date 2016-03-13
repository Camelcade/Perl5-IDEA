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

package resolve;

import base.PerlLightCodeInsightFixtureTestCase;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;

/**
 * Created by hurricup on 13.03.2016.
 */
public abstract class PerlResolveTestCase extends PerlLightCodeInsightFixtureTestCase
{
	public void doTest(String filename, boolean success, Class clazz)
	{
		initWithFile(filename);
		PsiElement element = getElementAtCaret(clazz);
		assertNotNull(element);
		PsiReference reference = element.getReference();
		assertNotNull(reference);
		if (success)
		{
			assertNotNull(reference.resolve());
			validateTarget(element, reference.resolve());
		}
		else
		{
			assertNull(reference.resolve());
		}
	}

	public void initWithFile(String filename)
	{
		initWithFileAsScript(filename);
	}

	public void validateTarget(PsiElement sourceElement, PsiElement targetElement)
	{
	}
}
