/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package intellilang;

import base.PerlLightTestCase;
import org.junit.Test;

public class PerlNoinjectTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/intellilang/perl/noinject";
  }

  @Test
  public void testEvalDirectInjected() {doTest();}

  @Test
  public void testEvalDirectNotInjected() {doTest(false);}

  @Test
  public void testEvalIndirectInjected() {doTest();}

  @Test
  public void testEvalIndirectNotInjected() {doTest(false);}

  @Test
  public void testHeredocMarkerInjected() {doTest();}

  @Test
  public void testHeredocMarkerNotInjected() {doTest(false);}

  @Test
  public void testStringSupportInjected() {doTest();}

  @Test
  public void testStringSupportNotInjected() {doTest(false);}

  private void doTest() {
    doTest(true);
  }

  private void doTest(boolean result) {
    initWithFileSmartWithoutErrors();
    if (result) {
      assertInjected();
    }
    else {
      assertNotInjected();
    }
  }
}
