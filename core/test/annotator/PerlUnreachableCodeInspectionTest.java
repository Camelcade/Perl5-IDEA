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

  public void testGotoFromInnerSub() {doTestUnreachableCode();}

  public void testGotoFromInnerFunc() {doTestUnreachableCode();}

  public void testGotoFromInnerMethod() {doTestUnreachableCode();}

  public void testDeleteInList() {doTestUnreachableCode();}

  public void testUnreachableReturnWithLpLogic() {doTestUnreachableCode();}

  public void testUnreachableGotoLabel() {doTestUnreachableCode();}

  public void testUnreachableDieWithHeredoc() {doTestUnreachableCode();}

  public void testUnreachableDbReturn() {doTestUnreachableCode();}

  public void testUnreachableInnerSub() {doTestUnreachableCode();}

  public void testUnreachableDereference() {doTestUnreachableCode();}

  public void testUnreachableCommaSequence() {doTestUnreachableCode();}

  public void testUnreachableCommaSequenceConditional() {doTestUnreachableCode();}

  public void testUnreachableCommaSequenceParens() {doTestUnreachableCode();}

  public void testUnreachableCommaSequenceParensConditional() {doTestUnreachableCode();}

  private void doTestUnreachableCode() {
    doInspectionTest(PerlUnreachableCodeInspection.class);
  }
}
