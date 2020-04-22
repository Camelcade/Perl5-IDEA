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

package findUsages;


import base.PerlLightTestCase;
import org.junit.Test;
public class PerlFindUsagesGroupingTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/findusages/perl/grouping";
  }

  @Test
  public void testFunctionParametersVar() {
    doTest();
  }

  @Test
  public void testFunctionParametersModifiers() {
    doTest();
  }

  @Test
  public void testBlessExpr() {doTest();}

  @Test
  public void testSpliceExpr() {doTest();}

  @Test
  public void testHashArgument() {doTest();}

  @Test
  public void testHashArgumentArray() {doTest();}

  @Test
  public void testArrayArguments() {doTest();}

  @Test
  public void testHash() {doTest();}

  @Test
  public void testArray() {doTest();}

  @Test
  public void testForeachIterator() {doTest();}

  @Test
  public void testScalarWithContext() {doTest();}

  @Test
  public void testScalar() {doTest();}

  @Test
  public void testSub() {doTest();}

  private void doTest() {doTestUsagesGrouping();}
}
