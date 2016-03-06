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

import com.perl5.lang.htmlmason.HTMLMasonParserDefinition;

/**
 * Created by hurricup on 06.03.2016.
 */
public class HTMLMasonParserTest extends PerlParserTestBase
{
	public HTMLMasonParserTest()
	{
		super("", "hm", new HTMLMasonParserDefinition());
	}

	@Override
	protected String getTestDataPath()
	{
		return "testData/parser/htmlmason";
	}

	public void testSyntax()
	{
		doTest("parser_test");
	}
}
