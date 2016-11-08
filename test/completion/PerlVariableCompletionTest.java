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

package completion;

/**
 * Created by hurricup on 25.04.2016.
 */
public class PerlVariableCompletionTest extends PerlCompletionCodeInsightFixtureTestCase
{
	@Override
	protected String getTestDataPath()
	{
		return "testData/completion/variable";
	}

	public void testLexicalMy()
	{
		initWithFileAsScript("lexical_my");
		assertContainsBuiltInVars();
		assertLookupIs("scalarname", "arrayname", "hashname");
	}

	public void testLexicalState()
	{
		initWithFileAsScript("lexical_state");
		assertContainsBuiltInVars();
		assertLookupIs("scalarname", "arrayname", "hashname");
	}

	public void testLexicalOur()
	{
		initWithFileAsScript("lexical_our");
		assertContainsBuiltInVars();
		assertLookupIs("scalarname", "arrayname", "hashname");
	}

	public void testSameStatementSimple()
	{
		initWithFileAsScript("same_statement_simple");
		assertContainsBuiltInVars();
		assertLookupIs("normvar");
		assertNotContainsLookupElements("testvar");
	}

	public void testSameStatementMap()
	{
		initWithFileAsScript("same_statement_map");
		assertContainsBuiltInVars();
		assertLookupIs("normvar");
		assertNotContainsLookupElements("testvar");
	}

	protected void assertContainsBuiltInVars()
	{
		assertLookupIs("a", "b", "ARGV", "INC");
	}
}
