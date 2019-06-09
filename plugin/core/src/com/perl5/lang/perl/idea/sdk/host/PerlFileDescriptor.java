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

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.io.FileUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * Descriptor for the remote file
 */
public class PerlFileDescriptor {
  public static final PerlFileDescriptor ROOT_DESCRIPTOR = new PerlFileDescriptor("/", "", Type.DIRECTORY, 0);
  private static final Logger LOG = Logger.getInstance(PerlFileDescriptor.class);
  @NotNull
  private final String myPath;
  @NotNull
  private final String myName;
  @NotNull
  private final Type myType;
  // in kilobytes
  private final int mySize;

  private PerlFileDescriptor(@NotNull String path, @NotNull String name, @NotNull Type type, int size) {
    myPath = path.endsWith("/") ? path : path + '/';
    myName = name;
    myType = type;
    mySize = size;
  }

  @Nullable
  public PerlFileDescriptor getParentDescriptor() {
    if (this == ROOT_DESCRIPTOR) {
      return null;
    }
    if (myPath.equals("/")) {
      return ROOT_DESCRIPTOR;
    }
    File parentFile = new File(myPath);
    return new PerlFileDescriptor(FileUtil.toSystemIndependentName(parentFile.getParent()), parentFile.getName(), Type.DIRECTORY, 0);
  }

  @NotNull
  public String getPath() {
    return myPath + myName;
  }

  @NotNull
  public String getName() {
    return myName;
  }

  @NotNull
  public Type getType() {
    return myType;
  }

  public int getSize() {
    return mySize;
  }

  public boolean isDirectory() {
    return myType.myIsDirectory;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PerlFileDescriptor)) {
      return false;
    }

    PerlFileDescriptor that = (PerlFileDescriptor)o;

    if (!myPath.equals(that.myPath)) {
      return false;
    }
    return myName.equals(that.myName);
  }

  @Override
  public int hashCode() {
    int result = myPath.hashCode();
    result = 31 * result + myName.hashCode();
    return result;
  }

  /**
   * @param input input in format {@code size name[suffix]}
   * @return new descriptor or null if failed to parse input
   * @implNote Prefixes are:
   */
  @Contract("_, null->null")
  @Nullable
  public static PerlFileDescriptor create(@NotNull String basePath, @Nullable String input) {
    if (input == null) {
      return null;
    }
    String[] parts = input.trim().split("\\s+");
    if (parts.length != 2) {
      LOG.warn("Unable to parse: " + input);
      return null;
    }

    if (parts[0].equals("total")) {
      return null;
    }

    int size;
    try {
      size = Integer.parseInt(parts[0]);
    }
    catch (NumberFormatException e) {
      LOG.error("Failed to parse size for: " + input, e);
      return null;
    }
    char lastChar = parts[1].charAt(parts[1].length() - 1);
    Type type = null;
    switch (lastChar) {
      case '/':
        type = Type.DIRECTORY;
        break;
      case '|':
        type = Type.PIPE;
        break;
      case '@':
        type = Type.SYMLINK;
        break;
      case '*':
        type = Type.EXECUTABLE;
        break;
      case '=':
        type = Type.SOCKET;
        break;
      case '>':
        type = Type.DOOR;
        break;
      default:
        type = Type.FILE;
    }
    String name = type == Type.FILE ? parts[1] : parts[1].substring(0, parts[1].length() - 1);
    return new PerlFileDescriptor(basePath, name, type, size);
  }

  public enum Type {
    FILE(false), DIRECTORY(true), PIPE(false), SYMLINK(false), EXECUTABLE(false), SOCKET(false), DOOR(false);
    private final boolean myIsDirectory;

    Type(boolean isDirectory) {
      myIsDirectory = isDirectory;
    }
  }
}
