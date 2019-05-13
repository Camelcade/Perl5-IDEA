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

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.psi.PerlAssignExpression.PerlAssignValueDescriptor;
import com.perl5.lang.perl.psi.utils.PerlContextType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue.PerlValueType.DEFERRED;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue.PerlValueType.DETERMINISTIC;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.*;
import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.HASH_CAST_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.HASH_VARIABLE;

/**
 * Parent for all perl values
 */
public abstract class PerlValue {
  private static final Logger LOG = Logger.getInstance(PerlValue.class);

  // transient cached values
  private volatile int myHashCode = 0;
  private volatile PerlValueType myValueDeterminism = null;
  private volatile PerlValue myScalarRepresentation = null;

  protected PerlValue() {
  }

  protected PerlValue(@NotNull StubInputStream dataStream) throws IOException {
  }

  /**
   * @return set of package name from the index, conforming current type
   */
  @NotNull
  public Set<String> getNamespaceNames() {
    return Collections.emptySet();
  }

  /**
   * @return a current value resolved in the context of the {@code project}.
   */
  @NotNull
  public final PerlValue resolve(@NotNull PsiElement contextElement, @NotNull Map<PerlValue, PerlValue> substitutions) {
    if (isUnknown() || isDeterministic()) {
      return this;
    }
    PerlValue substitution = substitutions.get(this);
    if (substitution != null) {
      return substitution;
    }
    return PerlValuesCacheService.getInstance(contextElement.getProject()).getResolvedValue(this, contextElement, substitutions);
  }

  @NotNull
  public final PerlValue resolve(@NotNull PsiElement contextElement) {
    return resolve(contextElement, Collections.emptyMap());
  }

  /**
   * @return current value resolved in context of the {@code contextElement}
   * @apiNote DO NOT use this method directly, use {@link #resolve(PsiElement)} (Project)}
   * @implSpec feel free to use indexes, resolve and any heavy activity you need
   */
  PerlValue computeResolve(@NotNull PsiElement contextElement,
                           @NotNull Map<PerlValue, PerlValue> substitutions) {
    throw new RuntimeException("Not implemented resolve in " + this);
  }

  /**
   * @return set of sub names which may be represented by the current value
   */
  @NotNull
  public Set<String> getSubNames() {
    return Collections.emptySet();
  }

