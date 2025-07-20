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

import com.intellij.psi.stubs.*
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceIndex.NAMESPACE_KEY
import com.perl5.lang.perl.psi.stubs.namespaces.deserializeNamespaceData
import com.perl5.lang.perl.psi.stubs.namespaces.serializeNamespaceData
import com.perl5.lang.perl.util.PerlPackageUtilCore


class PerlFileStubserializer(val fileType: PerlFileElementType) : StubSerializer<PerlFileStub> {
  override fun getExternalId(): String = "perl5.file.$fileType"

  override fun serialize(stub: PerlFileStub, dataStream: StubOutputStream): Unit = dataStream.serializeNamespaceData(stub.data)

  override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): PerlFileStub =
    PerlFileStub(dataStream.deserializeNamespaceData(), fileType)

  override fun indexStub(stub: PerlFileStub, sink: IndexSink) {
    if (stub.isEmpty && stub.getNamespaceName() == PerlPackageUtilCore.MAIN_NAMESPACE_NAME) {
      return
    }
    sink.occurrence(NAMESPACE_KEY, stub.getNamespaceName())
  }
}
