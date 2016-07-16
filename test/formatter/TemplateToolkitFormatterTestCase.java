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

package formatter;

/**
 * Created by hurricup on 11.07.2016.
 */
public class TemplateToolkitFormatterTestCase extends PerlFormatterTest
{
	TemplateToolkitTestSettings myTestSettings;

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		myTestSettings = new TemplateToolkitTestSettings(myFixture.getProject());
		myTestSettings.setUp();
	}

	@Override
	protected void tearDown() throws Exception
	{
		myTestSettings.tearDown();
		;
		super.tearDown();
	}

	@Override
	protected String getTestDataPath()
	{
		return "testData/formatter/tt2";
	}

	@Override
	public String getFileExtension()
	{
		return "tt";
	}

	public void testStatementModifiersSpacing() throws Exception
	{
		doFormatTest("block_anon");
	}
}
