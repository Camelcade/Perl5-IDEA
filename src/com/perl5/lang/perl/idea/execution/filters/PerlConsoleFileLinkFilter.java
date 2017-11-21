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

import com.intellij.execution.filters.FileHyperlinkInfoBase;
import com.intellij.execution.filters.Filter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PerlConsoleFileLinkFilter implements Filter {
  private static final String FILE_PATH_REGEXP = "((?:(?:\\p{Alpha}:)|/:)?[0-9a-z_A-Z\\-\\\\./]+)";
  private static final Pattern DIE_PATH_PATTERN = Pattern.compile("\\bat " + FILE_PATH_REGEXP + " line (\\d+)\\.?\\b");
  @NotNull
  private final Project myProject;

  public PerlConsoleFileLinkFilter(@NotNull Project project) {
    myProject = project;
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
    String filePath = matcher.group(1);
    int line = Integer.valueOf(matcher.group(2)) - 1;

    return new Result(
      lineStartOffset + fileStartOffset,
      lineStartOffset + lineNumberEndOffset,
      new MyHyperLinkInfo(myProject, line, filePath));
  }

  private static class MyHyperLinkInfo extends FileHyperlinkInfoBase {
    @NotNull
    private final String myFilePath;

    private final int myLine; // testing purposes

    public MyHyperLinkInfo(Project project, int documentLine, @NotNull String filePath) {
      super(project, documentLine, 0);
      myFilePath = filePath;
      myLine = documentLine;
    }

    @Nullable
    @Override
    protected VirtualFile getVirtualFile() {
      return VfsUtil.findFileByIoFile(new File(myFilePath), false);
    }

    @Override
    public String toString() {
      return "line " + myLine + " in " + myFilePath;
    }
  }
}
