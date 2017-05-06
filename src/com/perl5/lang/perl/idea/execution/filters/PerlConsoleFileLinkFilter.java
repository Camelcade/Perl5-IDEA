/*
 * Copyright 2015 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.execution.filters;

import com.intellij.execution.filters.Filter;
import com.intellij.execution.filters.OpenFileHyperlinkInfo;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ELI-HOME on 21-Sep-15.
 * This filter detects file paths and stack traces and turns them into code hyperlinks inside consoles (this doesn't affect the Terminal).
 * Attempts to find anything looks like path with optional line number.
 * Detected file should exists on your file system
 */
public class PerlConsoleFileLinkFilter implements Filter {
  @NonNls
  private static final String FILE_PATH_REGEXP = "((?:(?:\\p{Alpha}\\:)|/:)?[0-9a-z_A-Z\\-\\\\./]+)";
  private static final Pattern DIE_PATH_PATTERN = Pattern.compile("\\b" + FILE_PATH_REGEXP + "(?: line (\\d+)\\.?)?\\b");
  private final Project myProject;

  public PerlConsoleFileLinkFilter(Project project) {
    myProject = project;
  }

  @Nullable
  @Override
  public Result applyFilter(String textLine, int endPoint) {
    int startPoint = endPoint - textLine.length();
    List<ResultItem> results = new ArrayList<ResultItem>();
    match(results, textLine, startPoint);

    return new Result(results);
  }

  private void match(List<ResultItem> results, String textLine, int startPoint) {
    if (myProject == null || StringUtil.isEmpty(textLine)) {
      return;
    }

    Matcher matcher = DIE_PATH_PATTERN.matcher(textLine);
    while (matcher.find()) {
      int startIndex = matcher.start(0);
      int endIndex = matcher.end(0);
      String file = matcher.group(1);
      int line = (matcher.group(2) != null) ? (Integer.valueOf(matcher.group(2)) - 1) : 0;
      VirtualFile virtualFile = VfsUtil.findFileByIoFile(new File(file), false);
      if (virtualFile != null) {
        results.add(new Result(
          startPoint + startIndex,
          startPoint + endIndex,
          new OpenFileHyperlinkInfo(myProject, virtualFile, line)));
      }
    }
  }
}
