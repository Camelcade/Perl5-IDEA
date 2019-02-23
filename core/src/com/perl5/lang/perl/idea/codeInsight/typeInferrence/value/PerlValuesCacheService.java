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

package com.perl5.lang.perl.idea.codeInsight.typeInferrence.value;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.util.PsiModificationTracker;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

public class PerlValuesCacheService implements PsiModificationTracker.Listener {
  private static final Logger LOG = Logger.getInstance(PerlValuesCacheService.class);

  @NotNull
  private final Map<PerlValue, Set<String>> myNamespacesMaps = ContainerUtil.newConcurrentMap();
  @NotNull
  private final Map<PerlValue, Set<String>> mySubsNamesMap = ContainerUtil.newConcurrentMap();
  @NotNull
  private final Map<PerlValue, PerlValue> myRetrunValuesMap = ContainerUtil.newConcurrentMap();

  private final AtomicLong myNamespaceRequest = new AtomicLong();
  private final AtomicLong myNamespaceBuild = new AtomicLong();

  private final AtomicLong mySubsRequest = new AtomicLong();
  private final AtomicLong mySubsBuild = new AtomicLong();

  private final AtomicLong myReturnValueRequest = new AtomicLong();
  private final AtomicLong myReturnValueBuild = new AtomicLong();

  public PerlValuesCacheService(@NotNull Project project) {
    project.getMessageBus().connect().subscribe(PsiModificationTracker.TOPIC, this);
  }

  public PerlValue getReturnValue(@NotNull PerlValueCall value, @NotNull Supplier<? extends PerlValue> supplier) {
    myReturnValueRequest.incrementAndGet();
    return myRetrunValuesMap.computeIfAbsent(value, it -> {
      myReturnValueBuild.incrementAndGet();
      return supplier.get();
    });
  }

  public Set<String> getSubsNames(@NotNull PerlValue value, @NotNull Supplier<? extends Set<String>> supplier) {
    mySubsRequest.incrementAndGet();
    return mySubsNamesMap.computeIfAbsent(value, it -> {
      mySubsBuild.incrementAndGet();
      return supplier.get();
    });
  }

  public Set<String> getNamespaceNames(@NotNull PerlValue value, @NotNull Supplier<? extends Set<String>> supplier) {
    myNamespaceRequest.incrementAndGet();
    return myNamespacesMaps.computeIfAbsent(value, it -> {
      myNamespaceBuild.incrementAndGet();
      return supplier.get();
    });
  }

  @Override
  public void modificationCountChanged() {
    myRetrunValuesMap.clear();
    myNamespacesMaps.clear();
    mySubsNamesMap.clear();

    if (myReturnValueRequest.get() > 0) {
      LOG.debug(String.format("Return value effectiveness: %d, %d, %d", myReturnValueRequest.get(), myReturnValueBuild.get(),
                              (myReturnValueRequest.get() - myReturnValueBuild.get()) * 100 / myReturnValueRequest.get()));
    }
    myReturnValueRequest.set(0);
    myReturnValueBuild.set(0);

    if (myNamespaceRequest.get() > 0) {
      LOG.debug(String.format("Namespaces effectiveness: %d, %d, %d", myNamespaceRequest.get(), myNamespaceBuild.get(),
                              (myNamespaceRequest.get() - myNamespaceBuild.get()) * 100 / myNamespaceRequest.get()));
    }
    myNamespaceRequest.set(0);
    myNamespaceBuild.set(0);

    if (mySubsRequest.get() > 0) {
      LOG.debug(String.format("Subs cache effectiveness: %d, %d, %d", mySubsRequest.get(), mySubsBuild.get(),
                              (mySubsRequest.get() - mySubsBuild.get()) * 100 / mySubsRequest.get()));
    }
    mySubsRequest.set(0);
    mySubsBuild.set(0);
  }

  @NotNull
  public static PerlValuesCacheService getInstance(@NotNull Project project) {
    return ServiceManager.getService(project, PerlValuesCacheService.class);
  }
}
