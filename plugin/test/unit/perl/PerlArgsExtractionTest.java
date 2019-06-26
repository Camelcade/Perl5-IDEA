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

package unit.perl;


import base.PerlLightTestCase;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.PerlSubDefinitionElement;
import org.junit.Test;
public class PerlArgsExtractionTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/unit/perl/sub_arguments";
  }

  @Test
  public void testUnpackFake() {
    doTest("unpack_fake", "");
  }

  @Test
  public void testUnpackAll() {
    doTest("unpack_all", "($var, undef, $othervar)");
  }

  @Test
  public void testUnpackAllShit() {
    doTest("unpack_all_shift", "($var, undef, $othervar)");
  }

  @Test
  public void testUnpackAtAndShift() {
    doTest("unpack_at_and_shifts", "($var, undef, $othervar)");
  }

  @Test
  public void testUnpackAtElements() {
    doTest("unpack_at_elements", "($var, undef)");
  }

  @Test
  public void testUnpackMixed1() {
    doTest("unpack_mixed1", "($var, undef, $othervar)");
  }

  @Test
  public void testUnpackMixed2() {
    doTest("unpack_mixed2", "($var, undef, $othervar)");
  }

  @Test
  public void testUnpackMixed3() {
    doTest("unpack_mixed3", "($var, undef, $othervar)");
  }

  @Test
  public void testUnpackMultiShifts() {
    doTest("unpack_multi_shifts", "($var, undef, $othervar)");
  }


  protected void doTest(String fileName, String argsString) {
    initWithFileSmart(fileName);
    PsiElement elementAtCaret = myFixture.getElementAtCaret();
    assertNotNull(elementAtCaret);
    PerlSubDefinitionElement subBase = PsiTreeUtil.getParentOfType(elementAtCaret, PerlSubDefinitionElement.class, false);
    assertNotNull(subBase);
    assertEquals(argsString, subBase.getSubArgumentsListAsString());
  }
}
