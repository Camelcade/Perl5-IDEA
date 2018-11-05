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

package com.perl5.lang.perl.idea.sdk.host.wsl;

import com.intellij.execution.filters.ConsoleInputFilterProvider;
import com.intellij.execution.filters.InputFilter;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class PerlWslInputFilterProvider implements ConsoleInputFilterProvider {
  private static final String OSC = "\u001B]";
  private static final String ST = "\u001B\\";
  private static final char BEL = 7;

  private static final InputFilter[] EMPTY_ARRAY = new InputFilter[0];

  @NotNull
  @Override
  public InputFilter[] getDefaultFilters(@NotNull Project project) {
    return PerlWslHostData.from(PerlProjectManager.getSdk(project)) == null ? EMPTY_ARRAY : new InputFilter[]{new WslFilter()};
  }

  private static class WslFilter implements InputFilter {
    @Nullable
    @Override
    public List<Pair<String, ConsoleViewContentType>> applyFilter(@NotNull String text, @NotNull ConsoleViewContentType contentType) {
      int oscIndex;

      while ((oscIndex = text.indexOf(OSC)) > -1) {
        int endIndex = text.indexOf(BEL, oscIndex);
        if (endIndex == -1) {
          endIndex = text.indexOf(ST, oscIndex);
        }
        if (endIndex > -1) {
          text = text.substring(0, oscIndex) + text.substring(endIndex + 1);
        }
        else {
          text = text.substring(0, oscIndex);
          break;
        }
      }
      ;
      return Collections.singletonList(Pair.create(text, contentType));
    }
  }
}