  public final boolean isUnknown() {
    return this == UNKNOWN_VALUE;
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
   * @return a serialization id unique for this value.
   * @see PerlValuesManager
   */
  protected abstract int getSerializationId();

  /**
   * Serializes this value data
   */
  public final void serialize(@NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeVarInt(getSerializationId());
    serializeData(dataStream);
  }

  protected abstract void serializeData(@NotNull StubOutputStream dataStream) throws IOException;

  /**
   * @return a context type for this value. Or null if context can't be determined (can be any)
   */
  @Nullable
  protected abstract PerlContextType getContextType();

  /**
   * @return the scalar representation of the value
   */
  @NotNull
  public final PerlValue getScalarRepresentation() {
    if (isUnknown()) {
      return UNKNOWN_VALUE;
    }
    if (getContextType() == PerlContextType.SCALAR) {
      return this;
    }
    if (isDeterministic()) {
      if (myScalarRepresentation == null) {
        myScalarRepresentation = computeScalarRepresentation();
      }
      return myScalarRepresentation;
    }
    return new PerlScalarContextValue(this);
  }

  @NotNull
  public List<PerlValue> getListRepresentation() {
    return Collections.singletonList(this);
  }

  @NotNull
  protected PerlValue computeScalarRepresentation() {
    throw new RuntimeException("This method must be implemented for determined non-scalar values");
  }

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
   * @return a value computed by {@code converter} from the current value
   */
  public PerlValue convert(@NotNull Function<PerlValue, PerlValue> converter) {
    return converter.apply(this);
  }

  /**
   * Works the same way as {@link #convert(Function)}, but returns {@link PerlValues#UNKNOWN_VALUE} if
   * converter returned {@code UNKNOWN_VALUE} at least once.
   *
   * @see PerlHashElementValue#create(com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue, com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue)
   * @see PerlOneOfValue#convertStrict(Function)
   */
  public PerlValue convertStrict(@NotNull Function<PerlValue, PerlValue> converter) {
    return convert(converter);
  }

  /**
   * @return presentable text for tooltips
   */
  @NotNull
  public String getPresentableText() {
    return toString();
  }

  /**
   * @return a value representing reference to the current element
   */
  @NotNull
  public final PerlValue getReference() {
    return isUnknown() ? UNKNOWN_VALUE : convert(PerlValue::createReference);
  }

  @NotNull
  protected PerlValue createReference() {
    return new PerlReferenceValue(this);
  }

  /**
   * @return a value representing array element from current value. Deterministic if possible
   */
  @NotNull
  public final PerlValue getArrayElement(@NotNull PerlValue arrayIndex) {
    if (arrayIndex.isUnknown() || arrayIndex.isUndef()) {
      return UNKNOWN_VALUE;
    }
    return convert(it -> createArrayElement(arrayIndex));
  }

  @NotNull
  protected PerlValue createArrayElement(@NotNull PerlValue arrayIndex) {
    return new PerlArrayElementValue(this, arrayIndex);
  }

  /**
   * @return a value representing hash element from current value. Deterministic if possible
   */
  @NotNull
  public final PerlValue getHashElement(@NotNull PerlValue hashKey) {
    if (hashKey.isUnknown() || hashKey.isUndef()) {
      return UNKNOWN_VALUE;
    }
    return convert(hash -> hash.createHashElement(hashKey));
  }

  @NotNull
  protected PerlValue createHashElement(@NotNull PerlValue hashKey) {
    return new PerlHashElementValue(this, hashKey);
  }

  @Override
  public final int hashCode() {
    return myHashCode != 0 ? myHashCode : (myHashCode = computeHashCode());
  }

  protected int computeHashCode() {
    return getClass().hashCode();
  }

  /**
   * @return true iff {@code type}is null or {@link PerlValues#UNKNOWN_VALUE}
   */
  @Contract("null->true")
  public static boolean isUnknown(@Nullable PerlValue type) {
    return type == null || type == UNKNOWN_VALUE;
  }

  /**
   * @return true iff {@code} type is not empty and not {@link PerlValues#UNKNOWN_VALUE}
   */
  @Contract("null->false")
  public static boolean isNotEmpty(@Nullable PerlValue type) {
    return !isUnknown(type);
  }

  @NotNull
  public static PerlValue from(@NotNull PsiElement target,
                               @Nullable PerlAssignValueDescriptor assignValueDescriptor) {
    if (assignValueDescriptor == null) {
      return UNKNOWN_VALUE;
    }
    List<PsiElement> elements = assignValueDescriptor.getElements();
    PerlContextType targetContextType = PerlContextType.from(target);
    if (targetContextType == PerlContextType.SCALAR) {
      // fixme otherwise we should createa list/extract subelement
      if (elements.size() == 1) {
        if ((PerlContextType.from(elements.get(0)) == PerlContextType.SCALAR || assignValueDescriptor.getStartIndex() == -1)) {
          return PerlValuesManager.from(elements.get(0)).getScalarRepresentation();
        }
      }
      else if (elements.size() > 1) {
        LOG.error("Can't be: " + target + "; " + assignValueDescriptor);
      }
    }
    else if (targetContextType == PerlContextType.LIST &&
             assignValueDescriptor.getStartIndex() == 0) {
      IElementType elementType = PsiUtilCore.getElementType(target);
      if (elementType == HASH_VARIABLE || elementType == HASH_CAST_EXPR) {
        return PerlMapValue.builder().addPsiElements(elements).build();
      }
      return PerlArrayValue.builder().addPsiElements(elements).build();
    }
    return UNKNOWN_VALUE;
  }

  /**
   * @return {@code value} wrapped with atomic producer
   */
  @NotNull
  public static AtomicNotNullLazyValue<PerlValue> lazy(@NotNull PerlValue value) {
    return value.isUnknown() ? UNKNOWN_VALUE_PROVIDER : AtomicNotNullLazyValue.createValue(() -> value);
  }

  /**
   * @return lazy-computable value for the {@code element}
   */
  @NotNull
  public static AtomicNotNullLazyValue<PerlValue> lazy(@Nullable PsiElement element) {
    if (element == null) {
      return UNKNOWN_VALUE_PROVIDER;
    }
    return AtomicNotNullLazyValue.createValue(() -> PerlValuesManager.from(element));
  }

  enum PerlValueType {
    DETERMINISTIC,
    DEFERRED
  }
}
