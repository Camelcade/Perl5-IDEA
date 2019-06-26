/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package annotator;


import base.PerlLightTestCase;
import com.perl5.lang.perl.idea.inspections.PerlUnreachableCodeInspection;
import org.junit.Test;
public class PerlUnreachableCodeInspectionTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/annotator/perl/unreachableCode";
  }

  @Test
  public void testLastEval() {doTest();}

  @Test
  public void testLastEvalWithLabel() {doTest();}

  @Test
  public void testLpLogicAfterComma() {doTest();}

  @Test
  public void testFlowWithDereference() {doTest();}

  @Test
  public void testReturnGoto() {doTest();}

  @Test
  public void testReturnConfessIf() {doTest();}

  @Test
  public void testPushXorNext() {doTest();}

  @Test
  public void testPackageSubPackage() {doTest();}

  @Test
  public void testGotoFromInnerEval() {doTest();}

  @Test
  public void testTryCatchOrInFor() {doTest();}

  @Test
  public void testExitFromInnerSub() {doTest();}

  @Test
  public void testExitFromInnerFunc() {doTest();}

  @Test
  public void testExitFromInnerMethod() {doTest();}

  @Test
  public void testGotoAfterAnonSub() {doTest();}

  @Test
  public void testGotoFromInnerSub() {doTest();}

  @Test
  public void testGotoFromInnerFunc() {doTest();}

  @Test
  public void testGotoFromInnerMethod() {doTest();}

  @Test
  public void testDeleteInList() {doTest();}

  @Test
  public void testUnreachableReturnWithLpLogic() {doTest();}

  @Test
  public void testUnreachableGotoLabel() {doTest();}

  @Test
  public void testUnreachableDieWithHeredoc() {doTest();}

  @Test
  public void testUnreachableDbReturn() {doTest();}

  @Test
  public void testUnreachableInnerSub() {doTest();}

  @Test
  public void testUnreachableDereference() {doTest();}

  @Test
  public void testUnreachableCommaSequence() {doTest();}

  @Test
  public void testUnreachableCommaSequenceConditional() {doTest();}

  @Test
  public void testUnreachableCommaSequenceParens() {doTest();}

  @Test
  public void testUnreachableCommaSequenceParensConditional() {doTest();}

  private void doTest() {
    doInspectionTest(PerlUnreachableCodeInspection.class);
  }
}
