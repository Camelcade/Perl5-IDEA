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

package findUsages;

import base.PodLightTestCase;

public class PodFindUsagesTest extends PodLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/findusages/pod";
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    withPerlPod();
    myFixture.copyFileToProject("Test.pm");
  }

  public void testNameBare() {doTest();}

  public void testNameWithSection() {doTest();}

  public void testSectionWithName() {doTest();}

  public void testSectionBare() {doTest();}

  public void testSectionBareWithFormatting() {doTest();}

  public void testHead1() {doTest();}

  public void testHead1SameFormatting() {doTest();}

  public void testHead1DifferentFormatting() {doTest();}

  public void testHead1SectionEscaping() {doTest();}

  public void testHead1SameFormattingIndexed() {doTest();}

  public void testHead2() {doTest();}

  public void testHead3() {doTest();}

  public void testHead4() {doTest();}

  public void testItem() {doTest();}

  public void testIndex() {doTest();}

  private void doTest() {
    doTestFindUsages();
  }
}
