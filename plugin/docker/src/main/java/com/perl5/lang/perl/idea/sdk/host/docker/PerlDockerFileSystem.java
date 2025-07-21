/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.idea.sdk.host.PerlFileDescriptor;
import com.perl5.lang.perl.idea.sdk.host.PerlPluggableVirtualFileSystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

class PerlDockerFileSystem extends PerlPluggableVirtualFileSystem {
  private static final Logger LOG = Logger.getInstance(PerlDockerFileSystem.class);

  private final Map<String, VirtualFile> myFiles = new ConcurrentHashMap<>();

  private final @NotNull PerlDockerFileTransfer myTransfer;


  private PerlDockerFileSystem(@NotNull PerlDockerData dockerData) {
    myTransfer = new PerlDockerFileTransfer(dockerData);
  }

  PerlDockerFileSystem(@NotNull PerlDockerFileTransfer fileTransfer) {
    myTransfer = fileTransfer;
  }

  @Override
  public @Nullable VirtualFile findFileByPath(@NotNull String path) {
    return myFiles.computeIfAbsent(FileUtil.toSystemIndependentName(path), it -> {
      if (it.equals("/")) {
        return new PerlDockerVirtualFile(PerlFileDescriptor.ROOT_DESCRIPTOR);
      }
      File pathFile = new File(it);
      try {
        for (PerlFileDescriptor descriptor : listFiles(pathFile.getParent())) {
          if (pathFile.getName().equals(descriptor.getName())) {
            return new PerlDockerVirtualFile(descriptor);
          }
        }
      }
      catch (IOException e) {
        LOG.warn("Error listing files for " + path, e);
      }
      return null;
    });
  }

  private @NotNull List<PerlFileDescriptor> listFiles(@NotNull String path) throws IOException {
    return myTransfer.getAdapter().listFiles(myTransfer.getContainerName(), FileUtil.toSystemIndependentName(path));
  }

  @Override
  public @Nullable VirtualFile refreshAndFindFileByPath(@NotNull String path) {
    return findFileByPath(path);
  }

  @Override
  public synchronized void clean() {
    try {
      myTransfer.close();
    }
    catch (IOException e) {
      LOG.warn("Error cleaning file system for " + myTransfer.getAdapter(), e);
    }
    finally {
      super.clean();
    }
  }

  static PerlDockerFileSystem create(@NotNull PerlDockerData dockerData) {
    return new PerlDockerFileSystem(dockerData);
  }

  private class PerlDockerVirtualFile extends PerlPluggableVirtualFile {
    private final @NotNull PerlFileDescriptor myDescriptor;
    private VirtualFile[] myChildren;

    public PerlDockerVirtualFile(@NotNull PerlFileDescriptor descriptor) {
      myDescriptor = descriptor;
    }

    @Override
    public @NotNull String getPath() {
      return myDescriptor.getPath();
    }

    @Override
    public boolean isDirectory() {
      return myDescriptor.isDirectory();
    }

    @Override
    public @Nullable VirtualFile getParent() {
      if (myDescriptor == PerlFileDescriptor.ROOT_DESCRIPTOR) {
        return null;
      }
      return myFiles.computeIfAbsent(
        FileUtil.toSystemIndependentName(new File(getPath()).getParent()),
        it -> new PerlDockerVirtualFile(Objects.requireNonNull(myDescriptor.getParentDescriptor())));
    }

    @Override
    public @NotNull String getName() {
      return myDescriptor.getName();
    }

    @Override
    public long getLength() {
      return myDescriptor.getSize() * 1024L;
    }

    @Override
    public VirtualFile[] getChildren() {
      if (myChildren != null) {
        return myChildren;
      }
      if (!myDescriptor.isDirectory()) {
        return myChildren = VirtualFile.EMPTY_ARRAY;
      }

      List<VirtualFile> children;
      try {
        children = ContainerUtil.map(
          listFiles(myDescriptor.getPath()),
          it -> myFiles.computeIfAbsent(FileUtil.toSystemIndependentName(it.getPath()), it2 -> new PerlDockerVirtualFile(it)));
      }
      catch (IOException e) {
        LOG.warn("Error reading children for " + this, e);
        return myChildren = VirtualFile.EMPTY_ARRAY;
      }

      return myChildren = children.toArray(VirtualFile.EMPTY_ARRAY);
    }

    @Override
    public final boolean equals(Object o) {
      if (!(o instanceof PerlDockerVirtualFile virtualFile)) {
        return false;
      }

      return myDescriptor.equals(virtualFile.myDescriptor);
    }

    @Override
    public int hashCode() {
      return myDescriptor.hashCode();
    }
  }
}
