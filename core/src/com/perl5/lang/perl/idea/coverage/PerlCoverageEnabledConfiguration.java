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

package com.perl5.lang.perl.idea.coverage;

import com.intellij.coverage.CoverageRunner;
import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.execution.configurations.coverage.CoverageEnabledConfiguration;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.perl5.lang.perl.idea.run.GenericPerlRunConfiguration;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class PerlCoverageEnabledConfiguration extends CoverageEnabledConfiguration {
  public PerlCoverageEnabledConfiguration(RunConfigurationBase configuration) {
    super(configuration);
    setCoverageRunner(CoverageRunner.getInstance(PerlCoverageRunner.class));
  }

  @Override
  public GenericPerlRunConfiguration getConfiguration() {
    return (GenericPerlRunConfiguration)super.getConfiguration();
  }

  @Nullable
  @Override
  protected String createCoverageFile() {
    CoverageRunner coverageRunner = getCoverageRunner();
    if (coverageRunner == null) {
      return null;
    }
    String coverageRootPath = PathManager.getSystemPath() + File.separator + "coverage";
    GenericPerlRunConfiguration perlRunConfiguration = getConfiguration();
    Project project = perlRunConfiguration.getProject();
    String path = coverageRootPath + File.separator +
                  FileUtil.sanitizeFileName(project.getName()) +
                  this.coverageFileNameSeparator() +
                  FileUtil.sanitizeFileName(perlRunConfiguration.getName()) + "." + coverageRunner.getDataFileExtension();
    (new File(coverageRootPath)).mkdirs();
    return path;
  }

  @Override
  protected String coverageFileNameSeparator() {
    return "_S_";
  }
}
