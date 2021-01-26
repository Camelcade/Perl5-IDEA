/*
 * Copyright 2015-2021 Alexandr Evstigneev
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


import org.junit.Test;
public class PerlQuickDocTest extends PerlQuickDocTestCase {

  @Override
  protected String getBaseDataPath() {
    return "testData/documentation/perl/quickdoc";
  }

  @Test
  public void testEvalString() {doTest();}

  @Test
  public void testEvalStringQQ() {doTest();}

  @Test
  public void testDiamondSingle() {
    doTest();
  }

  @Test
  public void testDiamondDouble() {
    doTest();
  }

  @Test
  public void testIsaOperator() {
    doTest532();
  }

  @Test
  public void testAsyncSub() {
    withFuture();
    doTest();
  }

  @Test
  public void testAsyncSubExpr() {
    withFuture();
    doTest();
  }

  @Test
  public void testAsyncMethod() {
    withFuture();
    doTest();
  }

  @Test
  public void testUsePackage() {doTest();}

  @Test
  public void testSubDefinition() {doTest();}

  @Test
  public void testSubDeclaration() {doTest();}

  @Test
  public void testSubExpr() {doTest();}

  @Test
  public void testM() {doTest();}

  @Test
  public void testQ() {doTest();}

  @Test
  public void testQq() {doTest();}

  @Test
  public void testQr() {doTest();}

  @Test
  public void testQw() {doTest();}

  @Test
  public void testQx() {doTest();}

  @Test
  public void testS() {doTest();}

  @Test
  public void testTr() {doTest();}

  @Test
  public void testY() {doTest();}

  @Test
  public void testBlockAutoload() {doTest();}

  @Test
  public void testBlockDestroy() {doTest();}

  @Test
  public void testBlockBegin() {doTest();}

  @Test
  public void testBlockCheck() {doTest();}

  @Test
  public void testBlockEnd() {doTest();}

  @Test
  public void testBlockInit() {doTest();}

  @Test
  public void testBlockUnitcheck() {doTest();}

  @Test
  public void testTagData() {doTest();}

  @Test
  public void testTagEnd() {doTest();}

  @Test
  public void testTagFile() {doTest();}

  @Test
  public void testTagLine() {doTest();}

  @Test
  public void testTagPackage() {doTest();}

  @Test
  public void testTagSub() {doTest();}

  @Test
  public void testGivenCompound() {doTest();}

  @Test
  public void testWhenCompound() {doTest();}

  @Test
  public void testDefaultCompound() {doTest();}

  @Test
  public void testElseCompound() {doTest();}

  @Test
  public void testElsifCompound() {doTest();}

  @Test
  public void testForeachIdexedCompound() {doTest();}

  @Test
  public void testForeachIterateCompound() {doTest();}

  @Test
  public void testForIdexedCompound() {doTest();}

  @Test
  public void testForIterateCompound() {doTest();}

  @Test
  public void testIfCompound() {doTest();}

  @Test
  public void testUnlessCompound() {doTest();}

  @Test
  public void testUntilCompound() {doTest();}

  @Test
  public void testWhileCompound() {doTest();}

  @Test
  public void testIfModifier() {doTest();}

  @Test
  public void testUnlessModifier() {doTest();}

  @Test
  public void testWhileModifier() {doTest();}

  @Test
  public void testUntilModifier() {doTest();}

  @Test
  public void testForModifier() {doTest();}

  @Test
  public void testForeachModifier() {doTest();}

  @Test
  public void testWhenModifier() {doTest();}

  @Test
  public void testPodWeaverAttr() {doTest();}

  @Test
  public void testPodWeaverFunc() {doTest();}

  @Test
  public void testPodWeaverMethod() {doTest();}

  @Test
  public void testUnknownSectionWithContent() {doTest();}

  @Test
  public void testSubDefinitionInline() {doTest();}

  @Test
  public void testSubDefinitionUsageInline() {doTest();}

  @Test
  public void testExternalSubUsagePod() {doTest();}

  @Test
  public void testSubDefinitionCross() {doTest();}

  @Test
  public void testNamespaceDefinitionInline() {doTest();}

  protected void doTest532() {
    withPerlPod532();
    super.doTest();
  }

  @Override
  protected void doTest() {
    withPerlPod528();
    super.doTest();
  }
}
