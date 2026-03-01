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

package com.perl5.lang.perl.idea.terminal;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.sdk.host.PerlHostHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerData;
import com.perl5.lang.perl.util.PerlPluginUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.terminal.LocalTerminalCustomizer;

import java.util.Map;

public class PerlLocalTerminalCustomizer extends LocalTerminalCustomizer {
  private static final Logger LOG = Logger.getInstance(PerlLocalTerminalCustomizer.class);

  @Override
  public String[] customizeCommandAndEnvironment(@NotNull Project project,
                                                 @Nullable String workingDirectory,
                                                 @NotNull String[] command,
                                                 @NotNull Map<String, String> envs) {
    var perlSdk = PerlProjectManager.getSdk(project);
    if (perlSdk == null) {
      LOG.debug("No perl sdk set for the project ", project.getName());
      return command;
    }

    PerlHostHandler<?, ?> hostHandler = PerlHostHandler.from(perlSdk);
    if (hostHandler == null || !hostHandler.isLocal()) {
      LOG.debug("Non-local or missing host handler ", hostHandler, " in ", perlSdk);
      return command;
    }

    PerlVersionManagerData<?, ?> versionManagerData = PerlVersionManagerData.from(perlSdk);
    if (versionManagerData == null) {
      LOG.debug("Missing version manager data in ", perlSdk);
      return command;
    }

    var customizerScriptName = versionManagerData.getTerminalCustomizerScriptName();
    if (customizerScriptName == null) {
      LOG.debug("Version manager does not support terminal customization: ", perlSdk);
      return command;
    }

    var customizerScriptPath = PerlPluginUtil.getHelperPath(customizerScriptName);

    envs.put("JEDITERM_SOURCE", customizerScriptPath);
    envs.put("PERL_VERSION_MANAGER_PATH", versionManagerData.getVersionManagerPath());
    envs.put("PERL_VERSION_MANAGER_DISTRIBUTION_ID", versionManagerData.getDistributionId());

    return command;
  }
}
