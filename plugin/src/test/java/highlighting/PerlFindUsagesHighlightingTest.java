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

package highlighting;


import base.PerlLightTestCase;
import org.junit.Test;
public class PerlFindUsagesHighlightingTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "highlighting/perl/usages";
  }

  @Test
  public void testFunctionParametersMethodModifiers() {doTest();}

  @Test
  public void testFunctionParametersMethodModifiersSuper() {doTest();}

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
  public void testCaptureGroupsScalars() {doTest();}

  @Test
  public void testNamespace() {doTest();}

  @Test
  public void testBuiltInSubs() {doTest();}

  @Test
  public void testPackageCore() {doTest();}

  @Test
  public void testPackageCoreGlobal() {doTest();}

  @Test
  public void testPackageUniversal() {doTest();}

  @Test
  public void testPackageMain() {doTest();}

  @Test
  public void testBuiltInScalar() {doTest();}

  @Test
  public void testBuiltInArray() {doTest();}

  @Test
  public void testBuiltInHash() {doTest();}

  @Test
  public void testHasModification() {doTest();}

  private void doTest() {doTestUsagesHighlighting();}
}
