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
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlOneOfValue.Builder;
import com.perl5.lang.perl.psi.PerlAssignExpression.PerlAssignValueDescriptor;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.PerlReturnExpr;
import com.perl5.lang.perl.psi.PsiPerlExpr;
import com.perl5.lang.perl.psi.properties.PerlValuableEntity;
import com.perl5.lang.perl.psi.utils.PerlContextType;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
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

  @Nullable
  private final PerlValue myBless;

  private int myHashCode = 0;

  protected PerlValue() {
    this((PerlValue)null);
  }

  protected PerlValue(@Nullable PerlValue bless) {
    myBless = bless;
  }

  protected PerlValue(@NotNull StubInputStream dataStream) throws IOException {
    if (dataStream.readBoolean()) {
      myBless = PerlValuesManager.readValue(dataStream);
    }
    else {
      myBless = null;
    }
  }

  /**
   * @return return a blessing of this entity
   */
  @NotNull
  final PerlValue getBlessedInner() {
    return myBless != null ? myBless : UNKNOWN_VALUE;
  }

  /**
   * Should create a copy of current entity blessed with {@code bless}
   */
  @NotNull
  abstract PerlValue createBlessedCopy(@NotNull PerlValue bless);

  /**
   * @return result of {@code Scalar::Util::blessed} equivalent invocation, {@link UNDEF_VALUE} if not blessed
   */
  @NotNull
  public PerlValue getBless() {
    return UNDEF_VALUE;
  }

  public final boolean isBlessed() {
    return getBless() != UNDEF_VALUE;
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
    dataStream.writeInt(getSerializationId());
    dataStream.writeBoolean(myBless != null);
    if (myBless != null) {
      myBless.serialize(dataStream);
    }
    serializeData(dataStream);
  }

  protected abstract void serializeData(@NotNull StubOutputStream dataStream) throws IOException;

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
  public final String getPresentableText() {
    String presentableText = getPresentableValueText();
    return isBlessed() ?
           PerlBundle.message("perl.value.presentable.blessed", presentableText, getBlessedInner().getPresentableText()) :
           presentableText;
  }

  /**
   * @return presentable value text, without blessing information
   * @see #getPresentableText()
   */
  @NotNull
  protected String getPresentableValueText() {
    return toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PerlValue value = (PerlValue)o;

    return myBless != null ? myBless.equals(value.myBless) : value.myBless == null;
  }

  @Override
  public final int hashCode() {
    return myHashCode != 0 ? myHashCode : (myHashCode = computeHashCode());
  }

  protected int computeHashCode() {
    return getClass().hashCode() + (myBless != null ? 31 * myBless.hashCode() : 0);
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
  public static PerlValue from(@NotNull PerlContextType contextType,
                               @Nullable PerlAssignValueDescriptor assignValueDescriptor) {
    if (assignValueDescriptor == null ||
        assignValueDescriptor.getStartIndex() != 0 ||
        contextType != PerlContextType.SCALAR) {
      return UNKNOWN_VALUE;
    }
    List<PsiElement> elements = assignValueDescriptor.getElements();
    if (elements.size() != 1 || PerlContextType.from(elements.get(0)) != contextType) {
      return UNKNOWN_VALUE;
    }
    return fromNonNull(elements.get(0));
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
      Builder builder = new Builder();
      PsiElement[] children = element.getChildren();
      for (int i = 1; i < children.length; i++) {
        builder.addVariant(children[i]);
      }
      return builder.build();
    }
    else if (ONE_OF_VALUES.contains(elementType)) {
      return new Builder(element.getChildren()).build();
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
