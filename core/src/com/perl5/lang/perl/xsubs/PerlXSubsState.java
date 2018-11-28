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

package com.perl5.lang.perl.xsubs;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.process.ProcessOutput;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.progress.util.ProgressIndicatorUtils;
import com.intellij.openapi.progress.util.ReadTask;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.GlobalSearchScopesCore;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.util.FileContentUtil;
import com.intellij.util.Function;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.PerlPathMacros;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.util.PerlPluginUtil;
import com.perl5.lang.perl.util.PerlRunUtil;
import gnu.trove.THashMap;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@State(
  name = "Perl5XSubsState",
  storages = @Storage(PerlPathMacros.PERL5_PROJECT_SETTINGS_FILE)
)

public class PerlXSubsState implements PersistentStateComponent<PerlXSubsState> {
  private static final Logger LOG = Logger.getInstance(PerlXSubsState.class);
  @Transient
  public static final String DEPARSED_FILE_NAME = "_Deparsed_XSubs.pm";
  public boolean isActual = true;
  public Map<String, Long> myFilesMap = new THashMap<>();
  @Transient
  private Task.Backgroundable myParserTask = null;
  @Transient
  private Project myProject;

  public void setProject(Project myProject) {
    this.myProject = myProject;
  }

  @Nullable
  @Override
  public PerlXSubsState getState() {
    return this;
  }

  @Override
  public void loadState(@NotNull PerlXSubsState state) {
    XmlSerializerUtil.copyBean(state, this);
  }

  private Set<VirtualFile> getAllXSFiles(@NotNull Project project) {
    List<VirtualFile> classesRoots = PerlProjectManager.getInstance(project).getAllLibraryRoots();
    if (classesRoots.isEmpty()) {
      return Collections.emptySet();
    }

    GlobalSearchScope classRootsScope =
      GlobalSearchScopesCore.directoriesScope(myProject, true, classesRoots.toArray(new VirtualFile[classesRoots.size()]));

    Set<VirtualFile> result = new THashSet<>();
    for (VirtualFile virtualFile : FilenameIndex.getAllFilesByExt(project, getXSBinaryExtension(), classRootsScope)) {
      if (virtualFile.isValid() && !virtualFile.isDirectory() && !(virtualFile instanceof LightVirtualFile)) {
        String path = virtualFile.getCanonicalPath();
        if (path != null && StringUtil.contains(path, "/auto/")) {
          result.add(virtualFile);
        }
      }
    }
    return result;
  }

  public void rescanFiles() {
    ProgressIndicatorUtils.scheduleWithWriteActionPriority(new ReadTask() {
      @Override
      public void computeInReadAction(@NotNull ProgressIndicator indicator) throws ProcessCanceledException {
        if (!PerlProjectManager.isPerlEnabled(myProject)) {
          return;
        }
        int filesCounter = 0;
        indicator.setIndeterminate(false);
        indicator.setText(PerlBundle.message("perl.scanning.xs.changes"));
        if (isActual) {
          Set<VirtualFile> allXSFiles = getAllXSFiles(myProject);
          for (VirtualFile virtualFile : allXSFiles) {
            if (indicator.isCanceled()) {
              return;
            }

            if (!virtualFile.isValid()) {
              continue;
            }

            indicator.setFraction(filesCounter / allXSFiles.size());

            if (!isFileUpToDate(virtualFile)) {
              isActual = false;
              break;
            }
            else {
              filesCounter++;
            }
          }
        }

        isActual = isActual && (filesCounter == 0 || myProject.getBaseDir().findFileByRelativePath(DEPARSED_FILE_NAME) != null);

        if (!isActual) {
          showNotification(
            PerlBundle.message("perl.deparsing.change.detected.title"),
            PerlBundle.message("perl.deparsing.change.detected.message"),
            NotificationType.INFORMATION,
            notification -> Collections.singletonList(new AnAction(PerlBundle.message("perl.deparsing.action")) {
              @Override
              public void actionPerformed(@NotNull AnActionEvent e) {
                notification.expire();
                reparseXSubs();
              }
            })
          );
        }
      }

      @Override
      public void onCanceled(@NotNull ProgressIndicator indicator) {
        rescanFiles();
      }
    });
  }

  private boolean isFileUpToDate(VirtualFile virtualFile) {
    String path = virtualFile.getCanonicalPath();

    if (path != null) {
      Long modificationStamp = myFilesMap.get(path);
      return modificationStamp != null && modificationStamp == VfsUtilCore.virtualToIoFile(virtualFile).lastModified();
    }
    return false;
  }

