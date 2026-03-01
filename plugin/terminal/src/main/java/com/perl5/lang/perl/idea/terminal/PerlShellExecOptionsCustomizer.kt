/*
 * Copyright 2015-2026 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.terminal

import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.perl5.lang.perl.idea.project.PerlProjectManager
import com.perl5.lang.perl.idea.sdk.host.PerlHostHandler
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerData
import com.perl5.lang.perl.util.PerlPluginUtil
import org.jetbrains.plugins.terminal.startup.MutableShellExecOptions
import org.jetbrains.plugins.terminal.startup.ShellExecOptionsCustomizer

private val LOG = logger<PerlShellExecOptionsCustomizer>()

class PerlShellExecOptionsCustomizer : ShellExecOptionsCustomizer {
  override fun customizeExecOptions(
    project: Project,
    shellExecOptions: MutableShellExecOptions
  ) {
    val perlSdk = PerlProjectManager.getSdk(project) ?: run {
      LOG.debug("No perl sdk set for the project ", project.name)
      return
    }

    val hostHandler = PerlHostHandler.from(perlSdk)
    if (hostHandler?.isLocal != true) {
      LOG.debug("Non-local or missing host handler ", hostHandler, " in ", perlSdk)
      return
    }

    val versionManagerData = PerlVersionManagerData.from(perlSdk) ?: run {
      LOG.debug("Missing version manager data in ", perlSdk)
      return
    }

    val customizerScriptName = versionManagerData.terminalCustomizerScriptName ?: run {
      LOG.debug("Version manager does not support terminal customization: ", perlSdk)
      return
    }

    val customizerScriptPath = PerlPluginUtil.getHelperPath(customizerScriptName)

    shellExecOptions.setEnvironmentVariable("JEDITERM_SOURCE", customizerScriptPath)
    shellExecOptions.setEnvironmentVariable("PERL_VERSION_MANAGER_PATH", versionManagerData.versionManagerPath)
    shellExecOptions.setEnvironmentVariable("PERL_VERSION_MANAGER_DISTRIBUTION_ID", versionManagerData.distributionId)
  }
}