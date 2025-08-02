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

package com.perl5.lang.perl.util;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Misc helper methods
 */
public final class PerlUtil {
  private static final Logger LOG = Logger.getInstance(PerlUtil.class);
  private PerlUtil() {
  }

  @SafeVarargs
  public static @NotNull <E> List<E> mutableList(E @NotNull ... array) {
    return new ArrayList<>(Arrays.asList(array));
  }

  public static long getLastModifiedTime(@NotNull VirtualFile virtualFile){
    try {
      return Files.getLastModifiedTime(Paths.get(virtualFile.getPath())).toMillis();
    }
    catch (IOException e) {
      LOG.warn("Can't get last modified for " + virtualFile, e);
      return 0;
    }
  }
}
