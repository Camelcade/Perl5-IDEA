/*
 * Copyright 2015-2017 Alexandr Evstigneev
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
import com.intellij.psi.stubs.*;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.parser.elementTypes.PsiElementProvider;
import com.perl5.lang.perl.psi.PerlUseStatement;
import com.perl5.lang.perl.psi.impl.PsiPerlUseStatementImpl;
import com.perl5.lang.perl.psi.stubs.PerlStubSerializationUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created by hurricup on 20.08.2015.
 */
public class PerlUseStatementStubElementType extends IStubElementType<PerlUseStatementStub, PerlUseStatement>
  implements PsiElementProvider {
  public PerlUseStatementStubElementType(String debugName) {
    super(debugName, PerlLanguage.INSTANCE);
  }

  @Override
  public PerlUseStatement createPsi(@NotNull PerlUseStatementStub stub) {
    return new PsiPerlUseStatementImpl(stub, this);
  }

  @NotNull
  @Override
  public PsiElement getPsiElement(@NotNull ASTNode node) {
    return new PsiPerlUseStatementImpl(node);
  }

  @Override
  public PerlUseStatementStub createStub(@NotNull PerlUseStatement psi, StubElement parentStub) {
    return new PerlUseStatementStubImpl(
      parentStub,
      psi.getOuterPackageName(),
      psi.getPackageName(),
      psi.getImportParameters()
    );
  }

  @NotNull
  @Override
  public String getExternalId() {
    return "perl." + super.toString();
  }

  @Override
  public void serialize(@NotNull PerlUseStatementStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(stub.getOuterPackageName());
    dataStream.writeName(stub.getPackageName());
    PerlStubSerializationUtil.writeStringsList(dataStream, stub.getImportParameters());
  }

  @NotNull
  @Override
  public PerlUseStatementStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return new PerlUseStatementStubImpl(parentStub, dataStream.readName().toString(), dataStream.readName().toString(),
                                        PerlStubSerializationUtil.readStringsList(dataStream));
  }

  @Override
  public void indexStub(@NotNull PerlUseStatementStub stub, @NotNull IndexSink sink) {
  }

  @Override
  public boolean shouldCreateStub(ASTNode node) {
    PsiElement element = node.getPsi();
    return element instanceof PsiPerlUseStatementImpl &&
           element.isValid() &&
           StringUtil.isNotEmpty(((PsiPerlUseStatementImpl)element).getPackageName());
  }
}
