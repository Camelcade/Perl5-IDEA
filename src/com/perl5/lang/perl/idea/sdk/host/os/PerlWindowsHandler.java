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

package com.perl5.lang.perl.idea.sdk.host.os;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;

class PerlWindowsHandler extends PerlOsHandler {
  public PerlWindowsHandler(@NotNull String name) {
    super(name);
  }

  @NotNull
  @Override
  public String getPerlExecutableName() {
    return "perl.exe";
  }

  @Override
  public final boolean isMsWindows() {
    return true;
  }

  @NotNull
  @Override
  public Path getDefaultHomePath() {
    return Paths.get("c:\\Strawberry\\bin", getPerlExecutableName());
  }
}
