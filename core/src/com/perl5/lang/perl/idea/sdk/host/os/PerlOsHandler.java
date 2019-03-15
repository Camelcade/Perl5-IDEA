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

import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.util.ObjectUtils;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;

public abstract class PerlOsHandler {
  @NotNull
  private final String myName;

  @Nullable
  private final Icon myIcon;

  public PerlOsHandler(@NotNull String name) {
    this(name, null);
  }

  public PerlOsHandler(@NotNull String name, @Nullable Icon icon) {
    myName = name;
    myIcon = icon;
  }

  @NotNull
  public abstract String getPerlExecutableName();

  /**
   * @return operation system presentable name
   */
  @NotNull
  public final String getPresentableName() {
    return myName;
  }

  /**
   * @return true iff os is MS windows family
   */
  public abstract boolean isMsWindows();

  /**
   * @return true iff OS has Windows Subsystem for Linux support
   */
  public boolean hasWslSupport() {return false;}


  @Nullable
  public final Icon getIcon() {
    return myIcon;
  }

  @NotNull
  public static PerlOsHandler notNullFrom(@NotNull Sdk sdk) {
    return Objects.requireNonNull(from(sdk));
  }

  @Contract("null->null")
  @Nullable
  public static PerlOsHandler from(@Nullable Sdk sdk) {
    return ObjectUtils.doIfNotNull(PerlHostData.from(sdk), PerlHostData::getOsHandler);
  }
}
