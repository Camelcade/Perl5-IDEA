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

import com.intellij.psi.stubs.StubOutputStream;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.serialization.PerlValueBackendHelper;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collection;

public final class PerlValueSerializer {
  private final @NotNull StubOutputStream myOutputStream;
  private final Object2IntOpenHashMap<PerlValue> myDryMap = new Object2IntOpenHashMap<>();

  PerlValueSerializer(@NotNull StubOutputStream outputStream) {
    myOutputStream = outputStream;
  }

  public void writeValue(@NotNull PerlValue value) throws IOException {
    var serializerHelper = PerlValueBackendHelper.get(value);
    if (value instanceof PerlSpecialValue) {
      writeVarInt(serializerHelper.getSerializationId());
      return;
    }
    int duplicateId = myDryMap.getInt(value);
    if (duplicateId > 0) {
      writeVarInt(PerlValueBackendHelper.DUPLICATE_ID);
      writeVarInt(duplicateId);
      return;
    }
    writeVarInt(serializerHelper.getSerializationId());
    serializerHelper.serializeData(value, this);
    myDryMap.put(value, myDryMap.size() + 1);
  }

  public void writeValuesList(@NotNull Collection<? extends PerlValue> elements) throws IOException {
    writeVarInt(elements.size());
    for (PerlValue element : elements) {
      writeValue(element);
    }
  }

  public void writeName(final @Nullable String arg) throws IOException {
    myOutputStream.writeName(arg);
  }

  public void writeVarInt(final int value) throws IOException {
    myOutputStream.writeVarInt(value);
  }

  public void writeBoolean(boolean v) throws IOException {
    myOutputStream.writeBoolean(v);
  }

  public static void serialize(@NotNull PerlValue value, @NotNull StubOutputStream outputStream) throws IOException {
    new PerlValueSerializer(outputStream).writeValue(value);
  }
}