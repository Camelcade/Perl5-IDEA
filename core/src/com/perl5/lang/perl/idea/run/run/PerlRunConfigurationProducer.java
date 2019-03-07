/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.run.run;

import com.intellij.execution.actions.ConfigurationFromContext;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.perl.fileTypes.PerlFileTypeScript;
import com.perl5.lang.perl.fileTypes.PerlFileTypeTest;
import com.perl5.lang.perl.idea.run.GenericPerlRunConfigurationProducer;
import com.perl5.lang.perl.idea.run.prove.PerlTestRunConfigurationType;
import org.jetbrains.annotations.NotNull;

public class PerlRunConfigurationProducer extends GenericPerlRunConfigurationProducer<PerlRunConfiguration> {
  @NotNull
  @Override
  public ConfigurationFactory getConfigurationFactory() {
    return PerlRunConfigurationType.getInstance().getConfigurationFactories()[0];
  }

  public boolean isOurFile(@NotNull VirtualFile virtualFiles) {
    FileType fileType = virtualFiles.getFileType();
    return fileType == PerlFileTypeScript.INSTANCE || fileType == PerlFileTypeTest.INSTANCE;
  }

  @Override
  public boolean isPreferredConfiguration(ConfigurationFromContext self, ConfigurationFromContext other) {
    return !other.getConfigurationType().equals(PerlTestRunConfigurationType.getInstance());
  }

  @NotNull
  public static PerlRunConfigurationProducer getInstance() {
    return getInstance(PerlRunConfigurationProducer.class);
  }
}
