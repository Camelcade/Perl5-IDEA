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

package com.perl5.lang.perl.idea.run.prove;

import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.containers.SmartHashSet;
import com.perl5.lang.perl.buildSystem.PerlBuildSystemHandler;
import com.perl5.lang.perl.fileTypes.PerlFileTypeTest;
import com.perl5.lang.perl.idea.run.GenericPerlRunConfigurationProducer;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class PerlTestRunConfigurationProducer extends PerlAbstractTestRunConfigurationProducer<PerlTestRunConfiguration> {
  @Override
  public @NotNull ConfigurationFactory getConfigurationFactory() {
    return PerlTestRunConfigurationType.getInstance().getTestConfigurationFactory();
  }

  @Override
  protected @NotNull List<VirtualFile> computeTargetFiles(@NotNull ConfigurationContext configurationContext) {
    var targetFiles = super.computeTargetFiles(configurationContext);
    var projectFileIndex = ProjectFileIndex.getInstance(configurationContext.getProject());
    var processedModules = new SmartHashSet<Module>();
    for (VirtualFile targetFile : targetFiles) {
      var fileModule = projectFileIndex.getModuleForFile(targetFile);
      if( processedModules.add(fileModule) && PerlBuildSystemHandler.getTestsHandler(fileModule) != null ){
        return Collections.emptyList();
      }
    }
    return targetFiles;
  }

  public static @NotNull PerlTestRunConfigurationProducer getInstance() {
    return getInstance(PerlTestRunConfigurationProducer.class);
  }
}
