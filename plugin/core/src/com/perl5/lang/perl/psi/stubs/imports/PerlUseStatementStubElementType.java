/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package com.perl5.lang.perl.psi.stubs.imports;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.perl5.lang.perl.parser.elementTypes.PsiElementProvider;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement;
import com.perl5.lang.perl.psi.stubs.PerlPolyNamedElementType;
import com.perl5.lang.perl.psi.stubs.PerlStubSerializationUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.perl5.lang.perl.psi.stubs.PerlStubSerializationUtil.readString;
import static com.perl5.lang.perl.psi.stubs.PerlStubSerializationUtil.readStringsList;


public class PerlUseStatementStubElementType extends PerlPolyNamedElementType<PerlUseStatementStub, PerlUseStatementElement>
  implements PsiElementProvider {
  public PerlUseStatementStubElementType(String debugName) {
    super(debugName);
  }

  @Override
  public PerlUseStatementElement createPsi(@NotNull PerlUseStatementStub stub) {
    return new PerlUseStatementElement(stub, this);
  }

  @Override
  public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
    return new PerlUseStatementElement(node);
  }

  @Override
  protected @NotNull PerlUseStatementStub createStub(@NotNull PerlUseStatementElement psi,
                                                     StubElement<?> parentStub,
                                                     @NotNull List<StubElement<?>> lightElementsStubs) {
    return new PerlUseStatementStub(
      parentStub,
      psi.getNamespaceName(),
      Objects.requireNonNull(psi.getPackageName()),
      psi.getImportParameters(),
      lightElementsStubs
    );
  }

  @Override
  protected @NotNull PerlUseStatementStub deserialize(@NotNull StubInputStream dataStream,
                                                      StubElement<?> parentStub,
                                                      @NotNull List<StubElement<?>> lightElementsStubs) throws IOException {
    return new PerlUseStatementStub(parentStub,
                                    Objects.requireNonNull(readString(dataStream)),
                                    Objects.requireNonNull(readString(dataStream)),
                                    readStringsList(dataStream),
                                    lightElementsStubs);
  }

  @Override
  public void serializeStub(@NotNull PerlUseStatementStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(stub.getNamespaceName());
    dataStream.writeName(stub.getPackageName());
    PerlStubSerializationUtil.writeStringsList(dataStream, stub.getImportParameters());
  }

  @Override
  public void doIndexStub(@NotNull PerlUseStatementStub stub, @NotNull IndexSink sink) {
    sink.occurrence(PerlUseStatementsIndex.KEY, stub.getNamespaceName());
  }

  @Override
  public boolean shouldCreateStub(ASTNode node) {
    PsiElement element = node.getPsi();
    return element instanceof PerlUseStatementElement &&
           element.isValid() &&
           StringUtil.isNotEmpty(((PerlUseStatementElement)element).getPackageName());
  }
}
