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
import com.intellij.openapi.roots.SyntheticLibrary;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PerlLibraryProvider extends AdditionalLibraryRootsProvider {
  @NotNull
  @Override
  public Collection<SyntheticLibrary> getAdditionalProjectLibraries(@NotNull Project project) {
    PerlProjectManager perlProjectManager = PerlProjectManager.getInstance(project);
    List<VirtualFile> sdkLibs = perlProjectManager.getProjectSdkLibraryRoots();
    if (sdkLibs.isEmpty()) {
      return Collections.emptyList();
    }

    SyntheticLibrary sdkLibrary;
    Sdk sdk = perlProjectManager.getProjectSdk();
    if (sdk != null) {
      sdkLibrary = new PerlSdkLibrary(sdk, sdkLibs);
    }
    else {
      sdkLibrary = SyntheticLibrary.newImmutableLibrary(sdkLibs);
    }

    List<VirtualFile> libraryRoots = perlProjectManager.getNonSdkLibraryRoots();

    if (libraryRoots.isEmpty()) {
      return Collections.singletonList(sdkLibrary);
    }
    List<SyntheticLibrary> result = new ArrayList<>();
    result.add(SyntheticLibrary.newImmutableLibrary(libraryRoots));
    result.add(sdkLibrary);
    return result;
  }

  @NotNull
  @Override
  public Collection<VirtualFile> getRootsToWatch(@NotNull Project project) {
    return PerlProjectManager.getInstance(project).getAllLibraryRoots();
  }
}
