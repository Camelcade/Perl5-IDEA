/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.htmlmason.idea.lang;

import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.FileNameMatcher;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.LanguageSubstitutor;
import com.perl5.lang.htmlmason.HTMLMasonLanguage;
import com.perl5.lang.htmlmason.HTMLMasonUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 06.03.2016.
 */
public class HTMLMasonLanguageSubstitutor extends LanguageSubstitutor {
  protected final FileNameMatcher myFileNameMatcher;
  protected final Project myProject;

  public HTMLMasonLanguageSubstitutor(Project project, FileNameMatcher fileNameMatcher) {
    myFileNameMatcher = fileNameMatcher;
    myProject = project;
  }

  @Nullable
  @Override
  public Language getLanguage(@NotNull VirtualFile file, @NotNull Project project) {
    if (myFileNameMatcher.accept(file.getName()) && HTMLMasonUtil.getComponentRoot(myProject, file) != null) {
      //			System.err.println(file.getName() +  " substituted as HTML::Mason");
      return HTMLMasonLanguage.INSTANCE;
    }
    return null;
  }
}
