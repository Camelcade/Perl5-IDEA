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

package base;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

import static org.junit.Assert.assertNotNull;

class PerlBrewWithExternalLibrariesLocalInterpreterConfigurator extends PerlBrewLocalInterpreterConfigurator {
  public static final PerlBrewWithExternalLibrariesLocalInterpreterConfigurator INSTANCE =
    new PerlBrewWithExternalLibrariesLocalInterpreterConfigurator();

  protected static final String LIB_PATH = "~/.perlbrew/libs/" + DISTRIBUTION_ID + "/lib/perl5";

  private PerlBrewWithExternalLibrariesLocalInterpreterConfigurator() {
  }

  @Override
  void setUpPerlInterpreter(@NotNull Project project) {
    super.setUpPerlInterpreter(project);
    var libraryVirtualFile = LocalFileSystem.getInstance().findFileByPath(FileUtil.expandUserHome(LIB_PATH));
    assertNotNull("Unable to find library path " + LIB_PATH, libraryVirtualFile);
    PerlProjectManager.getInstance(project).addExternalLibraries(Collections.singletonList(libraryVirtualFile));
  }

  @Override
  protected @NotNull String getDistributionId() {
    return BASE_DISTRIBUTION_ID;
  }

  @Override
  public String toString() {
    return "perlbrew: " + BASE_DISTRIBUTION_ID + " with external library";
  }
}
