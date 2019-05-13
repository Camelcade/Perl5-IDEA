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
import com.intellij.openapi.util.RecursionManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.containers.WeakInterner;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.properties.PerlValuableEntity;
import com.perl5.lang.perl.util.PerlArrayUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.*;
import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.*;

/**
 * Manages {@link PerlValue} creation, serialization and deserialization, manages serialization ID
 * We could implement something like PerlValueElementType but this thing is not supported to be extendable, so good for now
 */
public final class PerlValuesManager {
  private static final Logger LOG = Logger.getInstance(PerlValuesManager.class);
  private static final TokenSet ONE_OF_VALUES = TokenSet.create(
    AND_EXPR, OR_EXPR, LP_AND_EXPR, LP_OR_XOR_EXPR, PARENTHESISED_EXPR
  );
  private static final TokenSet LIST_VALUES = TokenSet.create(
    STRING_LIST, COMMA_SEQUENCE_EXPR
  );

  private static int id = 0;
  // special values
  static final int UNKNOWN_ID = id++;
  static final int UNDEF_ID = id++;
  static final int ARGUMENTS_ID = id++;
  // primitives
  static final int SCALAR_ID = id++;
  static final int SCALAR_CAST_ID = id++;
  static final int SCALAR_CONTEXT_ID = id++;
  static final int STRINGIFY_ID = id++;
  static final int NUMIFY_ID = id++;

  static final int ARRAY_ID = id++;
  static final int ARRAY_ELEMENT_ID = id++;
  static final int ARRAY_SLICE_ID = id++;
  static final int ARRAY_CAST_ID = id++;
  static final int SPLICE_ID = id++;

  static final int HASH_ID = id++;
  static final int HASH_ELEMENT_VALUE = id++;
  static final int DEFERRED_HASH_ID = id++;
  static final int HASH_SLICE_ID = id++;
  static final int KEYS_ID = id++;
  static final int VALUES_ID = id++;
  static final int EACH_ID = id++;
  static final int HASH_CAST_ID = id++;
  static final int DELETE_ID = id++;

  static final int REFERENCE_ID = id++;
  static final int BLESSED_ID = id++;

  // synthetic values
  static final int CALL_STATIC_ID = id++;
  static final int CALL_OBJECT_ID = id++;
  static final int ONE_OF_ID = id++;
  static final int CONDITION_ID = id++;
  static final int DEFINED_ID = id++;
  static final int WANTARRAY_ID = id++;

  // MUST stay here. Automatically changes on new element creation
  public static final int VERSION = id++;

  private static final WeakInterner<PerlValue> INTERNER = new WeakInterner<>();

  public static PerlValue readValue(@NotNull StubInputStream dataStream) throws IOException {
    return intern(deserialize(dataStream));
  }

  public static <T extends PerlValue> T intern(T value) {
    //noinspection unchecked
    return (T)INTERNER.intern(value);
  }

  @NotNull
  private static PerlValue deserialize(@NotNull StubInputStream dataStream) throws IOException {
    final int valueId = dataStream.readVarInt();
    if (valueId == UNKNOWN_ID) {
      return UNKNOWN_VALUE;
    }
    else if (valueId == UNDEF_ID) {
      return UNDEF_VALUE;
    }
    else if (valueId == ARGUMENTS_ID) {
      return ARGUMENTS_VALUE;
    }
    else if (valueId == SCALAR_ID) {
      return new PerlScalarValue(dataStream);
    }
    else if (valueId == SCALAR_CONTEXT_ID) {
      return new PerlScalarContextValue(dataStream);
    }
    else if (valueId == ARRAY_ID) {
      return new PerlArrayValue(dataStream);
    }
    else if (valueId == HASH_ID) {
      return new PerlHashValue(dataStream);
    }
    else if (valueId == DEFERRED_HASH_ID) {
      return new PerlDeferredHashValue(dataStream);
    }
    else if (valueId == REFERENCE_ID) {
      return new PerlReferenceValue(dataStream);
    }
    else if (valueId == BLESSED_ID) {
      return new PerlBlessedValue(dataStream);
    }
    else if (valueId == ONE_OF_ID) {
      return new PerlOneOfValue(dataStream);
    }
    else if (valueId == CALL_OBJECT_ID) {
      return new PerlCallObjectValue(dataStream);
    }
    else if (valueId == CALL_STATIC_ID) {
      return new PerlCallStaticValue(dataStream);
    }
    else if (valueId == ARRAY_ELEMENT_ID) {
      return new PerlArrayElementValue(dataStream);
    }
    else if (valueId == HASH_ELEMENT_VALUE) {
      return new PerlHashElementValue(dataStream);
    }
    throw new RuntimeException("Don't know how to deserialize a value: " + valueId);
  }

