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

import com.intellij.openapi.vfs.DeprecatedVirtualFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class PerlHostVirtualFileSystem extends DeprecatedVirtualFileSystem {
  private static final String PROTOCOL = "perlhost";

  @Nullable
  private PerlPluggableVirtualFileSystem myDelegate;

  @NotNull
  @Override
  public String getProtocol() {
    return PROTOCOL;
  }

  @Nullable
  @Override
  public VirtualFile findFileByPath(@NotNull String path) {
    return myDelegate == null ? null : myDelegate.findFileByPath(path);
  }

  @Override
  public void refresh(boolean asynchronous) {
    if (myDelegate != null) {
      myDelegate.refresh(asynchronous);
    }
  }

  @Nullable
  @Override
  public VirtualFile refreshAndFindFileByPath(@NotNull String path) {
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

  public synchronized void setDelegate(@NotNull PerlPluggableVirtualFileSystem delegate) {
    if (myDelegate != null) {
      resetDelegate();
    }
    myDelegate = delegate;
  }

  public synchronized void resetDelegate() {
    if (myDelegate == null) {
      return;
    }
    Objects.requireNonNull(myDelegate).clean();
    myDelegate = null;
  }

  @NotNull
  public static PerlHostVirtualFileSystem getInstance() {
    return ((PerlHostVirtualFileSystem)Objects.requireNonNull(VirtualFileManager.getInstance().getFileSystem(PROTOCOL)));
  }
}
