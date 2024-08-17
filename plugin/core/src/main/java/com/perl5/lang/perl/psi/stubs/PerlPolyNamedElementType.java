/*
 * Copyright 2015-2024 Alexandr Evstigneev
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
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.*;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.parser.elementTypes.PsiElementProvider;
import com.perl5.lang.perl.psi.impl.PerlPolyNamedElement;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.perl5.lang.perl.parser.Class.Accessor.ClassAccessorElementTypes.CLASS_ACCESSOR_METHOD;
import static com.perl5.lang.perl.psi.stubs.PerlStubElementTypes.*;

public abstract class PerlPolyNamedElementType<Stub extends PerlPolyNamedElementStub<Psi>, Psi extends PerlPolyNamedElement<Stub>>
  extends IStubElementType<Stub, Psi> implements PsiElementProvider {
  private static final Logger LOG = Logger.getInstance(PerlPolyNamedElementType.class);
  private static final Object2IntOpenHashMap<IStubElementType<?, ?>> DIRECT_MAP = new Object2IntOpenHashMap<>();
  private static final Int2ObjectOpenHashMap<IStubElementType<?, ?>> REVERSE_MAP = new Int2ObjectOpenHashMap<>();

  static {
    // 0 is reserved for n/a
    DIRECT_MAP.put(LIGHT_SUB_DEFINITION, 1);
    DIRECT_MAP.put(LIGHT_NAMESPACE_DEFINITION, 2);
    DIRECT_MAP.put(LIGHT_METHOD_DEFINITION, 3);
    DIRECT_MAP.put(CLASS_ACCESSOR_METHOD, 4);
    DIRECT_MAP.put(LIGHT_ATTRIBUTE_DEFINITION, 5);
    LOG.assertTrue(DIRECT_MAP.size() == 5);

    DIRECT_MAP.forEach((type, id) -> REVERSE_MAP.put(id.intValue(), type));
  }

  public PerlPolyNamedElementType(@NotNull String debugName) {
    super(debugName, PerlLanguage.INSTANCE);
  }

  @Override
  public @NotNull Stub createStub(@NotNull Psi psi, StubElement<?> parentStub) {
    List<StubElement<?>> lightNamedElements = new ArrayList<>();
    Stub result = createStub(psi, parentStub, lightNamedElements);

    psi.getLightElements().forEach(lightPsi -> {
      StubElement<?> lightStubElement = lightPsi.getElementType().createStub(lightPsi, result);
      if (lightStubElement instanceof PerlLightElementStub lightElementStub && lightPsi.isImplicit()) {
        lightElementStub.setImplicit(true);
      }
      lightNamedElements.add(lightStubElement);
    });

    return result;
  }

  protected abstract @NotNull Stub createStub(@NotNull Psi psi,
                                              StubElement<?> parentStub,
                                              @NotNull List<StubElement<?>> lightElementsStubs);

  @Override
  public final @NotNull String getExternalId() {
    return "perl.poly." + super.toString();
  }

  @Override
  public final void serialize(@NotNull Stub stub, @NotNull StubOutputStream dataStream) throws IOException {
    List<StubElement<?>> childrenStubs = ContainerUtil.filter(stub.getLightNamedElementsStubs(), it ->
      !(it instanceof PerlLightElementStub) || !((PerlLightElementStub)it).isImplicit());
    dataStream.writeVarInt(childrenStubs.size());
    serializeStub(stub, dataStream);
    //noinspection rawtypes
    for (StubElement childStub : childrenStubs) {
      dataStream.writeVarInt(getSerializationId(childStub)); // serialization id
      //noinspection unchecked
      childStub.getStubType().serialize(childStub, dataStream);
    }
  }

  protected void serializeStub(@NotNull Stub stub, @NotNull StubOutputStream dataStream) throws IOException {
    // to save additional data in subclasses
  }

  @Override
  public final @NotNull Stub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    int size = dataStream.readVarInt();
    List<StubElement<?>> childStubs = new ArrayList<>(size);
    Stub result = deserialize(dataStream, parentStub, childStubs);

    for (int i = 0; i < size; i++) {
      childStubs.add(getElementTypeById(dataStream.readVarInt()).deserialize(dataStream, result));
    }

    return result;
  }

  protected abstract @NotNull Stub deserialize(@NotNull StubInputStream dataStream,
                                               StubElement<?> parentStub,
                                               @NotNull List<StubElement<?>> lightElementsStubs) throws IOException;

  @Override
  public final void indexStub(@NotNull Stub stub, @NotNull IndexSink sink) {
    stub.getLightNamedElementsStubs().forEach(childStub -> {
      @SuppressWarnings("rawtypes") var typedStub = (StubElement)childStub;
      //noinspection unchecked
      typedStub.getStubType().indexStub(typedStub, sink);
    });
    doIndexStub(stub, sink);
  }

  protected void doIndexStub(@NotNull Stub stub, @NotNull IndexSink sink) {
  }

  @Override
  public boolean shouldCreateStub(ASTNode node) {
    PsiElement psi = node.getPsi();
    assert psi instanceof PerlPolyNamedElement;
    return !((PerlPolyNamedElement<?>)psi).getLightElements().isEmpty();
  }

  private static int getSerializationId(@NotNull StubElement<?> stubElement) {
    int id = DIRECT_MAP.getInt(stubElement.getStubType());
    if (id > 0) {
      return id;
    }
    throw new IllegalArgumentException("Unregistered stub element class:" + stubElement.getStubType());
  }

  private static @NotNull IStubElementType<?, ?> getElementTypeById(int id) {
    assert id > 0;
    IStubElementType<?, ?> type = REVERSE_MAP.get(id);
    if (type != null) {
      return type;
    }
    throw new IllegalArgumentException("Unregistered stub element id:" + id);
  }
}
