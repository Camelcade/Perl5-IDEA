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

import com.intellij.psi.LanguageFileViewProviders;
import com.perl5.lang.mason2.Mason2TemplatingLanguage;
import com.perl5.lang.mason2.Mason2TemplatingParserDefinition;
import com.perl5.lang.mason2.psi.Mason2TemplatingFileViewProviderFactory;

/**
 * Created by hurricup on 04.03.2016.
 */
public class Mason2TemplatingParserTest extends PerlParserTestBase
{
	public Mason2TemplatingParserTest()
	{
		super("", "mc", new Mason2TemplatingParserDefinition());
	}

	@Override
	protected String getTestDataPath()
	{
		return "testData/parser/mason2/template";
	}


	public void testComponent()
	{
		doTest("test_component", true);
	}

	public void testIssue1077()
	{
		doTest("issue_1077", true);
	}

	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		LanguageFileViewProviders.INSTANCE.addExplicitExtension(Mason2TemplatingLanguage.INSTANCE, new Mason2TemplatingFileViewProviderFactory());
	}

}
