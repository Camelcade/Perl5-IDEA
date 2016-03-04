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

import java.util.List;

/**
 * Created by hurricup on 04.03.2016.
 */
public class PerlHeredocCompletionTest extends PerlCompletionCodeInsightFixtureTestCase
{
	@Override
	protected String getTestDataPath()
	{
		return "testData/completion/heredoc_opener";
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

}
