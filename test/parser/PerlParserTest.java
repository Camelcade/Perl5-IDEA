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

/**
 * Created by hurricup on 31.01.2016.
 */
public class PerlParserTest extends PerlParserTestBase
{
	public static final String DATA_PATH = "testData/parser/perlParsingSamples";

	@Override
	protected String getTestDataPath()
	{
		return DATA_PATH;
	}

	public void testInterpolation()
	{
		doTest("interpolation");
	}

	public void testConstant()
	{
		doTest("constant");
	}

	public void testLazyParsing()
	{
		doTest("lazy_parsing");
	}

	public void testParserTest()
	{
		doTest("parser_test_set");
	}

	public void testTrickySyntax()
	{
		doTest("tricky_syntax");
	}

	public void testVariables()
	{
		doTest("variables");
	}

	public void testMethodSignaturesSimple()
	{
		doTest("method_signatures_simple");
	}

	public void testUtfIdentifiers()
	{
		doTest("utfidentifiers");
	}

	public void testSwitch()
	{
		doTest("perl_switch");
	}

	public void testInterpolatedHashArrayElements()
	{
		doTest("interpolated_hash_array_elements");
	}

	public void testImplicitRegex()
	{
		doTest("implicit_regex");
	}

	public void testCamelcade94()
	{
		doTest("camelcade94", false);
	}

	public void testIssue855()
	{
		doTest("issue855", false);
	}

}
