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
import com.perl5.lang.perl.psi.PerlAssignExpression;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.PsiPerlSubCallExpr;
import org.jetbrains.annotations.NotNull;

public class PerlAssignExprUnitTest extends PerlLightTestCase {

  public void testSimpleCall() {
    doTestSubCall("my $var = ti<caret>me;");
  }

  public void testSimpleCallMulti() {
    doTestSubCall("my $var = (ti<caret>me, time);");
  }

  public void testSimpleCallNested() {
    doTestSubCall("my $var = (time, (ti<caret>me));");
  }

  public void testSimpleCallNestedWrong() {
    doTestSubCall("my $var = (time, (ti<caret>me + time));", false);
  }

  public void testSimpleLeftDeclaration() {
    doTestVariableDeclaration("my $v<caret>ar = time;");
  }

  public void testDeclarationRightParens() {
    doTestVariableDeclaration("my($v<caret>ar )= time;");
  }

  public void testDeclarationMultiFirst() {
    doTestVariableDeclaration("my($v<caret>ar, $othervar )= time;");
  }

  public void testDeclarationMultiSecond() {
    doTestVariableDeclaration("my($var, $other<caret>var )= time;");
  }

  public void testDeclarationMidSecond() {
    doTestVariableDeclaration("my($base, $other) = my($var, $other<caret>var )= time;");
  }

  public void testDeclarationMidFirst() {
    doTestVariableDeclaration("my($base, $other) = my($v<caret>ar, $othervar )= time;");
  }

  public void testSimpleLeftVariable() {
    doTestVariable("my $v<caret>ar = time;");
  }

  public void testWrongLeftVariable() {
    doTestVariable("$v<caret>ar + $othervar= time;", false);
  }

  public void testVariableRightParens() {
    doTestVariable("my($v<caret>ar )= time;");
  }

  public void testVariableMultiFirst() {
    doTestVariable("my($v<caret>ar, $othervar )= time;");
  }

  public void testVariableMultiSecond() {
    doTestVariable("my($var, $other<caret>var )= time;");
  }

  public void testVariableMidSecond() {
    doTestVariable("my($base, $other) = my($var, $other<caret>var )= time;");
  }

  public void testVariableMidFirst() {
    doTestVariable("my($base, $other) = my($v<caret>ar, $othervar )= time;");
  }

  public void testOurSimpleLeftDeclaration() {
    doTestVariableDeclaration("our $v<caret>ar = time;");
  }

  public void testOurDeclarationRightParens() {
    doTestVariableDeclaration("our($v<caret>ar )= time;");
  }

  public void testOurDeclarationMultiFirst() {
    doTestVariableDeclaration("our($v<caret>ar, $othervar )= time;");
  }

  public void testOurDeclarationMultiSecond() {
    doTestVariableDeclaration("our($var, $other<caret>var )= time;");
  }

  public void testOurDeclarationMidSecond() {
    doTestVariableDeclaration("our($base, $other) = our($var, $other<caret>var )= time;");
  }

  public void testOurDeclarationMidFirst() {
    doTestVariableDeclaration("our($base, $other) = our($v<caret>ar, $othervar )= time;");
  }

  public void testOurSimpleLeftVariable() {
    doTestVariable("our $v<caret>ar = time;");
  }

  public void testOurWrongLeftVariable() {
    doTestVariable("$v<caret>ar + $othervar= time;", false);
  }

  public void testOurVariableRightParens() {
    doTestVariable("our($v<caret>ar )= time;");
  }

  public void testOurVariableMultiFirst() {
    doTestVariable("our($v<caret>ar, $othervar )= time;");
  }

  public void testOurVariableMultiSecond() {
    doTestVariable("our($var, $other<caret>var )= time;");
  }

  public void testOurVariableMidSecond() {
    doTestVariable("our($base, $other) = our($var, $other<caret>var )= time;");
  }

  public void testOurVariableMidFirst() {
    doTestVariable("our($base, $other) = our($v<caret>ar, $othervar )= time;");
  }

  public void testLocalSimpleLeftDeclaration() {
    doTestVariableDeclaration("local $v<caret>ar = time;");
  }

  public void testLocalDeclarationRightParens() {
    doTestVariableDeclaration("local($v<caret>ar )= time;");
  }

