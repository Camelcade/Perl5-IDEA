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

package documentation;


import base.PodLightTestCase;
import org.junit.Test;
public class PodQuickDocTest extends PodLightTestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    withPerlPod528();
  }

  @Override
  protected String getBaseDataPath() {
    return "testData/documentation/pod/quickdoc";
  }

  @Test
  public void testVariousItems() {doTest();}

  @Test
  public void testHeader1() {doTest();}

  @Test
  public void testIndexContent() {doTest();}

  @Test
  public void testItemContent() {doTest();}

  @Test
  public void testBack() {doTest();}

  @Test
  public void testBegin() {doTest();}

  @Test
  public void testCut() {doTest();}

  @Test
  public void testEncoding() {doTest();}

  @Test
  public void testEnd() {doTest();}

  @Test
  public void testFor() {doTest();}

  @Test
  public void testFormatB() {doTest();}

  @Test
  public void testFormatC() {doTest();}

  @Test
  public void testFormatE() {doTest();}

  @Test
  public void testFormatF() {doTest();}

  @Test
  public void testFormatI() {doTest();}

  @Test
  public void testFormatL() {doTest();}

  @Test
  public void testFormatS() {doTest();}

  @Test
  public void testFormatX() {doTest();}

  @Test
  public void testFormatZ() {doTest();}

  @Test
  public void testHead1() {doTest();}

  @Test
  public void testHead2() {doTest();}

  @Test
  public void testHead3() {doTest();}

  @Test
  public void testHead4() {doTest();}

  @Test
  public void testItem() {doTest();}

  @Test
  public void testOver() {doTest();}

  @Test
  public void testPod() {doTest();}

  @Test
  public void testReferenceFile() {doTest();}

  @Test
  public void testReferenceSection() {doTest();}

  @Test
  public void testReferencedParagraph() {doTest();}

  private void doTest() {
    doTestQuickDoc();
  }
}
