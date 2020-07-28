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

package rename;


import base.PerlLightTestCase;
import com.intellij.codeInsight.TargetElementUtil;
import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.NotNull;
import org.junit.Ignore;
import org.junit.Test;
public class PodPerlRenameTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/rename/pod/perl";
  }

  @Test
  public void testScriptFile() {doTest();}

  @Test
  public void testScriptFileMethod() {doTest("new_method_name", true, false);}

  @Test
  public void testScriptFileMethodDropsFormatting() {doTest("new_method_name", true, false);}

  @Ignore("Bad elements manipulation, commit breaks psi, see #2267")
  @Test
  public void testScriptFileMethodInplace() {
    myFixture.copyFileToProject("scriptFileMethodInplace.pm");
    doTest("new_method_name", false, true);
  }

  private void doTest() { doTest("new >> C<name> << here", true, false); }

  private void doTest(@NotNull String newName, boolean checkReference, boolean inplace) {
    if (inplace) {
      doTestRenameInplace(newName);
    }
    else {
      doTestRename(newName);
    }
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
