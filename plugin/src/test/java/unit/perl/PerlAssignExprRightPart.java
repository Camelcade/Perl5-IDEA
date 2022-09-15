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

package unit.perl;


import base.PerlLightTestCase;
import com.intellij.psi.PsiElement;
import com.intellij.testFramework.UsefulTestCase;
import com.perl5.lang.perl.psi.PerlAssignExpression;
import com.perl5.lang.perl.psi.PerlVariable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import java.util.List;
public class PerlAssignExprRightPart extends PerlLightTestCase {

  @Override
  protected String getBaseDataPath() {
    return "unit/perl/assign/rightPart";
  }

  @Test
  public void testAssignListToScalar() {doTest();}

  @Test
  public void testAssignListToScalarMulti() {doTest();}

  @Test
  public void testAssignListToScalarInList() {doTest();}

  @Test
  public void testAssignHeredoc() {doTest();}

  @Test
  public void testAssignHeredocMid() {doTest();}

  @Test
  public void testAssignHeredocMidParallel() {doTest();}

  @Test
  public void testAssignHeredocMidRightPart() {doTest();}

  @Test
  public void testAssignChainScalar() {doTest();}

  @Test
  public void testAssignChainScalarArray() {doTest();}

  @Test
  public void testAssignChainScalarParallel() {doTest();}

  @Test
  public void testAssignChainScalarParallelUndef() {doTest();}

  @Test
  public void testAssignChainScalarParallelUndefMid() {doTest();}

  @Test
  public void testAssignListInListContext() {doTest();}

  @Test
  public void testAssignListInScalarContext() {doTest();}

  @Test
  public void testAssignStringListInListContext() {doTest();}

  @Test
  public void testAssignStringListInScalarContext() {doTest();}

  @Test
  public void testAssignScalarEmpty() {doTest();}

  @Test
  public void testAssignScalarFirst() {doTest();}

  @Test
  public void testAssignScalarList() {doTest();}

  @Test
  public void testAssignScalarListFirst() {doTest();}

  @Test
  public void testAssignScalarListWithValues() {doTest();}

  @Test
  public void testAssignScalarParensInside() {doTest();}

  @Test
  public void testAssignScalarParensOutside() {doTest();}

  @Test
  public void testAssignScalarSecond() {doTest();}

  @Test
  public void testAssignScalarSecondParens() {doTest();}

  @Test
  public void testAssignScalarSimple() {doTest();}

  @Test
  public void testAssignScalarThird() {
    doTestRightPart(PerlVariable.class);
  }

  @Test
  public void testDeclarationScalarThird() {
    doTestRightPart(PerlVariable.class);
  }

  @Test
  public void testAssignScalarThirdShift() {doTest();}

  @Test
  public void testAssignStringList() {
    doTestRightPart(PerlVariable.class);
  }

  @Test
  public void testDeclarationStringList() {doTest();}

  private void doTest() {
    doTestRightPart(PerlVariable.class);
  }

  private void doTestRightPart(@NotNull Class<? extends PsiElement> clazz) {
    initWithFileSmartWithoutErrors();
    PsiElement elementAtCaret = getElementAtCaret(clazz);
    assertNotNull(elementAtCaret);
    PerlAssignExpression assignExpression = PerlAssignExpression.getAssignmentExpression(elementAtCaret);
    assertNotNull(assignExpression);
    PerlAssignExpression.PerlAssignValueDescriptor perlAssignValueDescriptor = assignExpression.getRightPartOfAssignment(elementAtCaret);
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), serializeValueDescriptor(perlAssignValueDescriptor));
  }

  private String serializeValueDescriptor(@Nullable PerlAssignExpression.PerlAssignValueDescriptor descriptor) {
    StringBuilder result = new StringBuilder(getEditorTextWithCaretsAndSelections()).append("\n");
    if (descriptor == null) {
      return result.toString();
    }
    result.append("Index: ").append(descriptor.getStartIndex()).append("\n");
    List<PsiElement> elements = descriptor.getElements();
    result.append("Elements number: ").append(elements.size()).append("\n");
    result.append("Text: ").append(descriptor.getText()).append("\n");
    elements.forEach(it -> result.append(serializePsiElement(it)).append("\n"));
    return result.toString();
  }
}
