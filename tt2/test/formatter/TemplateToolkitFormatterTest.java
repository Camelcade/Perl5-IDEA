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

package formatter;

import base.TemplateToolkitLightTestCase;


public class TemplateToolkitFormatterTest extends TemplateToolkitLightTestCase {
  TemplateToolkitTestSettings myTestSettings;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    myTestSettings = new TemplateToolkitTestSettings(myFixture.getProject());
    myTestSettings.setUp();
  }

  @Override
  protected void tearDown() throws Exception {
    try {
      myTestSettings.tearDown();
    }
    finally {
      super.tearDown();
    }
  }

  @Override
  protected String getTestDataPath() {
    return "testData/formatter";
  }

  @Override
  public String getFileExtension() {
    return "tt";
  }

  public void testArray() {
    doFormatTest();
  }

  public void testBlockAnon() {
    doFormatTest();
  }

  public void testBlockNamed() {
    doFormatTest();
  }

  public void testCall() {
    doFormatTest();
  }

  public void testChompMarkers() {
    doFormatTest();
  }

  public void testClear() {
    doFormatTest();
  }

  public void testDebug() {
    doFormatTest();
  }

  public void testDefault() {
    doFormatTest();
  }

  public void testExpressions() {
    doFormatTest();
  }

  public void testFilter() {
    doFormatTest();
  }

  public void testFilterPostfix() {
    doFormatTest();
  }

  public void testForeach() {
    doFormatTest();
  }

  public void testGet() {
    doFormatTest();
  }

  public void testHash() {
    doFormatTest();
  }

  public void testIf() {
    doFormatTest();
  }

  public void testInclude() {
    doFormatTest();
  }

  public void testInsert() {
    doFormatTest();
  }

  public void testLast() {
    doFormatTest();
  }

  public void testMacro() {
    doFormatTest();
  }

  public void testMeta() {
    doFormatTest();
  }

  public void testMultiDirectives() {
    doFormatTest();
  }

  public void testNext() {
    doFormatTest();
  }

  public void testPerl() {
    doFormatTest();
  }

  public void testProcess() {
    doFormatTest();
  }

  public void testReturn() {
    doFormatTest();
  }

  public void testSet() {
    doFormatTest();
  }

  public void testStop() {
    doFormatTest();
  }

  public void testSubCall() {
    doFormatTest();
  }

  public void testSwitch() {
    doFormatTest();
  }

  public void testThrow() {
    doFormatTest();
  }

  public void testTryCatch() {
    doFormatTest();
  }

  public void testUse() {
    doFormatTest();
  }

  public void testWhile() {
    doFormatTest();
  }

  public void testWrapper() {
    doFormatTest();
  }
}
