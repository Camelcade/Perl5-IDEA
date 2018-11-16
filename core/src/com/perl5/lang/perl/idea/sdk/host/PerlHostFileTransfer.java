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

package com.perl5.lang.perl.idea.sdk.host;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.io.FileUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.util.PerlPluginUtil;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

public abstract class PerlHostFileTransfer<HostData extends PerlHostData<?, ?>> implements Closeable {
  private static final Logger LOG = Logger.getInstance(PerlHostFileTransfer.class);
  @NotNull
  protected final HostData myHostData;

  public PerlHostFileTransfer(@NotNull HostData hostData) {
    myHostData = hostData;
  }

  /**
   * Downloads {@code remoteFile} to the local cache
   */
  @Contract("null->null; !null->!null")
  @Nullable
  public final File syncFile(@Nullable File remoteFile) {
    if (remoteFile == null) {
      return null;
    }
    return new File(syncFile(FileUtil.toSystemIndependentName(remoteFile.getPath())));
  }

  /**
   * Downloads {@code remotePath} to the local cache
   */
  @Contract("null->null; !null->!null")
  @Nullable
  public final String syncFile(@Nullable String remotePath) {
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
      LOG.info("Syncing " + myHostData + ": " + remotePath + " => " + localPath);
      doSyncPath(remotePath, localPath);
    }
    catch (IOException e) {
      LOG.error("Error copying " + remotePath + " to " + localPath + " for " + myHostData, e);
      return null;
    }
    return localPath;
  }

  /**
   * Synchronizes local helpers from {@link PerlPluginUtil#getPluginHelpersRoot() helpers root} with remote machine
   */
  public final void syncHelpers() {
    if (ApplicationManager.getApplication().isDispatchThread()) {
      throw new RuntimeException("Should not be invoked from EDT");
    }
    PerlRunUtil.setProgressText(PerlBundle.message("perl.host.progress.uploading.helpers"));
    try {
      doSyncHelpers();
    }
    catch (IOException e) {
      LOG.error(e);
    }
  }

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
