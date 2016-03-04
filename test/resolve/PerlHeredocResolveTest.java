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
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlHeredocOpener;
import com.perl5.lang.perl.psi.PerlHeredocTerminatorElement;
import com.perl5.lang.perl.psi.PerlRecursiveVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 02.03.2016.
 */
public class PerlHeredocResolveTest extends PerlLightCodeInsightFixtureTestCase implements PerlElementTypes
{
	@Override
	protected String getTestDataPath()
	{
		return "testData/resolve";
	}

	public void testMarkers()
	{
		myFixture.configureByFile("heredoc_sequential.pl");
		final List<PerlHeredocOpener> openers = new ArrayList<PerlHeredocOpener>();
		final List<PerlHeredocTerminatorElement> terminators = new ArrayList<PerlHeredocTerminatorElement>();

		myFixture.getFile().accept(new PerlRecursiveVisitor()
		{
			@Override
			public void visitElement(PsiElement element)
			{
				if (element instanceof PerlHeredocOpener)
				{
					openers.add((PerlHeredocOpener) element);
				}
				else if (element instanceof PerlHeredocTerminatorElement)
				{
					terminators.add((PerlHeredocTerminatorElement) element);
				}
				super.visitElement(element);
			}
		});

		assertTrue(openers.size() > 0);
		assertEquals(openers.size(), terminators.size());

		for (int i = 0; i < terminators.size(); i++)
		{
			PsiReference reference = terminators.get(i).getReference();
			assertNotNull(reference);
			PsiElement opener = reference.resolve();
			assertNotNull(opener);
			assertEquals(opener, openers.get(i));
		}

	}
}
