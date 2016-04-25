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

import com.perl5.lang.perl.idea.configuration.settings.Perl5Settings;

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
		checkPackageFileCompletionWithArray("package_definition", "package_definition");
	}

	public void testPackageUse()
	{
		checkPackageAndVersionsCompletions("package_use");
	}

	public void testPackageNo()
	{
		checkPackageAndVersionsCompletions("package_no");
	}

	public void testPackageRequire()
	{
		checkPackageAndVersionsCompletions("package_require");
	}

	public void testLocal()
	{
		checkClassCompletions("package_local");
	}

	public void testMy()
	{
		checkClassCompletions("package_my");
	}

	public void testOur()
	{
		checkClassCompletions("package_our");
	}

	public void testState()
	{
		checkClassCompletions("package_state");
	}

	public void testTryCatch()
	{
		Perl5Settings.getInstance(getProject()).PERL_TRY_CATCH_ENABLED = true;
		checkClassCompletions("try_catch");
	}

	public void checkPackageAndVersionsCompletions(String fileName)
	{
		checkPackageFileCompletionWithArray(fileName, "v5.10", "B", "UNIVERSAL", "Scalar::Util", "strict", "warnings");
	}

	public void checkClassCompletions(String fileName)
	{
		checkPackageFileCompletionWithArray(fileName, "B", "UNIVERSAL", "Scalar::Util", "strict", "warnings");
	}

}
