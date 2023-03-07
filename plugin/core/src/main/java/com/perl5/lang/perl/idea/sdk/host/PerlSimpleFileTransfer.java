/*
 * Copyright 2015-2023 Alexandr Evstigneev
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

import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class PerlSimpleFileTransfer<Data extends PerlSimpleHostData<Data, Handler>, Handler extends PerlSimpleHostHandler<Data, Handler>>
  extends PerlHostFileTransfer<Data> {

  public PerlSimpleFileTransfer(@NotNull Data hostData) {
    super(hostData);
  }

  @Override
  protected void doSyncHelpers() {
  }

  @Override
  protected void doStubFiles(@NotNull String remoteDir, String localDir) throws IOException {
  }

  @Override
  protected void doSyncPath(@NotNull String remotePath, String localPath) {
  }

  @Override
  public @NotNull List<VirtualFile> listFiles(@NotNull String remotePath) {
    VirtualFile root = LocalFileSystem.getInstance().findFileByPath(remotePath);
    return root == null ? Collections.emptyList() : Collections.unmodifiableList(Arrays.asList(root.getChildren()));
  }

  @SuppressWarnings("RedundantThrows")
  @Override
  public void close() throws IOException {
  }
}
