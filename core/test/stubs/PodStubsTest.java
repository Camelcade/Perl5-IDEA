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

package stubs;

import base.PodLightTestCase;
import org.jetbrains.annotations.NotNull;

public class PodStubsTest extends PodLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/stubs/pod";
  }

  public void testItems_pod() {
    doTest();
  }

  public void testItemsLong_pod() {
    doTest();
  }

  public void testSections_pod() {
    doTest();
  }

  public void testIndexedParagraphTop_pod() {doTest();}

  public void testIndexedSectionContent_pod() {doTest();}

  @NotNull
  @Override
  protected String computeAnswerFileName(@NotNull String appendix) {
    return getTestName(true).replace('_', '.') + ".txt";
  }

  private void doTest() {
    doTestStubs();
  }
}
