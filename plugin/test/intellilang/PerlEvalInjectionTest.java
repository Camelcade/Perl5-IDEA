/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package intellilang;

import base.PerlLightTestCase;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public class PerlEvalInjectionTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/intellilang/perl/eval";
  }

  @Test
  public void testCharSubstitutionsMulti() {doTest();}

  @Test
  public void testDirectStringArgument() {
    doTest("eval 'sa<caret>y 42';");
  }

  @Test
  public void testDirectStringArgumentQQ() {
    doTest("eval \"sa<caret>y 42\";");
  }

  @Test
  public void testDirectStringArgumentQX() {
    doTest("eval `sa<caret>y 42`;");
  }

  @Test
  public void testVariableAssignedOnDeclaration() {
    doTest("my $variable = 'sa<caret>y 42'; eval $variable;");
  }

  @Test
  public void testVariableAssignedAfterDeclaration() {
    doTest("my $variable; $variable = 'sa<caret>y 42'; eval $variable;");
  }

  @Test
  public void testVariableAssignedAfterEvaluation() {
    doTest("my $variable; eval $variable;  $variable = 'sa<caret>y 42';");
  }

  @Test
  public void testExpressionsInStringQ() {doTest();}

  @Test
  public void testExpressionsInStringQAngles() {doTest();}

  @Test
  public void testExpressionsInStringQQ() {doTest();}

  @Test
  public void testExpressionsInStringQQAngles() {doTest();}

  @Test
  public void testExpressionsInStringQX() {doTest();}

  @Test
  public void testExpressionsInStringQXAngles() {doTest();}

  private void doTest(boolean checkErrors) {
    doTestInjection(checkErrors);
  }

  private void doTest() {
    doTestInjection();
  }

  private void doTest(@NotNull String content) {
    initWithTextSmartWithoutErrors(content);
    doTestInjectionWithoutInit();
  }
}
