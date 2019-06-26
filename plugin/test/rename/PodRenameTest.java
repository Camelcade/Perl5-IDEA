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

package rename;


import base.PodLightTestCase;
import com.intellij.codeInsight.TargetElementUtil;
import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
public class PodRenameTest extends PodLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/rename/pod";
  }

  @Test
  public void testHeading1() {doTest(true);}

  @Test
  public void testHeading1Format() {doTest("new C<name>", true);}

  @Test
  public void testHeading1FormatFromDeclaration() {doTest("new C<name>", false);}

  @Test
  public void testHeading1Angles() {doTest("new >> name", true);}

  @Test
  public void testHeading1AnglesFromDeclaration() {doTest("new >> name", false);}

  @Test
  public void testHeading1AnglesIndex() {doTest("new >> name", true);}

  @Test
  public void testHeading2() {doTest();}

  @Test
  public void testHeading3() {doTest();}

  @Test
  public void testHeading4() {doTest();}

  @Test
  public void testIndex() {doTest("some >> data << index", true);}

  @Test
  public void testItemNormal() {doTest();}

  private void doTest() { doTest("new >> C<name> << here", true); }

  private void doTest(boolean checkReference) {doTest("NewName", checkReference);}

  private void doTest(@NotNull String newName, boolean checkReference) {
    doTestRename(newName);
    if (!checkReference) {
      return;
    }
    PsiReference reference = TargetElementUtil.findReference(getEditor());
    if (reference == null) {
      fail("No reference in\n" + getEditorTextWithCaretsAndSelections());
    }
    if (reference.resolve() == null) {
      fail("Unresolved reference in\n" + getEditorTextWithCaretsAndSelections());
    }
  }
}
