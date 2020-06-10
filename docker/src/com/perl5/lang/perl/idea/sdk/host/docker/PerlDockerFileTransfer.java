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

package com.perl5.lang.perl.idea.sdk.host.docker;

import com.intellij.execution.ExecutionException;
import com.intellij.openapi.util.AtomicNullableLazyValue;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.perl.idea.sdk.host.PerlHostFileTransfer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class PerlDockerFileTransfer extends PerlHostFileTransfer<PerlDockerData> {
  private final @NotNull PerlDockerAdapter myAdapter;

  private volatile boolean isOpened = false;
  private volatile Throwable closedThrowable;
  private volatile ExecutionException myCreationError;

  private final AtomicNullableLazyValue<String> myContainerNameProvider = AtomicNullableLazyValue.createValue(() -> {
    try {
      String containerName = getAdapter().createRunningContainer("copying_" + myHostData.getSafeImageName());
      isOpened = true;
      return containerName;
    }
    catch (ExecutionException e) {
      myCreationError = e;
    }
    return null;
  });

  public PerlDockerFileTransfer(@NotNull PerlDockerData hostData) {
    super(hostData);
    myAdapter = new PerlDockerAdapter(hostData);
  }

  @NotNull
  PerlDockerAdapter getAdapter() {
    return myAdapter;
  }

  @Override
  protected void doSyncPath(@NotNull String remotePath, String localPath) throws IOException {
    assertNotClosed();
    try {
      myAdapter.copyRemote(getContainerName(), remotePath, localPath);
    }
    catch (ExecutionException e) {
      throw new IOException(e);
    }
  }

  private synchronized void assertNotClosed() throws IOException {
    if (closedThrowable != null) {
      throw new IOException("This transfer is already closed", closedThrowable);
    }
  }

  @Override
  protected void doSyncHelpers() throws IOException {
    assertNotClosed();
  }

  @NotNull
  String getContainerName() throws IOException {
    String containerName = myContainerNameProvider.getValue();
    if (containerName != null) {
      return containerName;
    }
    throw new IOException("Container could not be created for " + myHostData + ".", myCreationError);
  }

  @Override
  public synchronized void close() throws IOException {
    if (!isOpened) {
      return;
    }
    if (closedThrowable != null) {
      throw new IOException("This transfer is already closed.", closedThrowable);
    }
    closedThrowable = new Throwable();

    String containerName = getContainerName();
    try {
      myAdapter.killContainer(containerName);
    }
    catch (ExecutionException e) {
      throw new IOException("Error killing copy container: " + containerName, e);
    }
  }

  @Override
  public @NotNull List<VirtualFile> listFiles(@NotNull String remotePath) throws IOException {
    VirtualFile root = new PerlDockerFileSystem(this).refreshAndFindFileByPath(remotePath);
    return root == null ? Collections.emptyList() : Collections.unmodifiableList(Arrays.asList(root.getChildren()));
  }
}

