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

import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.openapi.vfs.newvfs.impl.VfsRootAccess;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import com.perl5.lang.perl.fileTypes.PerlFileType;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hurricup on 03.02.2016.
 * http://www.jetbrains.org/intellij/sdk/docs/tutorials/writing_tests_for_plugins/completion_test.html
 */
public class PerlCompletionTest extends LightCodeInsightFixtureTestCase
{
	public static final String DATA_PATH = "testData/completion";

	@Override
	protected String getTestDataPath()
	{
		return DATA_PATH;
	}

	@Override
	protected void setUp() throws Exception
	{
		VfsRootAccess.SHOULD_PERFORM_ACCESS_CHECK = false; // TODO: a workaround for v15
		super.setUp();
	}

	public void testRefTypes()
	{
		doTest("my $var = '<caret>'");
		List<String> strings = myFixture.getLookupElementStrings();
		assertTrue(strings.containsAll(Arrays.asList("ARRAY", "CODE", "FORMAT", "GLOB", "HASH", "IO", "LVALUE", "REF", "Regexp", "SCALAR", "VSTRING")));
	}

	public void testHashIndexBare()
	{
		doTest("$$a{testindex}; $b->{<caret>}");
		List<String> strings = myFixture.getLookupElementStrings();
		assertTrue(strings.containsAll(Arrays.asList("testindex")));
	}

	public void testPackageDefinition()
	{
		doTestFilePackage("package_definition");
		List<String> strings = myFixture.getLookupElementStrings();
		assertTrue(strings.containsAll(Arrays.asList("package_definition")));
	}

	public void testPackageUse()
	{
		doTestFilePackage("package_use");
		checkPackageCompletions();
	}

	public void testPackageNo()
	{
		doTestFilePackage("package_no");
		checkPackageCompletions();
	}

	public void testPackageRequire()
	{
		doTestFilePackage("package_require");
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
		doTestFileScript(filename);
		List<String> strings = myFixture.getLookupElementStrings();
		assertTrue(strings.contains("MYSUPERMARKER"));
		assertTrue(strings.contains("HTML"));
	}

	public void doTest(String input)
	{
		myFixture.configureByText(PerlFileType.INSTANCE, input);
		myFixture.complete(CompletionType.BASIC, 1);
	}

	public void doTestFileScript(String filename)
	{
		try
		{
			doTestFile(filename, "pl");
		} catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public void doTestFilePackage(String filename)
	{
		try
		{
			doTestFile(filename, "pm");
		} catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public void doTestFile(String filename, String extension) throws IOException
	{
		String content = FileUtil.loadFile(new File(getTestDataPath(), filename + ".code"), CharsetToolkit.UTF8, true).trim();
		myFixture.configureByText(filename + "." + extension, content);
		myFixture.complete(CompletionType.BASIC, 1);
	}

}
