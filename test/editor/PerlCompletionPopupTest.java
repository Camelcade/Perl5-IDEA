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

package editor;

import base.PerlLightTestCase;
import com.intellij.codeInsight.lookup.LookupEx;
import com.intellij.codeInsight.lookup.LookupManager;
import com.intellij.testFramework.fixtures.CompletionAutoPopupTester;
import com.perl5.lang.perl.fileTypes.PerlFileTypePackage;
import org.jetbrains.annotations.NotNull;

public class PerlCompletionPopupTest extends PerlLightTestCase {
  protected CompletionAutoPopupTester myTester;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    myTester = new CompletionAutoPopupTester(myFixture);
  }

  @Override
  public String getFileExtension() {
    return PerlFileTypePackage.EXTENSION; // requires for package.. test
  }

  public void testUse() {doTest("use<caret>", " ");}

  public void testNo() {doTest("no<caret>", " ");}

  public void testReturns() {doTest("#@returns<caret>", " ");}

  public void testType() {doTest("#@type<caret>", " ");}

  public void testPackage() {doTest("package<caret>", " ");}

  public void testAnnotations() {doTest("#<caret>", "@");}

  public void testObjectMethod() {
    doTest("UNIVERSAL-<caret>", ">");
  }

  public void testStaticMethod() {
    doTest("CORE:<caret>", ":");
  }

  public void testScalarName() {
    doTest("\n<caret>", "$");
  }

  public void testArrayName() {
    doTest("\n<caret>", "@");
  }

  public void testHashName() {
    doTest("\n<caret>", "%");
  }

  public void testArrayIndexName() {
    doTest("\n<caret>", "$#");
  }

  @Override
  protected boolean runInDispatchThread() {
    return false;
  }

  @Override
  protected void invokeTestRunnable(@NotNull Runnable runnable) {
    myTester.runWithAutoPopupEnabled(runnable);
  }

  private void doTest(@NotNull String initial, @NotNull String toType) {
    initWithTextSmart(initial);
    myTester.typeWithPauses(toType);
    LookupEx activeLookup = LookupManager.getActiveLookup(getEditor());
    assertNotNull(activeLookup);
  }
}
