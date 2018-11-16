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

package com.perl5.lang.perl.idea.execution.filters;

import com.intellij.execution.filters.Filter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.perl5.lang.perl.idea.execution.PerlRunConsole;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PerlConsoleFileLinkFilter implements Filter {
  private static final String FILE_PATH_REGEXP = "((?:(?:\\p{Alpha}:)|/:)?[0-9a-z_A-Z\\-\\\\./]+)";
  private static final Pattern DIE_PATH_PATTERN = Pattern.compile("\\bat " + FILE_PATH_REGEXP + " line (\\d+)\\.?\\b");
  @NotNull
  private final Project myProject;
  @NotNull
  private final PerlRunConsole myRunConsole;

  public PerlConsoleFileLinkFilter(@NotNull Project project, @NotNull PerlRunConsole runConsole) {
    myProject = project;
    myRunConsole = runConsole;
  }

  @Nullable
  @Override
  public Result applyFilter(String textLine, int endOffset) {
    if (StringUtil.isEmpty(textLine)) {
      return null;
    }
    Matcher matcher = DIE_PATH_PATTERN.matcher(textLine);
    if (!matcher.find()) {
      return null;
    }

    int lineStartOffset = endOffset - textLine.length();
    int fileStartOffset = matcher.start(1);
    int lineNumberEndOffset = matcher.end(2);
    PerlHostData hostData = myRunConsole.getHostData();
    String filePath = hostData == null ? matcher.group(1) : hostData.getLocalPath(matcher.group(1));
    int line = Integer.valueOf(matcher.group(2)) - 1;

    return new Result(
      lineStartOffset + fileStartOffset,
      lineStartOffset + lineNumberEndOffset,
      new MyHyperLinkInfo(myProject, line, filePath == null ? matcher.group(1) : filePath));
  }
}
