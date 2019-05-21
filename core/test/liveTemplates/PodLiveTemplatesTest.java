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

public class PodLiveTemplatesTest extends PodLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/liveTemplates/pod";
  }

  public void testH1() {doTest("=head1");}

  public void testH2() {doTest("=head2");}

  public void testAttr() {doTest("=attr");}

  public void testMethod() {doTest("=method");}

  public void testFunc() {doTest("=func");}

  public void testH3() {doTest("=head3");}

  public void testH4() {doTest("=head4");}

  public void testOverBack() {doTest("=over");}

  public void testBeginEnd() {doTest("=begin");}

  public void testFor() {doTest("=for");}

  public void testEncoding() {doTest("=encoding");}

  public void testItem() {doTest("=item");}

  public void testPod() {doTest("=pod");}

  public void testCut() {doTest("=cut");}

  public void testEnd() {doTest("=end");}

  public void testBack() {doTest("=back");}

  protected void doTest(@NotNull String textToType) {
    doLiveTemplateBulkTest(textToType);
  }
}
