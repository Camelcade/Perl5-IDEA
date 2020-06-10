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

package com.perl5.lang.perl.idea.completion.providers.processors;

import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.registry.Registry;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PerlCompletionProcessorBase extends AbstractPerlCompletionProcessor {
  private final @NotNull CompletionResultSet myResultSet;
  private final @NotNull PsiElement myLeafElement;
  private final @NotNull Counters myCounters;

  protected PerlCompletionProcessorBase(@NotNull CompletionResultSet resultSet,
                                        @NotNull PsiElement leafElement) {
    this(resultSet, leafElement, new Counters());
  }

  protected PerlCompletionProcessorBase(@NotNull PerlCompletionProcessor completionProcessor) {
    myResultSet = completionProcessor.getResultSet();
    myLeafElement = completionProcessor.getLeafElement();
    PerlCompletionProcessor run = completionProcessor;
    while (run instanceof PerlDelegatingCompletionProcessor) {
      run = ((PerlDelegatingCompletionProcessor<?>)run).getDelegate();
    }
    LOG.assertTrue(run instanceof PerlCompletionProcessorBase, "Got " + run);
    myCounters = ((PerlCompletionProcessorBase)run).myCounters;
  }

  protected PerlCompletionProcessorBase(@NotNull PerlCompletionProcessorBase original,
                                        @NotNull String newPrefixMatcher) {
    this(original.getResultSet().withPrefixMatcher(newPrefixMatcher),
         original.getLeafElement(),
         original.getCounters());
  }

  private PerlCompletionProcessorBase(@NotNull CompletionResultSet resultSet,
                                      @NotNull PsiElement leafElement,
                                      @NotNull Counters counters) {
    myResultSet = resultSet;
    myLeafElement = leafElement;
    myCounters = counters;
  }

  private @NotNull Counters getCounters() {
    return myCounters;
  }

  @Override
  public @NotNull CompletionResultSet getResultSet() {
    return myResultSet;
  }

  @Override
  public @NotNull PsiElement getLeafElement() {
    return myLeafElement;
  }

  @Override
  public void addElement(@NotNull LookupElementBuilder lookupElement) {
    myCounters.countProcessing();
    getResultSet().addElement(lookupElement);
    if (myCounters.exactLimit()) {
      myResultSet.restartCompletionOnAnyPrefixChange();
    }
  }

  @Override
  public boolean matches(@Nullable String suggestedName) {
    myCounters.countMatching();
    return super.matches(suggestedName);
  }

  @Override
  public boolean result() {
    return myCounters.result();
  }

  @Override
  public void logStatus(@NotNull Class<?> clazz) {
    myCounters.logStatus(clazz);
  }

  private static class Counters {
    private final int myLimit = Registry.intValue("ide.completion.variant.limit");

    private int myProcessCounter = 0;
    private int myMatchingCounter = 0;
    private long myFirstTime = -1;
    private long myLastTime = -1;

    void countProcessing() {
      updateTimes();
      myProcessCounter++;
      LOG.assertTrue(myProcessCounter <= myMatchingCounter, "Adding element without checking");
    }

    private void updateTimes() {
      if (myFirstTime < 0) {
        myFirstTime = System.currentTimeMillis();
      }
      myLastTime = System.currentTimeMillis();
    }

    void countMatching() {
      updateTimes();
      myMatchingCounter++;
    }

    boolean result() {
      return myProcessCounter < myLimit / 2;
    }

    boolean exactLimit() {
      return myProcessCounter == myLimit / 2;
    }

    void logStatus(@NotNull Class<?> clazz) {

      if (LOG.isDebugEnabled() || ApplicationManager.getApplication().isUnitTestMode()) {
        LOG.debug(clazz.getSimpleName() +
                  " checked for matching " + myMatchingCounter +
                  ", processed " + myProcessCounter +
                  ", in " + (myLastTime - myFirstTime) + " ms");
      }
    }
  }
}
