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

package com.perl5.lang.perl.psi.stubs;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.*;
import com.intellij.psi.tree.IElementType;
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
  private static final Object2IntOpenHashMap<IElementType> DIRECT_MAP = new Object2IntOpenHashMap<>();
  private static final Int2ObjectOpenHashMap<IElementType> REVERSE_MAP = new Int2ObjectOpenHashMap<>();

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

  @SuppressWarnings("rawtypes")
  @Override
  public @NotNull Stub createStub(@NotNull Psi psi, StubElement<?> parentStub) {
    List<StubElement<?>> lightNamedElements = new ArrayList<>();
    Stub result = createStub(psi, parentStub, lightNamedElements);

    psi.getLightElements().forEach(lightPsi -> {
      var lightElementType = lightPsi.getElementType();
      StubElement<?> lightStubElement;
      if (lightElementType instanceof IStubElementType stubElementType) {
        lightStubElement = stubElementType.createStub(lightPsi, result);
      }
      else {
        lightStubElement = ((StubElementFactory)getStubSerializer(lightElementType)).createStub(lightPsi, result);
      }

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
      !(it instanceof PerlLightElementStub elementStub) || !elementStub.isImplicit());
    dataStream.writeVarInt(childrenStubs.size());
    serializeStub(stub, dataStream);
    //noinspection rawtypes
    for (StubElement childStub : childrenStubs) {
      dataStream.writeVarInt(getSerializationId(childStub)); // serialization id
      getStubSerializer(childStub).serialize(childStub, dataStream);
    }
  }

  @SuppressWarnings("rawtypes")
  private static ObjectStubSerializer getStubSerializer(@NotNull StubElement<?> childStub) {
    var elementType = childStub.getElementType();
    if (elementType instanceof IStubElementType<?, ?> stubElementType) {
      return stubElementType;
    }
    ObjectStubSerializer<?, com.intellij.psi.stubs.Stub> serializer =
      StubElementRegistryService.getInstance().getStubSerializer(elementType);
    LOG.assertTrue(serializer != null,
                   "Don't know how to serialize:" + elementType + "; " + elementType.getClass());
    return serializer;
  }

  @SuppressWarnings("rawtypes")
  private static @NotNull StubSerializer getStubSerializer(@NotNull IElementType elementType) {
    if (elementType instanceof IStubElementType<?, ?> stubElementType) {
      return stubElementType;
    }
    else {
      return (StubSerializer)StubElementRegistryService.getInstance().getStubSerializer(elementType);
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
      childStubs.add((StubElement<?>)getStubSerializer(getElementTypeById(dataStream.readVarInt())).deserialize(dataStream, result));
    }

    return result;
  }

  protected abstract @NotNull Stub deserialize(@NotNull StubInputStream dataStream,
                                               StubElement<?> parentStub,
                                               @NotNull List<StubElement<?>> lightElementsStubs) throws IOException;

  @Override
  public final void indexStub(@NotNull Stub stub, @NotNull IndexSink sink) {
    stub.getLightNamedElementsStubs().forEach(childStub -> getStubSerializer(childStub).indexStub(childStub, sink));
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
    int id = DIRECT_MAP.getInt(stubElement.getElementType());
    if (id > 0) {
      return id;
    }
    throw new IllegalArgumentException("Unregistered stub element class:" + stubElement.getElementType());
  }

  private static @NotNull IElementType getElementTypeById(int id) {
    assert id > 0;
    var type = REVERSE_MAP.get(id);
    if (type != null) {
      return type;
    }
    throw new IllegalArgumentException("Unregistered stub element id:" + id);
  }
}
