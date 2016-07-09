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
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.psi.PerlFileViewProviderFactory;

/**
 * Created by hurricup on 31.01.2016.
 */
public class PerlParserTest extends PerlParserTestBase
{
	@Override
	protected String getTestDataPath()
	{
		return "testData/parser/perl";
	}

	public void testInterpolation()
	{
		doTest("interpolation");
	}

	public void testCompositeOps()
	{
		doTest("composite_ops");
	}

	public void testCompositeOpsSpaces()
	{
		doTest("composite_ops_spaces", false);
	}

	public void testAnnotations()
	{
		doTest("annotation", false);
	}

	public void testConstant()
	{
		doTest("constant");
	}

	public void testBareUtfString()
	{
		doTest("bare_utf_string");
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
		doTest("issue855");
	}

	public void testIssue867()
	{
		doTest("issue867");
	}

	public void testregex_n_modifier()
	{
		doTest("regex_n_modifier");
	}

	public void testutf8_package_name()
	{
		doTest("utf8_package_name");
	}

	public void testhex_bin_numbers_parsing()
	{
		doTest("hex_bin_numbers_parsing");
	}

	public void test28_backref_style_heredoc()
	{
		doTest("28_backref_style_heredoc");
	}

	public void testheredoc_sequential()
	{
		doTest("heredoc_sequential");
	}

	public void testlabels_parsing()
	{
		doTest("labels_parsing");
	}

	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		LanguageFileViewProviders.INSTANCE.addExplicitExtension(PerlLanguage.INSTANCE, new PerlFileViewProviderFactory());
	}

}
