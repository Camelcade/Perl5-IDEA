/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

package com.perl5.lang.perl;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.UserDataHolderEx;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.ProjectAndLibrariesScope;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 20.01.2016.
 */
public class PerlScopes {
  private static final Key<GlobalSearchScope> PROJECT_AND_LIBS_SCOPE_KEY = new Key<GlobalSearchScope>("PROJECT_AND_LIBS_SCOPE_KEY");

  @NotNull
  public static GlobalSearchScope getProjectAndLibrariesScope(@NotNull Project project) {
    GlobalSearchScope cached = project.getUserData(PROJECT_AND_LIBS_SCOPE_KEY);
    return cached != null
           ? cached
           : ((UserDataHolderEx)project).putUserDataIfAbsent(PROJECT_AND_LIBS_SCOPE_KEY, new ProjectAndLibrariesScope(project));
  }
}
