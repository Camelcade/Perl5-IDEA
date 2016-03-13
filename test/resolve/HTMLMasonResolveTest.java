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

package resolve;

/**
 * Created by hurricup on 13.03.2016.
 */
public class HTMLMasonResolveTest extends PerlVariableResolveTestCase
{
	@Override
	protected String getTestDataPath()
	{
		return "testData/resolve/htmlmason";
	}

	public void testLineToLine() throws Exception
	{
		doTest("line_to_line", true);
	}

	public void testLineToLineNegative() throws Exception
	{
		doTest("line_to_line_negative", false);
	}

	public void testLineToPerl() throws Exception
	{
		doTest("line_to_perl", true);
	}

	public void testPerlToLine() throws Exception
	{
		doTest("perl_to_line", true);
	}

	public void testPerlToPerl() throws Exception
	{
		doTest("perl_to_perl", true);
	}

	@Override
	public void initWithFile(String filename)
	{
		initWithFileAsHTMLMason(filename);
	}
}
