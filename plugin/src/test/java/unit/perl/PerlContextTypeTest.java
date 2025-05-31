/*
 * Copyright 2015-2025 Alexandr Evstigneev
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
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.utils.PerlContextType;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
public class PerlContextTypeTest extends PerlLightTestCase {

  @Test
  public void testScalarDeclarationExpression() {
    assertScalar("my $ar<caret>ray = 123;", PerlVariableDeclarationExpr.class);
  }

  @Test
  public void testScalarDeclarationExpressionParenthesized() {
    assertList("my( $ar<caret>ray) = 123;", PerlVariableDeclarationExpr.class);
  }

  @Test
  public void testArrayDeclarationExpression() {
    assertList("my @ar<caret>ray = 123;", PerlVariableDeclarationExpr.class);
  }

  @Test
  public void testArrayDeclarationExpressionParenthesized() {
    assertList("my( @ar<caret>ray) = 123;", PerlVariableDeclarationExpr.class);
  }

  @Test
  public void testHashDeclarationExpression() {
    assertList("my %ar<caret>ray = 123;", PerlVariableDeclarationExpr.class);
  }

  @Test
  public void testHashDeclarationExpressionParenthesized() {
    assertList("my( %ar<caret>ray) = 123;", PerlVariableDeclarationExpr.class);
  }

  @Test
  public void testStringList() {
    assertList("qw/first s<caret>econd third/", PerlStringList.class);
  }

  @Test
  public void testStringListParens() {
    assertList("(qw/fi<caret>rst second third/)", PerlStringList.class);
  }

  @Test
  public void testScalar() {
    assertScalarVariable("$v<caret>ar = @_;");
  }

  @Test
  public void testScalarParens() {
    assertScalarVariable("($v<caret>ar) = @_;");
  }

  @Test
  public void testArray() {
    assertListVariable("@v<caret>ar = @_;");
  }

  @Test
  public void testArrayParens() {
    assertListVariable("(@v<caret>ar) = @_;");
  }

  @Test
  public void testArrayCast() {
    assertList("@$v<caret>ar = @_;", PsiPerlArrayCastExpr.class);
  }

  @Test
  public void testArrayCastParens() {
    assertList("(@$v<caret>ar) = @_;", PsiPerlArrayCastExpr.class);
  }

  @Test
  public void testHash() {
    assertListVariable("%v<caret>ar = @_;");
  }

  @Test
  public void testHashParens() {
    assertListVariable("(%v<caret>ar) = @_;");
  }

  @Test
  public void testHashCast() {
    assertList("%$v<caret>ar = @_;", PsiPerlHashCastExpr.class);
  }

  @Test
  public void testHashParensCast() {
    assertList("(%$v<caret>ar) = @_;", PsiPerlHashCastExpr.class);
  }

  @Test
  public void testScalarDeclaration() {
    assertScalarDeclaration("my $v<caret>ar = 123;");
  }

  @Test
  public void testArrayDeclaration() {
    assertListDeclaration("my @v<caret>ar = 123;");
  }

  @Test
  public void testHashDeclaration() {
    assertListDeclaration("my %v<caret>ar = 123;");
  }

  @Test
  public void testScalarDeclarationAttrs() {
    assertScalarDeclaration("my $v<caret>ar :lvalue = 123;");
  }

  @Test
  public void testArrayDeclarationAttrs() {
    assertListDeclaration("my @v<caret>ar :lvalue = 123;");
  }

  @Test
  public void testHashDeclarationAttrs() {
    assertListDeclaration("my %v<caret>ar :lvalue = 123;");
  }

  @Test
  public void testScalarDeclarationParens() {
    assertScalarDeclaration("my( $v<caret>ar )= 123;");
  }

  @Test
  public void testArrayDeclarationParens() {
    assertListDeclaration("my( @v<caret>ar) = 123;");
  }

  @Test
  public void testHashDeclarationParens() {
    assertListDeclaration("my( %v<caret>ar) = 123;");
  }

  @Test
  public void testScalarDeclarationParensAttr() {
    assertScalarDeclaration("my( $v<caret>ar ): lvalue = 123;");
  }

  @Test
  public void testArrayDeclarationParensAttrs() {
    assertListDeclaration("my( @v<caret>ar) :lvalue = 123;");
  }

  @Test
  public void testHashDeclarationParensAttrs() {
    assertListDeclaration("my( %v<caret>ar) :lvalue = 123;");
  }

  private void assertScalarDeclaration(@NotNull String text) {
    assertScalar(text, PerlVariableDeclarationElement.class);
    assertScalarVariable(text);
  }

  private void assertListDeclaration(@NotNull String text) {
    assertList(text, PerlVariableDeclarationElement.class);
    assertListVariable(text);
  }

  private void assertScalarVariable(@NotNull String text) {
    assertScalar(text, PerlVariable.class);
  }

  private void assertListVariable(@NotNull String text) {
    assertList(text, PerlVariable.class);
  }

  private void assertScalar(@NotNull String text, @NotNull Class<? extends PsiElement> clazz) {
    doTest(text, clazz, PerlContextType.SCALAR);
  }

  private void assertList(@NotNull String text, @NotNull Class<? extends PsiElement> clazz) {
    doTest(text, clazz, PerlContextType.LIST);
  }

  private void doTest(@NotNull String text, @NotNull Class<? extends PsiElement> clazz, @NotNull PerlContextType expectedType) {
    initWithTextSmartWithoutErrors(text);
    PsiElement elementAtCaret = getElementAtCaret(clazz);
    assertNotNull(elementAtCaret);
    assertEquals(expectedType + "\n" + text, PerlContextType.from(elementAtCaret) + "\n" + text);
  }
}
