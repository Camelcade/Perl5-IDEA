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

public class PerlTypeNamespace extends PerlType {
  @NotNull
  private final String myNamespaceName;

  public PerlTypeNamespace(@NotNull String namespaceName) {
    myNamespaceName = namespaceName;
  }

  @NotNull
  public String getNamespaceName() {
    return myNamespaceName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PerlTypeNamespace)) return false;

    PerlTypeNamespace namespace = (PerlTypeNamespace)o;

    return getNamespaceName().equals(namespace.getNamespaceName());
  }

  @Override
  public int hashCode() {
    return getNamespaceName().hashCode();
  }

  @Override
  public String toString() {
    return myNamespaceName;
  }
}
