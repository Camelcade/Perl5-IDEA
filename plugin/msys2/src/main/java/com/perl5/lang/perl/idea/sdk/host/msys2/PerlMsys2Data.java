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

package com.perl5.lang.perl.idea.sdk.host.msys2;

import com.intellij.execution.ExecutionException;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.sdk.host.PerlSimpleHostData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

final class PerlMsys2Data extends PerlSimpleHostData<PerlMsys2Data, PerlMsys2Handler> {
  PerlMsys2Data(@NotNull PerlMsys2Handler handler) {
    super(handler);
  }

  @Override
  protected @NotNull PerlMsys2Data self() {
    return this;
  }

  @Override
  public @Nullable File findFileByName(@NotNull String fileName) {
    return null;
  }

  @Override
  protected @NotNull Process createProcess(@NotNull PerlCommandLine commandLine) throws ExecutionException {
    return null;
  }

  @Override
  protected @Nullable String doGetLocalPath(@NotNull String remotePath) {
    return null;
  }

  @Override
  protected @Nullable String doGetRemotePath(@NotNull String localPath) {
    return null;
  }

  @Override
  public @NotNull String expandUserHome(@NotNull String remotePath) {
    return null;
  }
}
