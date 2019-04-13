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

package documentation;

import base.PerlLightTestCase;
import org.jetbrains.annotations.NotNull;

public class PerlQuickDocTest extends PerlLightTestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    withPerlDoc();
  }

  @Override
  protected String getTestDataPath() {
    return "testData/documentation/perl/quickdoc";
  }

  public void testBlockAutoload() {doTest();}

  public void testBlockDestroy() {doTest();}

  public void testBlockBegin() {doTest();}

  public void testBlockCheck() {doTest();}

  public void testBlockEnd() {doTest();}

  public void testBlockInit() {doTest();}

  public void testBlockUnitcheck() {doTest();}

  public void testTagData() {doTest();}

  public void testTagEnd() {doTest();}

  public void testTagFile() {doTest();}

  public void testTagLine() {doTest();}

  public void testTagPackage() {doTest();}

  public void testTagSub() {doTest();}

  public void testGivenCompound() {doTest();}

  public void testWhenCompound() {doTest();}

  public void testDefaultCompound() {doTest();}

  public void testElseCompound() {doTest();}

  public void testElsifCompound() {doTest();}

  public void testForeachIdexedCompound() {doTest();}

  public void testForeachIterateCompound() {doTest();}

  public void testForIdexedCompound() {doTest();}

  public void testForIterateCompound() {doTest();}

  public void testIfCompound() {doTest();}

  public void testUnlessCompound() {doTest();}

  public void testUntilCompound() {doTest();}

  public void testWhileCompound() {doTest();}

  public void testIfModifier() {doTest();}

  public void testUnlessModifier() {doTest();}

  public void testWhileModifier() {doTest();}

  public void testUntilModifier() {doTest();}

  public void testForModifier() {doTest();}

  public void testForeachModifier() {doTest();}

  public void testWhenModifier() {doTest();}

  public void testPodWeaverAttr() {doTest();}

  public void testPodWeaverFunc() {doTest();}

  public void testPodWeaverMethod() {doTest();}

  public void testUnknownSectionWithContent() {doTest();}

  public void testSubDefinitionInline() {doTest();}

  public void testSubDefinitionUsageInline() {doTest();}

  public void testExternalSubUsagePod() {doTest();}

  public void testSubDefinitionCross() {doTest();}

  public void testNamespaceDefinitionInline() {doTest();}

  @NotNull
  @Override
  protected String getResultsFileExtension() {
    return "txt";
  }

  private void doTest() {
    doTestQuickDoc();
  }

}
