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

package com.perl5.lang.perl.util;

import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.util.ExecUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.sdk.PerlSdkType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * Created by hurricup on 26.04.2016.
 */
public class PerlRunUtil {
  @Nullable
  public static GeneralCommandLine getPerlCommandLine(@NotNull Project project,
                                                      @Nullable VirtualFile scriptFile,
                                                      String... perlParameters) {
    String perlPath = PerlProjectManager.getSdkPath(project, scriptFile);
    return perlPath == null ? null : getPerlCommandLine(project, perlPath, scriptFile, perlParameters);
  }

  @NotNull
  public static GeneralCommandLine getPerlCommandLine(@NotNull Project project,
                                                      @NotNull String perlDirectory,
                                                      @Nullable VirtualFile scriptFile,
                                                      String... perlParameters) {
    GeneralCommandLine commandLine = new GeneralCommandLine();
    String executablePath = PerlSdkType.getInstance().getExecutablePath(perlDirectory);
    commandLine.setExePath(FileUtil.toSystemDependentName(executablePath));
    for (VirtualFile libRoot : PerlProjectManager.getInstance(project).getModulesLibraryRoots()) {
      commandLine.addParameter("-I" + libRoot.getCanonicalPath());
    }

    commandLine.addParameters(perlParameters);

    if (scriptFile != null) {
      commandLine.addParameter(FileUtil.toSystemDependentName(scriptFile.getPath()));
    }
    return commandLine;
  }


  @Nullable
  public static String getPathFromPerl() {
    List<String> perlPathLines = getDataFromProgram("perl", "-le", "print $^X");

    if (perlPathLines.size() == 1) {
      int perlIndex = perlPathLines.get(0).lastIndexOf("perl");
      if (perlIndex > 0) {
        return perlPathLines.get(0).substring(0, perlIndex);
      }
    }
    return null;
  }

  @NotNull
  public static List<String> getDataFromProgram(String... command) {
    try {
      GeneralCommandLine commandLine = new GeneralCommandLine(command);
      return ExecUtil.execAndGetOutput(commandLine).getStdoutLines();
    }
    catch (Exception e) {
      //			throw new IncorrectOperationException("Error executing external perl, please report to plugin developers: " + e.getMessage());
      return Collections.emptyList();
    }
  }
}
