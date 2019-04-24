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

package base;

import com.intellij.codeInsight.lookup.LookupEx;
import com.intellij.codeInsight.lookup.LookupManager;
import com.intellij.testFramework.fixtures.CompletionAutoPopupTester;
import org.jetbrains.annotations.NotNull;

public abstract class PerlCompletionPopupTestCase extends PerlLightTestCaseBase {
  protected CompletionAutoPopupTester myTester;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    myTester = new CompletionAutoPopupTester(myFixture);
  }

  @Override
  protected boolean runInDispatchThread() {
    return false;
  }

  @Override
  protected void invokeTestRunnable(@NotNull Runnable runnable) {
    myTester.runWithAutoPopupEnabled(runnable);
  }

  protected void doTest(@NotNull String initial, @NotNull String toType) {
    initWithTextSmart(initial);
    doCheckAutopopupResult(toType, true);
  }

  protected void doTest(@NotNull String toType) {
    initWithFileSmart();
    doCheckAutopopupResult(toType, true);
  }

  protected void doTestNegative(@NotNull String initial, @NotNull String toType) {
    initWithTextSmart(initial);
    doCheckAutopopupResult(toType, false);
  }

  protected void doTestNegative(@NotNull String toType) {
    initWithFileSmart();
    doCheckAutopopupResult(toType, false);
  }

  protected void doCheckAutopopupResult(@NotNull String type, boolean result) {
    myTester.typeWithPauses(type);
    LookupEx activeLookup = LookupManager.getActiveLookup(getEditor());
    if (result) {
      assertNotNull(activeLookup);
    }
    else {
      assertNull(activeLookup);
    }
  }
}
