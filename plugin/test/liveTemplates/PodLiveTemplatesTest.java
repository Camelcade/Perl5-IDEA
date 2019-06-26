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

package liveTemplates;


import base.PodLightTestCase;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
public class PodLiveTemplatesTest extends PodLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/liveTemplates/pod";
  }

  @Test
  public void testH1() {doTest("=head1");}

  @Test
  public void testH2() {doTest("=head2");}

  @Test
  public void testAttr() {doTest("=attr");}

  @Test
  public void testMethod() {doTest("=method");}

  @Test
  public void testFunc() {doTest("=func");}

  @Test
  public void testH3() {doTest("=head3");}

  @Test
  public void testH4() {doTest("=head4");}

  @Test
  public void testOverBack() {doTest("=over");}

  @Test
  public void testBeginEnd() {doTest("=begin");}

  @Test
  public void testFor() {doTest("=for");}

  @Test
  public void testEncoding() {doTest("=encoding");}

  @Test
  public void testItem() {doTest("=item");}

  @Test
  public void testPod() {doTest("=pod");}

  @Test
  public void testCut() {doTest("=cut");}

  @Test
  public void testEnd() {doTest("=end");}

  @Test
  public void testBack() {doTest("=back");}

  protected void doTest(@NotNull String textToType) {
    doLiveTemplateBulkTest(textToType);
  }
}
