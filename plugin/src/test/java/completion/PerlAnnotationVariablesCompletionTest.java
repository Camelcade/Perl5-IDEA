/*
 * Copyright 2015-2022 Alexandr Evstigneev
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

package completion;


import org.junit.Test;

public class PerlAnnotationVariablesCompletionTest extends PerlCompletionTestCase {

  @Override
  protected String getBaseDataPath() {
    return "completion/perl/annotation_variables";
  }

  @Test
  public void testForIterator() { doTest(); }

  @Test
  public void testFuncDefinition() { doTest(); }

  @Test
  public void testLocalDeclarationMulti() { doTest(); }

  @Test
  public void testMethodDefinition() { doTest(); }

  @Test
  public void testMyDeclarationMulti() { doTest(); }

  @Test
  public void testMyDeclarationMultiAssigned() { doTest(); }

  @Test
  public void testMyDeclarationMultiMultiline() { doTest(); }

  @Test
  public void testMyDeclarationMultiTypedStrict() { doTest(); }

  @Test
  public void testMyDeclarationMultiTypedStrictAfter() { doTest(); }

  @Test
  public void testMyDeclarationMultiTypedWildcard() { doTest(); }

  @Test
  public void testMyDeclarationMultiWithSigilArray() { doTest(); }

  @Test
  public void testMyDeclarationMultiWithSigilArrayAndType() { doTest(); }

  @Test
  public void testMyDeclarationMultiWithSigilHash() { doTest(); }

  @Test
  public void testMyDeclarationMultiWithSigilHashAndType() { doTest(); }

  @Test
  public void testMyDeclarationMultiWithSigilScalar() { doTest(); }

  @Test
  public void testMyDeclarationMultiWithSigilScalarAndType() { doTest(); }

  @Test
  public void testMyDeclarationMultiWithType() { doTest(); }

  @Test
  public void testMyDeclarationSingle() { doTest(); }

  @Test
  public void testOurDeclarationMulti() { doTest(); }

  @Test
  public void testStateDeclarationMulti() { doTest(); }

  @Test
  public void testSubDeclaration() { doTest(); }

  @Test
  public void testSubDefinition() { doTest(); }

  @Test
  public void testSubDefinitionMultiline() { doTest(); }

  protected void doTest() {
    doTestCompletion();
  }
}
