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

package psi;

import base.PerlLightTestCase;
import com.perl5.lang.perl.psi.PerlUseStatement;

import java.util.Arrays;


public class PerlStringCollectionTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/psi/stringCollection";
  }

  public void testQ() {
    doTest("q", "testq I:/mylib/");
  }

  public void testQQ() {
    doTest("qq", "testqq I:/mylib/");
  }

  public void testQX() {
    doTest("qx", "testqx I:/mylib/");
  }

  public void testQW() {
    doTest("qw", "testqw", "I:/mylib/");
  }

  public void testBare() {
    doTest("bare", "testbare", "I:/mylib/");
  }

  public void testMixed() {
    doTest("mixed", "test", "I:/mylib/", "something", "else", "one", "three", "and", "even", "more");
  }

  protected void doTest(String fileName, String... result) {
    initWithFileSmart(fileName);
    PerlUseStatement useStatement = getElementAtCaret(PerlUseStatement.class);
    assertNotNull(useStatement);
    assertEquals(Arrays.asList(result), useStatement.getImportParameters());
  }
}
