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

import com.intellij.openapi.vfs.DeprecatedVirtualFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Objects;

public class PerlHostVirtualFileSystem extends DeprecatedVirtualFileSystem {
  private static final String PROTOCOL = "perlhost";

  private @Nullable PerlPluggableVirtualFileSystem myDelegate;

  @Override
  public @NotNull String getProtocol() {
    return PROTOCOL;
  }

  @Override
  public @Nullable VirtualFile findFileByPath(@NotNull String path) {
    return myDelegate == null ? null : myDelegate.findFileByPath(path);
  }

  @Override
  public void refresh(boolean asynchronous) {
    if (myDelegate != null) {
      myDelegate.refresh(asynchronous);
    }
  }

  @Override
  public @Nullable VirtualFile refreshAndFindFileByPath(@NotNull String path) {
    return myDelegate == null ? null : myDelegate.refreshAndFindFileByPath(path);
  }

  @Override
  public boolean isReadOnly() {
    return myDelegate != null && myDelegate.isReadOnly();
  }

  @Override
  public boolean isCaseSensitive() {
    return myDelegate != null && myDelegate.isCaseSensitive();
  }

  public void setDelegate(@NotNull PerlPluggableVirtualFileSystem delegate) {
    myDelegate = delegate;
  }

  @Override
  public @Nullable Path getNioPath(@NotNull VirtualFile file) {
    return myDelegate != null ? myDelegate.getNioPath(file) : super.getNioPath(file);
  }

  public void resetDelegate(@NotNull PerlPluggableVirtualFileSystem delegate) {
    if (myDelegate == delegate) {
      myDelegate = null;
    }
  }

  public static @NotNull PerlHostVirtualFileSystem getInstance() {
    return ((PerlHostVirtualFileSystem)Objects.requireNonNull(VirtualFileManager.getInstance().getFileSystem(PROTOCOL)));
  }
}
