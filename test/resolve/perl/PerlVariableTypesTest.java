/*
 * Copyright 2016 Alexandr Evstigneev
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

package resolve.perl;

import base.PerlLightCodeInsightFixtureTestCase;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.psi.PerlVariable;

/**
 * Created by hurricup on 02.04.2016.
 */
public class PerlVariableTypesTest extends PerlLightCodeInsightFixtureTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/resolve/perl/variableTypes";
  }

  public void testDeclarationSingle() {
    doTest("declaration_single", "Foo::Bar");
  }

  public void testDeclarationMulti() {
    doTest("declaration_multi", "Foo::Bar");
  }

  public void testDeclarationAssignment() {
    doTest("declaration_assignment_new", "Foo::Bar");
  }

  public void testBeforeAssignment() {
    doTest("variable_before_assignment", null);
  }

  public void testAfterAssignment() {
    doTest("variable_after_assignment", "Foo::Bar");
  }

  public void testAnnotatedSingleInside() {
    doTest("JSON::XS");
  }

  public void testAnnotatedSingle() {
    doTest("JSON::XS");
  }

  public void testAnnotatedMulti() {
    doTest("JSON::XS");
  }

  public void testAnnotatedMultiNonFirst() {
    doTest("JSON::XS");
  }

  public void testAnnotatedConcurrentStatement() {
    doTest("JSON::XS");
  }

  public void testAnnotatedConcurrentStatementOuter() {
    doTest("JSON::XS");
  }

  public void testAnnotatedConcurrentRealTypeInside() {
    doTest("JSON::XS");
  }

  public void testAnnotatedConcurrentRealTypeMulti() {
    doTest("JSON::XS");
  }

  public void testAnnotatedConcurrentRealTypeSingle() {
    doTest("JSON::XS");
  }

  public void testAnnotatedConcurrentRealTypeWins() {
    doTest("DBI");
  }

  public void doTest(String type) {
    doTest(getTestName(true), type);
  }


  public void doTest(String filename, String type) {
    initWithFileAsScript(filename);
    PsiElement element = getElementAtCaret(PerlVariable.class);
    assertNotNull(element);
    assertInstanceOf(element, PerlVariable.class);
    assertEquals(type, ((PerlVariable)element).guessVariableType());
  }
}
