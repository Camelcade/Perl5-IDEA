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

package lightElements;


import base.PerlLightTestCase;
import org.junit.Test;
public class PerlLightElementsTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/lightElements/perl";
  }

  @Test
  public void testMooseAttrs() {doTest();}

  @Test
  public void testConstants() {doTest();}

  @Test
  public void testExceptionClass() {doTest();}

  @Test
  public void testClassAccessor() {doTest();}

  @Test
  public void testMojoAttrs() {doTest();}

  @Test
  public void testMooseAttrsWithStandardTypes() {doTest();}

  private void doTest() {
    doTestLightElements();
  }
}
