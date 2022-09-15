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

package intellilang;


import base.PerlLightTestCase;
import org.junit.Test;
public class PerlInjectionSupportTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "intellilang/perl/injectionSupport";
  }

  @Test
  public void testHtmlQ() {doFileTest();}

  @Test
  public void testHtmlQQ() {doFileTest();}

  @Test
  public void testHtmlQX() {doFileTest();}

  @Test
  public void testHtmlHeredocQ() { doFileTest(); }

  @Test
  public void testHtmlHeredocQQ() { doFileTest(); }

  @Test
  public void testHtmlHeredocQX() { doFileTest(); }

  @Test
  public void testHtmlData() { doFileTest(); }

  private void doFileTest() {
    initWithFileSmartWithoutErrors();
    assertInjected();
  }
}
