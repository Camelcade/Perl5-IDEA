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

package com.perl5.lang.perl.idea.sdk.host.docker;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.process.ProcessOutput;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.util.xmlb.annotations.Tag;
import com.intellij.util.xmlb.annotations.Transient;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.host.PerlHostFileTransfer;
import com.perl5.lang.perl.idea.sdk.host.PerlHostVirtualFileSystem;
import com.perl5.lang.perl.idea.sdk.host.os.PerlOsHandler;
import com.perl5.lang.perl.idea.sdk.host.os.PerlOsHandlers;
import com.perl5.lang.perl.util.PerlFileUtil;
import com.perl5.lang.perl.util.PerlPluginUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.Objects;

class PerlDockerData extends PerlHostData<PerlDockerData, PerlDockerHandler> {
  static final String CONTAINER_ROOT = "/intellijperl";
  static final File CONTAINER_ROOT_FILE = new File(CONTAINER_ROOT);
  static final String HELPERS_ROOT = CONTAINER_ROOT + PerlFileUtil.linuxisePath(PerlPluginUtil.getPluginHelpersRoot());
  private static final Logger LOG = Logger.getInstance(PerlDockerData.class);

  @Transient
  @Nullable
  private PerlDockerFileSystem myFileSystem;

  @Nullable
  @Tag("image-name")
  private String myImageName;

  public PerlDockerData(@NotNull PerlDockerHandler handler) {
    super(handler);
  }

  @NotNull
  @Override
  public PerlOsHandler getOsHandler() {
    return PerlOsHandlers.LINUX;
  }

  @Nullable
  @Override
  public String getSecondaryShortName() {
    return "[" + myImageName + "]";
  }

  @NotNull
  public String getImageName() {
    return Objects.requireNonNull(myImageName);
  }

  public String getSafeImageName() {
    return getImageName().replaceAll("[:/]", "_");
  }

  @NotNull
  public PerlDockerData withImageName(@NotNull String imageName) {
    myImageName = imageName;
    return this;
  }

  @NotNull
  @Override
  public synchronized PerlHostVirtualFileSystem getFileSystem(@NotNull Disposable disposable) {
    if (myFileSystem == null) {
      myFileSystem = PerlDockerFileSystem.create(new PerlDockerAdapter(this));
      Disposer.register(disposable, () -> {
        PerlDockerFileSystem fileSystem = myFileSystem;
        ApplicationManager.getApplication().executeOnPooledThread(() -> {
          if (fileSystem != null) {
            fileSystem.clean();
          }
        });
        myFileSystem = null;
      });
    }
    PerlHostVirtualFileSystem hostSystem = PerlHostVirtualFileSystem.getInstance();
    hostSystem.setDelegate(myFileSystem);
    return hostSystem;
  }

  @Nullable
  @Override
  public File findFileByName(@NotNull String fileName) {
    try {
      ProcessOutput output = execAndGetOutput(new PerlCommandLine("\\which", fileName).withHostData(this));
      int exitCode = output.getExitCode();
      if (exitCode != 0 && exitCode != 1) {
        LOG.error("Got non-zero code from script " + exitCode + "; stderr: " + output.getStderr());
        return null;
      }
      List<String> lines = output.getStdoutLines();
      return lines.isEmpty() ? null : new File(lines.get(0));
    }
    catch (ExecutionException e) {
      LOG.error("Error seeking for " + fileName, e);
    }
    return null;
  }

  @NotNull
  @Override
  protected Process createProcess(@NotNull PerlCommandLine commandLine) throws ExecutionException {
    return new PerlDockerAdapter(this).createProcess(commandLine);
  }

  @Nullable
  @Override
  protected String doGetLocalPath(@NotNull String remotePath) {
    File remoteFile = new File(remotePath);
    if (FileUtil.isAncestor(CONTAINER_ROOT_FILE, remoteFile, false)) {
      return PerlFileUtil.unLinuxisePath('/' + FileUtil.getRelativePath(CONTAINER_ROOT_FILE, remoteFile));
    }
    return FileUtil.toSystemIndependentName(FileUtil.join(getLocalCacheRoot(), remotePath));
  }

  @Nullable
  @Override
  protected String doGetRemotePath(@NotNull String localPath) {
    return FileUtil.toSystemIndependentName(new File(CONTAINER_ROOT_FILE, PerlFileUtil.linuxisePath(localPath)).getPath());
  }

  @Nullable
  @Override
  public String getLocalCacheRoot() {
    String cachesPath = PerlPluginUtil.getRemotesCachePath();
    File cacheRoot = new File(cachesPath, "docker_" + getSafeImageName());
    FileUtil.createDirectory(cacheRoot);
    return cacheRoot.getAbsolutePath();
  }

  @NotNull
  @Override
  public PerlHostFileTransfer<PerlDockerData> getFileTransfer() {
    return new PerlDockerFileTransfer(this);
  }

  @NotNull
  @Override
  public String getHelpersRootPath() {
    return HELPERS_ROOT;
  }

  @NotNull
  @Override
  protected PerlDockerData self() {
    return this;
  }
}
