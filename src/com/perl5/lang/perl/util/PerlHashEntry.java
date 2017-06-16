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

package com.perl5.lang.perl.util;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents hash pair - key and value
 */
public class PerlHashEntry {
  @NotNull
  public PsiElement keyElement;
  @Nullable
  public PsiElement valueElement;

  private PerlHashEntry(@NotNull PsiElement keyElement, @Nullable PsiElement valueElement) {
    this.keyElement = keyElement;
    this.valueElement = valueElement;
  }

  @Nullable
  public String getValueString() {
    return PerlScalarUtil.getStringContent(valueElement);
  }

  public boolean isComplete() {
    return valueElement != null;
  }

  @NotNull
  public PsiElement getNonNullValueElement() {
    assert valueElement != null : "This method should be invoked only after isComplete() check";
    return valueElement;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PerlHashEntry pair = (PerlHashEntry)o;

    if (!keyElement.equals(pair.keyElement)) return false;
    return valueElement != null ? valueElement.equals(pair.valueElement) : pair.valueElement == null;
  }

  @Override
  public int hashCode() {
    int result = keyElement.hashCode();
    result = 31 * result + (valueElement != null ? valueElement.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PerlHashEntry{" +
           "keyElement=" + keyElement +
           ", valueElement=" + valueElement +
           '}';
  }

  public static PerlHashEntry create(@NotNull PsiElement key, @Nullable PsiElement value) {
    return new PerlHashEntry(key, value);
  }
}
