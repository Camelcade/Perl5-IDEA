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
import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.intellij.openapi.util.NotNullLazyValue;
import com.intellij.openapi.util.RecursionManager;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiCacheKey;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.containers.Interner;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlBuiltInVariable;
import com.perl5.lang.perl.psi.impl.PerlImplicitVariableDeclaration;
import com.perl5.lang.perl.psi.impl.PerlSubCallElement;
import com.perl5.lang.perl.psi.mixins.PerlVariableMixin;
import com.perl5.lang.perl.psi.utils.PerlContextType;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.psi.utils.PerlResolveUtil;
import com.perl5.lang.perl.util.PerlArrayUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.*;
import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.*;

/**
 * Manages {@link PerlValue} creation, serialization and deserialization, manages serialization ID
 * We could implement something like PerlValueElementType but this thing is not supported to be extendable, so good for now
 */
public final class PerlValuesManager {
  private static final Logger LOG = Logger.getInstance(PerlValuesManager.class);
  private static final NotNullLazyValue<TokenSet> ONE_OF_VALUES = NotNullLazyValue.createValue(() -> TokenSet.create(
    AND_EXPR, OR_EXPR, LP_AND_EXPR, LP_OR_XOR_EXPR, PARENTHESISED_EXPR
  ));
  private static final NotNullLazyValue<TokenSet> LIST_VALUES = NotNullLazyValue.createValue(() -> TokenSet.create(
    STRING_LIST, COMMA_SEQUENCE_EXPR
  ));
  private static final PsiCacheKey<PerlValue, PsiElement> VALUE_KEY = PsiCacheKey.create(
    "perl5.value", it -> RecursionManager.doPreventingRecursion(it, true, () -> intern(computeValue(it)))
  );
  /**
   * Values from this entities delegated to the kids
   */
  private static final TokenSet TRANSPARENT_VALUES = TokenSet.create(
    VARIABLE_DECLARATION_ELEMENT
  );

  private static int id = 0;
  static final int DUPLICATE_ID = id++;
  // special values
  static final int UNKNOWN_ID = id++;
  static final int UNDEF_ID = id++;
  static final int ARGUMENTS_ID = id++;
  // primitives
  static final int SCALAR_ID = id++;
  static final int SCALAR_DEREFERENCE_ID = id++;
  static final int SCALAR_CONTEXT_ID = id++;
  static final int ARRAY_ID = id++;
  static final int ARRAY_ELEMENT_ID = id++;
  static final int ARRAY_SLICE_ID = id++;
  static final int ARRAY_DEREFERENCE_ID = id++;
  static final int UNSHIFT_ID = id++;
  static final int PUSH_ID = id++;
  static final int SUBLIST_ID = id++;

  static final int HASH_ID = id++;
  static final int HASH_ELEMENT_VALUE = id++;
  static final int DEFERRED_HASH_ID = id++;
  static final int HASH_SLICE_ID = id++;
  static final int HASH_DEREFERENCE_ID = id++;
  static final int ARITHMETIC_NEGATION = id++;
  static final int REFERENCE_ID = id++;
  static final int BLESSED_ID = id++;

  // synthetic values
  static final int CALL_STATIC_ID = id++;
  static final int CALL_OBJECT_ID = id++;
  static final int ONE_OF_ID = id++;
  static final int DEFAULT_ARGUMENT_ID = id++;
  static final int SMART_GETTER_ID = id++;
  static final int DUCK_TYPE_ID = id++;
  static final int VALUE_WITH_FALLBACK = id++;

  public static int getVersion() {
    return id + (PerlDuckValue.isDuckTypingEnabled() ? 100 : 0);
  }

  private static final Interner<PerlValue> INTERNER = Interner.createWeakInterner();

  public static PerlValue readValue(@NotNull StubInputStream dataStream) throws IOException {
    return new PerlValueDeserializer(dataStream).readValue();
  }

  static <T extends PerlValue> T intern(T value) {
    //noinspection unchecked
    return (T)INTERNER.intern(value);
  }

