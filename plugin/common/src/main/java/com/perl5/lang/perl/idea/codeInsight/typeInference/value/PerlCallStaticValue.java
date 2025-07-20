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

import com.perl5.PerlBundle;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class PerlCallStaticValue extends PerlCallValue {
  private final boolean myHasExplicitNamespace;

  public PerlCallStaticValue(@NotNull PerlValue namespaceNameValue,
                             @NotNull PerlValue subNameValue,
                             @NotNull List<? extends PerlValue> arguments,
                             boolean hasExplicitNamespace) {
    super(namespaceNameValue, subNameValue, arguments);
    myHasExplicitNamespace = hasExplicitNamespace;
  }

  public boolean hasExplicitNamespace() {
    return myHasExplicitNamespace;
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

    PerlCallStaticValue aStatic = (PerlCallStaticValue)o;

    return myHasExplicitNamespace == aStatic.myHasExplicitNamespace;
  }

  @Override
  protected int computeHashCode() {
    int result = super.computeHashCode();
    result = 31 * result + (myHasExplicitNamespace ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    return getNamespaceNameValue() + "::" + getSubNameValue() + getArgumentsAsString();
  }

  @Override
  public @NotNull String getPresentableText() {
    return PerlBundle.message(
      "perl.value.call.static.presentable",
      getNamespaceNameValue().getPresentableText(),
      getSubNameValue().getPresentableText(),
      getPresentableArguments());
  }
}
