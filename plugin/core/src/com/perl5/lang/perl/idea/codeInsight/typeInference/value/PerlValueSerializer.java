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

package com.perl5.lang.perl.idea.codeInsight.typeInference.value;

import com.intellij.psi.stubs.StubOutputStream;
import gnu.trove.TObjectIntHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collection;

final class PerlValueSerializer {
  @NotNull
  private final StubOutputStream myOutputStream;
  private final TObjectIntHashMap<PerlValue> myDryMap = new TObjectIntHashMap<>();

  public PerlValueSerializer(@NotNull StubOutputStream outputStream) {
    myOutputStream = outputStream;
  }

  void writeValue(@NotNull PerlValue value) throws IOException {
    if (value instanceof PerlSpecialValue) {
      writeVarInt(value.getSerializationId());
      return;
    }
    int duplicateId = myDryMap.get(value);
    if (duplicateId > 0) {
      writeVarInt(PerlValuesManager.DUPLICATE_ID);
      writeVarInt(duplicateId);
      return;
    }
    writeVarInt(value.getSerializationId());
    value.serializeData(this);
    myDryMap.put(value, myDryMap.size() + 1);
  }

  void writeValuesList(@NotNull Collection<PerlValue> elements) throws IOException {
    writeVarInt(elements.size());
    for (PerlValue element : elements) {
      writeValue(element);
    }
  }

  public void writeName(@Nullable final String arg) throws IOException {
    myOutputStream.writeName(arg);
  }

  public void writeVarInt(final int value) throws IOException {
    myOutputStream.writeVarInt(value);
  }

  public void writeBoolean(boolean v) throws IOException {
    myOutputStream.writeBoolean(v);
  }
}