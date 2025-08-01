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

package com.perl5.lang.perl.psi.stubs

import com.intellij.psi.PsiFile
import com.intellij.psi.StubBuilder
import com.intellij.psi.stubs.DefaultStubBuilder
import com.intellij.psi.stubs.LanguageStubDefinition
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.util.elementType
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageProcessorService
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.serialization.PerlValueBackendHelper
import com.perl5.lang.perl.psi.PerlFile
import com.perl5.lang.perl.psi.PerlSubCallHandlerVersionService


private const val baseVersion = 7

internal open class PerlStubDefinition : LanguageStubDefinition {
  override val builder: StubBuilder
    get() = object : DefaultStubBuilder() {
      override fun createStubForFile(file: PsiFile): StubElement<*> =
        PerlFileStub(file as PerlFile, file.elementType as PerlFileElementType)
    }

  override val stubVersion: Int = baseVersion +
    PerlValueBackendHelper.getVersion() +
    PerlSubCallHandlerVersionService.getHandlersVersion() +
    PerlPackageProcessorService.getVersion()
}