  /**
   * @return a value for {@code element} or {@link PerlValues#UNKNOWN_VALUE} if element is null/not valuable.
   */
  public static @NotNull PerlValue from(@Nullable PsiElement element) {
    if (element == null) {
      return UNKNOWN_VALUE;
    }
    PsiElement finalElement = element.getOriginalElement();
    if (!finalElement.isValid()) {
      LOG.error("Attempt to compute value from invalid element");
      return UNKNOWN_VALUE;
    }
    else if (finalElement instanceof PerlDerefExpression) {
      return from(finalElement.getLastChild());
    }

    IElementType elementType = PsiUtilCore.getElementType(finalElement);
    if (TRANSPARENT_VALUES.contains(elementType)) {
      PsiElement[] children = finalElement.getChildren();
      if (children.length == 0) {
        return UNKNOWN_VALUE;
      }
      if (children.length > 1) {
        LOG.error("Got transparent value: " + element + " with children: " + Arrays.asList(children));
      }
      return from(children[0]);
    }

    return VALUE_KEY.getValue(finalElement);
  }

  private static @NotNull PerlValue computeValue(@NotNull PsiElement element) {
    switch (element) {
      case PerlVariableDeclarationExpr declarationExpr -> {
        if (declarationExpr.isParenthesized()) {
          return PerlArrayValue.builder().addPsiElements(Arrays.asList(element.getChildren())).build();
        }
        PsiElement[] children = element.getChildren();
        return children.length == 1 ? from(children[0]) : UNKNOWN_VALUE;
      }
      case PerlReturnExpr returnExpr -> {
        PsiPerlExpr expr = returnExpr.getReturnValueExpr();
        return expr == null ? UNDEF_VALUE : from(expr);
      }
      case PerlVariableMixin variableMixin -> {
        PerlVariableNameElement variableNameElement = variableMixin.getVariableNameElement();
        return variableNameElement == null ? UNKNOWN_VALUE : PerlResolveUtil.inferVariableValue(variableMixin);
      }
      case PerlString string -> {
        return PerlScalarValue.create(ElementManipulators.getValueText(string));
      }
      case PerlImplicitVariableDeclaration implicitVariableDeclaration -> {
        return implicitVariableDeclaration.getDeclaredValue();
      }
      case PerlMethod method -> {
        return computeValue(method);
      }
      default -> {
      }
    }

    IElementType elementType = PsiUtilCore.getElementType(element);
    if (elementType == UNDEF_EXPR) {
      return UNDEF_VALUE;
    }
    else if (elementType == PARENTHESISED_EXPR) {
      var expr = ((PsiPerlParenthesisedExpr)element).getExpr();
      return expr == null ? PerlArrayValue.EMPTY_ARRAY : PerlValuesManager.from(expr);
    }
    else if (elementType == PACKAGE_EXPR) {
      String elementText = element.getText();
      return PerlPackageUtil.__PACKAGE__.equals(elementText)
             ? PerlPackageUtil.getContextType(element)
             : PerlScalarValue.create(PerlPackageUtil.getCanonicalName(elementText));
    }
    else if (elementType == SUB_CALL) {
      return PerlValuesManager.from(((PerlSubCallElement)element).getMethod());
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
    else if (ONE_OF_VALUES.get().contains(elementType)) {
      return PerlOneOfValue.builder().addVariants(element.getChildren()).build();
    }
    else if (LIST_VALUES.get().contains(elementType)) {
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
    else if (element instanceof PsiPerlHashIndex hashIndex) {
      PsiElement parent = element.getParent();
      if (parent instanceof PsiPerlDerefExpr derefExpr) {
        return PerlHashElementValue.create(
          PerlHashDereferenceValue.create(from(derefExpr.getPreviousElement(element))),
          from(hashIndex.getExpr()));
      }
    }
    else if (element instanceof PsiPerlArrayIndex index) {
      PsiElement parent = element.getParent();
      if (parent instanceof PsiPerlDerefExpr derefExpr) {
        return PerlArrayElementValue.create(
          PerlArrayDereferenceValue.create(from(derefExpr.getPreviousElement(element))),
          from(index.getExpr()));
      }
    }
    else if (element instanceof PsiPerlPrefixUnaryExpr prefixUnaryExpr) {
      PsiPerlExpr target = prefixUnaryExpr.getExpr();
      IElementType firstChildElementType = PsiUtilCore.getElementType(element.getFirstChild());
      if (firstChildElementType == OPERATOR_MINUS) {
        return PerlArithmeticNegationValue.create(from(target));
      }
      else if (firstChildElementType == OPERATOR_PLUS) {
        return from(target);
      }
    }
    else if (element instanceof PsiPerlRefExpr perlRefExpr) {
      return PerlReferenceValue.create(from(perlRefExpr.getExpr()));
    }
    else if (element instanceof PsiPerlHashElement perlHashElement) {
      return PerlHashElementValue.create(
        from(perlHashElement.getExpr()), from(perlHashElement.getHashIndex().getExpr()));
    }
    else if (element instanceof PsiPerlArrayElement perlArrayElement) {
      return PerlArrayElementValue.create(
        from(perlArrayElement.getExpr()), from(perlArrayElement.getArrayIndex().getExpr())
      );
    }
    else if (element instanceof PsiPerlHashSlice hashSlice) {
      return PerlHashSliceValue.create(
        from(hashSlice.getExpr()), listContext(from(hashSlice.getHashIndex().getExpr())));
    }
    else if (element instanceof PsiPerlArraySlice arraySlice) {
      return PerlArraySliceValue.create(
        from(arraySlice.getExpr()), listContext(from(arraySlice.getArrayIndex().getExpr())));
    }
    else if (element instanceof PsiPerlScalarCastExpr scalarCastExpr) {
      PsiPerlExpr expr = scalarCastExpr.getExpr();
      if (expr == null) {
        expr = PerlPsiUtil.getSingleBlockExpression(scalarCastExpr.getBlock());
      }
      return PerlScalarDereferenceValue.create(from(expr));
    }
    else if (element instanceof PsiPerlArrayCastExpr arrayCastExpr) {
      PsiPerlExpr expr = arrayCastExpr.getExpr();
      if (expr == null) {
        expr = PerlPsiUtil.getSingleBlockExpression(arrayCastExpr.getBlock());
      }
      return PerlArrayDereferenceValue.create(from(expr));
    }
    else if (element instanceof PsiPerlHashCastExpr hashCastExpr) {
      PsiPerlExpr expr = hashCastExpr.getExpr();
      if (expr == null) {
        expr = PerlPsiUtil.getSingleBlockExpression(hashCastExpr.getBlock());
      }
      return PerlHashDereferenceValue.create(from(expr));
    }
    else if (element instanceof PsiPerlBlessExpr perlBlessExpr) {
      PerlValue targetValue = from(perlBlessExpr.getReferenceExpression());
      PsiElement blessExpression = perlBlessExpr.getBlessExpression();
      PerlValue blessValue = blessExpression != null ? from(blessExpression) : PerlPackageUtil.getContextType(element);
      return PerlBlessedValue.create(targetValue, blessValue);
    }
    else if (element instanceof PsiPerlArrayUnshiftExpr unshiftExpr) {
      return PerlScalarContextValue.create(PerlUnshiftValue.create(unshiftExpr));
    }
    else if (element instanceof PsiPerlArrayPushExpr pushExpr) {
      return PerlScalarContextValue.create(PerlPushValue.create(pushExpr));
    }
    else if (element instanceof PsiPerlArrayShiftExpr shiftExpr) {
      return createShiftPopValue(shiftExpr, FIRST_ELEMENT_INDEX_VALUE);
    }
    else if (element instanceof PsiPerlArrayPopExpr popExpr) {
      return createShiftPopValue(popExpr, LAST_ELEMENT_INDEX_VALUE);
    }
    return UNKNOWN_VALUE;
  }

  private static @NotNull PerlValue computeValue(@NotNull PerlMethod perlMethod) {
    PerlValue subNameValue = PerlScalarValue.create(perlMethod.getName());
    if (subNameValue == UNKNOWN_VALUE) {
      return UNKNOWN_VALUE;
    }

    String explicitNamespaceName = perlMethod.getExplicitNamespaceName();
    boolean hasExplicitNamespace = StringUtil.isNotEmpty(explicitNamespaceName);
    PsiElement parentElement = perlMethod.getParent();
    boolean isNestedCall = PerlSubCallElement.isNestedCall(parentElement);

    List<PerlValue> callArguments;
    if (parentElement instanceof PerlMethodContainer parentMethodContainer) {
      callArguments = ContainerUtil.map(parentMethodContainer.getCallArgumentsList(), PerlValuesManager::from);
    }
    else {
      // these are sort and code variable
      LOG.debug("Non-method container container: " + parentElement.getClass().getSimpleName());
      callArguments = Collections.emptyList();
    }

    PsiElement derefExpression = parentElement.getParent();
    if (isNestedCall && parentElement.getPrevSibling() != null) {
      boolean isSuper = PerlPackageUtil.isSUPER(explicitNamespaceName);
      if (hasExplicitNamespace && !isSuper) { // awkward $var->Foo::Bar::method->
        return new PerlCallStaticValue(PerlScalarValue.create(explicitNamespaceName), subNameValue, callArguments, true);
      }
      String superContext = isSuper ? PerlPackageUtil.getContextNamespaceName(perlMethod) : null;
      if (!(derefExpression instanceof PerlDerefExpression perlDerefExpression)) {
        LOG.warn("Expected deref expression, got " + derefExpression);
        return UNKNOWN_VALUE;
      }

      PsiElement previousValue = perlDerefExpression.getPreviousElement(parentElement);
      if (previousValue == null) { // first in chain
        return new PerlCallStaticValue(
          PerlScalarValue.create(PerlPackageUtil.getContextNamespaceName(perlMethod)), subNameValue, callArguments, false);
      }
      return PerlCallObjectValue.create(PerlValuesManager.from(previousValue), subNameValue, callArguments, superContext);
    }
    else if (perlMethod.isObjectMethod() && hasExplicitNamespace) { // this is for a fancy call new Foo::Bar
      return PerlCallObjectValue.create(PerlScalarValue.create(explicitNamespaceName), subNameValue, callArguments, null);
    }
    else if (hasExplicitNamespace) {
      return new PerlCallStaticValue(PerlScalarValue.create(explicitNamespaceName), subNameValue, callArguments, true);
    }
    return new PerlCallStaticValue(
      PerlScalarValue.create(PerlPackageUtil.getContextNamespaceName(perlMethod)), subNameValue, callArguments, false);
  }

  private static @NotNull PerlValue createShiftPopValue(@NotNull PerlShiftPopExpr shiftPopExpr, @NotNull PerlValue indexValue) {
    return PerlArrayElementValue.create(createShiftPopArgumentValue(shiftPopExpr), indexValue);
  }

  /**
   * @return value of the argument for shift/pop operations before operation been performed
   */
  public static @NotNull PerlValue createShiftPopArgumentValue(@NotNull PerlShiftPopExpr shiftPopExpr) {
    PsiElement target = shiftPopExpr.getTarget();
    PerlValue targetValue;
    if (target instanceof PerlBuiltInVariable builtInVariable) {
      targetValue = PerlResolveUtil.inferVariableValue(builtInVariable, shiftPopExpr);
    }
    else {
      targetValue = from(target);
    }
    return targetValue;
  }

  /**
   * @return when {@code value} has list context, returns it. Wraps in array otherwise
   */
  private static @NotNull PerlValue listContext(@NotNull PerlValue value) {
    return value.getContextType() == PerlContextType.LIST ? value : PerlArrayValue.builder().addElement(value).build();
  }

  /**
   * @return true iff {@code type}is null or {@link PerlValues#UNKNOWN_VALUE}
   */
  @Contract("null->true")
  public static boolean isUnknown(@Nullable PerlValue type) {
    return type == null || type == UNKNOWN_VALUE;
  }

  public static @NotNull PerlValue from(@NotNull PsiElement target,
                                        @Nullable PerlAssignExpression.PerlAssignValueDescriptor assignValueDescriptor) {
    if (assignValueDescriptor == null) {
      return UNKNOWN_VALUE;
    }
    List<PsiElement> elements = assignValueDescriptor.getElements();
    PerlContextType targetContextType = PerlContextType.from(target);
    if (targetContextType == PerlContextType.SCALAR) {
      int startIndex = assignValueDescriptor.getStartIndex();
      if (elements.size() == 1 && (PerlContextType.isScalar(elements.getFirst()) || startIndex == -1)) {
        return PerlScalarContextValue.create(from(elements.getFirst()));
      }
      else if (elements.size() > 1 || PerlContextType.isList(ContainerUtil.getFirstItem(elements))) {
        return PerlArrayElementValue.create(
          PerlArrayValue.builder().addPsiElements(elements).build(),
          PerlScalarValue.create(startIndex)
        );
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
  public static @NotNull AtomicNotNullLazyValue<PerlValue> lazy(@NotNull PerlValue value) {
    return value.isUnknown() ? UNKNOWN_VALUE_PROVIDER : AtomicNotNullLazyValue.createValue(() -> value);
  }

  /**
   * @return lazy-computable value for the {@code element}
   */
  public static @NotNull AtomicNotNullLazyValue<PerlValue> lazy(@Nullable PsiElement element) {
    if (element == null) {
      return UNKNOWN_VALUE_PROVIDER;
    }
    return AtomicNotNullLazyValue.createValue(() -> from(element));
  }
}
