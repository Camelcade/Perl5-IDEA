/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package com.perl5.lang.perl.psi.stubs.variables;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.*;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValuesManager;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.parser.elementTypes.PsiElementProvider;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.impl.PsiPerlVariableDeclarationElementImpl;
import com.perl5.lang.perl.psi.stubs.PerlStubSerializationUtil;
import com.perl5.lang.perl.psi.utils.PerlVariableAnnotations;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;


public class PerlVariableStubElementType extends IStubElementType<PerlVariableDeclarationStub, PerlVariableDeclarationElement>
  implements PerlElementTypes, PsiElementProvider {
  public PerlVariableStubElementType(@NotNull String debugName) {
    super(debugName, PerlLanguage.INSTANCE);
  }

  @NotNull
  @Override
  public PerlVariableDeclarationStub createStub(@NotNull PerlVariableDeclarationElement psi, StubElement parentStub) {
    return new PerlVariableDeclarationStub(
      parentStub,
      this,
      Objects.requireNonNull(psi.getNamespaceName()),
      Objects.requireNonNull(psi.getName()),
      psi.getDeclaredValue(),
      psi.getActualType(),
      psi.getLocalVariableAnnotations());
  }

  @Override
  public PerlVariableDeclarationElement createPsi(@NotNull PerlVariableDeclarationStub stub) {
    return new PsiPerlVariableDeclarationElementImpl(stub, this);
  }

  @NotNull
  @Override
  public PsiElement getPsiElement(@NotNull ASTNode node) {
    return new PsiPerlVariableDeclarationElementImpl(node);
  }

  @Override
  public boolean shouldCreateStub(ASTNode node) {
    PsiElement psi = node.getPsi();
    return psi instanceof PerlVariableDeclarationElement &&
           psi.isValid() &&
           ((PerlVariableDeclarationElement)psi).isGlobalDeclaration() &&
           StringUtil.isNotEmpty(((PerlVariableDeclarationElement)psi).getName()) &&
           StringUtil.isNotEmpty(((PerlVariableDeclarationElement)psi).getNamespaceName());
  }

  @NotNull
  @Override
  public String getExternalId() {
    return "perl." + super.toString();
  }

  @Override
  public void serialize(@NotNull PerlVariableDeclarationStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(stub.getNamespaceName());
    dataStream.writeName(stub.getVariableName());
    stub.getDeclaredValue().serialize(dataStream);
    dataStream.writeByte(stub.getActualType().ordinal());

    PerlVariableAnnotations annotations = stub.getVariableAnnotations();
    if (annotations == null) {
      dataStream.writeBoolean(false);
    }
    else {
      dataStream.writeBoolean(true);
      annotations.serialize(dataStream);
    }
  }

  @NotNull
  @Override
  public PerlVariableDeclarationStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return new PerlVariableDeclarationStub(
      parentStub,
      this,
      PerlStubSerializationUtil.readNotNullString(dataStream),
      PerlStubSerializationUtil.readNotNullString(dataStream),
      PerlValuesManager.readValue(dataStream),
      PerlVariableType.values()[dataStream.readByte()],
      readAnnotations(dataStream)
    );
  }

  @Nullable
  private PerlVariableAnnotations readAnnotations(@NotNull StubInputStream dataStream) throws IOException {
    return dataStream.readBoolean() ? PerlVariableAnnotations.deserialize(dataStream) : null;
  }

  @Override
  public void indexStub(@NotNull PerlVariableDeclarationStub stub, @NotNull IndexSink sink) {
    String variableName = stub.getNamespaceName() + PerlPackageUtil.PACKAGE_SEPARATOR + stub.getVariableName();
    sink.occurrence(stub.getIndexKey(), variableName);
    sink.occurrence(stub.getIndexKey(), "*" + stub.getNamespaceName());
  }
}
