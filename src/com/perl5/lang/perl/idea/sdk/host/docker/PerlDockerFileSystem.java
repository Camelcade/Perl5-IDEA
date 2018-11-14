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
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.AtomicNullableLazyValue;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.idea.sdk.host.PerlFileDescriptor;
import com.perl5.lang.perl.idea.sdk.host.PerlPluggableVirtualFileSystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

class PerlDockerFileSystem extends PerlPluggableVirtualFileSystem {
  private static final Logger LOG = Logger.getInstance(PerlDockerFileSystem.class);

  private final Map<String, VirtualFile> myFiles = ContainerUtil.newConcurrentMap();

  @NotNull
  private final PerlDockerAdapter myAdapter;

  private volatile boolean myContainerCreated = false;

  @NotNull
  private final AtomicNullableLazyValue<String> myContainerNameProvider = AtomicNullableLazyValue.createValue(() -> {
    PerlDockerAdapter dockerAdapter = getAdapter();
    String containerName;
    try {
      containerName = dockerAdapter.createRunningContainer("filesystem_" + dockerAdapter.getData().getSafeImageName());
    }
    catch (ExecutionException e) {
      LOG.error("Error creating container from data: " + dockerAdapter.getData(), e);
      return null;
    }
    myContainerCreated = true;
    return containerName;
  });

  private PerlDockerFileSystem(@NotNull PerlDockerAdapter adapter) {
    myAdapter = adapter;
  }

  @NotNull
  private PerlDockerAdapter getAdapter() {
    return myAdapter;
  }

  @Nullable
  @Override
  public VirtualFile findFileByPath(@NotNull String path) {
    return myFiles.computeIfAbsent(FileUtil.toSystemIndependentName(path), it -> {
      if (it.equals("/")) {
        return new PerlDockerVirtualFile(PerlFileDescriptor.ROOT_DESCRIPTOR);
      }
      File pathFile = new File(it);
      for (PerlFileDescriptor descriptor : listFiles(pathFile.getParent())) {
        if (pathFile.getName().equals(descriptor.getName())) {
          return new PerlDockerVirtualFile(descriptor);
        }
      }
      return null;
    });
  }

  @NotNull
  private List<PerlFileDescriptor> listFiles(@NotNull String path) {
    String containerName = myContainerNameProvider.getValue();
    return containerName == null ? Collections.emptyList() : myAdapter.listFiles(containerName, FileUtil.toSystemIndependentName(path));
  }

  @Nullable
  @Override
  public VirtualFile refreshAndFindFileByPath(@NotNull String path) {
    return findFileByPath(path);
  }

  @Override
  public synchronized void clean() {
    if (!myContainerCreated) {
      return;
    }
    myContainerCreated = false;
    String containerName = myContainerNameProvider.getValue();
    try {
      myAdapter.killContainer(containerName);
    }
    catch (ExecutionException e) {
      LOG.warn("Error killing container: " + containerName, e);
    }
    super.clean();
  }

  static PerlDockerFileSystem create(@NotNull PerlDockerAdapter adapter) {
    return new PerlDockerFileSystem(adapter);
  }

  private class PerlDockerVirtualFile extends PerlPluggableVirtualFile {
    @NotNull
    private final PerlFileDescriptor myDescriptor;
    private VirtualFile[] myChildren;

    public PerlDockerVirtualFile(@NotNull PerlFileDescriptor descriptor) {
      myDescriptor = descriptor;
    }

    @NotNull
    @Override
    public String getPath() {
      return myDescriptor.getPath();
    }

    @Override
    public boolean isDirectory() {
      return myDescriptor.isDirectory();
    }

    @Override
    public VirtualFile getParent() {
      if (myDescriptor == PerlFileDescriptor.ROOT_DESCRIPTOR) {
        return null;
      }
      return myFiles.computeIfAbsent(
        FileUtil.toSystemIndependentName(new File(getPath()).getParent()),
        it -> new PerlDockerVirtualFile(Objects.requireNonNull(myDescriptor.getParentDescriptor())));
    }

    @NotNull
    @Override
    public String getName() {
      return myDescriptor.getName();
    }

    @Override
    public long getLength() {
      return myDescriptor.getSize() * 1024;
    }

    @Override
    public VirtualFile[] getChildren() {
      if (myChildren != null) {
        return myChildren;
      }
      if (!myDescriptor.isDirectory()) {
        return myChildren = VirtualFile.EMPTY_ARRAY;
      }

      List<VirtualFile> children = ContainerUtil.map(
        listFiles(myDescriptor.getPath()),
        it -> myFiles.computeIfAbsent(FileUtil.toSystemIndependentName(it.getPath()), it2 -> new PerlDockerVirtualFile(it)));

      return myChildren = children.toArray(VirtualFile.EMPTY_ARRAY);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof PerlDockerVirtualFile)) {
        return false;
      }

      PerlDockerVirtualFile file = (PerlDockerVirtualFile)o;

      return myDescriptor.equals(file.myDescriptor);
    }

    @Override
    public int hashCode() {
      return myDescriptor.hashCode();
    }
  }
}
