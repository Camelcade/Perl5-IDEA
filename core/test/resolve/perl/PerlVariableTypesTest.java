/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

import base.PerlLightTestCase;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.types.PerlType;
import com.perl5.lang.perl.types.PerlTypeArray;
import com.perl5.lang.perl.types.PerlTypeArrayRef;
import com.perl5.lang.perl.types.PerlTypeNamespace;

/**
 * Created by hurricup on 02.04.2016.
 */
public class PerlVariableTypesTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/resolve/perl/variableTypes";
  }

  public void testBuiltIn() {doTest(new PerlType[]{null});}

  public void testDeclarationSingle() {
    doTest("declaration_single", new PerlTypeNamespace("Foo::Bar"));
  }

  public void testDeclarationMulti() {
    doTest("declaration_multi", new PerlTypeNamespace("Foo::Bar"));
  }

  public void testDeclarationAssignment() {
    doTest("declaration_assignment_new", new PerlTypeNamespace("Foo::Bar"));
  }

  public void testDeclarationExprAssignment() {
    doTest("declaration_assignment_expr", new PerlTypeNamespace("Foo::Bar"));
  }

  public void testBeforeAssignment() {
    doTest("variable_before_assignment", new PerlType[]{null});
  }

  public void testAfterAssignment() {
    doTest("variable_after_assignment", new PerlTypeNamespace("Foo::Bar"));
  }

  public void testAnnotatedSingleInside() {
    doTest(new PerlTypeNamespace("JSON::XS"));
  }

  public void testAnnotatedSingle() {
    doTest(new PerlTypeNamespace("JSON::XS"));
  }

  public void testAnnotatedMulti() {
    doTest(new PerlTypeNamespace("JSON::XS"));
  }

  public void testAnnotatedMultiNonFirst() {
    doTest(new PerlTypeNamespace("JSON::XS"));
  }

  public void testAnnotatedConcurrentStatement() {
    doTest(new PerlTypeNamespace("JSON::XS"));
  }

  public void testAnnotatedConcurrentStatementOuter() {
    doTest(new PerlTypeNamespace("JSON::XS"));
  }

  public void testAnnotatedConcurrentRealTypeInside() {
    doTest(new PerlTypeNamespace("JSON::XS"));
  }

  public void testAnnotatedConcurrentRealTypeMulti() {
    doTest(new PerlTypeNamespace("JSON::XS"));
  }

  public void testAnnotatedConcurrentRealTypeSingle() {
    doTest(new PerlTypeNamespace("JSON::XS"));
  }

  public void testAnnotatedConcurrentRealTypeWins() {
    doTest(new PerlTypeNamespace("DBI"));
  }

  public void testTypeOfDereferencedArrayRef() {
    doTest(
      new PerlTypeArray(new PerlTypeNamespace("JSON::XS")),
      new PerlTypeArray(new PerlTypeNamespace("JSON::XS")),
      new PerlTypeArray(new PerlTypeNamespace("JSON::XS")),
      new PerlTypeArray(new PerlTypeNamespace("JSON::XS")),
      new PerlTypeArray(new PerlTypeNamespace("JSON::XS"))
    );
  }

  public void testTypeOfReferencedArray() {
    doTest(new PerlTypeArrayRef(new PerlTypeNamespace("JSON::XS")));
  }

  public void testElementTypeOfArrayRef() {
    doTest(new PerlTypeNamespace("JSON::XS"));
  }

  public void testElementTypeOfArray() {
    doTest(new PerlTypeNamespace("JSON::XS"));
  }

  public void testIteratorTypeOfForStatement() {
    doTest(
      new PerlTypeNamespace("JSON::XS"),
      null
    );
  }

  public void testDefaultVariableTypeOfForStatement() {
    doTest(
      new PerlTypeNamespace("Foo"),
      new PerlTypeNamespace("Foo")
    );
  }

  public void testDefaultVariableTypeOfMap() {
    doTest(
      new PerlTypeNamespace("JSON::XS"),
      new PerlTypeNamespace("JSON::XS"),
      new PerlTypeNamespace("JSON::XS"),
      new PerlTypeNamespace("JSON::XS"),
      new PerlTypeNamespace("JSON::XS"),
      new PerlTypeNamespace("JSON::XS")
    );
  }

  public void testDefaultVariableTypeOfGrep() {
    doTest(
      new PerlTypeNamespace("JSON::XS"),
      new PerlTypeNamespace("JSON::XS"),
      new PerlTypeNamespace("JSON::XS"),
      new PerlTypeNamespace("JSON::XS"),
      new PerlTypeNamespace("JSON::XS"),
      new PerlTypeNamespace("JSON::XS")
    );
  }

  public void testVariableTypeInSortBlock() {
    doTest(
      new PerlTypeNamespace("JSON::XS"),
      new PerlTypeNamespace("JSON::XS")
    );
  }

  public void testTypeOfMap() {
    doTest(
      new PerlTypeArray(new PerlTypeNamespace("Foo::Bar")),
      new PerlTypeArray(new PerlTypeNamespace("Foo::Bar")),
      new PerlTypeArray(new PerlTypeNamespace("Foo::Bar"))
    );
  }

  public void testTypeOfGrep() {
    doTest(
      new PerlTypeArray(new PerlTypeNamespace("Foo::Bar")),
      new PerlTypeArray(new PerlTypeNamespace("Foo::Bar")),
      new PerlTypeArray(new PerlTypeNamespace("Foo::Bar"))
    );
  }

  public void testTypeOfSort() {
    doTest(
      new PerlTypeArray(new PerlTypeNamespace("JSON::XS")),
      new PerlTypeArray(new PerlTypeNamespace("JSON::XS")),
      new PerlTypeArray(new PerlTypeNamespace("JSON::XS"))
    );
  }

  public void doTest(PerlType... types) {
    doTest(getTestName(true), types);
  }

  public void doTest(String filename, PerlType... types) {
    initWithFileSmart(filename);
    for (PerlType type : types) {
      PsiElement element = getElementAtCaret(PerlVariable.class);
      assertNotNull(element);
      assertInstanceOf(element, PerlVariable.class);
      PerlType guessedType = ((PerlVariable)element).guessVariableType();
      assertEquals(type, guessedType);

      moveCaretToNextLine();
    }
  }
}
