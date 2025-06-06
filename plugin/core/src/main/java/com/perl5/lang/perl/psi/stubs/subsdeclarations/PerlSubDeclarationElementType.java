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

package com.perl5.lang.perl.psi.stubs.subsdeclarations;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.*;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.parser.elementTypes.PsiElementProvider;
import com.perl5.lang.perl.psi.PerlSubDeclarationElement;
import com.perl5.lang.perl.psi.impl.PsiPerlSubDeclarationImpl;
import com.perl5.lang.perl.psi.stubs.PerlStubSerializationUtil;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlCallableNamesIndex;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;


public class PerlSubDeclarationElementType extends IStubElementType<PerlSubDeclarationStub, PerlSubDeclarationElement>
  implements PsiElementProvider {
  public PerlSubDeclarationElementType(String name) {
    super(name, PerlLanguage.INSTANCE);
  }

  @Override
  public PerlSubDeclarationElement createPsi(@NotNull PerlSubDeclarationStub stub) {
    return new PsiPerlSubDeclarationImpl(stub, this);
  }

  @Override
  public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
    return new PsiPerlSubDeclarationImpl(node);
  }

  @Override
  public @NotNull PerlSubDeclarationStub createStub(@NotNull PerlSubDeclarationElement psi, StubElement parentStub) {
    return new PerlSubDeclarationStub(parentStub, psi.getNamespaceName(), psi.getSubName(), psi.getAnnotations(), this);
  }


  @Override
  public @NotNull String getExternalId() {
    return "perl." + super.toString();
  }

  @Override
  public void serialize(@NotNull PerlSubDeclarationStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(stub.getNamespaceName());
    dataStream.writeName(stub.getSubName());
    PerlSubAnnotations subAnnotations = stub.getAnnotations();
    if (subAnnotations == null) {
      dataStream.writeBoolean(false);
    }
    else {
      dataStream.writeBoolean(true);
      subAnnotations.serialize(dataStream);
    }
  }

  @Override
  public @NotNull PerlSubDeclarationStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    String packageName = PerlStubSerializationUtil.readString(dataStream);
    String subName = PerlStubSerializationUtil.readString(dataStream);
    PerlSubAnnotations annotations = null;
    if (dataStream.readBoolean()) {
      annotations = PerlSubAnnotations.deserialize(dataStream);
    }
    return new PerlSubDeclarationStub(parentStub, packageName, subName, annotations, this);
  }

  @Override
  public void indexStub(@NotNull PerlSubDeclarationStub stub, @NotNull IndexSink sink) {
    String canonicalName = stub.getCanonicalName();
    if (canonicalName != null) {
      sink.occurrence(PerlSubDeclarationIndex.KEY, canonicalName);
    }
    String packageName = stub.getNamespaceName();
    if (packageName != null) {
      sink.occurrence(PerlSubDeclarationReverseIndex.KEY, packageName);
    }
    var callableName = stub.getCallableName();
    if (StringUtil.isNotEmpty(callableName)) {
      sink.occurrence(PerlCallableNamesIndex.KEY, callableName);
    }
  }

  @Override
  public boolean shouldCreateStub(ASTNode node) {
    PsiElement psi = node.getPsi();
    return psi instanceof PerlSubDeclarationElement subDeclarationElement &&
           StringUtil.isNotEmpty(subDeclarationElement.getNamespaceName()) &&
           StringUtil.isNotEmpty(subDeclarationElement.getSubName())
      ;
  }
}
