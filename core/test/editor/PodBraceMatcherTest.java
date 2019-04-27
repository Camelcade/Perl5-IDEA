/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

import base.PodLightTestCase;

public class PodBraceMatcherTest extends PodLightTestCase {

  @Override
  protected String getTestDataPath() {
    return "testData/braceMatcher/pod";
  }

  public void testAngleClose() {doTest();}

  public void testAngleCloseFormatter() {doTest();}

  public void testAngleOpen() {doTest();}

  public void testAngleOpenFormatter() {doTest();}

  public void testBack() {doTest();}

  public void testBegin() {doTest();}

  public void testBraceClose() {doTest();}

  public void testBraceOpen() {doTest();}

  public void testBracketClose() {doTest();}

  public void testBracketOpen() {doTest();}

  public void testEnd() {doTest();}

  public void testOver() {doTest();}

  public void testParenClose() {doTest();}

  public void testParenOpen() {doTest();}

  private void doTest() {
    doTestBraceMatcher();
  }
}
