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
import com.perl5.lang.perl.extensions.parser.PerlParserExtension;
import com.perl5.lang.perl.idea.application.PerlParserExtensions;

import java.util.regex.Pattern;

/**
 * Created by hurricup on 31.01.2016.
 */
public class PerlParserTest extends ParsingTestCase
{
	protected final static String DATA_PATH = "test/parser/perlParsingSamples";
	protected final static Pattern filesPattern = Pattern.compile(".+\\.(pl|pm|cgi|t)");
	String myFileName;

	public PerlParserTest()
	{
		super("", "", new PerlParserDefinition());
	}

	public void testInterpolation()
	{
		doTest("interpolation.pl");
	}

	public void testConstant()
	{
		doTest("constant.pl");
	}

	public void testLazyParsing()
	{
		doTest("lazy_parsing.pl");
	}

	public void testParserTest()
	{
		doTest("parser_test_set.pl");
	}

	public void testTrickySyntax()
	{
		doTest("tricky_syntax.pl");
	}

	public void testVariables()
	{
		doTest("variables.pl");
	}

	public void doTest(String filename)
	{
		myFileName = filename;
		doTest(true);
	}


	@Override
	protected String getTestDataPath()
	{
		return DATA_PATH;
	}

	@Override
	protected String getTestName(boolean lowercaseFirstLetter)
	{
		return myFileName;
	}

	@Override
	protected boolean skipSpaces()
	{
		return false;
	}

	@Override
	public void setUp() throws Exception
	{
		super.setUp();

		CoreApplicationEnvironment.registerExtensionPoint(Extensions.getRootArea(), "com.perl5.parserExtension", PerlParserExtension.class);
		new PerlParserExtensions().initComponent();
	}
}
