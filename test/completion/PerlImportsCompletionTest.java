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

import com.perl5.lang.perl.extensions.packageprocessor.impl.PerlDancer2DSL;
import com.perl5.lang.perl.extensions.packageprocessor.impl.PerlDancerDSL;

/**
 * Created by hurricup on 04.06.2016.
 */
public class PerlImportsCompletionTest extends PerlCompletionCodeInsightFixtureTestCase
{
	@Override
	protected String getTestDataPath()
	{
		return "testData/completion/imports";
	}

	public void testSubs()
	{
		assertPackageFileCompletionContains("subs", "somecode", "someothercode");
		assertNotContainsLookupElements("somescalar", "somearray", "somehash");
	}

	public void testHashes()
	{
		assertPackageFileCompletionContains("hashes", "somehash");
		assertNotContainsLookupElements("somesub", "someothersub", "somescalar", "somearray");
	}

	public void testArrays()
	{
		assertPackageFileCompletionContains("arrays", "somearray", "somehash");
		assertNotContainsLookupElements("somesub", "someothersub", "somescalar");
	}

	public void testScalars()
	{
		assertPackageFileCompletionContains("scalars", "somearray", "somehash", "somescalar");
		assertNotContainsLookupElements("somesub", "someothersub");
	}

	public void testDancer()
	{
		assertPackageFileCompletionContains("dancer", PerlDancerDSL.DSL_KEYWORDS);
	}

	public void testDancer2()
	{
		assertPackageFileCompletionContains("dancer2", PerlDancer2DSL.DSL_KEYWORDS);
	}

	public void testPosix()
	{
		assertPackageFileCompletionContains("posix", "FLT_MIN_10_EXP", "FP_INFINITE", "INFINITY");
	}

	public void testPosixVar()
	{
		assertPackageFileCompletionContains("posix_var", "SIGRT");
	}

	public void testPosixOk()
	{
		assertPackageFileCompletionContains("posix_ok", "isgreaterequal");
	}

}
