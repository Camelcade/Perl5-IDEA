/*
 * Copyright 2015-2018 Alexandr Evstigneev
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
import com.intellij.psi.stubs.StubOutputStream;
import com.perl5.PerlBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public final class PerlHashValue extends PerlValue {
  @NotNull
  private final PerlValue myValuesType;

  public PerlHashValue(@NotNull PerlValue valuesType, @Nullable PerlValue bless) {
    super(bless);
    myValuesType = valuesType;
  }

  public PerlHashValue(@NotNull StubInputStream dataStream) throws IOException {
    super(dataStream);
    myValuesType = PerlValuesManager.deserialize(dataStream);
  }

  @Override
  protected int getSerializationId() {
    return PerlValuesManager.HASH_ID;
  }

  @Override
  protected void serializeData(@NotNull StubOutputStream dataStream) throws IOException {
    myValuesType.serialize(dataStream);
  }

  @NotNull
  public PerlValue getValuesType() {
    return myValuesType;
  }

  @NotNull
  @Override
  PerlValue createBlessedCopy(@NotNull PerlValue bless) {
    return new PerlHashValue(this.myValuesType, bless);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    PerlHashValue hash = (PerlHashValue)o;

    return myValuesType.equals(hash.myValuesType);
  }

  @Override
  protected int computeHashCode() {
    int result = super.computeHashCode();
    result = 31 * result + myValuesType.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "Hash of: " + myValuesType;
  }

  @NotNull
  @Override
  protected String getPresentableValueText() {
    return PerlBundle.message("perl.value.hash.presentable", myValuesType.getPresentableText());
  }
}
