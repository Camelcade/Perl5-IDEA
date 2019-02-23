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

public final class PerlValueArray extends PerlBlessableValue {
  @NotNull
  private final PerlValue myElementsType;

  public PerlValueArray(@NotNull PerlValue elementsType) {
    myElementsType = elementsType;
  }

  private PerlValueArray(@NotNull PerlValueArray original, @NotNull PerlValue bless) {
    super(original, bless);
    myElementsType = original.myElementsType;
  }

  @NotNull
  @Override
  PerlValue createBlessedCopy(@NotNull PerlValue bless) {
    return new PerlValueArray(this, bless);
  }

  @NotNull
  public PerlValue getElementsType() {
    return myElementsType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PerlValueArray array = (PerlValueArray)o;

    return myElementsType.equals(array.myElementsType);
  }

  @Override
  public int hashCode() {
    return myElementsType.hashCode();
  }

  @Override
  public String toString() {
    return "Array of: " + myElementsType;
  }
}
