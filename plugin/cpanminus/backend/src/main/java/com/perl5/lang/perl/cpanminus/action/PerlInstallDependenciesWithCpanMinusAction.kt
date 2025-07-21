/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package com.perl5.lang.perl.cpanminus.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import com.perl5.PerlIcons
import com.perl5.lang.perl.cpanminus.PerlCpanminusBundle
import com.perl5.lang.perl.cpanminus.adapter.CpanminusAdapter
import com.perl5.lang.perl.cpanminus.cpanfile.PerlFileTypeCpanfile
import com.perl5.lang.perl.idea.actions.PerlActionBase
import com.perl5.lang.perl.idea.project.PerlProjectManager

private val log = Logger.getInstance(PerlInstallDependenciesWithCpanMinusAction::class.java)

class PerlInstallDependenciesWithCpanMinusAction :
  PerlActionBase(PerlCpanminusBundle.message("install.dependencies.with.cpanminus"), PerlIcons.METACPAN) {
  override fun isEnabled(event: AnActionEvent): Boolean {
    if (!super.isEnabled(event)) {
      return false
    }

    return getVirtualFile(event)?.fileType == PerlFileTypeCpanfile.INSTANCE
  }

  override fun actionPerformed(event: AnActionEvent) {
    val eventFile = getVirtualFile(event)
    if (eventFile?.fileType != PerlFileTypeCpanfile.INSTANCE) {
      log.warn("No file or wrong file type $eventFile")
      return
    }

    val perlSdk = PerlProjectManager.getSdk(getEventProject(event))
    if (perlSdk == null) {
      log.warn("No perl sdk for the project")
      return
    }

    CpanminusAdapter.installModules(perlSdk, getEventProject(event), listOf("--installdeps", eventFile.parent!!.path), null, true)
  }
}