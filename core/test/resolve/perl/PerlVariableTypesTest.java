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
import com.perl5.lang.perl.psi.PerlVariable;

/**
 * Created by hurricup on 02.04.2016.
 */
public class PerlVariableTypesTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/resolve/perl/variableTypes";
  }

  public void testBuiltIn() {doTest("Value: UNKNOWN");}

  public void testDeclarationSingle() {
    doTest("Value: Foo::Bar");
  }

  public void testDeclarationMulti() {
    doTest("Value: Foo::Bar");
  }

  public void testDeclarationAssignmentNew() {
    doTest("Object: Value: Foo::Bar->Value: new");
  }

  public void testVariableBeforeAssignment() {
    doTest("Value: UNKNOWN");
  }

  public void testVariableAfterAssignment() {
    doTest("Object: Value: Foo::Bar->Value: new");
  }

  public void testAnnotatedSingleInside() {
    doTest("Value: JSON::XS");
  }

  public void testAnnotatedSingle() {
    doTest("Value: JSON::XS");
  }

  public void testAnnotatedMulti() {
    doTest("Value: JSON::XS");
  }

  public void testAnnotatedMultiNonFirst() {
    doTest("Value: JSON::XS");
  }

  public void testAnnotatedConcurrentStatement() {
    doTest("Value: JSON::XS");
  }

  public void testAnnotatedConcurrentStatementOuter() {
    doTest("Value: JSON::XS");
  }

  public void testAnnotatedConcurrentRealTypeInside() {
    doTest("Value: JSON::XS");
  }

  public void testAnnotatedConcurrentRealTypeMulti() {
    doTest("Value: JSON::XS");
  }

  public void testAnnotatedConcurrentRealTypeSingle() {
    doTest("Value: JSON::XS");
  }

  public void testAnnotatedConcurrentRealTypeWins() {
    doTest("Value: DBI");
  }

  public void doTest(String type) {
    initWithFileSmart(getTestName(true));
    PerlVariable element = getElementAtCaret(PerlVariable.class);
    assertNotNull(element);
    assertInstanceOf(element, PerlVariable.class);
    assertEquals(type, element.getPerlValue().toString());
  }
}
