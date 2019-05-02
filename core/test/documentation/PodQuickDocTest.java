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

package documentation;

import base.PodLightTestCase;

public class PodQuickDocTest extends PodLightTestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    withPerlPod();
  }

  @Override
  protected String getTestDataPath() {
    return "testData/documentation/pod/quickdoc";
  }

  public void testBack() {doTest();}

  public void testBegin() {doTest();}

  public void testCut() {doTest();}

  public void testEncoding() {doTest();}

  public void testEnd() {doTest();}

  public void testFor() {doTest();}

  public void testFormatB() {doTest();}

  public void testFormatC() {doTest();}

  public void testFormatE() {doTest();}

  public void testFormatF() {doTest();}

  public void testFormatI() {doTest();}

  public void testFormatL() {doTest();}

  public void testFormatS() {doTest();}

  public void testFormatX() {doTest();}

  public void testFormatZ() {doTest();}

  public void testHead1() {doTest();}

  public void testHead2() {doTest();}

  public void testHead3() {doTest();}

  public void testHead4() {doTest();}

  public void testItem() {doTest();}

  public void testOver() {doTest();}

  public void testPod() {doTest();}

  public void testReferenceFile() {doTest();}

  public void testReferenceSection() {doTest();}

  private void doTest() {
    doTestQuickDoc();
  }
}
