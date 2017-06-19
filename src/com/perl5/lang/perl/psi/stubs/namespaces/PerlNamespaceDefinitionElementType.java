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

package com.perl5.lang.perl.psi.stubs.namespaces;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.*;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.parser.elementTypes.PsiElementProvider;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.impl.PsiPerlNamespaceDefinitionImpl;
import com.perl5.lang.perl.psi.mro.PerlMroType;
import com.perl5.lang.perl.psi.stubs.PerlStubSerializationUtil;
import com.perl5.lang.perl.psi.utils.PerlNamespaceAnnotations;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by hurricup on 28.05.2015.
 */
public class PerlNamespaceDefinitionElementType extends IStubElementType<PerlNamespaceDefinitionStub, PerlNamespaceDefinitionElement>
  implements PsiElementProvider {
  public PerlNamespaceDefinitionElementType(String name) {
    super(name, PerlLanguage.INSTANCE);
  }

  public PerlNamespaceDefinitionElementType(@NotNull @NonNls String debugName, @Nullable Language language) {
    super(debugName, language);
  }

  @Override
  public PerlNamespaceDefinitionElement createPsi(@NotNull PerlNamespaceDefinitionStub stub) {
    return new PsiPerlNamespaceDefinitionImpl(stub, this);
  }

  @NotNull
  @Override
  public PsiElement getPsiElement(@NotNull ASTNode node) {
    return new PsiPerlNamespaceDefinitionImpl(node);
  }

  @Override
  public PerlNamespaceDefinitionStub createStub(@NotNull PerlNamespaceDefinitionElement psi, StubElement parentStub) {
    return createStubElement(parentStub,
                             psi.getPackageName(),
                             psi.getMroType(),
                             psi.getParentNamespacesNames(),
                             psi.getEXPORT(),
                             psi.getEXPORT_OK(),
                             psi.getEXPORT_TAGS(),
                             psi.getAnnotations()
    );
  }

  protected PerlNamespaceDefinitionStub createStubElement(@Nullable StubElement parentStub,
                                                          @NotNull String packageName,
                                                          @NotNull PerlMroType mroType,
                                                          @NotNull List<String> parentNamespaceNames,
                                                          @NotNull List<String> export,
                                                          @NotNull List<String> exportOk,
                                                          @NotNull Map<String, List<String>> exportTags,
                                                          @Nullable PerlNamespaceAnnotations annotations

  ) {
    return new PerlNamespaceDefinitionStub(parentStub,
                                           this,
                                           packageName,
                                           mroType,
                                           parentNamespaceNames,
                                           export,
                                           exportOk,
                                           exportTags,
                                           annotations
    );
  }

  @NotNull
  @Override
  public String getExternalId() {
    return "perl." + super.toString();
  }

  @Override
  public void indexStub(@NotNull PerlNamespaceDefinitionStub stub, @NotNull IndexSink sink) {
    String name = stub.getPackageName();
    assert name != null;
    sink.occurrence(getDirectKey(), name);

    for (String parent : stub.getParentNamespacesNames()) {
      if (parent != null && !parent.isEmpty()) {
        sink.occurrence(getReverseKey(), parent);
      }
    }
  }

  protected StubIndexKey<String, ? extends PsiElement> getDirectKey() {
    return PerlNamespaceIndex.KEY;
  }

  protected StubIndexKey<String, ? extends PsiElement> getReverseKey() {
    return PerlNamespaceReverseIndex.KEY;
  }

  @Override
  public void serialize(@NotNull PerlNamespaceDefinitionStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(stub.getPackageName());
    dataStream.writeName(stub.getMroType().toString());
    PerlStubSerializationUtil.writeStringsList(dataStream, stub.getParentNamespacesNames());
    PerlStubSerializationUtil.writeStringsList(dataStream, stub.getEXPORT());
    PerlStubSerializationUtil.writeStringsList(dataStream, stub.getEXPORT_OK());
    PerlStubSerializationUtil.writeStringListMap(dataStream, stub.getEXPORT_TAGS());

    PerlNamespaceAnnotations namespaceAnnotations = stub.getAnnotations();
    if (namespaceAnnotations == null) {
      dataStream.writeBoolean(false);
    }
    else {
      dataStream.writeBoolean(true);
      namespaceAnnotations.serialize(dataStream);
    }
  }

  @NotNull
  @Override
  public PerlNamespaceDefinitionStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    String packageName = dataStream.readName().toString();
    PerlMroType mroType = PerlMroType.valueOf(dataStream.readName().toString());
    List<String> parentNamespaces = PerlStubSerializationUtil.readStringsList(dataStream);
    List<String> EXPORT = PerlStubSerializationUtil.readStringsList(dataStream);
    List<String> EXPORT_OK = PerlStubSerializationUtil.readStringsList(dataStream);
    Map<String, List<String>> EXPORT_TAGS = PerlStubSerializationUtil.readStringListMap(dataStream);

    return createStubElement(
      parentStub,
      packageName,
      mroType,
      parentNamespaces,
      EXPORT,
      EXPORT_OK,
      EXPORT_TAGS,
      desearializeAnnotations(dataStream)
    );
  }

  @Nullable
  private PerlNamespaceAnnotations desearializeAnnotations(@NotNull StubInputStream dataStream) throws IOException {
    return dataStream.readBoolean() ? PerlNamespaceAnnotations.deserialize(dataStream) : null;
  }

  @Override
  public boolean shouldCreateStub(ASTNode node) {
    PsiElement psi = node.getPsi();
    return psi instanceof PerlNamespaceDefinitionElement &&
           psi.isValid() &&
           StringUtil.isNotEmpty(((PerlNamespaceDefinitionElement)psi).getPackageName());
  }
}
