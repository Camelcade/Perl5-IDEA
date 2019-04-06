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
import com.perl5.lang.perl.idea.codeInsight.typeInferrence.value.PerlValue;
import com.perl5.lang.perl.psi.PerlVariable;

/**
 * Created by hurricup on 02.04.2016.
 */
public class PerlVariableTypesTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/resolve/perl/variableTypes";
  }

  public void testBuiltIn() {doTest("UNKNOWN_VALUE");}

  public void testDeclarationSingle() {
    doTest("Foo::Bar");
  }

  public void testDeclarationMulti() {
    doTest("Foo::Bar");
  }

  public void testDeclarationAssignmentNew() {
    doTest("Foo::Bar->new()");
  }

  public void testVariableBeforeAssignment() {
    doTest("UNKNOWN_VALUE");
  }

  public void testVariableAfterAssignment() {
    doTest("Foo::Bar->new()");
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
    initWithFileSmart(getTestName(true));
    PerlVariable element = getElementAtCaret(PerlVariable.class);
    assertNotNull(element);
    assertInstanceOf(element, PerlVariable.class);
    assertEquals(type, PerlValue.fromNonNull(element).toString());
  }
}
