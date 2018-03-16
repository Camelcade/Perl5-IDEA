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

package com.perl5.lang.perl.psi.stubs.globs;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.*;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.parser.elementTypes.PsiElementProvider;
import com.perl5.lang.perl.psi.PerlGlobVariable;
import com.perl5.lang.perl.psi.PsiPerlGlobVariable;
import com.perl5.lang.perl.psi.impl.PsiPerlGlobVariableImpl;
import com.perl5.lang.perl.psi.stubs.PerlStubSerializationUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created by hurricup on 25.05.2015.
 */
public class PerlGlobStubElementType extends IStubElementType<PerlGlobStub, PsiPerlGlobVariable> implements PsiElementProvider {

  public PerlGlobStubElementType(String name) {
    super(name, PerlLanguage.INSTANCE);
  }

  @Override
  public PsiPerlGlobVariable createPsi(@NotNull PerlGlobStub stub) {
    return new PsiPerlGlobVariableImpl(stub, this);
  }

  @NotNull
  @Override
  public PsiElement getPsiElement(@NotNull ASTNode node) {
    return new PsiPerlGlobVariableImpl(node);
  }

  @Override
  public PerlGlobStub createStub(@NotNull PsiPerlGlobVariable psi, StubElement parentStub) {
    return new PerlGlobStubImpl(parentStub, psi.getPackageName(), psi.getName(), psi.isLeftSideOfAssignment());
  }

  @NotNull
  @Override
  public String getExternalId() {
    return "perl." + super.toString();
  }

  @Override
  public void indexStub(@NotNull PerlGlobStub stub, @NotNull IndexSink sink) {
    String name = stub.getPackageName() + PerlPackageUtil.PACKAGE_SEPARATOR + stub.getName();
    sink.occurrence(PerlGlobsStubIndex.KEY, name);
    sink.occurrence(PerlGlobsStubIndex.KEY, "*" + stub.getPackageName());
  }

  @Override
  public void serialize(@NotNull PerlGlobStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(stub.getPackageName());
    dataStream.writeName(stub.getName());
    dataStream.writeBoolean(stub.isLeftSideOfAssignment());
  }

  @NotNull
  @Override
  public PerlGlobStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return new PerlGlobStubImpl(parentStub, PerlStubSerializationUtil.readString(dataStream),
                                PerlStubSerializationUtil.readString(dataStream), dataStream.readBoolean());
  }

  @Override
  public boolean shouldCreateStub(ASTNode node) {
    PsiElement psi = node.getPsi();
    return psi instanceof PerlGlobVariable &&
           psi.isValid() &&
           StringUtil.isNotEmpty(((PerlGlobVariable)psi).getCanonicalName());
  }
}
