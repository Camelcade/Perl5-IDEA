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

package unit.reparse;

import base.MojoLightTestCase;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public class MojoTemplateReparseTest extends MojoLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/unit/templates/reparse";
  }

  @Test
  public void testAnnotation() {doTest("edit");}

  @Test
  public void testHashIndex() {doTest("edit");}

  @Test
  public void testHeredoc() {doTest("edit");}

  @Test
  public void testUseVars() {doTest("edit");}

  @Test
  public void testCommentInBlock() {doTest("edit");}

  @Test
  public void testCommentInLine() {doTest("edit");}

  @Test
  public void testCommentInBlockNewLine() {doTest("\n");}

  @Test
  public void testCommentInLineNewLine() {doTest("\n");}

  @Test
  public void testNamespaceNameInLine() {doTest("edit");}

  @Test
  public void testStringQInLine() {doTest("edit");}

  @Test
  public void testStringQInLineNewLine() {doTest("\n");}

  @Test
  public void testStringQQInLine() {doTest("edit");}

  @Test
  public void testStringQQInLineNewLine() {doTest("\n");}

  @Test
  public void testStringQXInLine() {doTest("edit");}

  @Test
  public void testStringQXInLineNewLine() {doTest("\n");}

  @Test
  public void testSubBlockInLine() {doTest("edit");}

  @Test
  public void testSubBlockInLineNewLine() {doTest("\n");}

  @Test
  public void testTemplateMidAfterLine() {doTest("edit");}

  @Test
  public void testTemplateMidBeforeLine() {doTest("edit");}

  @Test
  public void testTemplateMidBetweenLines() {doTest("edit");}

  @Test
  public void testBlockWithTemplate() {doTest("edit");}

  @Test
  public void testBlockWithTemplateCloseBrace() {doTest("edit");}

  @Test
  public void testNamespaceName() {doTest("edit");}

  @Test
  public void testStringQ() {doTest("edit");}

  @Test
  public void testStringQQ() {doTest("edit");}

  @Test
  public void testStringQX() {doTest("edit");}

  @Test
  public void testSubBlock() {doTest("edit");}

  @Test
  public void testSubName() {doTest("edit");}

  @Test
  public void testVariableName() {doTest("edit");}

  @Test
  public void testPodMiddle() {doTest("edit");}

  @Test
  public void testPodEnd() {doTest("edit");}

  @Test
  public void testTemplateBottom() {doTest("edit");}

  @Test
  public void testTemplateBottomClose() {doTest("<% ");}

  @Test
  public void testTemplateMid() {doTest("edit");}

  @Test
  public void testTemplateMidClose() {doTest("<% ");}

  @Test
  public void testTemplateTop() {doTest("edit");}

  @Test
  public void testTemplateTopClose() {doTest("<% ");}

  private void doTest(@NotNull String toType) {
    doTestReparse(toType);
  }

  private void doTestBs() {
    doTestReparseBs();
  }
}
