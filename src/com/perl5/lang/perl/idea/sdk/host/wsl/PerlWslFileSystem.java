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
import com.intellij.openapi.util.KeyedExtensionCollector;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.*;
import com.intellij.openapi.vfs.impl.VirtualFileManagerImpl;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.util.ObjectUtils;
import com.intellij.util.ReflectionUtil;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
class PerlWslFileSystem extends DeprecatedVirtualFileSystem {
  private static final Logger LOG = Logger.getInstance(PerlWslFileSystem.class);
  private static final Map<WSLDistributionWithRoot, PerlWslFileSystem> CACHE = new HashMap<>();

  @NotNull
  private final WSLDistributionWithRoot myDistribution;

  private PerlWslFileSystem(@NotNull WSLDistributionWithRoot distribution) {
    myDistribution = distribution;
  }

  /**
   * @implNote this id is wrong. Theoretically we should be able to find an FS by this key in EP. but we don't
   * This FS is supposed to be obtained from host only
   */
  @NotNull
  @Override
  public String getProtocol() {
    return "wsl-" + myDistribution.getId().toLowerCase();
  }

  @Nullable
  @Override
  public VirtualFile findFileByPath(@NotNull String path) {
    String windowsPath = myDistribution.getWindowsPath(path);
    VirtualFile realFile = windowsPath == null ? null : VfsUtil.findFile(Paths.get(windowsPath), false);
    return realFile == null ? null : new WslVirtualFile(realFile, path);
  }

  @Contract("null->null")
  @Nullable
  public VirtualFile findFile(@Nullable File path) {
    return ObjectUtils.doIfNotNull(path, it -> findFileByPath(it.toString()));
  }

  @Override
  public void refresh(boolean asynchronous) {
    VirtualFile rootFile = VfsUtil.findFile(Paths.get(Objects.requireNonNull(myDistribution.getWindowsPath("/"))), false);
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

  static PerlWslFileSystem getOrCreate(@NotNull WSLDistributionWithRoot distributionWithRoot) {
    return CACHE.computeIfAbsent(distributionWithRoot, distribution -> {
      PerlWslFileSystem fileSystem = new PerlWslFileSystem(distribution);
      try {
        Field collectorField = ReflectionUtil.findField(VirtualFileManagerImpl.class, KeyedExtensionCollector.class, "myCollector");
        collectorField.setAccessible(true);
        @SuppressWarnings("unchecked")
        KeyedExtensionCollector<VirtualFileSystem, String> collector =
          (KeyedExtensionCollector<VirtualFileSystem, String>)collectorField.get(VirtualFileManagerImpl.getInstance());
        collector.addExplicitExtension(fileSystem.getProtocol(), fileSystem);
        return fileSystem;
      }
      catch (NoSuchFieldException | IllegalAccessException e) {
        LOG.error(e);
      }
      return null;
    });
  }

  private class WslVirtualFile extends LightVirtualFile {
    // fixme this is introduced because wsl improperly map root file
    @NotNull
    private final String myPath;

    public WslVirtualFile(@NotNull VirtualFile originalVirtualFile, @NotNull String path) {
      setOriginalFile(originalVirtualFile);
      myPath = FileUtil.toSystemIndependentName(path);
    }

    @NotNull
    @Override
    public VirtualFileSystem getFileSystem() {
      return PerlWslFileSystem.this;
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
    public CharSequence getNameSequence() {
      return getOriginalFile().getNameSequence();
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
    public boolean is(@NotNull VFileProperty property) {
      return false;
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
