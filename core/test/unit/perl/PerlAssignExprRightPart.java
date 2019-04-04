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
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.psi.PerlAssignExpression;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class PerlAssignExprRightPart extends PerlLightTestCase {

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

  public void testAssignScalarThird() {doTestVariable();}

  public void testDeclarationScalarThird() {doTestVariable();}

  public void testAssignScalarThirdShift() {doTest();}

  @Override
  protected String getTestDataPath() {
    return "testData/unit/perl/assign/rightPart";
  }

  public void testAssignStringList() {doTestVariable();}

  public void testDeclarationStringList() {doTest();}

  private void doTestVariable() {
    doTestRightPart(PerlVariable.class);
  }

  private void doTest() {
    // this should check all a once
    doTestVariable();
    doTestRightPart(PerlVariableDeclarationElement.class);
  }

  private void doTestRightPart(@NotNull Class clazz) {
    initWithFileSmartWithoutErrors();
    PsiElement elementAtCaret = getElementAtCaret(clazz);
    assertNotNull(elementAtCaret);
    PerlAssignExpression assignExpression = PerlAssignExpression.getAssignmentExpression(elementAtCaret);
    assertNotNull(assignExpression);
    PerlAssignExpression.ValueDescriptor valueDescriptor = assignExpression.getRightPartOfAssignment(elementAtCaret);
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), serializeValueDescriptor(valueDescriptor));
  }

  private String serializeValueDescriptor(@Nullable PerlAssignExpression.ValueDescriptor descriptor) {
    StringBuilder result = new StringBuilder(getEditorTextWithCaretsAndSelections()).append("\n");
    if (descriptor == null) {
      return result.toString();
    }
    result.append("Index: ").append(descriptor.getStartIndex()).append("\n");
    List<PsiElement> elements = descriptor.getElements();
    result.append("Elements number: ").append(elements.size()).append("\n");
    PsiElement firstElement = elements.get(0);
    PsiElement lastElement = Objects.requireNonNull(ContainerUtil.getLastItem(elements));
    result.append("Text: ")
      .append(firstElement.getContainingFile().getText(),
              firstElement.getTextRange().getStartOffset(),
              lastElement.getTextRange().getEndOffset())
      .append("\n");
    elements.forEach(it -> result.append(serializePsiElement(it)).append("\n"));
    return result.toString();
  }
}
