/*
 * Copyright 2015-2022 Alexandr Evstigneev
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
import com.perl5.lang.perl.psi.PerlGlobVariableElement;
import com.perl5.lang.perl.psi.PsiPerlGlobVariable;
import com.perl5.lang.perl.psi.impl.PsiPerlGlobVariableImpl;
import com.perl5.lang.perl.psi.stubs.PerlStubSerializationUtil;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlCallableNamesIndex;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static com.perl5.lang.perl.psi.stubs.globs.PerlGlobNamespaceStubIndex.KEY_GLOB_NAMESPACE;
import static com.perl5.lang.perl.psi.stubs.globs.PerlGlobStubIndex.KEY_GLOB;


public class PerlGlobStubElementType extends IStubElementType<PerlGlobStub, PsiPerlGlobVariable> implements PsiElementProvider {

  public PerlGlobStubElementType(String name) {
    super(name, PerlLanguage.INSTANCE);
  }

  @Override
  public PsiPerlGlobVariable createPsi(@NotNull PerlGlobStub stub) {
    return new PsiPerlGlobVariableImpl(stub, this);
  }

  @Override
  public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
    return new PsiPerlGlobVariableImpl(node);
  }

  @Override
  public @NotNull PerlGlobStub createStub(@NotNull PsiPerlGlobVariable psi, StubElement parentStub) {
    return new PerlGlobStub(parentStub, psi.getNamespaceName(), psi.getName(), psi.isLeftSideOfAssignment());
  }

  @Override
  public @NotNull String getExternalId() {
    return "perl." + super.toString();
  }

  @Override
  public void indexStub(@NotNull PerlGlobStub stub, @NotNull IndexSink sink) {
    String canonicalName = stub.getCanonicalName();
    if (StringUtil.isNotEmpty(canonicalName)) {
      sink.occurrence(KEY_GLOB, canonicalName);
    }
    var namespaceName = stub.getNamespaceName();
    if (StringUtil.isNotEmpty(namespaceName)) {
      sink.occurrence(KEY_GLOB_NAMESPACE, namespaceName);
    }
    var callableName = stub.getCallableName();
    if (StringUtil.isNotEmpty(callableName)) {
      sink.occurrence(PerlCallableNamesIndex.KEY, callableName);
    }
  }

  @Override
  public void serialize(@NotNull PerlGlobStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(stub.getNamespaceName());
    dataStream.writeName(stub.getGlobName());
    dataStream.writeBoolean(stub.isLeftSideOfAssignment());
  }

  @Override
  public @NotNull PerlGlobStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return new PerlGlobStub(parentStub, PerlStubSerializationUtil.readString(dataStream),
                            PerlStubSerializationUtil.readString(dataStream), dataStream.readBoolean());
  }

  @Override
  public boolean shouldCreateStub(ASTNode node) {
    return node.getPsi() instanceof PerlGlobVariableElement perlGlobVariableElement &&
           perlGlobVariableElement.isValid() && StringUtil.isNotEmpty(perlGlobVariableElement.getCanonicalName());
  }
}
