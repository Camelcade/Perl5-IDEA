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

package com.perl5.lang.mojolicious.model;

import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.mojolicious.MojoBundle;
import com.perl5.lang.mojolicious.MojoIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.regex.Pattern;

public class MojoPlugin extends MojoProject {
  public MojoPlugin(@NotNull VirtualFile root) {
    super(root);
  }

  @Override
  public @NotNull Icon getIcon() {
    return MojoIcons.pluginIcon();
  }

  @Override
  public @NotNull String getTypeName() {
    return MojoBundle.message("mojo.plugin.type.name");
  }

  public static class NameValidator extends MojoProject.NameValidator {
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z](?:[\\w_-]|::)++$");

    @Override
    protected @NotNull Pattern getPattern() {
      return NAME_PATTERN;
    }
  }
}
