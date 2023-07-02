/*
 * Copyright 2015-2023 Alexandr Evstigneev
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

package unit.perl;

import base.PerlLightTestCase;
import categories.Heavy;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class PerlHighlightingRestartTest extends PerlLightTestCase {

  @Override
  protected String getBaseDataPath() {
    return "unit/perl/highlightingRestart";
  }

  @Test
  public void testMyVars() {
    doTest();
  }

  @Test
  public void testOurVars() {
    doTest();
  }

  @Test
  public void testStateVars() {
    doTest();
  }

  @Test
  public void testComma() {
    doTest();
  }

  @Test
  public void testFatComma() {
    doTest();
  }

  @Test
  public void testLongList() {
    doTest();
  }

  @Test
  public void testLongCall() {
    doTest();
  }

  @Test
  public void testDoBlock() {
    doTest();
  }

  @Test
  public void testEvalBlock() {
    doTest();
  }

  @Test
  public void testAnonHash() {
    doTest();
  }

  @Test
  public void testAnonArray() {
    doTest();
  }

  @Test
  public void testAnonList() {
    doTest();
  }

  @Test
  public void testAnonQwList() {
    doTest();
  }

  @Test
  public void testSimple() {
    doTest();
  }

  @Test
  @Category(Heavy.class)
  public void testPerlTidy() {
    doTestLarge(true);
  }

  @Test
  @Category(Heavy.class)
  public void testPinxi() {
    doTestLarge(true);
  }

  @Test
  @Category(Heavy.class)
  public void testCatalyst() {
    doTestLarge(true);
  }

  @Test
  @Category(Heavy.class)
  public void testMojo() {
    doTestLarge(true);
  }

  @Test
  @Category(Heavy.class)
  public void testMoose() {
    doTestLarge(true);
  }

  @Test
  @Category(Heavy.class)
  public void testMysqltuner() {
    doTestLarge(true);
  }

  @Test
  @Category(Heavy.class)
  public void testPerl532() {
    doTestLarge(false);
  }

  @Test
  @Category(Heavy.class)
  public void testPerl534() {
    doTestLarge(false);
  }

  @Test
  @Category(Heavy.class)
  public void testPerl536() {
    doTestLarge(false);
  }

  @Test
  @Category(Heavy.class)
  public void testPerl5125() {
    doTestLarge(false);
  }

  @Test
  @Category(Heavy.class)
  public void testPerl5303() {
    doTestLarge(false);
  }

  private void doTestLarge(boolean checkErrors) {
    initWithLarge(getTestName(true), checkErrors);
    doTestHighlighterRestartWithoutInit();
  }

  private void doTest() {
    doTestHighlighterRestart();
  }
}
