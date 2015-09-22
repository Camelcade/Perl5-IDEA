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
import com.intellij.openapi.project.ProjectManager;
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
 */
public class PerlConsoleFileLinkFilter implements Filter {

    @Nullable
    @Override
    public Result applyFilter(String textLine, int endPoint) {
        int startPoint = endPoint - textLine.length();
        List<ResultItem> results = new ArrayList<ResultItem>();
        match(results, textLine, startPoint);

        return new Result(results);
    }

    private void match(List<ResultItem> results, String textLine, int startPoint) {
        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        if (projects.length > 0) {
            Project project = ProjectManager.getInstance().getOpenProjects()[0];
            String projectDir = project.getBaseDir().toString();
            String projectName = project.getBaseDir().getName();

            String separator = (SystemInfo.isWindows) ? File.separator + File.separator : File.separator;
            Pattern pattern = Pattern.compile(projectName + "(" + separator + "([\\w-]+" + separator + ")*\\w([\\w-.])+)( line (\\d+))?");
            Matcher matcher = pattern.matcher(textLine);
            while (matcher.find()) {
                int startIndex = matcher.start(0);
                int endIndex = matcher.end(0);
                String file = matcher.group(1);
                int line = (matcher.group(5) != null) ? (Integer.valueOf(matcher.group(5)) - 1) : 0;
                VirtualFile virtualFile = VirtualFileManager.getInstance().findFileByUrl(projectDir + file);
                results.add(new Result(
                        startPoint + startIndex,
                        startPoint + endIndex,
                        new OpenFileHyperlinkInfo(project, virtualFile, line)));
            }
        }
    }
}
