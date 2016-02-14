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

import com.intellij.core.CoreApplicationEnvironment;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.testFramework.ParsingTestCase;
import com.perl5.lang.perl.PerlParserDefinition;
import com.perl5.lang.perl.idea.application.PerlParserExtensions;

import java.io.File;

/**
 * Created by hurricup on 31.01.2016.
 */
public class PerlParserTest extends ParsingTestCase
{
	public static final String DATA_PATH = "testData/parser/perlParsingSamples";
	String myFileName;

	public PerlParserTest()
	{
		super("", "pl", new PerlParserDefinition());
	}

	@Override
	protected String getTestDataPath()
	{
		System.out.println(new File("").getAbsolutePath());
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

	public void testCamelcade94()
	{
		doTest("camelcade94", false);
	}

	public void doTest(String filename)
	{
		doTest(filename, true);
	}

	public void doTest(String filename, boolean checkErrors)
	{
		myFileName = filename;
		doTest(true);

		if (checkErrors)
		assertFalse(
				"PsiFile contains error elements",
				toParseTreeText(myFile, skipSpaces(), includeRanges()).contains("PsiErrorElement")
		);
	}


	@Override
	protected String getTestName(boolean lowercaseFirstLetter)
	{
		return myFileName;
	}

	@Override
	protected boolean skipSpaces()
	{
		return true;
	}

	@Override
	public void setUp() throws Exception
	{
		super.setUp();

		CoreApplicationEnvironment.registerExtensionPointAndExtensions(new File("resources"), "plugin.xml", Extensions.getRootArea());
		new PerlParserExtensions().initComponent();
	}
}
