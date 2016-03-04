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

import java.util.Arrays;
import java.util.List;

/**
 * Created by hurricup on 04.03.2016.
 */
public class PerlLabelCompletionTest extends PerlCompletionCodeInsightFixtureTestCase
{
	@Override
	protected String getTestDataPath()
	{
		return "testData/completion/label";
	}

	public void testNextCompletion()
	{
		initWithFileAsScript("next_labels");
		List<String> strings = myFixture.getLookupElementStrings();
		assertTrue(strings.containsAll(Arrays.asList("LABEL1", "LABEL2", "LABEL3")));
		assertFalse(strings.contains("LABEL4"));
		assertFalse(strings.contains("LABEL5"));
		assertFalse(strings.contains("LABEL6"));
		assertFalse(strings.contains("LABEL7"));
		assertFalse(strings.contains("LABEL8"));
	}

	public void testGotoCompletion()
	{
		initWithFileAsScript("goto_labels");
		List<String> strings = myFixture.getLookupElementStrings();
		assertTrue(strings.containsAll(Arrays.asList("LABEL1", "LABEL2", "LABEL3", "LABEL4", "LABEL5", "LABEL6", "LABEL8")));
		assertFalse(strings.contains("LABEL7"));
	}
}
