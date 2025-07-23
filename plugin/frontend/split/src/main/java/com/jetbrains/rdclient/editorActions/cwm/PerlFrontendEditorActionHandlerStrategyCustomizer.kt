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

package com.jetbrains.rdclient.editorActions.cwm

import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor
import com.jetbrains.rd.ide.model.ActionCallStrategyKind
import com.perl5.lang.perl.util.PerlFrontendUtil

class PerlFrontendEditorActionHandlerStrategyCustomizer : FrontendEditorActionHandlerStrategyCustomizer {
  private val uncustomizableActions = setOf(
    com.intellij.openapi.actionSystem.IdeActions.MOVE_ELEMENT_RIGHT
  )

  override fun getCustomStrategy(
    actionId: String,
    editor: Editor,
    caret: Caret?,
    dataContext: DataContext
  ): ActionCallStrategyKind? =
    if ((actionId !in uncustomizableActions) && PerlFrontendUtil.isPluginDocument(editor.document))
      ActionCallStrategyKind.FrontendOnly
    else null
}