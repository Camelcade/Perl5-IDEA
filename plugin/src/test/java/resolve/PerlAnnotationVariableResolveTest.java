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

package resolve;

import base.PerlLightTestCase;
import org.junit.Test;

public class PerlAnnotationVariableResolveTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "resolve/perl/annotation_variable";
  }

  @Test
  public void testArrayDeclarationMy() { doTest(); }

  @Test
  public void testHashDeclarationMy() { doTest(); }

  @Test
  public void testScalarDeclarationFuncSignature() { doTest(); }

  @Test
  public void testScalarDeclarationLocal() { doTest(); }

  @Test
  public void testScalarDeclarationMethodSignature() { doTest(); }

  @Test
  public void testScalarDeclarationMy() { doTest(); }

  @Test
  public void testScalarDeclarationMyBlock() { doTest(); }

  @Test
  public void testScalarDeclarationMyCommentAfter() { doTest(); }

  @Test
  public void testScalarDeclarationMyExpr() { doTest(); }

  @Test
  public void testScalarDeclarationMyExprMulti() { doTest(); }

  @Test
  public void testScalarDeclarationMyExprTrailingComment() { doTest(); }

  @Test
  public void testScalarDeclarationMyExprWrong() { doTest(); }

  @Test
  public void testScalarDeclarationMyForeach() { doTest(); }

  @Test
  public void testScalarDeclarationMyNewLine() { doTest(); }

  @Test
  public void testScalarDeclarationMySecondAssignment() { doTest(); }

  @Test
  public void testScalarDeclarationMyThirdMultiline() { doTest(); }

  @Test
  public void testScalarDeclarationMyTrailingComment() { doTest(); }

  @Test
  public void testScalarDeclarationOur() { doTest(); }

  @Test
  public void testScalarDeclarationState() { doTest(); }

  @Test
  public void testScalarDeclarationSubSignature() { doTest(); }

  @Test
  public void testScalarDeclarationSubSignatureAsync() { doTest(); }

  @Test
  public void testScalarDeclarationSubSignatureMultiline() { doTest(); }

  private void doTest() {
    doTestResolve();
  }
}
