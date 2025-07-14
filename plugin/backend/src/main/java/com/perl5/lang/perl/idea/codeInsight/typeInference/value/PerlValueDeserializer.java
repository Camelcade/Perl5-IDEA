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

package com.perl5.lang.perl.idea.codeInsight.typeInference.value;

import com.intellij.psi.stubs.StubInputStream;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.serialization.PerlValueBackendHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.serialization.PerlValueBackendHelper.DUPLICATE_ID;

public class PerlValueDeserializer {

  private final @NotNull StubInputStream myInputStream;
  private final Int2ObjectOpenHashMap<PerlValue> myDryMap = new Int2ObjectOpenHashMap<>();

  public PerlValueDeserializer(@NotNull StubInputStream inputStream) {
    myInputStream = inputStream;
  }

  public @NotNull PerlValue readValue() throws IOException {
    final int valueId = readVarInt();
    if (valueId == DUPLICATE_ID) {
      int duplicateId = readVarInt();
      PerlValue duplicate = myDryMap.get(duplicateId);
      if (duplicate == null) {
        throw new IOException("Missing duplicate with id: " + duplicateId);
      }
      return duplicate;
    }
    PerlValueBackendHelper<?> serializationHelper = PerlValueBackendHelper.get(valueId);
    PerlValue value = serializationHelper.deserialize(this);
    if (value instanceof PerlSpecialValue) {
      return value;
    }
    myDryMap.put(myDryMap.size() + 1, value);
    return PerlValuesManager.intern(value);
  }

  public @NotNull List<PerlValue> readValuesList() throws IOException {
    int size = readVarInt();
    if (size == 0) {
      return Collections.emptyList();
    }
    List<PerlValue> elements = new ArrayList<>(size);
    for (int i = 0; i < size; i++) {
      elements.add(readValue());
    }
    return Collections.unmodifiableList(elements);
  }

  public @NotNull Set<PerlValue> readValuesSet() throws IOException {
    int size = readVarInt();
    if (size == 0) {
      return Collections.emptySet();
    }
    Set<PerlValue> elements = new HashSet<>(size);
    for (int i = 0; i < size; i++) {
      elements.add(readValue());
    }
    return Collections.unmodifiableSet(elements);
  }

  public @Nullable String readNameString() throws IOException {
    return myInputStream.readNameString();
  }

  public int readVarInt() throws IOException {
    return myInputStream.readVarInt();
  }

  public boolean readBoolean() throws IOException {
    return myInputStream.readBoolean();
  }
}
