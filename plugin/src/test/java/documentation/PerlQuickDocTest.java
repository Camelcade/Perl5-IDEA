/*
 * Copyright 2015-2024 Alexandr Evstigneev
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
    return "documentation/perl/quickdoc";
  }

  @Test
  public void testClass() { doTest538(); }

  @Test
  public void testField() { doTest538(); }

  @Test
  public void testAdjust() { doTest538(); }

  @Test
  public void testEvalString() { doTest528(); }

  @Test
  public void testEvalStringQQ() { doTest528(); }

  @Test
  public void testDiamondSingle() {
    doTest528();
  }

  @Test
  public void testDiamondDouble() {
    doTest528();
  }

  @Test
  public void testIsaOperator() {
    doTest532();
  }

  @Test
  public void testAwaitSub() {
    doTestFuture();
  }

  private void doTestFuture() {
    withFuture();
    doTest528();
  }

  @Test
  public void testAsyncSub() {
    doTestFuture();
  }

  @Test
  public void testAsyncSubExpr() {
    doTestFuture();
  }

  @Test
  public void testAsyncMethod() {
    doTestFuture();
  }

  @Test
  public void testUsePackage() { doTest528(); }

  @Test
  public void testSubDefinition() { doTest528(); }

  @Test
  public void testSubDeclaration() { doTest528(); }

  @Test
  public void testSubExpr() { doTest528(); }

  @Test
  public void testM() { doTest528(); }

  @Test
  public void testQ() { doTest528(); }

  @Test
  public void testQq() { doTest528(); }

  @Test
  public void testQr() { doTest528(); }

  @Test
  public void testQw() { doTest528(); }

  @Test
  public void testQx() { doTest528(); }

  @Test
  public void testS() { doTest528(); }

  @Test
  public void testTr() { doTest528(); }

  @Test
  public void testY() { doTest528(); }

  @Test
  public void testBlockAutoload() { doTest528(); }

  @Test
  public void testBlockDestroy() { doTest528(); }

  @Test
  public void testBlockBegin() { doTest528(); }

  @Test
  public void testBlockCheck() { doTest528(); }

  @Test
  public void testBlockEnd() { doTest528(); }

  @Test
  public void testBlockInit() { doTest528(); }

  @Test
  public void testBlockUnitcheck() { doTest528(); }

  @Test
  public void testTagData() { doTest528(); }

  @Test
  public void testTagEnd() { doTest528(); }

  @Test
  public void testTagFile() { doTest528(); }

  @Test
  public void testTagLine() { doTest528(); }

  @Test
  public void testTagPackage() { doTest528(); }

  @Test
  public void testTagSub() { doTest528(); }

  @Test
  public void testGivenCompound() { doTest528(); }

  @Test
  public void testWhenCompound() { doTest528(); }

  @Test
  public void testDefaultCompound() { doTest528(); }

  @Test
  public void testElseCompound() { doTest528(); }

  @Test
  public void testElsifCompound() { doTest528(); }

  @Test
  public void testForeachIndexedCompound() { doTest528(); }

  @Test
  public void testForeachIterateCompound() { doTest528(); }

  @Test
  public void testForIdexedCompound() { doTest528(); }

  @Test
  public void testForIterateCompound() { doTest528(); }

  @Test
  public void testIfCompound() { doTest528(); }

  @Test
  public void testUnlessCompound() { doTest528(); }

  @Test
  public void testUntilCompound() { doTest528(); }

  @Test
  public void testWhileCompound() { doTest528(); }

  @Test
  public void testIfModifier() { doTest528(); }

  @Test
  public void testUnlessModifier() { doTest528(); }

  @Test
  public void testWhileModifier() { doTest528(); }

  @Test
  public void testUntilModifier() { doTest528(); }

  @Test
  public void testForModifier() { doTest528(); }

  @Test
  public void testForeachModifier() { doTest528(); }

  @Test
  public void testWhenModifier() { doTest528(); }

  @Test
  public void testPodWeaverAttr() { doTest528(); }

  @Test
  public void testPodWeaverFunc() { doTest528(); }

  @Test
  public void testPodWeaverMethod() { doTest528(); }

  @Test
  public void testUnknownSectionWithContent() { doTest528(); }

  @Test
  public void testSubDefinitionInline() { doTest528(); }

  @Test
  public void testSubDefinitionUsageInline() { doTest528(); }

  @Test
  public void testExternalSubUsagePod() { doTest528(); }

  @Test
  public void testSubDefinitionCross() { doTest528(); }

  @Test
  public void testNamespaceDefinitionInline() { doTest528(); }

  protected void doTest538() {
    withPerl538();
    super.doTest();
  }

  protected void doTest532() {
    withPerl532();
    super.doTest();
  }

  protected void doTest528() {
    withPerl528();
    super.doTest();
  }
}
