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

package com.perl5.lang.perl.idea.coverage;

import com.intellij.coverage.SimpleCoverageAnnotator;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class PerlCoverageAnnotator extends SimpleCoverageAnnotator {
  public PerlCoverageAnnotator(Project project) {
    super(project);
  }

  @NotNull
  public static PerlCoverageAnnotator getInstance(@NotNull Project project) {
    return ServiceManager.getService(project, PerlCoverageAnnotator.class);
  }

  @Nullable
  @Override
  protected FileCoverageInfo fillInfoForUncoveredFile(@NotNull File file) {
    return new FileCoverageInfo();
  }

  @Nullable
  @Override
  protected String getLinesCoverageInformationString(@NotNull FileCoverageInfo info) {
    if (info.totalLineCount == 0) {
      return null;
    }
    if (info.coveredLineCount == 0) {
      return info instanceof DirCoverageInfo ? null : "no lines covered";
    }
    return (int)((double)info.coveredLineCount * 100. / (double)info.totalLineCount) + "% lines covered";
  }

  @Nullable
  @Override
  protected String getFilesCoverageInformationString(@NotNull DirCoverageInfo info) {
    if (info.totalFilesCount == 0 || info.coveredFilesCount == 0) {
      return null;
    }
    return info.coveredFilesCount + " of " + info.totalFilesCount + " files";
  }
}
