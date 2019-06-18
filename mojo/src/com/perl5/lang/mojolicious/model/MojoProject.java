/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package com.perl5.lang.mojolicious.model;

import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.regex.Pattern;

/**
 * Base class for mojo-based projects: applications and plugins
 */
public abstract class MojoProject {
  @NotNull
  private final VirtualFile myRoot;

  public MojoProject(@NotNull VirtualFile root) {
    myRoot = root;
  }

  @NotNull
  public VirtualFile getRoot() {
    return myRoot;
  }

  @NotNull
  public abstract Icon getIcon();

  @NotNull
  public abstract String getTypeName();

  public boolean isValid() {
    return myRoot.isValid();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    MojoProject project = (MojoProject)o;

    return myRoot.equals(project.myRoot);
  }

  @Override
  public int hashCode() {
    return myRoot.hashCode();
  }

  @Override
  public String toString() {
    return getTypeName() + ": " + myRoot;
  }

  protected static abstract class NameValidator implements InputValidator {
    @NotNull
    protected abstract Pattern getPattern();

    @Override
    public boolean checkInput(String inputString) {
      return getPattern().matcher(inputString).matches();
    }

    @Override
    public boolean canClose(String inputString) {
      return checkInput(inputString);
    }
  }
}
