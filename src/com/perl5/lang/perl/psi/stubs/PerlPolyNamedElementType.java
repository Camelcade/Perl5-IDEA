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
import gnu.trove.TIntObjectHashMap;
import gnu.trove.TObjectIntHashMap;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class PerlPolyNamedElementType<Stub extends PerlPolyNamedElementStub, Psi extends PerlPolyNamedElement>
  extends IStubElementType<Stub, Psi> implements PsiElementProvider {
  private static final TObjectIntHashMap<IStubElementType> DIRECT_MAP = new TObjectIntHashMap<>();
  private static final TIntObjectHashMap<IStubElementType> REVERSE_MAP = new TIntObjectHashMap<>();

  static {
    // 0 is reserved for n/a
    DIRECT_MAP.put(PerlStubElementTypes.LIGHT_SUB_DEFINITION, 1);
    DIRECT_MAP.put(PerlStubElementTypes.LIGHT_NAMESPACE_DEFINITION, 2);
    DIRECT_MAP.put(PerlStubElementTypes.LIGHT_METHOD_DEFINITION, 3);

    DIRECT_MAP.forEachEntry((type, id) -> {
      REVERSE_MAP.put(id, type);
      return true;
    });
  }

  public PerlPolyNamedElementType(@NotNull String debugName) {
    super(debugName, PerlLanguage.INSTANCE);
  }

  @NotNull
  @Override
  public final Stub createStub(@NotNull Psi psi, StubElement parentStub) {
    List<StubElement> lightNamedElements = new ArrayList<>();
    Stub result = createStub(psi, parentStub, lightNamedElements);

    //noinspection unchecked
    psi.getLightElements().forEach(lightPsi -> lightNamedElements.add(lightPsi.getElementType().createStub(lightPsi, result)));

    return result;
  }

  @NotNull
  protected Stub createStub(@NotNull Psi psi, StubElement parentStub, @NotNull List<StubElement> lightStubs) {
    //noinspection unchecked
    return (Stub)new PerlPolyNamedElementStub(parentStub, this, lightStubs);
  }


  @NotNull
  @Override
  public final String getExternalId() {
    return "perl.poly." + super.toString();
  }

  @Override
  public final void serialize(@NotNull Stub stub, @NotNull StubOutputStream dataStream) throws IOException {
    List<StubElement> childrenStubs = stub.getLightNamedElementsStubs();
    dataStream.writeInt(childrenStubs.size());
    serializeStub(stub, dataStream);
    for (StubElement childStub : childrenStubs) {
      dataStream.writeInt(getSerializationId(childStub)); // serialization id
      //noinspection unchecked
      childStub.getStubType().serialize(childStub, dataStream);
    }
  }

  protected void serializeStub(@NotNull Stub stub, @NotNull StubOutputStream dataStream) throws IOException {
    // to save additional data in subclasses
  }

  @NotNull
  @Override
  public final Stub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    int size = dataStream.readInt();
    List<StubElement> childStubs = new ArrayList<>(size);
    Stub result = deserialize(dataStream, parentStub, childStubs);

    for (int i = 0; i < size; i++) {
      //noinspection unchecked
      childStubs.add((StubElement)getElementTypeById(dataStream.readInt()).deserialize(dataStream, result));
    }

    return result;
  }

  @NotNull
  protected Stub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub, @NotNull List<StubElement> childStubs)
    throws IOException {
    //noinspection unchecked
    return (Stub)new PerlPolyNamedElementStub(parentStub, this, childStubs);
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
    return !((PerlPolyNamedElement)psi).getLightElements().isEmpty();
  }

  private static int getSerializationId(@NotNull StubElement stubElement) {
    int id = DIRECT_MAP.get(stubElement.getStubType());
    if (id > 0) {
      return id;
    }
    throw new IllegalArgumentException("Unregistered stub element class:" + stubElement.getStubType());
  }

  @NotNull
  private static IStubElementType getElementTypeById(int id) {
    assert id > 0;
    IStubElementType type = REVERSE_MAP.get(id);
    if (type != null) {
      return type;
    }
    throw new IllegalArgumentException("Unregistered stub element id:" + id);
  }
}
