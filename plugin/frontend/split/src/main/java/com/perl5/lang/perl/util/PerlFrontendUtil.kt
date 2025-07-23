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

package com.perl5.lang.perl.util

import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileTypes.FileTypeManager
import com.perl5.lang.perl.fileTypes.PerlPluginBaseFileType

object PerlFrontendUtil {
  fun isPluginDocument(document: Document?): Boolean {
    if (document == null) return false
    val virtualFile = FileDocumentManager.getInstance().getFile(document) ?: return false
    val fileType = FileTypeManager.getInstance().getFileTypeByFileName(virtualFile.nameSequence)
    return fileType is PerlPluginBaseFileType
  }
}