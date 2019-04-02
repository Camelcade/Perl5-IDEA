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

package unit.perl;

import base.PerlLightTestCase;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.fileTypes.PerlFileTypePackage;
import com.perl5.lang.perl.psi.PerlSubDefinitionElement;

/**
 * Created by hurricup on 14.06.2016.
 */
public class MethodsDetectionTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/unit/perl/methods";
  }

  @Override
  public String getFileExtension() {
    return PerlFileTypePackage.EXTENSION;
  }

  public void testAnnotation() {
    doTest();
  }

  public void testEmpty() {
    doTest(false);
  }

  public void testStaticUnpack() {
    doTest(false);
  }

  public void testStaticShift() {
    doTest(false);
  }

  public void testEmptyShift() {
    doTest();
  }

  public void testSelfShift() {
    doTest();
  }

  public void testSelfUnpack() {
    doTest();
  }

  public void testUnpackUndef() {
    doTest();
  }

  public void testShiftDeref() {
    doTest();
  }

  public void testShiftDerefParenthesised() {
    doTest();
  }

  public void testShiftDerefReturn() {
    doTest();
  }

  public void testShiftDerefReturnParenthesised() {
    doTest();
  }

  public void testDefaultDeref() {
    doTest();
  }

  public void testDefaultDerefParenthesised() {
    doTest();
  }

  public void testDefaultDerefReturn() {
    doTest();
  }

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
