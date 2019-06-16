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
import org.junit.Test;
public class PerlLabelResolveTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/resolve/perl/labels";
  }

  @Test
  public void testNextOutAnonSub() {
    doTestResolve();
  }

  @Test
  public void testNextOutDo() {
    doTestResolve();
  }

  @Test
  public void testNextOutEval() {
    doTestResolve();
  }

  @Test
  public void testNextOutGrep() {
    doTestResolve();
  }

  @Test
  public void testNextOutMap() {
    doTestResolve();
  }

  @Test
  public void testNextOutSort() {
    doTestResolve();
  }

  @Test
  public void testNextOutSub() {
    doTestResolve();
  }

  @Test
  public void testNextInAnonSub() {
    doTestResolve();
  }

  @Test
  public void testNextInGrep() {
    doTestResolve();
  }

  @Test
  public void testNextInMap() {
    doTestResolve();
  }

  @Test
  public void testNextInSort() {
    doTestResolve();
  }

  @Test
  public void testNextInSub() {
    doTestResolve();
  }

  @Test
  public void testNextLabeledBlock() {
    doTestResolve();
  }

  @Test
  public void testRedoLabeledBlock() {
    doTestResolve();
  }

  @Test
  public void testLastLabeledBlock() {
    doTestResolve();
  }

  @Test
  public void testNextLabelBeforePod() {
    doTestResolve();
  }

  @Test
  public void testNextLabelBeforeComment() {
    doTestResolve();
  }

  @Test
  public void testNextLabelOtherStatement() {
    doTestResolve();
  }

  @Test
  public void testNextToFor() {
    doTestResolve();
  }

  @Test
  public void testNextToForeach() {
    doTestResolve();
  }

  @Test
  public void testNextToWhile() {
    doTestResolve();
  }

  @Test
  public void testNextToUntil() {
    doTestResolve();
  }

  @Test
  public void testNextToGiven() {
    doTestResolve();
  }

  @Test
  public void testNextToIf() {
    doTestResolve();
  }

  @Test
  public void testNextToUnless() {
    doTestResolve();
  }

  @Test
  public void testGotoAfter() {
    doTestResolve();
  }

  @Test
  public void testGotoBefore() {
    doTestResolve();
  }

  @Test
  public void testGotoFromDeclaration() {
    doTestResolve();
  }

  @Test
  public void testGotoInAfter() {
    doTestResolve();
  }

  @Test
  public void testGotoInBefore() {
    doTestResolve();
  }

  @Test
  public void testGotoOutAfter() {
    doTestResolve();
  }

  @Test
  public void testGotoOutBefore() {
    doTestResolve();
  }
}
