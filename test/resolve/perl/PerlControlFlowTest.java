/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

import base.PerlLightTestCase;

public class PerlControlFlowTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/controlFlow/perl";
  }

  public void testAssignChainParallel() {doTest();}

  public void testAssignDeclarationSimple() {doTest();}

  public void testAssignDeclarationParens() {doTest();}

  public void testAssignDeclarationMulti() {doTest();}

  public void testSimpleRead() {doTest();}

  public void testAssignSimple() {doTest();}

  public void testAssignParallel() {doTest();}

  public void testAssignChain() {doTest();}

  public void testAssignExpression() {doTest();}

  public void testStatementModifier() {doTest();}

  public void testPrintExpression() {doTest();}

  public void testElementsAndSlices() {doTest();}

  private void doTest() {doTestControlFlow();}
}
