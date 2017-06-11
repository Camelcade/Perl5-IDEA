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

package com.perl5.lang.perl.psi.stubs;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.*;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.parser.elementTypes.PsiElementProvider;
import com.perl5.lang.perl.psi.PerlPolyNamedElement;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class PerlPolyNamedElementType extends IStubElementType<PerlPolyNamedElementStub, PerlPolyNamedElement>
  implements PsiElementProvider {
  public PerlPolyNamedElementType(@NotNull String debugName) {
    super(debugName, PerlLanguage.INSTANCE);
  }

  @NotNull
  @Override
  public PerlPolyNamedElementStub createStub(@NotNull PerlPolyNamedElement psi, StubElement parentStub) {
    List<StubElement> lightNamedElements = new ArrayList<>();
    PerlPolyNamedElementStub result = new PerlPolyNamedElementStub(parentStub, this, lightNamedElements);

    //noinspection unchecked
    psi.getLightElements().forEach(lightPsi -> lightNamedElements.add(lightPsi.getElementType().createStub(lightPsi, result)));

    return result;
  }

  @NotNull
  @Override
  public final String getExternalId() {
    return "perl.poly." + super.toString();
  }

  @Override
  public final void serialize(@NotNull PerlPolyNamedElementStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    List<StubElement> childrenStubs = stub.getLightNamedElementsStubs();
    dataStream.writeInt(childrenStubs.size());
    for (StubElement childStub : childrenStubs) {
      dataStream.writeInt(getSerializationId(childStub)); // serialization id
      //noinspection unchecked
      childStub.getStubType().serialize(childStub, dataStream);
    }
  }

  @NotNull
  @Override
  public final PerlPolyNamedElementStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    int size = dataStream.readInt();
    List<StubElement> childStubs = new ArrayList<>(size);
    PerlPolyNamedElementStub result = new PerlPolyNamedElementStub(parentStub, this, childStubs);

    for (int i = 0; i < size; i++) {
      //noinspection unchecked
      childStubs.add((StubElement)getElementTypeById(dataStream.readInt()).deserialize(dataStream, result));
    }

    return result;
  }

  @Override
  public final void indexStub(@NotNull PerlPolyNamedElementStub stub, @NotNull IndexSink sink) {
    //noinspection unchecked
    stub.getLightNamedElementsStubs().forEach(childStub -> childStub.getStubType().indexStub(childStub, sink));
  }

  @Override
  public final boolean shouldCreateStub(ASTNode node) {
    PsiElement psi = node.getPsi();
    assert psi instanceof PerlPolyNamedElement;
    return ((PerlPolyNamedElement)psi).getLightElements().size() > 0;
  }

  private static int getSerializationId(@NotNull StubElement stubElement) {
    if (stubElement instanceof PerlSubDefinitionStub) {
      return 0;
    }
    throw new IllegalArgumentException("Unregistered stub element class:" + stubElement);
  }

  @NotNull
  private static IStubElementType getElementTypeById(int id) {
    if (id == 0) {
      return PerlStubElementTypes.LIGHT_SUB_DEFINITION;
    }
    throw new IllegalArgumentException("Unregistered stub element id:" + id);
  }
}
