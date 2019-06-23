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

package com.perl5.lang.perl.psi.stubs;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.*;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.parser.elementTypes.PsiElementProvider;
import com.perl5.lang.perl.psi.impl.PerlPolyNamedElement;
import gnu.trove.TIntObjectHashMap;
import gnu.trove.TObjectIntHashMap;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.perl5.lang.perl.parser.Class.Accessor.ClassAccessorElementTypes.CLASS_ACCESSOR_METHOD;
import static com.perl5.lang.perl.psi.stubs.PerlStubElementTypes.*;

public abstract class PerlPolyNamedElementType<Stub extends PerlPolyNamedElementStub<Psi>, Psi extends PerlPolyNamedElement<Stub>>
  extends IStubElementType<Stub, Psi> implements PsiElementProvider {
  private static final TObjectIntHashMap<IStubElementType> DIRECT_MAP = new TObjectIntHashMap<>();
  private static final TIntObjectHashMap<IStubElementType> REVERSE_MAP = new TIntObjectHashMap<>();

  static {
    // 0 is reserved for n/a
    DIRECT_MAP.put(LIGHT_SUB_DEFINITION, 1);
    DIRECT_MAP.put(LIGHT_NAMESPACE_DEFINITION, 2);
    DIRECT_MAP.put(LIGHT_METHOD_DEFINITION, 3);
    DIRECT_MAP.put(CLASS_ACCESSOR_METHOD, 4);
    DIRECT_MAP.put(LIGHT_ATTRIBUTE_DEFINITION, 5);
    assert DIRECT_MAP.size() == 5;

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

    psi.getLightElements().forEach(lightPsi -> {
      //noinspection unchecked
      StubElement lightStubElement = lightPsi.getElementType().createStub(lightPsi, result);
      if (lightStubElement instanceof PerlLightElementStub && lightPsi.isImplicit()) {
        ((PerlLightElementStub)lightStubElement).setImplicit(true);
      }
      lightNamedElements.add(lightStubElement);
    });

    return result;
  }

  @NotNull
  protected abstract Stub createStub(@NotNull Psi psi, StubElement parentStub, @NotNull List<StubElement> lightElementsStubs);

  @NotNull
  @Override
  public final String getExternalId() {
    return "perl.poly." + super.toString();
  }

  @Override
  public final void serialize(@NotNull Stub stub, @NotNull StubOutputStream dataStream) throws IOException {
    List<StubElement> childrenStubs = ContainerUtil.filter(stub.getLightNamedElementsStubs(), it ->
      !(it instanceof PerlLightElementStub) || !((PerlLightElementStub)it).isImplicit());
    dataStream.writeVarInt(childrenStubs.size());
    serializeStub(stub, dataStream);
    for (StubElement childStub : childrenStubs) {
      dataStream.writeVarInt(getSerializationId(childStub)); // serialization id
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
    int size = dataStream.readVarInt();
    List<StubElement> childStubs = new ArrayList<>(size);
    Stub result = deserialize(dataStream, parentStub, childStubs);

    for (int i = 0; i < size; i++) {
      //noinspection unchecked
      childStubs.add((StubElement)getElementTypeById(dataStream.readVarInt()).deserialize(dataStream, result));
    }

    return result;
  }

  @NotNull
  protected abstract Stub deserialize(@NotNull StubInputStream dataStream,
                                      StubElement parentStub,
                                      @NotNull List<StubElement> lightElementsStubs) throws IOException;

  @Override
  public final void indexStub(@NotNull Stub stub, @NotNull IndexSink sink) {
    //noinspection unchecked
    stub.getLightNamedElementsStubs().forEach(childStub -> childStub.getStubType().indexStub(childStub, sink));
    doIndexStub(stub, sink);
  }

  protected void doIndexStub(@NotNull Stub stub, @NotNull IndexSink sink) {
  }

  @Override
  public boolean shouldCreateStub(ASTNode node) {
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
