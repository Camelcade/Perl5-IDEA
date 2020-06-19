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

package com.perl5.lang.perl.idea.copyright;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.maddyhome.idea.copyright.pattern.CopyrightVariablesProvider;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class PerlCopyrightsVariablesProvider extends CopyrightVariablesProvider {
  @Override
  public void collectVariables(@NotNull Map<String, Object> context, Project project, Module module, @NotNull PsiFile file) {
    if (file instanceof PerlFileImpl) {
      context.put("perlfile", new PerlFileInfo((PerlFileImpl)file));
    }
  }
}
