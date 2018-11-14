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
import com.perl5.lang.perl.idea.sdk.host.PerlHostFileTransfer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

class PerlDockerFileTransfer extends PerlHostFileTransfer<PerlDockerData> {
  private final PerlDockerAdapter myAdapter;

  public PerlDockerFileTransfer(@NotNull PerlDockerData hostData) {
    super(hostData);
    myAdapter = new PerlDockerAdapter(hostData);
  }

  @Override
  protected void doSyncPath(@NotNull String remotePath, String localPath) throws ExecutionException {
    myAdapter.copyRemote(remotePath, localPath);
  }

  @Override
  protected void doSyncHelpers() {
  }

  @Override
  public void close() throws IOException {

  }
}

