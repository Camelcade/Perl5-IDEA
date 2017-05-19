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

package resolve.perl;

/**
 * Created by hurricup on 18.02.2016.
 */
public class PerlLexicalVariableResolveTest extends PerlVariableResolveTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/resolve/perl/variables";
  }

  public void testSimple() {
    doTest("variable_simple", true);
  }

  public void testIfCondition() {
    doTest("variable_if_condition", true);
  }

  public void testForIterator() {
    doTest("variable_for_iterator", true);
  }

  public void testForIteratorIterator() {
    doTest("for_iterator_iterator", true);
  }

  public void testUseVars() {
    doTest("variable_use_vars", true);
  }

  public void testIfElsifElse() {
    doTest("variable_if_elsif_else", true);
  }

  public void testSubSignature() {
    doTest("variable_sub_signature", true);
  }

  public void testMethodExplicitInvocant() {
    doTest("variable_method_explicit_invocant", true);
  }

  public void testVariableInInvocation() {
    doTest("variable_in_call_expression", true);
  }

  public void testMethodImplicitInvocant() {
    doTest("variable_method_implicit_invocant", true);
  }

  public void testNegativeBlock() {
    doTest("negative_variable_block", false);
  }

  public void testNegativeIfElse() {
    doTest("negative_if_else", false);
  }

  public void testSameStatementSimple() {
    doTest("same_statement_simple", false);
  }

  public void testSameStatementMap() {
    doTest("same_statement_map", false);
  }

  public void testPackageLimitations() {
    doTestWithFileCheck();
  }

/*
        public void testPerlTidy()
	{
		initWithPerlTidy();
		doTestWithFileCheckWithoutInit(); // fails on travis
	}
*/
}
