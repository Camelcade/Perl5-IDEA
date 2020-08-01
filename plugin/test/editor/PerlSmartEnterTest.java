/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package editor;

import base.PerlLightTestCase;
import com.intellij.codeInsight.editorActions.smartEnter.SmartEnterAction;
import com.intellij.testFramework.UsefulTestCase;
import org.junit.Test;

public class PerlSmartEnterTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/smartEnter/perl";
  }

  @Test
  public void testHeredocOpener() {doTest();}

  @Test
  public void testAfterSig() {doTest();}

  @Test
  public void testAroundSig() {doTest();}

  @Test
  public void testAugmentSig() {doTest();}

  @Test
  public void testBeforeSig() {doTest();}

  @Test
  public void testForCondition() {doTest();}

  @Test
  public void testForIncomplete() {doTest();}

  @Test
  public void testFuncSig() {doTest();}

  @Test
  public void testFunSig() {doTest();}

  @Test
  public void testGivenCondition() {doTest();}

  @Test
  public void testIfCondition() {doTest();}

  @Test
  public void testElsifCondition() {doTest();}

  @Test
  public void testMethodSig() {doTest();}

  @Test
  public void testNestedStatements() {doTest();}

  @Test
  public void testNoStatement() {doTest();}

  @Test
  public void testOverrideSig() {doTest();}

  @Test
  public void testStatementCallParams() {doTest();}

  @Test
  public void testStatementEnd() {doTest();}

  @Test
  public void testStatementString() {doTest();}

  @Test
  public void testSubDeclaration() {doTest();}

  @Test
  public void testSubDeclarationPrototype() {doTest();}

  @Test
  public void testSubDeclarationSignature() {doTest();}

  @Test
  public void testUnlessCondition() {doTest();}

  @Test
  public void testUntilCondition() {doTest();}

  @Test
  public void testUseStatement() {doTest();}

  @Test
  public void testWhenCondition() {doTest();}

  @Test
  public void testWhileCondition() {doTest();}

  private void doTest() {
    initWithFileSmart();
    myFixture.testAction(new SmartEnterAction());
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), getEditorTextWithCaretsAndSelections());
  }
}
