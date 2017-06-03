/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.findusages;

import com.intellij.openapi.vcs.FileStatus;
import com.intellij.usages.UsageGroup;
import com.intellij.usages.UsageView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by hurricup on 11.04.2016.
 * https://github.com/consulo/consulo-csharp/blob/255e7ce81b7e5e9bee1a8a9c151f1854265e4259/csharp-impl/src/org/mustbe/consulo/csharp/ide/findUsage/groupingRule/CSharpBaseGroupingRule.java
 */
public class PerlUsageGroupRule implements UsageGroup {
  @Nullable
  @Override
  public Icon getIcon(boolean isOpen) {
    return null;
  }

  @NotNull
  @Override
  public String getText(@Nullable UsageView view) {
    return null;
  }

  @Nullable
  @Override
  public FileStatus getFileStatus() {
    return null;
  }

  @Override
  public boolean isValid() {
    return false;
  }

  @Override
  public void update() {

  }

  @Override
  public int compareTo(UsageGroup o) {
    return 0;
  }

  @Override
  public void navigate(boolean requestFocus) {

  }

  @Override
  public boolean canNavigate() {
    return false;
  }

  @Override
  public boolean canNavigateToSource() {
    return false;
  }
}
