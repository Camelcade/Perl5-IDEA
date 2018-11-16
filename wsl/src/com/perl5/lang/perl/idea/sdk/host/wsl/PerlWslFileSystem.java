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

package com.perl5.lang.perl.idea.sdk.host.wsl;

import com.intellij.execution.wsl.WSLDistributionWithRoot;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.idea.sdk.host.PerlPluggableVirtualFileSystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * Per-distribution filesystem for WSL distributions.
 * File url looks like: {@code wsl-distro_id:///path/to/file}
 *
 * @apiNote this filesystem is pretty lame and good enough for browsing only. Do not use it for real things
 * @implNote known issues:
 * - changes are not watched
 * - listeners not supported
 */
class PerlWslFileSystem extends PerlPluggableVirtualFileSystem {
  private static final Logger LOG = Logger.getInstance(PerlWslFileSystem.class);

  @NotNull
  private final WSLDistributionWithRoot myDistribution;

  private PerlWslFileSystem(@NotNull WSLDistributionWithRoot distribution) {
    myDistribution = distribution;
  }

  @Nullable
  @Override
  public VirtualFile findFileByPath(@NotNull String path) {
    String windowsPath = myDistribution.getWindowsPath(path);
    VirtualFile realFile = windowsPath == null ? null : VfsUtil.findFileByIoFile(new File(windowsPath), false);
    return realFile == null ? null : new WslVirtualFile(realFile, path);
  }

  @Override
  public void refresh(boolean asynchronous) {
    VirtualFile rootFile = VfsUtil.findFileByIoFile(new File(Objects.requireNonNull(myDistribution.getWindowsPath("/"))), false);
    if (rootFile != null) {
      rootFile.refresh(asynchronous, true);
    }
  }

  @Nullable
  @Override
  public VirtualFile refreshAndFindFileByPath(@NotNull String path) {
    String windowsPath = myDistribution.getWindowsPath(path);
    VirtualFile realFile = windowsPath == null ? null : LocalFileSystem.getInstance().refreshAndFindFileByPath(windowsPath);
    return realFile == null ? null : new WslVirtualFile(realFile, path);
  }

  static PerlWslFileSystem create(@NotNull WSLDistributionWithRoot distributionWithRoot) {
    return new PerlWslFileSystem(distributionWithRoot);
  }

  private class WslVirtualFile extends PerlPluggableVirtualFile {
    // fixme this is introduced because wsl improperly map root file
    @NotNull
    private final String myPath;

    public WslVirtualFile(@NotNull VirtualFile originalVirtualFile, @NotNull String path) {
      setOriginalFile(originalVirtualFile);
      myPath = FileUtil.toSystemIndependentName(path);
    }

    @NotNull
    @Override
    public String getPath() {
      return myPath;
    }

    @Override
    public boolean isDirectory() {
      return getOriginalFile().isDirectory();
    }

    @Override
    public VirtualFile getParent() {
      File currentFilePath = new File(myPath);
      File parentPath = currentFilePath.getParentFile();
      return parentPath == null ? null : new WslVirtualFile(getOriginalFile().getParent(), parentPath.getPath());
    }

    @Override
    public long getModificationStamp() {
      return getOriginalFile().getModificationStamp();
    }

    @Override
    public long getTimeStamp() {
      return getOriginalFile().getTimeStamp();
    }

    @Override
    public long getLength() {
      return getOriginalFile().getLength();
    }

    @NotNull
    @Override
    public String getName() {
      return getOriginalFile().getName();
    }

    @Nullable
    @Override
    public String getExtension() {
      return getOriginalFile().getExtension();
    }

    @NotNull
    @Override
    public String getNameWithoutExtension() {
      return getOriginalFile().getNameWithoutExtension();
    }

    @Override
    public boolean isValid() {
      return getOriginalFile().isValid();
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof WslVirtualFile)) {
        return false;
      }

      WslVirtualFile file = (WslVirtualFile)o;

      return getOriginalFile().equals(file.getOriginalFile());
    }

    @Override
    public int hashCode() {
      return getOriginalFile().hashCode();
    }

    @Override
    public VirtualFile[] getChildren() {
      VirtualFile[] children = getOriginalFile().getChildren();
      if (children.length == 0) {
        return children;
      }
      List<WslVirtualFile> wrappedFiles = ContainerUtil.map(children, it -> {
        String childPath = myPath.endsWith("/") ? myPath + it.getName() : myPath + '/' + it.getName();
        return new WslVirtualFile(it, childPath);
      });
      return wrappedFiles.toArray(VirtualFile.EMPTY_ARRAY);
    }
  }
}
