/*
 * Copyright 2015-2023 Alexandr Evstigneev
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

import com.intellij.openapi.projectRoots.Sdk;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public abstract class PerlOsHandler {
  private final @NotNull String myName;

  private final @Nullable Icon myIcon;

  public PerlOsHandler(@NotNull String name) {
    this(name, null);
  }

  public PerlOsHandler(@NotNull String name, @Nullable Icon icon) {
    myName = name;
    myIcon = icon;
  }

  public abstract @NotNull String getPerlExecutableName();

  /**
   * @return operation system presentable name
   */
  public final @NotNull String getPresentableName() {
    return myName;
  }

  /**
   * @return true iff os is MS windows family
   */
  public abstract boolean isMsWindows();


  public final @Nullable Icon getIcon() {
    return myIcon;
  }

  /**
   * @return file extension used for compiled xs extensions.
   */
  public abstract @NotNull String getXSBinaryExtension();

  public static @NotNull PerlOsHandler notNullFrom(@NotNull Sdk sdk) {
    return PerlHostData.notNullFrom(sdk).getOsHandler();
  }
}
