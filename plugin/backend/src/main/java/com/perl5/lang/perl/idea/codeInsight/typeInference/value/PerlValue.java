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

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.psi.utils.PerlContextType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue.PerlValueType.DEFERRED;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue.PerlValueType.DETERMINISTIC;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNDEF_VALUE;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;

/**
 * Parent for all perl values
 */
public abstract class PerlValue {
  protected static final Logger LOG = Logger.getInstance(PerlValue.class);

  // transient cached values
  private volatile int myHashCode = 0;
  private volatile PerlValueType myValueDeterminism = null;

  protected PerlValue() {
  }

  @SuppressWarnings("RedundantThrows")
  PerlValue(@NotNull PerlValueDeserializer ignoredDeserializer) throws IOException {
  }

  /**
   * @return set of package name from the index, conforming current type
   */
  public @NotNull Set<String> getNamespaceNames() {
    return Collections.emptySet();
  }


  /**
   * @return a current value resolved in the context of the {@code contextElement}.
   */
  public final @NotNull PerlValue resolve(@NotNull PsiElement contextElement) {
    return new PerlSimpleValueResolver(contextElement).resolve(this);
  }

  /**
   * @return current value resolved in context of the {@code contextElement}
   * @apiNote DO NOT use this method directly, use {@link #resolve(PsiElement)} (Project)}
   * @implSpec feel free to use indexes, resolve and any heavy activity you need
   */
  abstract @NotNull PerlValue computeResolve(@NotNull PerlValueResolver resolver);

  /**
   * @return set of sub names which may be represented by the current value
   */
  public @NotNull Set<String> getSubNames() {
    return Collections.emptySet();
  }

  @Contract(pure=true)
  public final boolean isUnknown() {
    return this == UNKNOWN_VALUE;
  }

  /**
   * @return true iff {@code value} is null or {@link PerlValues#UNKNOWN_VALUE}
   */
  @Contract(value = "null -> true", pure = true)
  public static boolean isUnknown(@Nullable PerlValue value) {
    return value == null || value.isUnknown();
  }

  public final boolean isUndef() {
    return this == UNDEF_VALUE;
  }

  /**
   * @return true iff this type can represent a {@code namespaceName}
   */
  public boolean canRepresentNamespace(@Nullable String namespaceName) {
    return false;
  }

  public boolean canRepresentSubName(@Nullable String subName) {
    return false;
  }

  /**
   * @return a context type for this value. Or null if context can't be determined (can be any)
   */
  protected abstract @Nullable PerlContextType getContextType();

  /**
   * @return true iff this value is deterministic and don't need to be computed
   */
  public final boolean isDeterministic() {
    if (myValueDeterminism == null) {
      myValueDeterminism = computeIsDeterministic() ? DETERMINISTIC : DEFERRED;
    }
    return myValueDeterminism == DETERMINISTIC;
  }

  /**
   * Don't use this method directly, use cached version: {@link #isDeterministic()}
   *
   * @see #isDeterministic()
   */
  protected boolean computeIsDeterministic() {
    return false;
  }

  /**
   * @return code representation, that may be used e.g. in annotation
   */
  public String toCode() {
    return toString();
  }

  /**
   * @return presentable text for tooltips
   */
  public @NotNull @NlsSafe String getPresentableText() {
    return toString();
  }

  @Override
  public final int hashCode() {
    return myHashCode != 0 ? myHashCode : (myHashCode = computeHashCode());
  }

  protected int computeHashCode() {
    return getClass().hashCode();
  }

  enum PerlValueType {
    DETERMINISTIC,
    DEFERRED
  }
}
