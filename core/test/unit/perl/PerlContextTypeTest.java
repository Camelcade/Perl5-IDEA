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
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.utils.PerlContextType;
import org.jetbrains.annotations.NotNull;

public class PerlContextTypeTest extends PerlLightTestCase {

  public void testStringList() {
    assertList("qw/first s<caret>econd third/", PerlStringList.class);
  }

  public void testStringListParens() {
    assertList("(qw/fi<caret>rst second third/)", PerlStringList.class);
  }

  public void testScalar() {
    assertScalarVariable("$v<caret>ar = @_;");
  }

  public void testScalarParens() {
    assertListVariable("($v<caret>ar) = @_;");
  }

  public void testArray() {
    assertListVariable("@v<caret>ar = @_;");
  }

  public void testArrayParens() {
    assertListVariable("(@v<caret>ar) = @_;");
  }

  public void testArrayCast() {
    assertList("@$v<caret>ar = @_;", PsiPerlArrayCastExpr.class);
  }

  public void testArrayCastParens() {
    assertList("(@$v<caret>ar) = @_;", PsiPerlArrayCastExpr.class);
  }

  public void testHash() {
    assertListVariable("%v<caret>ar = @_;");
  }

  public void testHashParens() {
    assertListVariable("(%v<caret>ar) = @_;");
  }

  public void testHashCast() {
    assertList("%$v<caret>ar = @_;", PsiPerlHashCastExpr.class);
  }

  public void testHashParensCast() {
    assertList("(%$v<caret>ar) = @_;", PsiPerlHashCastExpr.class);
  }

  public void testScalarDeclaration() {
    assertScalarDeclaration("my $v<caret>ar = 123;");
  }

  public void testArrayDeclaration() {
    assertListDeclaration("my @v<caret>ar = 123;");
  }

  public void testHashDeclaration() {
    assertListDeclaration("my %v<caret>ar = 123;");
  }

  public void testScalarDeclarationAttrs() {
    assertScalarDeclaration("my $v<caret>ar :lvalue = 123;");
  }

  public void testArrayDeclarationAttrs() {
    assertListDeclaration("my @v<caret>ar :lvalue = 123;");
  }

  public void testHashDeclarationAttrs() {
    assertListDeclaration("my %v<caret>ar :lvalue = 123;");
  }

  public void testScalarDeclarationParens() {
    assertListDeclaration("my( $v<caret>ar )= 123;");
  }

  public void testArrayDeclarationParens() {
    assertListDeclaration("my( @v<caret>ar) = 123;");
  }

  public void testHashDeclarationParens() {
    assertListDeclaration("my( %v<caret>ar) = 123;");
  }

  public void testScalarDeclarationParensAttr() {
    assertListDeclaration("my( $v<caret>ar ): lvalue = 123;");
  }

  public void testArrayDeclarationParensAttrs() {
    assertListDeclaration("my( @v<caret>ar) :lvalue = 123;");
  }

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

  private void assertVoid(@NotNull String text, @NotNull Class<? extends PsiElement> clazz) {
    doTest(text, clazz, PerlContextType.VOID);
  }

  private void doTest(@NotNull String text, @NotNull Class<? extends PsiElement> clazz, @NotNull PerlContextType expectedType) {
    initWithTextSmartWithoutErrors(text);
    PsiElement elementAtCaret = getElementAtCaret(clazz);
    assertNotNull(elementAtCaret);
    assertEquals(expectedType, PerlContextType.from(elementAtCaret));
  }
}
