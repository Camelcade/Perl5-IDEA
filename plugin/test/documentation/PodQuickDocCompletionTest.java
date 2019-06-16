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
import org.junit.Test;
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

  @Test
  public void testHead1() {doTest();}

  @Test
  public void testHead2() {doTest();}

  @Test
  public void testAttr() {doTest();}

  @Test
  public void testFunc() {doTest();}

  @Test
  public void testMethod() {doTest();}

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
  public void testPerlpod() {
    initWithTextSmart("L<<caret>>");
    doTestCompletionQuickDoc("perlpod");
  }

  @Test
  public void testPerlpodBack() {
    initWithTextSmart("L<perlpod/<caret>>");
    doTestCompletionQuickDoc("=back");
  }

  @Test
  public void testMojoliciousController() {
    initWithTextSmart("L<<caret>>");
    doTestCompletionQuickDoc("Mojolicious::Controller");
  }

  @Test
  public void testMojoliciousControllerSection() {
    initWithTextSmart("L<Mojolicious::Controller/<caret>>");
    doTestCompletionQuickDoc("write_chunk");
  }

  @Test
  public void testPerlVarIndex() {
    initWithTextSmart("L<perlvar/<caret>>");
    doTestCompletionQuickDoc("line number");
  }

  @Test
  public void testIndexedParagraph() {
    initWithTextSmart("L<perldata/<caret>>");
    doTestCompletionQuickDoc("interpolation");
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
