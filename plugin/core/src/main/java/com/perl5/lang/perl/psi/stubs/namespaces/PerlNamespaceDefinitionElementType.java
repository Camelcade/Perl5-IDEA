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
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import static com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceDescendantsIndex.NAMESPACE_DESCENDANTS_KEY;
import static com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceIndex.NAMESPACE_KEY;


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

  @Override
  public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
    return new PsiPerlNamespaceDefinitionImpl(node);
  }

  @Override
  public @NotNull PerlNamespaceDefinitionStub createStub(@NotNull PerlNamespaceDefinitionElement psi, StubElement parentStub) {
    return createStubElement(parentStub, new PerlNamespaceDefinitionData(psi));
  }

  protected PerlNamespaceDefinitionStub createStubElement(@Nullable StubElement<?> parentStub, @NotNull PerlNamespaceDefinitionData data) {
    return new PerlNamespaceDefinitionStub(parentStub, this, data);
  }

  @Override
  public @NotNull String getExternalId() {
    return "perl." + super.toString();
  }

  @Override
  public void indexStub(@NotNull PerlNamespaceDefinitionStub stub, @NotNull IndexSink sink) {
    String name = stub.getNamespaceName();
    sink.occurrence(getNamespacesIndexKey(), name);

    for (String parent : stub.getParentNamespacesNames()) {
      if (parent != null && !parent.isEmpty()) {
        sink.occurrence(getDescendantsIndexKey(), parent);
      }
    }
  }

  protected StubIndexKey<String, ? extends PsiElement> getNamespacesIndexKey() {
    return NAMESPACE_KEY;
  }

  protected StubIndexKey<String, ? extends PsiElement> getDescendantsIndexKey() {
    return NAMESPACE_DESCENDANTS_KEY;
  }

  @Override
  public void serialize(@NotNull PerlNamespaceDefinitionStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    stub.getData().serialize(dataStream);
  }

  @Override
  public @NotNull PerlNamespaceDefinitionStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return createStubElement(parentStub, PerlNamespaceDefinitionData.deserialize(dataStream));
  }

  @Override
  public boolean shouldCreateStub(ASTNode node) {
    PsiElement psi = node.getPsi();
    return psi instanceof PerlNamespaceDefinitionElement namespaceDefinitionElement &&
           psi.isValid() &&
           StringUtil.isNotEmpty(namespaceDefinitionElement.getNamespaceName());
  }
}
