/*
 * Copyright 2015-2021 Alexandr Evstigneev
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

package com.perl5.lang.perl.debugger;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.vfs.DeprecatedVirtualFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.VirtualFileSystem;
import com.intellij.testFramework.LightVirtualFile;
import com.perl5.lang.perl.debugger.protocol.PerlStackFrameDescriptor;
import com.perl5.lang.perl.fileTypes.PerlFileTypeScript;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Clone of mock file system
 */
public class PerlRemoteFileSystem extends DeprecatedVirtualFileSystem {
  public static final String PROTOCOL = "perl5_remote";
  public static final String PROTOCOL_PREFIX = "perl5_remote://";
  private final Map<String, VirtualFile> virtualFilesMap = new HashMap<>();

  @Override
  public @Nullable VirtualFile findFileByPath(@NotNull String path) {
    return virtualFilesMap.get(path);
  }

  public void dropFiles() {
    ApplicationManager.getApplication().runWriteAction(new Runnable() {
      @Override
      public void run() {
        for (Map.Entry<String, VirtualFile> entry : virtualFilesMap.entrySet()) {
          VirtualFile value = entry.getValue();
          fireBeforeFileDeletion(this, value);
          fireFileDeleted(this, value, value.getName(), null);
        }
      }
    });
    virtualFilesMap.clear();
  }

  public @Nullable VirtualFile registerRemoteFile(@NotNull String filePath, @NotNull String fileSource) {
    String fileName;
    if (filePath.startsWith(PerlStackFrameDescriptor.EVAL_PREFIX)) // make name for eval
    {
      fileName = filePath.substring(0, filePath.indexOf(')') + 1);
    }
    else // make name for regular file
    {
      int slashIndex = filePath.lastIndexOf('/');
      fileName = slashIndex == -1 ? filePath : filePath.substring(slashIndex + 1);
    }
    return registerRemoteFile(fileName, filePath, fileSource);
  }

  public @NotNull VirtualFile registerRemoteFile(@NotNull String fileName, @NotNull String filePath, @NotNull String fileSource) {
    //		System.err.println("Registering file: "+ fileName + " " + filePath);
    LightVirtualFile newVirtualFile = new PerlRemoteVirtualFile(fileName, filePath, fileSource);
    virtualFilesMap.put(filePath, newVirtualFile);
    return newVirtualFile;
  }

  @Override
  public @NotNull String getProtocol() {
    return PROTOCOL;
  }

  @Override
  public void refresh(boolean asynchronous) {
  }

  @Override
  public void deleteFile(Object requestor, @NotNull VirtualFile vFile) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void moveFile(Object requestor, @NotNull VirtualFile vFile, @NotNull VirtualFile newParent) {
    throw new UnsupportedOperationException();
  }

  @Override
  public @NotNull VirtualFile copyFile(Object requestor,
                                       @NotNull VirtualFile vFile,
                                       @NotNull VirtualFile newParent,
                                       final @NotNull String copyName) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void renameFile(Object requestor, @NotNull VirtualFile vFile, @NotNull String newName) {
    throw new UnsupportedOperationException();
  }

  @Override
  public @NotNull VirtualFile createChildFile(Object requestor, @NotNull VirtualFile vDir, @NotNull String fileName) {
    throw new UnsupportedOperationException();
  }

  @Override
  public @NotNull VirtualFile createChildDirectory(Object requestor, @NotNull VirtualFile vDir, @NotNull String dirName)
    throws IOException {
    throw new IOException();
  }

  @Override
  public VirtualFile refreshAndFindFileByPath(@NotNull String path) {
    return findFileByPath(path);
  }

  public static @NotNull PerlRemoteFileSystem getInstance() {
    return ((PerlRemoteFileSystem)Objects.requireNonNull(VirtualFileManager.getInstance().getFileSystem(PROTOCOL)));
  }

  public class PerlRemoteVirtualFile extends LightVirtualFile {
    private final String myPath;

    public PerlRemoteVirtualFile(@NotNull String name, String path, String text) {
      super(name, PerlFileTypeScript.INSTANCE, text);
      myPath = path;
    }

    @Override
    public @NotNull VirtualFileSystem getFileSystem() {
      return PerlRemoteFileSystem.this;
    }

    @Override
    public boolean isDirectory() {
      return false;
    }

    @Override
    public @NotNull String getPath() {
      return myPath;
    }
  }
}
