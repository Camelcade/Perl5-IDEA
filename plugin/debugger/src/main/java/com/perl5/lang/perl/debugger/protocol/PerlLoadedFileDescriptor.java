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

package com.perl5.lang.perl.debugger.protocol;

import com.google.gson.annotations.Expose;
import com.intellij.openapi.util.NlsSafe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;


public class PerlLoadedFileDescriptor {
  @Expose private @NlsSafe @NotNull String path;
  @Expose private @NlsSafe @Nullable String name;

  public PerlLoadedFileDescriptor() {
  }

  @VisibleForTesting
  public PerlLoadedFileDescriptor(@NotNull String path, @Nullable String name) {
    this.path = path;
    this.name = name;
  }

  public @NotNull @NlsSafe String getPath() {
    return path;
  }

  public @Nullable @NlsSafe String getName() {
    return name;
  }

  @NlsSafe
  public @NotNull String getPresentableName() {
    return isEval() && name != null ? name : path;
  }

  public boolean isEval() {
    return path.startsWith(PerlStackFrameDescriptor.EVAL_PREFIX);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PerlLoadedFileDescriptor that)) {
      return false;
    }

    return getPath().equals(that.getPath());
  }

  @Override
  public int hashCode() {
    return getPath().hashCode();
  }

  @Override
  public String toString() {
    return "PerlLoadedFileDescriptor{" +
           "path='" + path + '\'' +
           ", name='" + name + '\'' +
           '}';
  }
}
