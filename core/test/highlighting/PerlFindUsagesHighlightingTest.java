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

public class PerlFindUsagesHighlightingTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/highlighting/perl/usages";
  }

  public void testHashArgument() {doTest();}

  public void testHashArgumentArray() {doTest();}

  public void testArrayArguments() {doTest();}

  public void testHash() {doTest();}

  public void testArray() {doTest();}

  public void testForeachIterator() {doTest();}

  public void testScalarWithContext() {doTest();}

  public void testCaptureGroupsScalars() {doTest();}

  public void testNamespace() {doTest();}

  public void testBuiltInSubs() {doTest();}

  public void testPackageCore() {doTest();}

  public void testPackageCoreGlobal() {doTest();}

  public void testPackageUniversal() {doTest();}

  public void testPackageMain() {doTest();}

  public void testBuiltInScalar() {doTest();}

  public void testBuiltInArray() {doTest();}

  public void testBuiltInHash() {doTest();}

  public void testHasModification() {doTest();}

  private void doTest() {doTestUsagesHighlighting();}
}
