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

package findUsages;


import base.PodLightTestCase;
import org.junit.Test;
public class PodFindUsagesTest extends PodLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "findusages/pod";
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    withPerlPod528();
    myFixture.copyFileToProject("Test.pm");
  }

  @Test
  public void testMultiDeclarationsFirst() {doTest();}

  @Test
  public void testMultiDeclarationsSecond() {doTest();}

  @Test
  public void testMultiDeclarationsUsage() {doTest();}

  @Test
  public void testNameBare() {doTest();}

  @Test
  public void testNameWithSection() {doTest();}

  @Test
  public void testSectionWithName() {doTest();}

  @Test
  public void testSpacelessWithEscapes() {doTest();}

  @Test
  public void testSectionBare() {doTest();}

  @Test
  public void testSectionBareWithFormatting() {doTest();}

  @Test
  public void testHead1() {doTest();}

  @Test
  public void testHead1SameFormatting() {doTest();}

  @Test
  public void testHead1DifferentFormatting() {doTest();}

  @Test
  public void testHead1SectionEscaping() {doTest();}

  @Test
  public void testHead1SameFormattingIndexed() {doTest();}

  @Test
  public void testHead2() {doTest();}

  @Test
  public void testHead3() {doTest();}

  @Test
  public void testHead4() {doTest();}

  @Test
  public void testItem() {doTest();}

  @Test
  public void testIndex() {doTest();}

  @Test
  public void testIndexWithAngles() {doTest();}

  private void doTest() {
    doTestFindUsages();
  }
}
