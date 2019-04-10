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

package com.perl5.lang.perl.idea.codeInsight.typeInference.value;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.RecursionManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.ObjectUtils;
import com.intellij.util.Processor;
import com.perl5.lang.perl.psi.PerlAssignExpression.PerlAssignValueDescriptor;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.PerlReturnExpr;
import com.perl5.lang.perl.psi.PsiPerlExpr;
import com.perl5.lang.perl.psi.properties.PerlValuableEntity;
import com.perl5.lang.perl.psi.utils.PerlContextType;
import com.perl5.lang.perl.util.PerlArrayUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlUndefValue.UNDEF_VALUE;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlUnknownValue.UNKNOWN_VALUE;
import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.*;

/**
 * Parent for all perl values
 */
public abstract class PerlValue {
  private static final Logger LOG = Logger.getInstance(PerlValue.class);
  private static final TokenSet ONE_OF_VALUES = TokenSet.create(
    AND_EXPR, OR_EXPR, LP_AND_EXPR, LP_OR_XOR_EXPR, PARENTHESISED_EXPR
  );

  private static final TokenSet LIST_VALUES = TokenSet.create(
    STRING_LIST, COMMA_SEQUENCE_EXPR
  );

  private int myHashCode = 0;

  protected PerlValue() {
  }

  protected PerlValue(@NotNull StubInputStream dataStream) throws IOException {
  }

  /**
   * Processes all namespaces conforming this type with {@code processor}
   */
  public final boolean processNamespaces(@NotNull Project project,
                                         @NotNull GlobalSearchScope searchScope,
                                         @NotNull Processor<? super PerlNamespaceDefinitionElement> processor) {
    return processNamespaceNames(project, searchScope, it -> PerlPackageUtil.processNamespaces(it, project, searchScope, processor));
  }

  public final boolean processNamespaceNames(@NotNull Project project,
                                             @NotNull GlobalSearchScope searchScope,
                                             @NotNull Processor<String> processor) {
    for (String namespaceName : getNamespaceNames(project, searchScope)) {
      if (!processor.process(namespaceName)) {
        return false;
      }
    }
    return true;
  }

  @NotNull
  protected final Set<String> getNamespaceNames(@NotNull Project project,
                                                @NotNull GlobalSearchScope searchScope) {
    return getNamespaceNames(project, searchScope, null);
  }

  /**
   * @return set of package name from the index, conforming current type
   */
  @NotNull
  protected Set<String> getNamespaceNames(@NotNull Project project,
                                          @NotNull GlobalSearchScope searchScope,
                                          @Nullable Set<PerlValue> recursion) {
    return Collections.emptySet();
  }

  /**
   * Processes all sub names which may be represented with current value
   */
  public final boolean processSubNames(@NotNull Project project,
                                       @NotNull GlobalSearchScope searchScope,
                                       @NotNull Processor<String> processor) {
    for (String subName : getSubNames(project, searchScope)) {
      if (!processor.process(subName)) {
        return false;
      }
    }
    return true;
  }

  @NotNull
  protected final Set<String> getSubNames(@NotNull Project project,
                                          @NotNull GlobalSearchScope searchScope) {
    return getSubNames(project, searchScope, null);
  }

  /**
   * @return set of sub names which may be represented by the current value
   */
  @NotNull
  protected Set<String> getSubNames(@NotNull Project project,
                                    @NotNull GlobalSearchScope searchScope,
                                    @Nullable Set<PerlValue> recursion) {
    return Collections.emptySet();
  }

  /**
   * Processes all related items: subs declarations, definitions and typeglobs in the namespaces conforming this type
   */
  public final boolean processRelatedItems(@NotNull Project project,
                                           @NotNull GlobalSearchScope searchScope,
                                           @NotNull Processor<? super PsiNamedElement> processor) {
    Set<String> subNames = getSubNames(project, searchScope);
    for (String namespaceName : getNamespaceNames(project, searchScope)) {
      for (String subName : subNames) {
        if (!PerlSubUtil.processRelatedItems(project, searchScope, PerlPackageUtil.join(namespaceName, subName), processor)) {
          return false;
        }
      }
    }
    return true;
  }

  public final boolean isEmpty() {
    return this == UNKNOWN_VALUE;
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
   * @return code representation, that may be used e.g. in annotation
   */
  public String toCode() {
    return toString();
  }

  /**
   * @return presentable text for tooltips
   */
  @NotNull
  public String getPresentableText() {
    return toString();
  }

  @Override
  public final int hashCode() {
    return myHashCode != 0 ? myHashCode : (myHashCode = computeHashCode());
  }

  protected int computeHashCode() {
    return getClass().hashCode();
  }

  /**
   * @return true iff {@code type}is null or {@link PerlUnknownValue#UNKNOWN_VALUE}
   */
  @Contract("null->true")
  public static boolean isEmpty(@Nullable PerlValue type) {
    return type == null || type == UNKNOWN_VALUE;
  }

  /**
   * @return true iff {@code} type is not empty and not {@link PerlUnknownValue#UNKNOWN_VALUE}
   */
  @Contract("null->false")
  public static boolean isNotEmpty(@Nullable PerlValue type) {
    return !isEmpty(type);
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
      if (elements.size() == 1 && PerlContextType.from(elements.get(0)) == PerlContextType.SCALAR) {
        return fromNonNull(elements.get(0));
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
   * @return a value for {@code element} or null if element is null/not valuable.
   */
  @Nullable
  @Contract("null->null")
  public static PerlValue from(@Nullable PsiElement element) {
    if (element == null) {
      return null;
    }
    element = element.getOriginalElement();
    if (element instanceof PerlReturnExpr) {
      PsiPerlExpr expr = ((PerlReturnExpr)element).getReturnValueExpr();
      return expr == null ? UNDEF_VALUE : from(expr);
    }
    IElementType elementType = PsiUtilCore.getElementType(element);
    if (elementType == UNDEF_EXPR) {
      return UNDEF_VALUE;
    }
    else if (elementType == TRENAR_EXPR) {
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
    else if (elementType == REF_EXPR) {
      PsiElement[] children = element.getChildren();
      if (children.length == 1) {
        return PerlReferenceValue.create(children[0]);
      }
    }

    return element instanceof PerlValuableEntity ? from((PerlValuableEntity)element) : null;
  }

  @NotNull
  public static PerlValue from(@NotNull PerlValuableEntity element) {
    return CachedValuesManager.getCachedValue(
      element, () -> {
        //noinspection deprecation
        PerlValue computedValue = RecursionManager.doPreventingRecursion(element, true, element::computePerlValue);
        if (computedValue == null) {
          LOG.error("Recursion while computing value of " + element + " from " + PsiUtilCore.getVirtualFile(element));
          computedValue = UNKNOWN_VALUE;
        }
        return CachedValueProvider.Result.create(PerlValuesManager.intern(computedValue), element.getContainingFile());
      });
  }

  /**
   * @return a value for {@code element} or {@link PerlUnknownValue#UNKNOWN_VALUE} if element is null/not applicable
   */
  @NotNull
  public static PerlValue fromNonNull(@Nullable PsiElement element) {
    return ObjectUtils.notNull(from(element), UNKNOWN_VALUE);
  }
}
