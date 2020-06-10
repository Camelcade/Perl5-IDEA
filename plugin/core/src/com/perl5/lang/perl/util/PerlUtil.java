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
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.util.processors.PerlInternalIndexKeysProcessor;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

/**
 * Misc helper methods
 */
public class PerlUtil implements PerlElementTypes {

  @Deprecated // make reverse index and use it
  public static Collection<String> getIndexKeysWithoutInternals(@NotNull StubIndexKey<String, ?> key, @NotNull Project project) {
    final Set<String> result = new THashSet<>();

    // safe for getElements
    StubIndex.getInstance().processAllKeys(key, project, new
      PerlInternalIndexKeysProcessor() {
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

  public static @NotNull String getParentsChain(@Nullable PsiElement element) {
    if (element == null) {
      return "null";
    }
    StringBuilder sb = new StringBuilder();
    while (true) {
      sb.append(element.getClass()).append("(").append(PsiUtilCore.getElementType(element)).append(")").append(": ");
      if (element instanceof PsiFile || element.getParent() == null) {
        break;
      }
      element = element.getParent();
    }
    return sb.toString();
  }
}
