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

package packageProcessors;

import base.PerlLightCodeInsightFixtureTestCase;
import com.perl5.lang.perl.extensions.packageprocessor.*;
import com.perl5.lang.perl.psi.PerlNamespaceDefinition;
import com.perl5.lang.perl.psi.PerlUseStatement;
import com.perl5.lang.perl.psi.mro.PerlMroType;

/**
 * Created by hurricup on 01.06.2016.
 */
public class packageProcessorsTest extends PerlLightCodeInsightFixtureTestCase
{
	@Override
	protected String getTestDataPath()
	{
		return "testData/packageProcessors";
	}

	public void testModernPerl()
	{
		initWithFileAsScript("ModernPerl");
		PerlNamespaceDefinition namespaceDefinition = getElementAtCaret(PerlNamespaceDefinition.class);
		assertNotNull(namespaceDefinition);
		assertEquals(PerlMroType.C3, namespaceDefinition.getMroType());
		PerlUseStatement useStatement = getElementAtCaret(PerlUseStatement.class);
		assertNotNull(useStatement);
		PerlPackageProcessor packageProcessor = useStatement.getPackageProcessor();
		assertNotNull(packageProcessor);
		assertInstanceOf(packageProcessor, PerlStrictProvider.class);
		assertInstanceOf(packageProcessor, PerlMroProvider.class);
		assertInstanceOf(packageProcessor, PerlWarningsProvider.class);
		assertInstanceOf(packageProcessor, PerlPackageLoader.class);
	}
}
