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
import com.intellij.psi.PsiElement;
import com.intellij.testFramework.UsefulTestCase;
import com.perl5.lang.perl.psi.PerlAssignExpression;
import com.perl5.lang.perl.psi.PerlVariable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PerlAssignExprRightPart extends PerlLightTestCase {

  @Override
  protected String getTestDataPath() {
    return "testData/unit/perl/assign/rightPart";
  }

  public void testAssignListToScalar() {doTest();}

  public void testAssignListToScalarMulti() {doTest();}

  public void testAssignListToScalarInList() {doTest();}

  public void testAssignHeredoc() {doTest();}

  public void testAssignHeredocMid() {doTest();}

  public void testAssignHeredocMidParallel() {doTest();}

  public void testAssignHeredocMidRightPart() {doTest();}

  public void testAssignChainScalar() {doTest();}

  public void testAssignChainScalarArray() {doTest();}

  public void testAssignChainScalarParallel() {doTest();}

  public void testAssignChainScalarParallelUndef() {doTest();}

  public void testAssignChainScalarParallelUndefMid() {doTest();}

  public void testAssignListInListContext() {doTest();}

  public void testAssignListInScalarContext() {doTest();}

  public void testAssignStringListInListContext() {doTest();}

  public void testAssignStringListInScalarContext() {doTest();}

  public void testAssignScalarEmpty() {doTest();}

  public void testAssignScalarFirst() {doTest();}

  public void testAssignScalarList() {doTest();}

  public void testAssignScalarListFirst() {doTest();}

  public void testAssignScalarListWithValues() {doTest();}

  public void testAssignScalarParensInside() {doTest();}

  public void testAssignScalarParensOutside() {doTest();}

  public void testAssignScalarSecond() {doTest();}

  public void testAssignScalarSecondParens() {doTest();}

  public void testAssignScalarSimple() {doTest();}

  public void testAssignScalarThird() {
    doTestRightPart(PerlVariable.class);
  }

  public void testDeclarationScalarThird() {
    doTestRightPart(PerlVariable.class);
  }

  public void testAssignScalarThirdShift() {doTest();}

  public void testAssignStringList() {
    doTestRightPart(PerlVariable.class);
  }

  public void testDeclarationStringList() {doTest();}

  private void doTest() {
    doTestRightPart(PerlVariable.class);
  }

  private void doTestRightPart(@NotNull Class clazz) {
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
