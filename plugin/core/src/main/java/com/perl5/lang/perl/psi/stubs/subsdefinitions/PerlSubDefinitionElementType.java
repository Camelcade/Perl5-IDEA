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

package com.perl5.lang.perl.psi.stubs.subsdefinitions;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.*;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValuesManager;
import com.perl5.lang.perl.parser.elementTypes.PsiElementProvider;
import com.perl5.lang.perl.psi.PerlSubDefinitionElement;
import com.perl5.lang.perl.psi.impl.PsiPerlSubDefinitionImpl;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;


public class PerlSubDefinitionElementType extends IStubElementType<PerlSubDefinitionStub, PerlSubDefinitionElement>
  implements PsiElementProvider {

  public PerlSubDefinitionElementType(String name) {
    super(name, PerlLanguage.INSTANCE);
  }

  public PerlSubDefinitionElementType(@NotNull @NonNls String debugName, @Nullable Language language) {
    super(debugName, language);
  }

  @Override
  public PerlSubDefinitionElement createPsi(@NotNull PerlSubDefinitionStub stub) {
    return new PsiPerlSubDefinitionImpl(stub, this);
  }

  @Override
  public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
    return new PsiPerlSubDefinitionImpl(node);
  }

  @Override
  public @NotNull PerlSubDefinitionStub createStub(@NotNull PerlSubDefinitionElement psi, StubElement parentStub) {
    return createStubElement(parentStub,
                             psi.getNamespaceName(),
                             psi.getSubName(),
                             psi.getSubArgumentsList(),
                             psi.getReturnValueFromCode(),
                             psi.getAnnotations());
  }


  @Override
  public @NotNull String getExternalId() {
    return "perl." + super.toString();
  }

  @Override
  public void indexStub(@NotNull PerlSubDefinitionStub stub, @NotNull IndexSink sink) {
    String canonicalName = stub.getCanonicalName();
    if (canonicalName != null) {
      sink.occurrence(getDirectKey(), canonicalName);
    }
    String packageName = stub.getNamespaceName();
    if (packageName != null) {
      sink.occurrence(getReverseKey(), packageName);
    }
    var callableName = stub.getCallableName();
    if (StringUtil.isNotEmpty(callableName)) {
      sink.occurrence(getCallableNameKey(), callableName);
    }
  }

  protected StubIndexKey<String, ? extends PsiElement> getDirectKey() {
    return PerlSubDefinitionsIndex.KEY;
  }

  protected StubIndexKey<String, ? extends PsiElement> getReverseKey() {
    return PerlSubDefinitionReverseIndex.KEY;
  }

  protected StubIndexKey<String, ? extends PsiElement> getCallableNameKey() {
    return PerlCallableNamesIndex.KEY;
  }

  @Override
  public void serialize(@NotNull PerlSubDefinitionStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(stub.getNamespaceName());
    dataStream.writeName(stub.getSubName());

    PerlSubArgument.serializeList(dataStream, stub.getSubArgumentsList());


    PerlSubAnnotations subAnnotations = stub.getAnnotations();
    if (subAnnotations == null) {
      dataStream.writeBoolean(false);
    }
    else {
      dataStream.writeBoolean(true);
      subAnnotations.serialize(dataStream);
    }
    stub.getReturnValueFromCode().serialize(dataStream);
  }

  @Override
  public @NotNull PerlSubDefinitionStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    //noinspection ConstantConditions
    String packageName = dataStream.readName().getString();
    //noinspection ConstantConditions
    String functionName = dataStream.readName().getString();

    List<PerlSubArgument> arguments = PerlSubArgument.deserializeList(dataStream);

    PerlSubAnnotations annotations = null;
    if (dataStream.readBoolean()) {
      annotations = PerlSubAnnotations.deserialize(dataStream);
    }

    return createStubElement(parentStub, packageName, functionName, arguments, PerlValuesManager.readValue(dataStream), annotations);
  }

  protected @NotNull PerlSubDefinitionStub createStubElement(
    StubElement<?> parentStub,
    String packageName,
    String functionName,
    List<PerlSubArgument> arguments,
    @NotNull PerlValue returnValueFromCode,
    PerlSubAnnotations annotations
  ) {
    return new PerlSubDefinitionStub(parentStub, packageName, functionName, arguments, annotations, returnValueFromCode, this);
  }

  @Override
  public boolean shouldCreateStub(ASTNode node) {
    PsiElement element = node.getPsi();
    return element instanceof PerlSubDefinitionElement subDefinitionElement &&
           element.isValid() &&
           StringUtil.isNotEmpty(subDefinitionElement.getCanonicalName());
  }
}
