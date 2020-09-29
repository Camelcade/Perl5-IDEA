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

package base;

import com.intellij.codeInsight.lookup.LookupEx;
import com.intellij.codeInsight.lookup.LookupManager;
import com.intellij.codeInsight.template.impl.editorActions.ExpandLiveTemplateByTabAction;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.testFramework.fixtures.CompletionAutoPopupTester;
import com.intellij.util.ThrowableRunnable;
import org.jetbrains.annotations.NotNull;

public abstract class PerlCompletionPopupTestCase extends PerlLightTestCaseBase {
  protected CompletionAutoPopupTester myTester;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    myTester = new CompletionAutoPopupTester(myFixture);
  }

  @Override
  protected final boolean runInDispatchThread() {
    return false;
  }

  @Override
  protected void runTestRunnable(@NotNull ThrowableRunnable<Throwable> testRunnable) throws Throwable {
    myTester.runWithAutoPopupEnabled(testRunnable);
  }

  protected void doTest(@NotNull String initial, @NotNull String toType) {
    initWithTextSmart(initial);
    doCompletionOnTypingTest(toType, true);
  }

  protected void doTestPopupAfterCompletion(@NotNull String initial,
                                            @NotNull String lookupToChoose,
                                            boolean shouldPresent) {
    initWithTextSmart(initial);
    doCompleteLookupString(lookupToChoose, getCompletionType(), getCompletionInvocationCount(), getCompletionCompleteChar());
    myTester.joinAutopopup();// for the autopopup handler's alarm, or the restartCompletion's invokeLater
    myTester.joinCompletion();
    if (shouldPresent) {
      assertNotNull(LookupManager.getActiveLookup(getEditor()));
    }
    else {
      assertNull(LookupManager.getActiveLookup(getEditor()));
    }
  }

  protected void doTest(@NotNull String toType) {
    initWithFileSmart();
    doCompletionOnTypingTest(toType, true);
  }

  protected void doTestNegative(@NotNull String initial, @NotNull String toType) {
    initWithTextSmart(initial);
    doCompletionOnTypingTest(toType, false);
  }

  protected void doTestNegative(@NotNull String toType) {
    initWithFileSmart();
    doCompletionOnTypingTest(toType, false);
  }

  protected void doCompletionOnTypingTest(@NotNull String textToType, boolean result) {
    addVirtualFileFilter();
    myTester.typeWithPauses(textToType);
    LookupEx activeLookup = LookupManager.getActiveLookup(getEditor());
    if (result) {
      assertNotNull(activeLookup);
    }
    else {
      assertNull(activeLookup);
    }
  }

  /**
   * fixme Probably this should be smarter and iterate through variables. But we need only one for now
   */
  protected void doLiveTemplateVariablePopupTest(@NotNull String text) {
    initWithTextSmartWithoutErrors(text);
    ApplicationManager.getApplication().invokeAndWait(() -> myFixture.testAction(new ExpandLiveTemplateByTabAction()));
    myTester.joinAutopopup();
    myTester.joinCompletion();
    assertNotNull(LookupManager.getActiveLookup(getEditor()));
  }
}
