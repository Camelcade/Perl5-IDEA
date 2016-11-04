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

package rename;

import base.PerlLightCodeInsightFixtureTestCase;

/**
 * Created by hurricup on 03.11.2016.
 */
public class PerlRenameTest extends PerlLightCodeInsightFixtureTestCase
{
	@Override
	protected String getTestDataPath()
	{
		return "testData/rename/perl";
	}

	public void testGlobRename()
	{
		doTest();
	}

	public void testLexicalVariable()
	{
		doTest();
	}

	public void testMultiVariable()
	{
		doTest();
	}

	public void testPackageName()
	{
		doTest("Foo::Bar::Moo");
	}

	public void testMultiPackage()
	{
		doTest("Foo::Bar::Boo");
	}

	protected void doTest()
	{
		doTest("NewName");
	}

	protected void doTest(String newName)
	{
		String testName = getTestName(true);
		String sourceFileName = testName + "." + getFileExtension();
		String targetFileName = testName + "_after." + getFileExtension();

		myFixture.testRename(sourceFileName, targetFileName, newName);
	}
}
