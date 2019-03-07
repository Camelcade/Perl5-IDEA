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
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtil;
import com.perl5.lang.perl.idea.run.debugger.PerlRemoteFileSystem;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public abstract class GenericPerlRunConfigurationProducer<Configuration extends GenericPerlRunConfiguration>
  extends LazyRunConfigurationProducer<Configuration> {
  @NotNull
  private List<VirtualFile> computeTargetFiles(ConfigurationContext configurationContext) {
    if (configurationContext.containsMultipleSelection() && !allowMultipleFiles()) {
      return Collections.emptyList();
    }
    Set<VirtualFile> virtualFiles = new LinkedHashSet<>();
    Consumer<Location> locationConsumer = location -> {
      if (location != null) {
        VirtualFile virtualFile = location.getVirtualFile();
        if (virtualFile != null && !(virtualFile instanceof PerlRemoteFileSystem.PerlRemoteVirtualFile) && isOurFile(virtualFile)) {
          virtualFiles.add(virtualFile);
        }
      }
    };

    DataContext dataContext = configurationContext.getDataContext();
    Location<?>[] locations = Location.DATA_KEYS.getData(dataContext);
    if (locations != null) {
      for (Location<?> location : locations) {
        locationConsumer.accept(location);
      }
    }

    PsiElement[] psiElements = LangDataKeys.PSI_ELEMENT_ARRAY.getData(dataContext);
    if (psiElements != null) {
      for (PsiElement psiElement : psiElements) {
        if (psiElement instanceof PsiFile || psiElement instanceof PsiDirectory) {
          VirtualFile virtualFile = PsiUtil.getVirtualFile(psiElement);
          if (virtualFile != null && isOurFile(virtualFile)) {
            virtualFiles.add(virtualFile);
          }
        }
      }
    }

    locationConsumer.accept(configurationContext.getLocation());

    return new ArrayList<>(virtualFiles);
  }

  @Override
  public boolean isConfigurationFromContext(Configuration runConfiguration, ConfigurationContext configurationContext) {
    return Comparing.equal(runConfiguration.computeTargetFiles(), computeTargetFiles(configurationContext));
  }

  @Override
  protected boolean setupConfigurationFromContext(Configuration runConfiguration,
                                                  ConfigurationContext configurationContext,
                                                  Ref<PsiElement> ref) {
    List<VirtualFile> targetFiles = computeTargetFiles(configurationContext);
    if (targetFiles.isEmpty()) {
      return false;
    }
    runConfiguration.setScriptPath(GenericPerlRunConfiguration.computePathsFromVirtualFiles(targetFiles));
    runConfiguration.setConsoleCharset(targetFiles.get(0).getCharset().displayName());
    runConfiguration.setGeneratedName();
    return true;
  }

  /**
   * @return true iff configuration allows multiple files
   */
  public boolean allowMultipleFiles() {
    return false;
  }

  /**
   * @return true iff {@code virtualFile} is acceptable for this run configuration producer
   */
  public abstract boolean isOurFile(@NotNull VirtualFile virtualFile);
}
