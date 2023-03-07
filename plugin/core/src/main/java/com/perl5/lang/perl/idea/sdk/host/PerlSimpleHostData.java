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

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFileSystem;
import com.perl5.lang.perl.idea.sdk.host.os.PerlOsHandler;
import com.perl5.lang.perl.util.PerlPluginUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PerlSimpleHostData<Data extends PerlSimpleHostData<Data, Handler>, Handler extends PerlSimpleHostHandler<Data, Handler>>
  extends PerlHostData<Data, Handler> {

  private final PerlSimpleFileTransfer<Data, Handler> FILE_TRANSFER = new PerlSimpleFileTransfer(this);


  public PerlSimpleHostData(@NotNull Handler handler) {
    super(handler);
  }

  @Override
  public @NotNull PerlOsHandler getOsHandler() {
    return getHandler().getOsHandler();
  }

  @Override
  public @NotNull PerlHostFileTransfer<Data> getFileTransfer() {
    return FILE_TRANSFER;
  }


  @Override
  public @Nullable String getSecondaryShortName() {
    return null;
  }

  @Override
  public @NotNull String getHelpersRootPath() {
    return PerlPluginUtil.getPluginHelpersRoot();
  }

  @Override
  public @Nullable VirtualFileSystem getFileSystem(@NotNull Disposable disposable) {
    return LocalFileSystem.getInstance();
  }

  @Override
  public @Nullable String getLocalCacheRoot() {
    return null;
  }

  @Override
  public void fixPermissionsRecursively(@NotNull String localPath, @Nullable Project project) {
  }
}
