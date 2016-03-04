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
public class PerlMiscCompletionTest extends PerlCompletionCodeInsightFixtureTestCase
{
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
}
