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

package com.perl5.lang.perl.psi.stubs.subsdefinitions;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.*;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.parser.elementTypes.PsiElementProvider;
import com.perl5.lang.perl.psi.PerlSubDefinition;
import com.perl5.lang.perl.psi.impl.PsiPerlSubDefinitionImpl;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

/**
 * Created by hurricup on 25.05.2015.
 */
public class PerlSubDefinitionStubElementType extends IStubElementType<PerlSubDefinitionStub, PerlSubDefinition>
  implements PsiElementProvider {

  public PerlSubDefinitionStubElementType(String name) {
    super(name, PerlLanguage.INSTANCE);
  }

  public PerlSubDefinitionStubElementType(@NotNull @NonNls String debugName, @Nullable Language language) {
    super(debugName, language);
  }

  @Override
  public PerlSubDefinition createPsi(@NotNull PerlSubDefinitionStub stub) {
    return new PsiPerlSubDefinitionImpl(stub, this);
  }

  @NotNull
  @Override
  public PsiElement getPsiElement(@NotNull ASTNode node) {
    return new PsiPerlSubDefinitionImpl(node);
  }

  @Override
  public PerlSubDefinitionStub createStub(@NotNull PerlSubDefinition psi, StubElement parentStub) {
    //noinspection unchecked
    return createStubElement(parentStub, psi.getPackageName(), psi.getSubName(), psi.getSubArgumentsList(), psi.getLocalAnnotations());
  }


  @NotNull
  @Override
  public String getExternalId() {
    return "perl." + super.toString();
  }

  @Override
  public void indexStub(@NotNull PerlSubDefinitionStub stub, @NotNull IndexSink sink) {
    sink.occurrence(PerlSubDefinitionsStubIndex.KEY, stub.getCanonicalName());
    sink.occurrence(PerlSubDefinitionsStubIndex.KEY, "*" + stub.getPackageName());
  }

  @Override
  public void serialize(@NotNull PerlSubDefinitionStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(stub.getPackageName());
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
  }

  @NotNull
  @Override
  public PerlSubDefinitionStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    //noinspection ConstantConditions
    String packageName = dataStream.readName().getString();
    //noinspection ConstantConditions
    String functionName = dataStream.readName().getString();

    List<PerlSubArgument> arguments = PerlSubArgument.deserializeList(dataStream);

    PerlSubAnnotations annotations = null;
    if (dataStream.readBoolean()) {
      annotations = PerlSubAnnotations.deserialize(dataStream);
    }

    return createStubElement(parentStub, packageName, functionName, arguments, annotations);
  }

  @NotNull
  protected PerlSubDefinitionStub createStubElement(
    StubElement parentStub,
    String packageName,
    String functionName,
    List<PerlSubArgument> arguments,
    PerlSubAnnotations annotations
  ) {
    return new PerlSubDefinitionStub(parentStub, packageName, functionName, arguments, annotations, this);
  }

  @Override
  public boolean shouldCreateStub(ASTNode node) {
    PsiElement element = node.getPsi();
    return element instanceof PerlSubDefinition &&
           element.isValid() &&
           StringUtil.isNotEmpty(((PerlSubDefinition)element).getCanonicalName());
  }
}
