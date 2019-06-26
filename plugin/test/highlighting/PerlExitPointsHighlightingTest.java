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

package highlighting;


import base.PerlLightTestCase;
import org.junit.Test;
public class PerlExitPointsHighlightingTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/highlighting/perl/exitPoints";
  }

  @Test
  public void testNextLastRedo() {doTest();}

  @Test
  public void testNextLastRedoOutbreak() {doTest();}

  @Test
  public void testAnonSubNested() {doTest();}

  @Test
  public void testDoNested() {doTest();}

  @Test
  public void testEvalNested() {doTest();}

  @Test
  public void testSortNested() {doTest();}

  @Test
  public void testSubWithNestedEntities() {doTest();}

  private void doTest() {doTestUsagesHighlighting();}
}
