/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package com.perl5.lang.perl.coverage;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.intellij.coverage.*;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.process.ProcessOutput;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.rt.coverage.data.ClassData;
import com.intellij.rt.coverage.data.LineCoverage;
import com.intellij.rt.coverage.data.LineData;
import com.intellij.rt.coverage.data.ProjectData;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.util.PerlPluginUtil;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class PerlCoverageRunner extends CoverageRunner {
  private static final String COVER = "cover";
  private static final String COVER_LIB = "Devel::Cover";
  private static final Logger LOG = Logger.getInstance(PerlCoverageRunner.class);

  @Override
  protected @NotNull CoverageLoadingResult loadCoverageData(@NotNull File sessionDataFile,
                                                            @Nullable CoverageSuite baseCoverageSuite,
                                                            @NotNull CoverageLoadErrorReporter reporter) {
    if (!(baseCoverageSuite instanceof PerlCoverageSuite perlCoverageSuite)) {
      return new FailedCoverageLoadingResult("Wrong type of coverage suite: " + baseCoverageSuite.getClass().getCanonicalName());
    }
    final Ref<ProjectData> projectDataRef = new Ref<>();
    if (ApplicationManager.getApplication().isDispatchThread()) {

      ProgressManager.getInstance().runProcessWithProgressSynchronously(
        () -> projectDataRef.set(doLoadCoverageData(sessionDataFile, (PerlCoverageSuite)baseCoverageSuite)),
        PerlCoverageBundle.message("dialog.title.loading.coverage.data"), true, baseCoverageSuite.getProject());

    }
    else {
      projectDataRef.set(doLoadCoverageData(sessionDataFile, perlCoverageSuite));
    }
    return new SuccessCoverageLoadingResult(projectDataRef.get());
  }

  private static @Nullable ProjectData doLoadCoverageData(@NotNull File sessionDataFile, @NotNull PerlCoverageSuite perlCoverageSuite) {
    Project project = perlCoverageSuite.getProject();
    Sdk effectiveSdk;
    try {
      effectiveSdk = perlCoverageSuite.getConfiguration().getEffectiveSdk();
    }
    catch (ExecutionException e) {
      LOG.error(e);
      return null;
    }

    PerlHostData<?, ?> hostData = PerlHostData.from(effectiveSdk);
    if (hostData == null) {
      LOG.warn("No host data for " + effectiveSdk);
      return null;
    }
    try {
      hostData.fixPermissionsRecursively(sessionDataFile.getAbsolutePath(), project);
    }
    catch (ExecutionException e) {
      LOG.warn("Error fixing permissions for " + sessionDataFile);
    }

    PerlCommandLine perlCommandLine = ReadAction.compute(() -> {
      if (project.isDisposed()) {
        LOG.debug("Project disposed");
        return null;
      }

      VirtualFile coverFile = PerlRunUtil.findLibraryScriptWithNotification(effectiveSdk, project, COVER, COVER_LIB);
      if (coverFile == null) {
        LOG.warn("No `cover` script found in " + effectiveSdk);
        return null;
      }

      PerlCommandLine commandLine = PerlRunUtil.getPerlCommandLine(
        project, effectiveSdk, coverFile,
        Collections.singletonList(PerlRunUtil.PERL_I + hostData.getRemotePath(PerlPluginUtil.getHelpersLibPath())),
        Collections.emptyList());

      if (commandLine == null) {
        LOG.warn("Unable to create a command line: " +
                 " project: " + project +
                 "; sdk:" + effectiveSdk +
                 "; coverageFile: " + coverFile);
        return null;
      }

      String remotePath = hostData.getRemotePath(sessionDataFile.getAbsolutePath());
      if (StringUtil.isEmpty(remotePath)) {
        LOG.warn("Unable to map remote path for: " + sessionDataFile.getAbsolutePath() + " in " + hostData);
        return null;
      }

      commandLine
        .addParameters("--silent", "--nosummary", "-report", "camelcade", remotePath);
      commandLine.withSdk(effectiveSdk);
      commandLine.withProject(project);

      return commandLine;
    });

    if (perlCommandLine == null) {
      return null;
    }

    try {
      LOG.info("Loading coverage by: " + perlCommandLine.getCommandLineString());
      ProcessOutput output = PerlHostData.execAndGetOutput(perlCommandLine);
      if (output.getExitCode() != 0) {
        String errorMessage = output.getStderr();
        if (StringUtil.isEmpty(errorMessage)) {
          errorMessage = output.getStdout();
        }

        if (!StringUtil.isEmpty(errorMessage)) {
          showError(project, errorMessage);
        }
        return null;
      }
      String stdout = output.getStdout();
      if (StringUtil.isEmpty(stdout)) {
        return null;
      }

      try {
        PerlFileCoverageData[] filesData = new Gson().fromJson(stdout, PerlFileCoverageData[].class);
        if (filesData != null) {
          return parsePerlFileData(PerlHostData.notNullFrom(effectiveSdk), filesData);
        }
      }
      catch (JsonParseException e) {
        LOG.warn("Error parsing JSON", e);
        showError(project, e.getMessage());
      }
    }
    catch (ExecutionException e) {
      LOG.warn("Error loading coverage", e);
      showError(project, e.getMessage());
    }
    return null;
  }

  private static @NotNull ProjectData parsePerlFileData(@NotNull PerlHostData<?, ?> hostData, @NotNull PerlFileCoverageData[] filesData) {
    ProjectData projectData = new ProjectData();
    for (PerlFileCoverageData perlFileCoverageData : filesData) {
      if (StringUtil.isEmpty(perlFileCoverageData.name) || perlFileCoverageData.lines == null) {
        LOG.warn("Name or lines is null in " + perlFileCoverageData);
        continue;
      }
      String localPath = hostData.getLocalPath(perlFileCoverageData.name);
      if (localPath == null) {
        continue;
      }
      ClassData classData = projectData.getOrCreateClassData(FileUtil.toSystemIndependentName(localPath));
      Set<Map.Entry<Integer, PerlLineData>> linesEntries = perlFileCoverageData.lines.entrySet();
      Integer maxLineNumber = linesEntries.stream().map(Map.Entry::getKey).max(Integer::compare).orElse(0);
      LineData[] linesData = new LineData[maxLineNumber + 1];
      for (Map.Entry<Integer, PerlLineData> lineEntry : linesEntries) {
        PerlLineData perlLineData = lineEntry.getValue();
        final Integer lineNumber = lineEntry.getKey();
        LineData lineData = new LineData(lineNumber, null) {
          @Override
          public int getStatus() {
            if (perlLineData.cover == 0) {
              return LineCoverage.NONE;
            }
            else if (perlLineData.cover < perlLineData.data) {
              return LineCoverage.PARTIAL;
            }
            return LineCoverage.FULL;
          }
        };
        lineData.setHits(perlLineData.cover);
        linesData[lineNumber] = lineData;
      }

      classData.setLines(linesData);
    }

    return projectData;
  }

  private static void showError(@NotNull Project project, @NlsSafe @NotNull String message) {
    ReadAction.run(() -> {
      if (!project.isDisposed()) {
        LOG.warn("Error loading coverage: " + message);
        Notifications.Bus.notify(
          new Notification(
            PerlBundle.message("perl.coverage.loading.error"),
            PerlBundle.message("perl.coverage.loading.error"),
            message,
            NotificationType.ERROR
          ),
          project
        );
      }
    });
  }

  @Override
  public @NotNull String getPresentableName() {
    return PerlBundle.message("perl.perl5");
  }

  @Override
  public @NotNull String getId() {
    return "Perl5CoverageRunner";
  }

  @Override
  public @NotNull String getDataFileExtension() {
    return "json";
  }

  @Override
  public boolean acceptsCoverageEngine(@NotNull CoverageEngine engine) {
    return engine instanceof PerlCoverageEngine;
  }
}
