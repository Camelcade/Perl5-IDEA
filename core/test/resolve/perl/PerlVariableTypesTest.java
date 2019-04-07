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

/**
 * Created by hurricup on 02.04.2016.
 */
public class PerlVariableTypesTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/resolve/perl/variableTypes";
  }

  public void testBuiltIn() {doTest();}

  public void testDeclarationSingle() {
    doTest();
  }

  public void testDeclarationMulti() {
    doTest();
  }

  public void testDeclarationAssignmentNew() {
    doTest();
  }

  public void testVariableBeforeAssignment() {
    doTest();
  }

  public void testVariableAfterAssignment() {
    doTest();
  }

  public void testAnnotatedSingleInside() {
    doTest();
  }

  public void testAnnotatedSingle() {
    doTest();
  }

  public void testAnnotatedMulti() {
    doTest();
  }

  public void testAnnotatedMultiNonFirst() {
    doTest();
  }

  public void testAnnotatedConcurrentStatement() {
    doTest();
  }

  public void testAnnotatedConcurrentStatementOuter() {
    doTest();
  }

  public void testAnnotatedConcurrentRealTypeInside() {
    doTest();
  }

  public void testAnnotatedConcurrentRealTypeMulti() {
    doTest();
  }

  public void testAnnotatedConcurrentRealTypeSingle() {
    doTest();
  }

  public void testAnnotatedConcurrentRealTypeWins() {
    doTest();
  }

  private void doTest() {
    doTestPerlValue();
  }
}
