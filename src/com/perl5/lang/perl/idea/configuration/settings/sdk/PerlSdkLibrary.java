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

package com.perl5.lang.perl.idea.configuration.settings.sdk;

import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkType;
import com.intellij.openapi.roots.SyntheticLibrary;
import com.intellij.openapi.vfs.VirtualFile;
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

  @Nullable
  @Override
  public String getPresentableText() {
    return mySdk.getName();
  }

  @Nullable
  @Override
  public String getLocationString() {
    return mySdk.getHomePath();
  }

  @Nullable
  @Override
  public Icon getIcon(boolean unused) {
    return ((SdkType)mySdk.getSdkType()).getIcon();
  }

  @NotNull
  @Override
  public Collection<VirtualFile> getSourceRoots() {
    return myRoots;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PerlSdkLibrary)) return false;

    PerlSdkLibrary library = (PerlSdkLibrary)o;

    if (myRoots != null ? !myRoots.equals(library.myRoots) : library.myRoots != null) return false;
    return mySdk != null ? mySdk.equals(library.mySdk) : library.mySdk == null;
  }

  @Override
  public int hashCode() {
    int result = myRoots != null ? myRoots.hashCode() : 0;
    result = 31 * result + (mySdk != null ? mySdk.hashCode() : 0);
    return result;
  }
}