  @NotNull
  static List<PerlValue> readList(@NotNull StubInputStream dataStream) throws IOException {
    int size = dataStream.readVarInt();
    if (size == 0) {
      return Collections.emptyList();
    }
    List<PerlValue> elements = new ArrayList<>(size);
    for (int i = 0; i < size; i++) {
      elements.add(readValue(dataStream));
    }
    return Collections.unmodifiableList(elements);
  }

  static void writeCollection(@NotNull StubOutputStream dataStream, @NotNull Collection<PerlValue> elements) throws IOException {
    dataStream.writeVarInt(elements.size());
    for (PerlValue element : elements) {
      element.serialize(dataStream);
    }
  }

  /**
   * @return a value for {@code element} or {@link PerlValues#UNKNOWN_VALUE} if element is null/not valuable.
   */
  @NotNull
  public static PerlValue from(@Nullable PsiElement element) {
    if (element == null) {
      return UNKNOWN_VALUE;
    }
    if (!element.isValid()) {
      LOG.error("Attempt to compute value from invalid element");
      return UNKNOWN_VALUE;
    }
    element = element.getOriginalElement();
    if (element instanceof PerlReturnExpr) {
      PsiPerlExpr expr = ((PerlReturnExpr)element).getReturnValueExpr();
      return expr == null ? UNDEF_VALUE : from(expr);
    }
    else if (element instanceof PerlValuableEntity) {
      return from((PerlValuableEntity)element);
    }

    IElementType elementType = PsiUtilCore.getElementType(element);
    if (elementType == UNDEF_EXPR) {
      return UNDEF_VALUE;
    }
    else if (elementType == SCALAR_EXPR) {
      PsiElement[] children = element.getChildren();
      return children.length == 0 ? UNKNOWN_VALUE : from(children[0]).getScalarRepresentation();
    }
    else if (elementType == TERNARY_EXPR) {
      PerlOneOfValue.Builder builder = PerlOneOfValue.builder();
      PsiElement[] children = element.getChildren();
      for (int i = 1; i < children.length; i++) {
        builder.addVariant(children[i]);
      }
      return builder.build();
    }
    else if (ONE_OF_VALUES.contains(elementType)) {
      return PerlOneOfValue.builder().addVariants(element.getChildren()).build();
    }
    else if (LIST_VALUES.contains(elementType)) {
      return PerlArrayValue.builder().addPsiElements(PerlArrayUtil.collectListElements(element)).build();
    }
    else if (elementType == ANON_ARRAY) {
      return PerlReferenceValue.create(PerlArrayValue.builder().addPsiElements(Arrays.asList(element.getChildren())).build());
    }
    else if (elementType == ANON_HASH) {
      return PerlReferenceValue.create(PerlMapValue.builder().addPsiElements(Arrays.asList(element.getChildren())).build());
    }
    else if (elementType == NUMBER_CONSTANT) {
      return PerlScalarValue.create(element.getText());
    }
    else if (element instanceof PsiPerlRefExpr) {
      return PerlReferenceValue.create(((PsiPerlRefExpr)element).getExpr());
    }
    else if (element instanceof PsiPerlHashElement) {
      return from(((PsiPerlHashElement)element).getExpr()).getHashElement(
        from(((PsiPerlHashElement)element).getHashIndex().getExpr()));
    }
    else if (element instanceof PsiPerlArrayElement) {
      return from(((PsiPerlArrayElement)element).getExpr()).getArrayElement(
        from(((PsiPerlArrayElement)element).getArrayIndex().getExpr()));
    }
    return UNKNOWN_VALUE;
  }

  @NotNull
  private static PerlValue from(@NotNull PerlValuableEntity element) {
    return CachedValuesManager.getCachedValue(
      element, () -> {
        //noinspection deprecation
        PerlValue computedValue = RecursionManager.doPreventingRecursion(element, true, element::computePerlValue);
        if (computedValue == null) {
          LOG.error("Recursion while computing value of " + element + " from " + PsiUtilCore.getVirtualFile(element));
          computedValue = UNKNOWN_VALUE;
        }
        return CachedValueProvider.Result.create(intern(computedValue), element.getContainingFile());
      });
  }
}
