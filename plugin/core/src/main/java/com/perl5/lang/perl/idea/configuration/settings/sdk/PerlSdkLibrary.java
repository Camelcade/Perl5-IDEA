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

package com.perl5.lang.perl.idea.configuration.settings.sdk;

import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkType;
import com.intellij.openapi.roots.SyntheticLibrary;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Collection;
import java.util.List;

public class PerlSdkLibrary extends SyntheticLibrary implements ItemPresentation {
  private final List<VirtualFile> myRoots;
  private final Sdk mySdk;

  public PerlSdkLibrary(@NotNull Sdk sdk, @NotNull List<VirtualFile> roots) {
    mySdk = sdk;
    myRoots = roots;
  }

  @Override
  public @Nullable String getPresentableText() {
    return mySdk.getName();
  }

  @Override
  public @Nullable String getLocationString() {
    return PerlProjectManager.getInterpreterPath(mySdk);
  }

  @Override
  public @Nullable Icon getIcon(boolean unused) {
    return ((SdkType)mySdk.getSdkType()).getIcon();
  }

  @Override
  public @NotNull Collection<VirtualFile> getSourceRoots() {
    return myRoots;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PerlSdkLibrary)) return false;

    PerlSdkLibrary library = (PerlSdkLibrary)o;

    if (!myRoots.equals(library.myRoots)) {
      return false;
    }
    return mySdk.equals(library.mySdk);
  }

  @Override
  public int hashCode() {
    int result = myRoots.hashCode();
    result = 31 * result + mySdk.hashCode();
    return result;
  }
}
