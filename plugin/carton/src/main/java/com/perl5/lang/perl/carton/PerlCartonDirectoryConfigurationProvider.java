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

package com.perl5.lang.perl.carton;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.perl.idea.project.PerlDirectoryConfigurationProvider;
import com.perl5.lang.perl.idea.project.PerlDirectoryInfoCollector;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.sdk.PerlConfig;
import org.jetbrains.annotations.NotNull;

public class PerlCartonDirectoryConfigurationProvider implements PerlDirectoryConfigurationProvider {
  @Override
  public void configureContentRoot(@NotNull Module module,
                                   @NotNull VirtualFile contentRoot,
                                   @NotNull PerlDirectoryInfoCollector collector) {
    var localRoot = contentRoot.findChild("local");
    if (localRoot == null) {
      return;
    }
    var localLibRoot = localRoot.findChild("lib");
    if (localLibRoot == null) {
      return;
    }

    var perl5Root = localLibRoot.findChild("perl5");
    var cpanfile = contentRoot.findChild("cpanfile");

    if (cpanfile != null || perl5Root != null) {
      collector.addTestRoot(contentRoot.findChild("t"));
      collector.addLibRoot(contentRoot.findChild("lib"));
    }

    if (perl5Root == null) {
      return;
    }

    collector.addExcludedRoot(localRoot);
    collector.addExternalLibRoot(perl5Root);

    var perlConfig = PerlConfig.from(PerlProjectManager.getSdk(module));
    if (perlConfig == null) {
      return;
    }

    var versionString = perlConfig.getVersion();
    var versionDir = versionString == null ? null : perl5Root.findChild(versionString);
    collector.addExternalLibRoot(versionDir);

    var archname = perlConfig.getArchname();
    if (archname != null) {
      collector.addExternalLibRoot(perl5Root.findChild(archname));

      if (versionDir != null) {
        collector.addExternalLibRoot(versionDir.findChild(archname));
      }
    }
  }
}
