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
import org.jetbrains.annotations.NotNull;

public class PodQuickDocCompletionTest extends PodLightTestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    withPerlPod();
  }

  @Override
  protected String getTestDataPath() {
    return "testData/documentation/pod/completion";
  }

  public void testHead1() {doTest();}

  public void testHead2() {doTest();}

  public void testAttr() {doTest();}

  public void testFunc() {doTest();}

  public void testMethod() {doTest();}

  public void testHead3() {doTest();}

  public void testHead4() {doTest();}

  public void testItem() {doTest();}

  public void testOver() {doTest();}

  public void testPod() {doTest();}

  public void testBack() {doTest();}

  public void testBegin() {doTest();}

  public void testCut() {doTest();}

  public void testEncoding() {doTest();}

  public void testEnd() {doTest();}

  public void testFor() {doTest();}

  public void testPerlpod() {
    initWithTextSmart("L<<caret>>");
    doTestCompletionQuickDoc("perlpod");
  }

  public void testPerlpodBack() {
    initWithTextSmart("L<perlpod/<caret>>");
    doTestCompletionQuickDoc("=back");
  }

  public void testMojoliciousController() {
    initWithTextSmart("L<<caret>>");
    doTestCompletionQuickDoc("Mojolicious::Controller");
  }

  public void testMojoliciousControllerSection() {
    initWithTextSmart("L<Mojolicious::Controller/<caret>>");
    doTestCompletionQuickDoc("write_chunk");
  }

  @NotNull
  @Override
  protected String getBuiltInFromTestName() {
    return "=" + getTestName(true);
  }

  private void doTest() {
    initWithTextSmartWithoutErrors("=over\n" +
                                   "\n" +
                                   "=<caret>\n" +
                                   "\n" +
                                   "=back");
    doTestCompletionQuickDoc(getBuiltInFromTestName());
  }
}
