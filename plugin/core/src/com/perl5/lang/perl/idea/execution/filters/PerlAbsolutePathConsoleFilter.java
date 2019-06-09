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

package com.perl5.lang.perl.idea.execution.filters;


import com.intellij.execution.filters.Filter;
import com.intellij.openapi.project.Project;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.host.PerlHostDataProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles any path in the console. Should be used only for our consoles
 */
public class PerlAbsolutePathConsoleFilter implements Filter {
  private static final Pattern PATTERN = Pattern.compile(
    "(?<=^|-I|[^.\\w/~:])" +
    "((?:[/~]|\\w:[/\\\\])" +
    "[-@\\w./~:\\\\]+[-@\\w.~])"
  );

  @NotNull
  private final Project myProject;

  @NotNull
  private final PerlHostDataProvider myHostDataContainer;

  public PerlAbsolutePathConsoleFilter(@NotNull Project project, @NotNull PerlHostDataProvider hostDataContainer) {
    myProject = project;
    myHostDataContainer = hostDataContainer;
  }

  @Nullable
  @Override
  public Result applyFilter(String line, int entireLength) {
    int startOffset = entireLength - line.length();
    List<ResultItem> resultList = ContainerUtil.newArrayList();
    Matcher matcher = PATTERN.matcher(line);
    while (matcher.find()) {
      String filePath;
      PerlHostData hostData = myHostDataContainer.getHostData();
      filePath = hostData == null ? matcher.group(1) : hostData.getLocalPath(matcher.group(1));
      resultList.add(new Result(
        startOffset + matcher.start(1),
        startOffset + matcher.end(1),
        new MyHyperLinkInfo(myProject, 0, filePath == null ? matcher.group(1) : filePath)));
    }

    return resultList.isEmpty() ? null :
           resultList.size() == 1 ? (Result)resultList.get(0) :
           new Result(resultList);
  }
}
