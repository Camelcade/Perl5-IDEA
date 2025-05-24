/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package com.perl5.lang.perl.psi.mro;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.psi.util.PsiModificationTracker;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.containers.Interner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

@Service(Service.Level.PROJECT)
final class PerlIsaCache implements PsiModificationTracker.Listener, Disposable {
  private final Map<String, List<String>> myCache = ContainerUtil.createConcurrentWeakMap();
  private static final Interner<List<String>> INTERNER = Interner.createWeakInterner();

  public PerlIsaCache(@NotNull Project project) {
    project.getMessageBus().connect(this).subscribe(PsiModificationTracker.TOPIC, this);
  }

  @Nullable List<String> get(@Nullable String namespaceName) {
    var cachedValue = myCache.get(namespaceName);
    return cachedValue == null ? null : List.copyOf(cachedValue);
  }

  @NotNull List<String> put(@Nullable String namespaceName, @NotNull List<String> linearIsa) {
    var internedValue = INTERNER.intern(linearIsa);
    myCache.put(namespaceName, internedValue);
    return List.copyOf(internedValue);
  }

  @Override
  public void modificationCountChanged() {
    myCache.clear();
  }

  @Override
  public void dispose() {
  }

  static @NotNull PerlIsaCache getInstance(@NotNull Project project) {
    return project.getService(PerlIsaCache.class);
  }
}
