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

package com.perl5.lang.perl.types;

import org.jetbrains.annotations.NotNull;

public abstract class PerlTypeWrapping extends PerlType {
  @NotNull
  private final PerlType myInnerType;

  public PerlTypeWrapping(@NotNull PerlType innerType) {
    myInnerType = innerType;
  }

  @NotNull
  public PerlType getInnerType() {
    return myInnerType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PerlTypeWrapping)) return false;

    PerlTypeWrapping wrapping = (PerlTypeWrapping)o;

    return getInnerType().equals(wrapping.getInnerType());
  }

  @Override
  public int hashCode() {
    return getInnerType().hashCode();
  }

  @Override
  public String toString() {
    return "[" + myInnerType.toString() + "]";
  }
}
