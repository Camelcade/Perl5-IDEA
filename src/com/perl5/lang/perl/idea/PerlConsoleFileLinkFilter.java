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

package com.perl5.lang.perl.idea;

import com.intellij.execution.filters.Filter;
import com.intellij.execution.filters.OpenFileHyperlinkInfo;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ELI-HOME on 21-Sep-15.
 * This filter detects file paths and stack traces and turns them into code hyperlinks inside consoles (this doesn't affect the Terminal).
 * You can create various consoles using the external tools or remote SSH external tools (under Settings > Tools).
 * The classic usage will be to create an local or ssh tool to tail your server logs,
 * then whenever an exception occurs - you can click on it to go to the specific line in the code.
 *
 * the logic works in a way that allows the remote server and you local code base to exists on different libraries (providing they both have the same project folder name).
 * example:
 * the file in your local project folder:  /home/user.folder/main.project/lib/ABC.pm
 * the file in your remote server folder: /usr/server/main.project/lib/ABC.pm
 *
 * since the project folder is named main.project, it will know to remove the prefix of the path and replace it with the correct one.
 */
public class PerlConsoleFileLinkFilter implements Filter {

    private final Project project;

    public PerlConsoleFileLinkFilter(Project project) {
        this.project = project;
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
        if (project != null) {
            String projectDir = project.getBaseDir().toString();
            String projectName = project.getBaseDir().getName();

            String separator = "[\\\\/]";
            Pattern pattern = Pattern.compile("([A-Za-z:]+)?" + separator + "+([\\w-.]+" + separator + ")+" + projectName + "(" + separator + "+([\\w-.]+" + separator + "+)*\\w([\\w-.])+)( line (\\d+))?");
            Matcher matcher = pattern.matcher(textLine);
            while (matcher.find()) {
                int startIndex = matcher.start(0);
                int endIndex = matcher.end(0);
                String file = matcher.group(3);
                int line = (matcher.group(7) != null) ? (Integer.valueOf(matcher.group(7)) - 1) : 0;
                VirtualFile virtualFile = VirtualFileManager.getInstance().findFileByUrl(projectDir + file);
                if(virtualFile != null){
                results.add(new Result(
                        startPoint + startIndex,
                        startPoint + endIndex,
                        new OpenFileHyperlinkInfo(project, virtualFile, line)));
                }
            }
        }
    }
}
