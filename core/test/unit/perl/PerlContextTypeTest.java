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
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.utils.PerlContextType;
import org.jetbrains.annotations.NotNull;

public class PerlContextTypeTest extends PerlLightTestCase {

  public void testScalarDeclaration() {
    String data = "my $v<caret>ar = 123;";
    assertScalarVariable(data);
    assertScalarDeclaration(data);
  }


  private void assertScalarDeclaration(@NotNull String text) {
    assertScalar(text, PerlVariableDeclarationElement.class);
  }

  private void assertListDeclaration(@NotNull String text) {
    assertList(text, PerlVariableDeclarationElement.class);
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
