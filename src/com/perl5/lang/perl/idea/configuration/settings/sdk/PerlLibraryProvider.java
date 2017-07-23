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

import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.AdditionalLibraryRootsProvider;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.SyntheticLibrary;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PerlLibraryProvider extends AdditionalLibraryRootsProvider {
  @NotNull
  @Override
  public Collection<SyntheticLibrary> getAdditionalProjectLibraries(@NotNull Project project) {
    List<VirtualFile> sdkLibs = getSdkLibs(project);
    if (sdkLibs.isEmpty()) {
      return Collections.emptyList();
    }
    Sdk sdk = PerlSharedSettings.getInstance(project).getSdk();
    assert sdk != null;
    return Collections.singletonList(new PerlSdkLibrary(sdk, sdkLibs));
  }

  private List<VirtualFile> getSdkLibs(@NotNull Project project) {
    Sdk sdk = PerlSharedSettings.getInstance(project).getSdk();
    if (sdk == null) {
      return Collections.emptyList();
    }
    return Arrays.asList(sdk.getRootProvider().getFiles(OrderRootType.CLASSES));
  }

  @NotNull
  @Override
  public Collection<VirtualFile> getRootsToWatch(@NotNull Project project) {
    return getSdkLibs(project);
  }
}
