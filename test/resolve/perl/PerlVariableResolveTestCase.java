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

package resolve.perl;

import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PerlVariableDeclarationWrapper;
import com.perl5.lang.perl.psi.PerlVariableNameElement;

/**
 * Created by hurricup on 13.03.2016.
 */
public abstract class PerlVariableResolveTestCase extends PerlResolveTestCase
{
	public void doTest(String filename, boolean success)
	{
		doTest(filename, success, PerlVariableNameElement.class);
	}

	@Override
	public void validateTarget(PsiElement sourceElement, PsiElement targetElement)
	{
		assertTrue(targetElement instanceof PerlVariableDeclarationWrapper);
		PerlVariable targetVariable = ((PerlVariableDeclarationWrapper) targetElement).getVariable();
		assertEquals(targetVariable.getName(), sourceElement.getText());
		assertEquals(targetVariable.getActualType(), ((PerlVariable) sourceElement.getParent()).getActualType());
	}
}
