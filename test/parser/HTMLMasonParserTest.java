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

import com.intellij.psi.PsiFile;
import com.perl5.lang.htmlmason.HTMLMasonParserDefinition;

import java.util.List;

/**
 * Created by hurricup on 06.03.2016.
 */
public class HTMLMasonParserTest extends PerlMultiPsiParserTestBase
{
	public HTMLMasonParserTest()
	{
		super("", "mas", new HTMLMasonParserDefinition());
	}

	@Override
	protected String getTestDataPath()
	{
		return "testData/parser/htmlmason";
	}

	public void testArgs()
	{
		doTest("parse_args");
	}

	public void testAttr()
	{
		doTest("parse_attr");
	}

	public void testCalls()
	{
		doTest("parse_calls");
	}

	public void testCallsUnclosed()
	{
		doTest("parse_calls_unclosed", false);
	}

	public void testCallsUnclosedTag()
	{
		doTest("parse_calls_unclosed_tag", false);
	}

	public void testCallsFiltering()
	{
		doTest("parse_calls_filtering");
	}

	public void testCode()
	{
		doTest("parse_code");
	}

	public void testDef()
	{
		doTest("parse_def");
	}

	public void testDoc()
	{
		doTest("parse_doc");
	}

	public void testFilter()
	{
		doTest("parse_filter");
	}

	public void testFlags()
	{
		doTest("parse_flags");
	}

	public void testInit()
	{
		doTest("parse_init");
	}

	public void testMethod()
	{
		doTest("parse_method");
	}

	public void testOnce()
	{
		doTest("parse_once");
	}

	public void testPerl()
	{
		doTest("parse_perl");
	}

	public void testShared()
	{
		doTest("parse_shared");
	}

	public void testText()
	{
		doTest("parse_text");
	}

	public void testSpaceless()
	{
		doTest("parse_spaceless");
	}

	public void testEscapedBlock()
	{
		doTest("parse_escaped_block");
	}

	public void testMasonSample()
	{
		doTest("parse_mason_sample");
	}

	public void testErrorFilter() throws Exception
	{
		String name = "error_filter";
		String text = loadFile(name + "." + myFileExt);
		myFile = createPsiFile(name, text);
		ensureParsed(myFile);
		List<PsiFile> allFiles = myFile.getViewProvider().getAllFiles();
		assertEquals(2, allFiles.size());
		// fixme this is not actually works we need to check annotations, not eror elements, they are still there, see #917
		// see https://github.com/JetBrains/intellij-plugins/blob/master/handlebars/src/com/dmarcotte/handlebars/inspections/HbErrorFilter.java
//		assertFalse(
//				"PsiFile contains error elements",
//				toParseTreeText(allFiles.get(1), skipSpaces(), includeRanges()).contains("PsiErrorElement")
//		);
	}

}
