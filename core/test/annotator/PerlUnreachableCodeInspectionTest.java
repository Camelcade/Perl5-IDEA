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

package annotator;

import base.PerlLightTestCase;
import com.perl5.lang.perl.idea.inspections.PerlUnreachableCodeInspection;

public class PerlUnreachableCodeInspectionTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/annotator/perl/unreachableCode";
  }

  public void testReturnConfessIf() {doTest();}

  public void testPushXorNext() {doTest();}

  public void testPackageSubPackage() {doTest();}

  public void testExitFromInnerSub() {doTest();}

  public void testExitFromInnerFunc() {doTest();}

  public void testExitFromInnerMethod() {doTest();}

  public void testGotoFromInnerSub() {doTest();}

  public void testGotoFromInnerFunc() {doTest();}

  public void testGotoFromInnerMethod() {doTest();}

  public void testDeleteInList() {doTest();}

  public void testUnreachableReturnWithLpLogic() {doTest();}

  public void testUnreachableGotoLabel() {doTest();}

  public void testUnreachableDieWithHeredoc() {doTest();}

  public void testUnreachableDbReturn() {doTest();}

  public void testUnreachableInnerSub() {doTest();}

  public void testUnreachableDereference() {doTest();}

  public void testUnreachableCommaSequence() {doTest();}

  public void testUnreachableCommaSequenceConditional() {doTest();}

  public void testUnreachableCommaSequenceParens() {doTest();}

  public void testUnreachableCommaSequenceParensConditional() {doTest();}

  private void doTest() {
    doInspectionTest(PerlUnreachableCodeInspection.class);
  }
}
