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

package com.perl5.lang.perl.idea.completion;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.util.HashSet;
import java.util.Set;

public class PerlStringCompletionCache {
  private final Set<String> myHashIndexesCache = new HashSet<>();
  private final Set<String> myHeredocOpenersCache = new HashSet<>();

  public Set<String> getHashIndexesCache() {
    return myHashIndexesCache;
  }

  public Set<String> getHeredocOpenersCache() {
    return myHeredocOpenersCache;
  }

  @TestOnly
  public void clear() {
    myHashIndexesCache.clear();
    myHeredocOpenersCache.clear();
  }

  public static @NotNull PerlStringCompletionCache getInstance(@NotNull Project project) {
    return project.getService(PerlStringCompletionCache.class);
  }
}
