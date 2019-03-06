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

package com.perl5.lang.perl.idea.run;

import com.intellij.execution.Location;
import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.actions.LazyRunConfigurationProducer;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.idea.run.debugger.PerlRemoteFileSystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class GenericPerlRunConfigurationProducer<Configuration extends GenericPerlRunConfiguration>
  extends LazyRunConfigurationProducer<Configuration> {
  @Nullable
  private VirtualFile findPerlFile(ConfigurationContext configurationContext) {
    Location location = configurationContext.getLocation();
    VirtualFile virtualFile = location == null ? null : location.getVirtualFile();
    return virtualFile != null && !(virtualFile instanceof PerlRemoteFileSystem.PerlRemoteVirtualFile) && isOurFile(virtualFile) ?
           virtualFile : null;
  }

  @Override
  public boolean isConfigurationFromContext(Configuration runConfiguration, ConfigurationContext configurationContext) {
    VirtualFile perlFile = findPerlFile(configurationContext);
    return perlFile != null && Comparing.equal(runConfiguration.getScriptFile(), perlFile);
  }

  @Override
  protected boolean setupConfigurationFromContext(Configuration runConfiguration,
                                                  ConfigurationContext configurationContext,
                                                  Ref<PsiElement> ref) {
    VirtualFile perlFile = findPerlFile(configurationContext);
    if (perlFile != null) {
      runConfiguration.setScriptPath(perlFile.getPath());
      runConfiguration.setConsoleCharset(perlFile.getCharset().displayName());
      runConfiguration.setGeneratedName();
      return true;
    }
    return false;
  }

  /**
   * @return true iff {@code virtualFile} acceptable for this run configuration prodducer
   */
  protected abstract boolean isOurFile(@NotNull VirtualFile virtualFile);
}
