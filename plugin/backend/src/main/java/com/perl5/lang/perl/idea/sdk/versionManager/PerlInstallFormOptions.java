/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.sdk.versionManager;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

/**
 * Additional possible options for version manager perl installation
 */
public abstract class PerlInstallFormOptions {
  public @NotNull JPanel getRootPanel() {
    return new JPanel();
  }

  /**
   * Returns target perl installation id. perlbrew allows to override target name
   */
  public @NotNull String getTargetName(@NotNull String distributionId) {
    return distributionId;
  }

  /**
   * Builds command line parameters based on form options
   */
  public @NotNull List<String> buildParametersList() {return Collections.emptyList();}
}
