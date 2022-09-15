/*
 * Copyright 2015-2022 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.sdk.host.wsl;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.perl.idea.sdk.host.PerlHostFileTransfer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class PerlWslFileTransfer extends PerlHostFileTransfer<PerlWslData> {
  private static final Logger LOG = Logger.getInstance(PerlWslFileTransfer.class);

  public PerlWslFileTransfer(@NotNull PerlWslData hostData) {
    super(hostData);
  }

  @Override
  protected void doSyncPath(@NotNull String remotePath, String localPath) throws IOException {
  }

  @Override
  protected void doStubFiles(@NotNull String remoteDir, String localDir) throws IOException {
  }

  @Override
  public @NotNull List<VirtualFile> listFiles(@NotNull String remotePath) throws IOException {
    var localPath = myHostData.getLocalPath(remotePath);
    if (localPath == null) {
      LOG.info("Unable to get local path for: " + remotePath + " in " + myHostData);
      return Collections.emptyList();
    }

    var localVirtualFile = VfsUtil.findFile(Path.of(localPath), true);
    if (localVirtualFile == null) {
      LOG.info("Unable to find local virtual file for: " + localPath);
      return Collections.emptyList();
    }
    return Arrays.asList(localVirtualFile.getChildren());
  }

  @Override
  protected void doSyncHelpers() {
  }

  @SuppressWarnings("RedundantThrows")
  @Override
  public void close() throws IOException {
  }
}
