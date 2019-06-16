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
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.fileTypes.PerlFileTypePackage;
import com.perl5.lang.perl.psi.PerlSubDefinitionElement;
import org.junit.Test;
public class MethodsDetectionTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/unit/perl/methods";
  }

  @Override
  public String getFileExtension() {
    return PerlFileTypePackage.EXTENSION;
  }

  @Test
  public void testAnnotation() {
    doTest();
  }

  @Test
  public void testEmpty() {
    doTest(false);
  }

  @Test
  public void testStaticUnpack() {
    doTest(false);
  }

  @Test
  public void testStaticShift() {
    doTest(false);
  }

  @Test
  public void testEmptyShift() {
    doTest();
  }

  @Test
  public void testSelfShift() {
    doTest();
  }

  @Test
  public void testSelfUnpack() {
    doTest();
  }

  @Test
  public void testUnpackUndef() {
    doTest();
  }

  @Test
  public void testShiftDeref() {
    doTest();
  }

  @Test
  public void testShiftDerefParenthesised() {
    doTest();
  }

  @Test
  public void testShiftDerefReturn() {
    doTest();
  }

  @Test
  public void testShiftDerefReturnParenthesised() {
    doTest();
  }

  @Test
  public void testDefaultDeref() {
    doTest();
  }

  @Test
  public void testDefaultDerefParenthesised() {
    doTest();
  }

  @Test
  public void testDefaultDerefReturn() {
    doTest();
  }

  @Test
  public void testDefaultDerefReturnParenthesised() {
    doTest();
  }

  public void doTest() {
    doTest(true);
  }

  public void doTest(boolean result) {
    initWithFileSmart(getTestName(false).toLowerCase());
    PsiFile file = myFixture.getFile();
    assertNotNull(file);
    PerlSubDefinitionElement subDefinition = PsiTreeUtil.findChildOfType(file, PerlSubDefinitionElement.class);
    assertNotNull(subDefinition);
    if (result) {
      assertTrue(subDefinition.isMethod());
    }
    else {
      assertFalse(subDefinition.isMethod());
    }
  }
}
