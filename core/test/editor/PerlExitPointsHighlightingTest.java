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

package editor;

import base.PerlLightTestCase;

public class PerlExitPointsHighlightingTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/highlighting/exitPoints";
  }

  public void testNextLastRedo() {doTest();}

  public void testNextLastRedoOutbreak() {doTest();}

  public void testAnonSubNested() {doTest();}

  public void testDoNested() {doTest();}

  public void testEvalNested() {doTest();}

  public void testSortNested() {doTest();}

  public void testSubWithNestedEntities() {doTest();}

  private void doTest() {doTestUsagesHighlighting();}
}
