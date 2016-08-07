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

package parser;

import com.perl5.lang.ea.PerlExternalAnnotationsParserDefinition;

/**
 * Created by hurricup on 07.08.2016.
 */
public class PerlExternalAnnotationsParserTest extends PerlParserTestBase
{
	public PerlExternalAnnotationsParserTest()
	{
		super("", "pmea", new PerlExternalAnnotationsParserDefinition());
	}

	@Override
	protected String getTestDataPath()
	{
		return "testData/parser/ea";
	}

	public void testPackageIncomplete()
	{
		doTest(false);
	}

	public void testPackageSemi()
	{
		doTest(false);
	}

	public void testSubSemi()
	{
		doTest(false);
	}

	public void testProper()
	{
		doTest();
	}

	public void testProperWithVersion()
	{
		doTest();
	}

	public void testUnknownPerlCode()
	{
		doTest();
	}

}
