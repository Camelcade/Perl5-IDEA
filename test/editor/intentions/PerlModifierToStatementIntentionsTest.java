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

package editor.intentions;

import com.intellij.openapi.util.io.FileUtil;
import com.perl5.PerlBundle;

public class PerlModifierToStatementIntentionsTest extends PerlIntentionsTestCase {

  @Override
  protected String getTestDataPath() {
    return FileUtil.join(super.getTestDataPath(), "modifierToStatement");
  }

  public void testModifierToStatementFor() {doTest();}

  public void testModifierToStatementForeach() {doTest();}

  public void testModifierToStatementIf() {doTest();}

  public void testModifierToStatementIfDo() {doTest();}

  public void testModifierToStatementIfEmpty() {doTest(false);}

  public void testModifierToStatementIfParenthesized() {doTest();}

  public void testModifierToStatementUnless() {doTest();}

  public void testModifierToStatementUntil() {doTest();}

  public void testModifierToStatementWhen() {doTest();}

  public void testModifierToStatementWhile() {doTest();}

  public void testModifierToStatementWithError() {doTestNoIntention(PerlBundle.message("perl.intention.convert.to.compound"));}

  private void doTest(boolean checkErrors) {
    doTestIntention(PerlBundle.message("perl.intention.convert.to.compound"), checkErrors);
  }

  private void doTest() {doTest(true);}
}
