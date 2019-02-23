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

package com.perl5.lang.perl.idea.codeInsight.typeInferrence.value;

import org.jetbrains.annotations.NotNull;

public final class PerlValueHash extends PerlBlessableValue {
  @NotNull
  private final PerlValue myValuesType;

  @NotNull
  public PerlValue getValuesType() {
    return myValuesType;
  }

  private PerlValueHash(@NotNull PerlValueHash original, @NotNull PerlValue bless) {
    super(original, bless);
    myValuesType = original.myValuesType;
  }

  @NotNull
  @Override
  PerlValue createBlessedCopy(@NotNull PerlValue bless) {
    return new PerlValueHash(this, bless);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PerlValueHash hash = (PerlValueHash)o;

    return myValuesType.equals(hash.myValuesType);
  }

  @Override
  public int hashCode() {
    return myValuesType.hashCode();
  }

  @Override
  public String toString() {
    return "Hash of: " + myValuesType;
  }
}
