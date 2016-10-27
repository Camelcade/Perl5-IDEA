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

package parser;

import com.intellij.lang.LanguageParserDefinitions;
import com.intellij.psi.LanguageFileViewProviders;
import com.perl5.lang.embedded.EmbeddedPerlLanguage;
import com.perl5.lang.embedded.EmbeddedPerlParserDefinition;
import com.perl5.lang.embedded.psi.EmbeddedPerlFileViewProviderFactory;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.PerlParserDefinition;

/**
 * Created by hurricup on 05.03.2016.
 */
public class EmbeddedPerlParserTest extends PerlParserTestBase
{
	public EmbeddedPerlParserTest()
	{
		super("", "thtml", new EmbeddedPerlParserDefinition());
	}

	@Override
	protected String getTestDataPath()
	{
		return "testData/parser/embedded";
	}

	public void testWithPod()
	{
		doTest();
	}

	public void testParserTest()
	{
		doTest();
	}

	public void testIncompletePerlBlock()
	{
		doTest();
	}

	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		LanguageFileViewProviders.INSTANCE.addExplicitExtension(EmbeddedPerlLanguage.INSTANCE, new EmbeddedPerlFileViewProviderFactory());
	}
}
