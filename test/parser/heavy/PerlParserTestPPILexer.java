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

package parser.heavy;

/**
 * Created by hurricup on 28.02.2016.
 */
public class PerlParserTestPPILexer extends PerlParserTestPPIBase
{
	private static final String myGroup = "05_lexer";

	@Override
	protected String getTestsGroup()
	{
		return myGroup;
	}

	public void test01_simpleassign()
	{
		doTest("01_simpleassign");
	}

	public void test02_END()
	{
		doTest("02_END");
	}

	public void test03_subroutine_attributes()
	{
		doTest("03_subroutine_attributes");
	}

	public void test04_anonymous_subroutines()
	{
		doTest("04_anonymous_subroutines");
	}

	public void test05_compound_loops()
	{
		doTest("05_compound_loops");
	}

	public void test06_subroutine_prototypes()
	{
		doTest("06_subroutine_prototypes");
	}

	public void test07_unmatched_braces()
	{
		doTest("07_unmatched_braces", false);
	}

	public void test08_subroutines()
	{
		doTest("08_subroutines");
	}

	public void test09_heredoc()
	{
		doTest("09_heredoc");
	}

	public void test10_readline()
	{
		doTest("10_readline");
	}

	public void test11_dor()
	{
		doTest("11_dor");
	}

	public void test12_switch()
	{
		doTest("12_switch");
	}

}
