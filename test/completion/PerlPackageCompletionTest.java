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

import com.perl5.lang.perl.fileTypes.PerlFileTypePackage;

import java.util.Arrays;
import java.util.List;

/**
 * Created by hurricup on 03.02.2016.
 * http://www.jetbrains.org/intellij/sdk/docs/tutorials/writing_tests_for_plugins/completion_test.html
 */
@SuppressWarnings("unchecked")
public class PerlPackageCompletionTest extends PerlCompletionCodeInsightFixtureTestCase
{
	@Override
	protected String getTestDataPath()
	{
		return "testData/completion/namespace";
	}

	@Override
	public String getFileExtension()
	{
		return PerlFileTypePackage.EXTENSION;
	}

	public void testPackageDefinition()
	{
		doTest("packageDefinition");
	}

	public void testPackageUse()
	{
		doTestPackageAndVersions();
	}

	public void testPackageNo()
	{
		doTestPackageAndVersions();
	}

	public void testPackageRequire()
	{
		doTestPackageAndVersions();
	}

	public void testPackageMy()
	{
		doTestAllPackages();
	}

	public void testPackageOur()
	{
		doTestAllPackages();
	}

	public void testPackageState()
	{
		doTestAllPackages();
	}

	public void testTryCatch()
	{
		doTestAllPackages();
	}

	private void doTest(List<String>... result)
	{
		assertCompletionIs(result);
	}

	private void doTest(String... result)
	{
		doTest(Arrays.asList(result));
	}

	public void doTestPackageAndVersions()
	{
		doTest(BUILT_IN_PACKAGES, BUILT_IN_VERSIONS, LIBRARY_PACKAGES);
	}

	public void doTestAllPackages()
	{
		doTest(BUILT_IN_PACKAGES, LIBRARY_PACKAGES);
	}
}
