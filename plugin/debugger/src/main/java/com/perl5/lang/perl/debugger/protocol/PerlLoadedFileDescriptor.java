/*
 * Copyright 2015-2022 Alexandr Evstigneev
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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class PerlLoadedFileDescriptor {
  @SuppressWarnings("unused") private String path;
  @SuppressWarnings("unused") private String name;

  public @NotNull String getPath() {
    return path;
  }

  public @Nullable String getName() {
    return name;
  }

  public @NotNull String getNameOrPath() {
    return name == null ? path : name;
  }

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
    if (!(o instanceof PerlLoadedFileDescriptor)) {
      return false;
    }

    PerlLoadedFileDescriptor that = (PerlLoadedFileDescriptor)o;

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
