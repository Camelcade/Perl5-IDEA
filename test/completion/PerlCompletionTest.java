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

import base.PerlLightCodeInsightFixtureTestCase;
import com.intellij.codeInsight.completion.CompletionType;
import com.perl5.lang.perl.fileTypes.PerlFileType;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hurricup on 03.02.2016.
 * http://www.jetbrains.org/intellij/sdk/docs/tutorials/writing_tests_for_plugins/completion_test.html
 */
public class PerlCompletionTest extends PerlLightCodeInsightFixtureTestCase
{
	@Override
	protected String getTestDataPath()
	{
		return "testData/completion";
	}

	public void testRefTypes()
	{
		initWithTextAsScript("my $var = '<caret>'");
		List<String> strings = myFixture.getLookupElementStrings();
		assertTrue(strings.containsAll(Arrays.asList("ARRAY", "CODE", "FORMAT", "GLOB", "HASH", "IO", "LVALUE", "REF", "Regexp", "SCALAR", "VSTRING")));
	}

	public void testHashIndexBare()
	{
		initWithTextAsScript("$$a{testindex}; $b->{<caret>}");
		List<String> strings = myFixture.getLookupElementStrings();
		assertTrue(strings.containsAll(Arrays.asList("testindex")));
	}

	public void testPackageDefinition()
	{
		initWithFileAsPackage("package_definition");
		List<String> strings = myFixture.getLookupElementStrings();
		assertTrue(strings.containsAll(Arrays.asList("package_definition")));
	}

	public void testPackageUse()
	{
		initWithFileAsPackage("package_use");
		checkPackageCompletions();
	}

	public void testPackageNo()
	{
		initWithFileAsPackage("package_no");
		checkPackageCompletions();
	}

	public void testPackageRequire()
	{
		initWithFileAsPackage("package_require");
		checkPackageCompletions();
	}

	public void checkPackageCompletions()
	{
		List<String> strings = myFixture.getLookupElementStrings();
		assertTrue(strings.containsAll(Arrays.asList("v5.10", "B", "UNIVERSAL", "Scalar::Util", "strict", "warnings")));
	}

	public void testHeredocOpenerBare()
	{
		doTestHeredocOpenerFile("heredoc_marker_completion_bare");
	}

	public void testHeredocOpenerBackref()
	{
		doTestHeredocOpenerFile("heredoc_marker_completion_backref");
	}

	public void testHeredocOpenerQQ()
	{
		doTestHeredocOpenerFile("heredoc_marker_completion_qq");
	}

	public void testHeredocOpenerSQ()
	{
		doTestHeredocOpenerFile("heredoc_marker_completion_sq");
	}

	public void testHeredocOpenerXQ()
	{
		doTestHeredocOpenerFile("heredoc_marker_completion_xq");
	}

	public void doTestHeredocOpenerFile(String filename)
	{
		initWithFileAsScript(filename);
		List<String> strings = myFixture.getLookupElementStrings();
		assertTrue(strings.contains("MYSUPERMARKER"));
		assertTrue(strings.contains("HTML"));
	}

	public void doTest(String input)
	{
		myFixture.configureByText(PerlFileType.INSTANCE, input);
		myFixture.complete(CompletionType.BASIC, 1);
	}

	@Override
	public void initWithFileContent(String filename, String extension, String content) throws IOException
	{
		super.initWithFileContent(filename, extension, content);
		myFixture.complete(CompletionType.BASIC, 1);
	}

}
