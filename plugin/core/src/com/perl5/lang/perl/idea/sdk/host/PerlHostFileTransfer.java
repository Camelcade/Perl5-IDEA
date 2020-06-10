/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.sdk.host;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.util.PerlPluginUtil;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class PerlHostFileTransfer<HostData extends PerlHostData<?, ?>> implements Closeable {
  private static final Logger LOG = Logger.getInstance(PerlHostFileTransfer.class);
  protected final @NotNull HostData myHostData;

  public PerlHostFileTransfer(@NotNull HostData hostData) {
    myHostData = hostData;
  }

  /**
   * Downloads {@code remoteFile} to the local cache
   */
  @Contract("null->null; !null->!null")
  public final @Nullable File syncFile(@Nullable File remoteFile) throws IOException {
    if (remoteFile == null) {
      return null;
    }
    return new File(syncFile(FileUtil.toSystemIndependentName(remoteFile.getPath())));
  }

  /**
   * Downloads {@code remotePath} to the local cache
   */
  @Contract("null->null; !null->!null")
  public final @Nullable String syncFile(@Nullable String remotePath) throws IOException {
    if (remotePath == null) {
      return null;
    }
    if (ApplicationManager.getApplication().isDispatchThread()) {
      throw new RuntimeException("Should not be invoked from EDT");
    }
    String localPath = myHostData.getLocalPath(remotePath);
    if (localPath == null) {
      throw new RuntimeException("Unable to compute local path for " + remotePath);
    }
    PerlRunUtil.setProgressText(PerlBundle.message("perl.host.progress.syncing", remotePath));
    try {
      LOG.info(myHostData + " syncing: " + remotePath + " => " + localPath);
      doSyncPath(remotePath, localPath);
    }
    catch (IOException e) {
      throw new IOException(PerlBundle.message("perl.sync.error.copying", remotePath, localPath, myHostData.getShortName()), e);
    }
    return localPath;
  }

  /**
   * Creates a local stubs for the  {@code remoteDir}: empty files with same names
   */
  @Contract("null->null; !null->!null")
  public final @Nullable File stubFiles(@Nullable File remoteDir) throws IOException {
    if (remoteDir == null) {
      return null;
    }
    return new File(stubFiles(FileUtil.toSystemIndependentName(remoteDir.getPath())));
  }

  /**
   * @see #stubFiles(File)
   */
  @Contract("null->null; !null->!null")
  private @Nullable String stubFiles(@Nullable String remoteDir) throws IOException {
    if (remoteDir == null) {
      return null;
    }
    if (ApplicationManager.getApplication().isDispatchThread()) {
      throw new RuntimeException("Should not be invoked from EDT");
    }
    String localDir = myHostData.getLocalPath(remoteDir);
    if (localDir == null) {
      throw new RuntimeException("Unable to compute local path for " + remoteDir);
    }
    PerlRunUtil.setProgressText(PerlBundle.message("perl.host.progress.stubbing", remoteDir));
    try {
      LOG.info(myHostData + " stubbing: " + remoteDir + " => " + localDir);

      doStubFiles(remoteDir, localDir);
    }
    catch (IOException e) {
      throw new IOException(PerlBundle.message("perl.sync.error.stubbing", remoteDir, localDir, myHostData.getShortName()), e);
    }
    return localDir;
  }

  /**
   * Synchronizes local helpers from {@link PerlPluginUtil#getPluginHelpersRoot() helpers root} with remote machine
   */
  public final void syncHelpers() throws IOException {
    if (ApplicationManager.getApplication().isDispatchThread()) {
      throw new RuntimeException("Should not be invoked from EDT");
    }
    PerlRunUtil.setProgressText(PerlBundle.message("perl.host.progress.uploading.helpers"));
    doSyncHelpers();
  }

  /**
   * synchronizes {@code remoteDir} with local cache
   *
   * @implNote always invoked on pooled thread
   */
  protected void doStubFiles(@NotNull String remoteDir, String localDir) throws IOException {
    File localDirFile = new File(localDir);
    FileUtil.createDirectory(localDirFile);
    Set<String> localFileNames = new HashSet<>();
    for (File localFile : localDirFile.listFiles()) {
      if (localFile.length() == 0 || !localFile.delete()) {
        localFileNames.add(localFile.getName());
      }
    }
    Set<String> remoteFileNames = ContainerUtil.map2Set(listFiles(remoteDir), VirtualFile::getName);

    for (String fileName : ContainerUtil.subtract(localFileNames, remoteFileNames)) {
      String localFileToDelete = FileUtil.join(localDir, fileName);
      if (!new File(localFileToDelete).delete()) {
        LOG.warn("Failed to delete: " + localFileToDelete);
      }
    }


    for (String fileName : ContainerUtil.subtract(remoteFileNames, localFileNames)) {
      String localFileToDelete = FileUtil.join(localDir, fileName);
      if (!new File(localFileToDelete).createNewFile()) {
        LOG.warn("Failed to create: " + localFileToDelete);
      }
    }
  }

  /**
   * @return contents of {@code remotePath} on remote machine.
   * @implNote we need this method to optimize working with docker and/or ssh. Using virtual file system may cause additional container
   * start or additional connection created.
   */
  public abstract @NotNull List<VirtualFile> listFiles(@NotNull String remotePath) throws IOException;

  /**
   * synchronizes {@code remotePath} with local cache
   *
   * @implNote always invoked on pooled thread
   */
  protected abstract void doSyncPath(@NotNull String remotePath, String localPath) throws IOException;

  /**
   * Uploads local helpers to the remote machine
   *
   * @implNote always invoked on pooled thread
   */
  protected abstract void doSyncHelpers() throws IOException;
}
