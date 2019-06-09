/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

import base.PerlLightTestCase;


public class PerlLabelResolveTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/resolve/perl/labels";
  }

  public void testNextOutAnonSub() {
    doTestResolve();
  }

  public void testNextOutDo() {
    doTestResolve();
  }

  public void testNextOutEval() {
    doTestResolve();
  }

  public void testNextOutGrep() {
    doTestResolve();
  }

  public void testNextOutMap() {
    doTestResolve();
  }

  public void testNextOutSort() {
    doTestResolve();
  }

  public void testNextOutSub() {
    doTestResolve();
  }

  public void testNextInAnonSub() {
    doTestResolve();
  }

  public void testNextInGrep() {
    doTestResolve();
  }

  public void testNextInMap() {
    doTestResolve();
  }

  public void testNextInSort() {
    doTestResolve();
  }

  public void testNextInSub() {
    doTestResolve();
  }

  public void testNextLabeledBlock() {
    doTestResolve();
  }

  public void testRedoLabeledBlock() {
    doTestResolve();
  }

  public void testLastLabeledBlock() {
    doTestResolve();
  }

  public void testNextLabelBeforePod() {
    doTestResolve();
  }

  public void testNextLabelBeforeComment() {
    doTestResolve();
  }

  public void testNextLabelOtherStatement() {
    doTestResolve();
  }

  public void testNextToFor() {
    doTestResolve();
  }

  public void testNextToForeach() {
    doTestResolve();
  }

  public void testNextToWhile() {
    doTestResolve();
  }

  public void testNextToUntil() {
    doTestResolve();
  }

  public void testNextToGiven() {
    doTestResolve();
  }

  public void testNextToIf() {
    doTestResolve();
  }

  public void testNextToUnless() {
    doTestResolve();
  }

  public void testGotoAfter() {
    doTestResolve();
  }

  public void testGotoBefore() {
    doTestResolve();
  }

  public void testGotoFromDeclaration() {
    doTestResolve();
  }

  public void testGotoInAfter() {
    doTestResolve();
  }

  public void testGotoInBefore() {
    doTestResolve();
  }

  public void testGotoOutAfter() {
    doTestResolve();
  }

  public void testGotoOutBefore() {
    doTestResolve();
  }
}
