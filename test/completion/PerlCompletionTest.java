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
import com.intellij.openapi.vfs.newvfs.impl.VfsRootAccess;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import com.perl5.lang.perl.fileTypes.PerlFileType;

import java.util.Arrays;
import java.util.List;

/**
 * Created by hurricup on 03.02.2016.
 * http://www.jetbrains.org/intellij/sdk/docs/tutorials/writing_tests_for_plugins/completion_test.html
 */
public class PerlCompletionTest extends LightCodeInsightFixtureTestCase
{
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

	public void doTest(String input)
	{
		myFixture.configureByText(PerlFileType.INSTANCE, input);
		myFixture.complete(CompletionType.BASIC, 1);
	}

}
