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
import com.perl5.lang.perl.psi.utils.PerlContextType;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.util.PerlArrayUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.Contract;
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
  static final int SCALAR_DEREFERENCE_ID = id++;
  static final int SCALAR_CONTEXT_ID = id++;
  static final int STRINGIFY_ID = id++;
  static final int NUMIFY_ID = id++;

  static final int ARRAY_ID = id++;
  static final int ARRAY_ELEMENT_ID = id++;
  static final int ARRAY_SLICE_ID = id++;
  static final int ARRAY_CAST_ID = id++;
  static final int SPLICE_ID = id++;
  static final int UNSHIFT_ID = id++;
  static final int PUSH_ID = id++;

  static final int HASH_ID = id++;
  static final int HASH_ELEMENT_VALUE = id++;
  static final int DEFERRED_HASH_ID = id++;
  static final int HASH_SLICE_ID = id++;
  static final int KEYS_ID = id++;
  static final int VALUES_ID = id++;
  static final int EACH_ID = id++;
  static final int HASH_CAST_ID = id++;
  static final int DELETE_ID = id++;

  static final int ARITHMETIC_NEGATION = id++;
  static final int LOGICAL_NEGATION = id++;
  static final int BITWISE_NEGATION = id++;

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
  public static final int VERSION = id += 2;

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
    else if (valueId == ARITHMETIC_NEGATION) {
      return new PerlArithmeticNegationValue(dataStream);
    }
    else if (valueId == ARRAY_SLICE_ID) {
      return new PerlArraySliceValue(dataStream);
    }
    else if (valueId == HASH_SLICE_ID) {
      return new PerlHashSliceValue(dataStream);
    }
    else if (valueId == SCALAR_DEREFERENCE_ID) {
      return new PerlScalarDereferenceValue(dataStream);
    }
    else if (valueId == UNSHIFT_ID) {
      return new PerlUnshiftValue(dataStream);
    }
    else if (valueId == PUSH_ID) {
      return new PerlPushValue(dataStream);
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
      return children.length == 0 ? UNKNOWN_VALUE : PerlScalarContextValue.create(from(children[0]));
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
    else if (element instanceof PsiPerlPrefixUnaryExpr) {
      PsiPerlExpr target = ((PsiPerlPrefixUnaryExpr)element).getExpr();
      IElementType firstChildElementType = PsiUtilCore.getElementType(element.getFirstChild());
      if (firstChildElementType == OPERATOR_MINUS) {
        return PerlArithmeticNegationValue.create(from(target));
      }
      else if (firstChildElementType == OPERATOR_PLUS) {
        return from(target);
      }
    }
    else if (element instanceof PsiPerlRefExpr) {
      return PerlReferenceValue.create(from(((PsiPerlRefExpr)element).getExpr()));
    }
    else if (element instanceof PsiPerlHashElement) {
      return PerlHashElementValue.create(
        from(((PsiPerlHashElement)element).getExpr()), from(((PsiPerlHashElement)element).getHashIndex().getExpr()));
    }
    else if (element instanceof PsiPerlArrayElement) {
      return PerlArrayElementValue.create(
        from(((PsiPerlArrayElement)element).getExpr()), from(((PsiPerlArrayElement)element).getArrayIndex().getExpr())
      );
    }
    else if (element instanceof PsiPerlHashSlice) {
      return PerlHashSliceValue.create(
        from(((PsiPerlHashSlice)element).getExpr()), listContext(from(((PsiPerlHashSlice)element).getHashIndex().getExpr())));
    }
    else if (element instanceof PsiPerlArraySlice) {
      return PerlArraySliceValue.create(
        from(((PsiPerlArraySlice)element).getExpr()), listContext(from(((PsiPerlArraySlice)element).getArrayIndex().getExpr())));
    }
    else if (element instanceof PsiPerlScalarCastExpr) {
      PsiPerlExpr expr = ((PsiPerlScalarCastExpr)element).getExpr();
      if (expr == null) {
        expr = PerlPsiUtil.getSingleBlockExpression(((PsiPerlScalarCastExpr)element).getBlock());
      }
      return PerlScalarDereferenceValue.create(from(expr));
    }
    else if (element instanceof PsiPerlBlessExpr) {
      PerlValue targetValue = from(((PsiPerlBlessExpr)element).getReferenceExpression());
      PsiElement blessExpression = ((PsiPerlBlessExpr)element).getBlessExpression();
      PerlValue blessValue = blessExpression != null ? from(blessExpression) : PerlPackageUtil.getContextType(element);
      return PerlBlessedValue.create(targetValue, blessValue);
    }
    else if (element instanceof PsiPerlArrayUnshiftExpr) {
      return PerlScalarContextValue.create(PerlUnshiftValue.create((PsiPerlArrayUnshiftExpr)element));
    }
    else if (element instanceof PsiPerlArrayPushExpr) {
      return PerlScalarContextValue.create(PerlPushValue.create((PsiPerlArrayPushExpr)element));
    }
    return UNKNOWN_VALUE;
  }

  /**
   * @return when {@code value} has list context, returns it. Wraps in array otherwise
   */
  @NotNull
  private static PerlValue listContext(@NotNull PerlValue value) {
    return value.getContextType() == PerlContextType.LIST ? value : PerlArrayValue.builder().addElement(value).build();
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
                               @Nullable PerlAssignExpression.PerlAssignValueDescriptor assignValueDescriptor) {
    if (assignValueDescriptor == null) {
      return UNKNOWN_VALUE;
    }
    List<PsiElement> elements = assignValueDescriptor.getElements();
    PerlContextType targetContextType = PerlContextType.from(target);
    if (targetContextType == PerlContextType.SCALAR) {
      // fixme otherwise we should createa list/extract subelement
      if (elements.size() == 1) {
        if ((PerlContextType.from(elements.get(0)) == PerlContextType.SCALAR || assignValueDescriptor.getStartIndex() == -1)) {
          return PerlScalarContextValue.create(from(elements.get(0)));
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
    return AtomicNotNullLazyValue.createValue(() -> from(element));
  }
}