  public void reparseXSubs() {
    if (!PerlProjectManager.isPerlEnabled(myProject)) {
      return;
    }

    if (myParserTask != null) {
      Messages.showErrorDialog(
        myProject,
        PerlBundle.message("perl.deparsing.in.progress.message"),
        PerlBundle.message("perl.deparsing.in.progress.title")
      );
      return;
    }

    PerlCommandLine commandLine = PerlRunUtil.getPerlCommandLine(myProject, PerlPluginUtil.getHelperPath("xs_parser_simple.pl"));

    if (commandLine == null) {
      LOG.warn("Unable to create deparser command line");
      return;
    }
    commandLine.withCharset(CharsetToolkit.UTF8_CHARSET);

    LOG.info("Deparsing: " + commandLine.getCommandLineString());

    myParserTask = new Task.Backgroundable(myProject, PerlBundle.message("perl.deparsing.xsubs"), false) {
      @Override
      public void run(@NotNull ProgressIndicator indicator) {
        indicator.setIndeterminate(true);
        Map<String, Long> newFilesMap = ReadAction.compute(() -> {
          if (myProject.isDisposed()) {
            return null;
          }
          final Map<String, Long> result = new THashMap<>();
          for (VirtualFile virtualFile : getAllXSFiles(myProject)) {
            if (virtualFile.isValid()) {
              String filePath = virtualFile.getCanonicalPath();
              if (filePath != null) {
                result.put(filePath, VfsUtilCore.virtualToIoFile(virtualFile)
                  .lastModified());
              }
            }
          }
          return result;
        });
        if (newFilesMap == null) {
          myParserTask = null;
          return;
        }

        ProcessOutput processOutput;
        try {
          processOutput = PerlHostData.execAndGetOutput(commandLine);
        }
        catch (ExecutionException e) {
          LOG.warn("Error deparsing", e);

          showNotification(
            PerlBundle.message("perl.deparsing.error.execution"),
            e.getMessage(),
            NotificationType.ERROR
          );
          myParserTask = null;
          return;
        }
        final String stdout = processOutput.getStdout();
        String stderr = processOutput.getStderr();
        int exitCode = processOutput.getExitCode();
        LOG.info("Deparsing finished with exit code: " + exitCode +
                 (StringUtil.isEmpty(stderr) ? "" : ". STDERR:\n" + stderr));

        if (exitCode != 0) {
          showNotification(
            PerlBundle.message("perl.deparsing.error.execution"),
            stderr,
            NotificationType.ERROR
          );
        }
        else if (!stdout.isEmpty()) {
          Application application = ApplicationManager.getApplication();
          application.invokeAndWait(
            () -> WriteAction.run(() -> {
              if (myProject.isDisposed()) {
                return;
              }
              try {
                VirtualFile newFile = myProject.getBaseDir().findOrCreateChildData(this, DEPARSED_FILE_NAME);
                newFile.setWritable(true);
                OutputStream outputStream = newFile.getOutputStream(null);
                outputStream.write(stdout.getBytes());
                outputStream.close();
                newFile.setWritable(false);
                FileContentUtil.reparseFiles(newFile);

                myFilesMap = newFilesMap;
                isActual = true;

                showNotification(
                  PerlBundle.message("perl.deparsing.finished"),
                  "",
                  NotificationType.INFORMATION
                );
              }
              catch (IOException e) {
                LOG.warn("Error creating deparsed file", e);
                showNotification(
                  PerlBundle.message("perl.deparsing.error.creating.file"),
                  e.getMessage(),
                  NotificationType.ERROR
                );
              }
              // fixme fix modality state
            }));
        }
        myParserTask = null;
      }
    };
    myParserTask.queue();
  }

  private void showNotification(@NotNull String title,
                                @NotNull String message,
                                @NotNull NotificationType type) {
    showNotification(title, message, type, null);
  }

  private void showNotification(@NotNull String title,
                                @NotNull String message,
                                @NotNull NotificationType type,
                                @Nullable Function<Notification, List<AnAction>> actionsProvider) {
    Notification notification = new Notification(PerlBundle.message("perl.deparsing.notification"), title, message, type);

    if (actionsProvider != null) {
      List<AnAction> actions = actionsProvider.fun(notification);
      if (actions != null) {
        actions.forEach(notification::addAction);
      }
    }
    Notifications.Bus.notify(notification, myProject);
  }

  public static PerlXSubsState getInstance(@NotNull Project project) {
    PerlXSubsState persisted = ServiceManager.getService(project, PerlXSubsState.class);
    if (persisted == null) {
      persisted = new PerlXSubsState();
    }

    persisted.setProject(project);
    return persisted;
  }

  @NotNull
  private static String getXSBinaryExtension() {
    return SystemInfo.isWindows ? "xs.dll" : "so";
  }
}
