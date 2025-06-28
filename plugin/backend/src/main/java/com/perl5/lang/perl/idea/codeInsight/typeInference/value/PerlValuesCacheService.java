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

package com.perl5.lang.perl.idea.codeInsight.typeInference.value;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.RecursionManager;
import com.intellij.psi.util.PsiModificationTracker;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;

public class PerlValuesCacheService implements PsiModificationTracker.Listener, Disposable {
  private static final Logger LOG = Logger.getInstance(PerlValuesCacheService.class);
  private final @NotNull Map<Pair<PerlValue, PerlValueResolver>, PerlValue> myResolveMap = ContainerUtil.createConcurrentWeakMap();

  private final AtomicLong myResolveRequests = new AtomicLong();
  private final AtomicLong myResolveBuilds = new AtomicLong();

  public PerlValuesCacheService(@NotNull Project project) {
    project.getMessageBus().connect(this).subscribe(PsiModificationTracker.TOPIC, this);
  }

  @Override
  public void dispose() {

  }

  public @NotNull PerlValue getResolvedValue(@NotNull PerlValue deferredValue, @NotNull PerlValueResolver resolver) {
    Pair<PerlValue, PerlValueResolver> key = Pair.create(deferredValue, resolver);
    myResolveRequests.incrementAndGet();
    PerlValue result = myResolveMap.get(key);
    if (result != null) {
      return result;
    }
    myResolveBuilds.incrementAndGet();
    PerlValue resolvedValue = RecursionManager.doPreventingRecursion(
      key, true, () -> PerlValuesManager.intern(deferredValue.computeResolve(resolver)));
    if (resolvedValue == null) {
      // fixme probably we could use this for recursion prevention. Actually, this may happen because of flaws of our loops/conditions handling
      return UNKNOWN_VALUE;
    }
    myResolveMap.putIfAbsent(key, resolvedValue);
    return resolvedValue;
  }

  @Override
  public void modificationCountChanged() {
    myResolveMap.clear();

    if (myResolveRequests.get() > 0 && LOG.isDebugEnabled()) {
      LOG.debug(String.format("Value resolve effectiveness: %d, %d, %d", myResolveRequests.get(), myResolveBuilds.get(),
                              (myResolveRequests.get() - myResolveBuilds.get()) * 100 / myResolveRequests.get()));
    }
    myResolveRequests.set(0);
    myResolveBuilds.set(0);
  }

  public static @NotNull PerlValuesCacheService getInstance(@NotNull Project project) {
    return project.getService(PerlValuesCacheService.class);
  }
}
