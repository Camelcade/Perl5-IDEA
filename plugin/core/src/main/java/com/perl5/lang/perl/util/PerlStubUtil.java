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

package com.perl5.lang.perl.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import com.intellij.util.Processor;
import com.intellij.util.Processors;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class PerlStubUtil {
  private PerlStubUtil() {
  }

  /**
   * @return all names from the index with {@code indexKey} limited with {@code scope}
   */
  public static @NotNull Collection<String> getAllKeys(@NotNull StubIndexKey<String, ?> indexKey, @NotNull GlobalSearchScope scope) {
    Set<String> allKeys = new HashSet<>();
    StubIndex.getInstance().processAllKeys(indexKey, Processors.cancelableCollectProcessor(allKeys), scope, null);
    return allKeys;
  }

  @Deprecated // make reverse index and use it
  public static Collection<String> getIndexKeysWithoutInternals(@NotNull StubIndexKey<String, ?> key, @NotNull Project project) {
    final Set<String> result = new HashSet<>();

    // safe for getElements
    StubIndex.getInstance().processAllKeys(key, project, new PerlInternalIndexKeysProcessor() {
      @Override
      public boolean process(String name) {
        if (super.process(name)) {
          result.add(name);
        }
        return true;
      }
    });

    return result;
  }

  public static class PerlInternalIndexKeysProcessor implements Processor<String> {
    @Override
    public boolean process(String string) {
      if (StringUtil.isEmpty(string)) {
        return false;
      }
      char firstChar = string.charAt(0);
      return firstChar == '_' || Character.isLetterOrDigit(firstChar);
    }
  }
}
