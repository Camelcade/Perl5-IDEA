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

import base.TemplateToolkitLightCodeInsightFixtureTestCase;

/**
 * Created by hurricup on 11.07.2016.
 */
public class TemplateToolkitFormatterTestCase extends TemplateToolkitLightCodeInsightFixtureTestCase
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

	public void testArray() throws Exception
	{
		doFormatTest();
	}

	public void testBlockAnon() throws Exception
	{
		doFormatTest();
	}

	public void testBlockNamed() throws Exception
	{
		doFormatTest();
	}

	public void testCall() throws Exception
	{
		doFormatTest();
	}

	public void testChompMarkers() throws Exception
	{
		doFormatTest();
	}

	public void testClear() throws Exception
	{
		doFormatTest();
	}

	public void testDebug() throws Exception
	{
		doFormatTest();
	}

	public void testDefault() throws Exception
	{
		doFormatTest();
	}

	public void testExpressions() throws Exception
	{
		doFormatTest();
	}

	public void testFilter() throws Exception
	{
		doFormatTest();
	}

	public void testFilterPostfix() throws Exception
	{
		doFormatTest();
	}

	public void testForeach() throws Exception
	{
		doFormatTest();
	}

	public void testGet() throws Exception
	{
		doFormatTest();
	}

	public void testHash() throws Exception
	{
		doFormatTest();
	}

	public void testIf() throws Exception
	{
		doFormatTest();
	}

	public void testInclude() throws Exception
	{
		doFormatTest();
	}

	public void testInsert() throws Exception
	{
		doFormatTest();
	}

	public void testLast() throws Exception
	{
		doFormatTest();
	}

	public void testMacro() throws Exception
	{
		doFormatTest();
	}

	public void testMeta() throws Exception
	{
		doFormatTest();
	}

	public void testMultiDirectives() throws Exception
	{
		doFormatTest();
	}

	public void testNext() throws Exception
	{
		doFormatTest();
	}

	public void testPerl() throws Exception
	{
		doFormatTest();
	}

	public void testProcess() throws Exception
	{
		doFormatTest();
	}

	public void testReturn() throws Exception
	{
		doFormatTest();
	}
}