  public void testLocalDeclarationMultiFirst() {
    doTestVariableDeclaration("local($v<caret>ar, $othervar )= time;");
  }

  public void testLocalDeclarationMultiSecond() {
    doTestVariableDeclaration("local($var, $other<caret>var )= time;");
  }

  public void testLocalDeclarationMidSecond() {
    doTestVariableDeclaration("local($base, $other) = local($var, $other<caret>var )= time;");
  }

  public void testLocalDeclarationMidFirst() {
    doTestVariableDeclaration("local($base, $other) = local($v<caret>ar, $othervar )= time;");
  }

  public void testLocalSimpleLeftVariable() {
    doTestVariable("local $v<caret>ar = time;");
  }

  public void testLocalWrongLeftVariable() {
    doTestVariable("$v<caret>ar + $othervar= time;", false);
  }

  public void testLocalVariableRightParens() {
    doTestVariable("local($v<caret>ar )= time;");
  }

  public void testLocalVariableMultiFirst() {
    doTestVariable("local($v<caret>ar, $othervar )= time;");
  }

  public void testLocalVariableMultiSecond() {
    doTestVariable("local($var, $other<caret>var )= time;");
  }

  public void testLocalVariableMidSecond() {
    doTestVariable("local($base, $other) = local($var, $other<caret>var )= time;");
  }

  public void testLocalVariableMidFirst() {
    doTestVariable("local($base, $other) = local($v<caret>ar, $othervar )= time;");
  }


  public void testStateSimpleLeftDeclaration() {
    doTestVariableDeclaration("state $v<caret>ar = time;");
  }

  public void testStateDeclarationRightParens() {
    doTestVariableDeclaration("state($v<caret>ar )= time;");
  }

  public void testStateDeclarationMultiFirst() {
    doTestVariableDeclaration("state($v<caret>ar, $othervar )= time;");
  }

  public void testStateDeclarationMultiSecond() {
    doTestVariableDeclaration("state($var, $other<caret>var )= time;");
  }

  public void testStateDeclarationMidSecond() {
    doTestVariableDeclaration("state($base, $other) = state($var, $other<caret>var )= time;");
  }

  public void testStateDeclarationMidFirst() {
    doTestVariableDeclaration("state($base, $other) = state($v<caret>ar, $othervar )= time;");
  }

  public void testStateSimpleLeftVariable() {
    doTestVariable("state $v<caret>ar = time;");
  }

  public void testStateWrongLeftVariable() {
    doTestVariable("$v<caret>ar + $othervar= time;", false);
  }

  public void testStateVariableRightParens() {
    doTestVariable("state($v<caret>ar )= time;");
  }

  public void testStateVariableMultiFirst() {
    doTestVariable("state($v<caret>ar, $othervar )= time;");
  }

  public void testStateVariableMultiSecond() {
    doTestVariable("state($var, $other<caret>var )= time;");
  }

  public void testStateVariableMidSecond() {
    doTestVariable("state($base, $other) = state($var, $other<caret>var )= time;");
  }

  public void testStateVariableMidFirst() {
    doTestVariable("state($base, $other) = state($v<caret>ar, $othervar )= time;");
  }

  private void doTestVariable(@NotNull String text) {
    doTestVariable(text, true);
  }

  private void doTestSubCall(@NotNull String text) {
    doTestSubCall(text, true);
  }

  private void doTestSubCall(@NotNull String text, boolean result) {
    doTest(text, PsiPerlSubCallExpr.class, result);
  }

  private void doTestVariable(@NotNull String text, boolean result) {
    doTest(text, PerlVariable.class, result);
  }

  private void doTestVariableDeclaration(@NotNull String text) {
    doTest(text, PerlVariableDeclarationElement.class);
  }


  private void doTest(@NotNull String text, @NotNull Class<? extends PsiElement> sourceClass) {
    doTest(text, sourceClass, true);
  }

  private void doTest(@NotNull String text, @NotNull Class<? extends PsiElement> sourceClass, boolean result) {
    initWithTextSmartWithoutErrors(text);
    PsiElement elementAtCaret = getElementAtCaret(sourceClass);
    assertNotNull(elementAtCaret);
    PerlAssignExpression assignExpression = PerlAssignExpression.getAssignmentExpression(elementAtCaret);
    if (result) {
      assertNotNull(assignExpression);
    }
    else {
      assertNull(assignExpression);
    }
  }
}
