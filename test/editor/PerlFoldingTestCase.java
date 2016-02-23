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

package editor;

import com.intellij.openapi.vfs.newvfs.impl.VfsRootAccess;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;

/**
 * Created by hurricup on 23.02.2016.
 */
public class PerlFoldingTestCase extends LightCodeInsightFixtureTestCase
{
	public static final String DATA_PATH = "testData/folding";

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

	public void testPerlFolding()
	{
		myFixture.testFolding(getTestDataPath() + "/perl_folding_test.pl");
	}

	public void testMason2Folding()
	{
		myFixture.testFolding(getTestDataPath() + "/mason2_folding_test.mc");
	}
}