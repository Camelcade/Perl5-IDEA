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
 * Created by hurricup on 03.02.2016.
 * http://www.jetbrains.org/intellij/sdk/docs/tutorials/writing_tests_for_plugins/completion_test.html
 */
public class PerlPackageCompletionTest extends PerlCompletionCodeInsightFixtureTestCase
{
	@Override
	protected String getTestDataPath()
	{
		return "testData/completion/namespace";
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
}
