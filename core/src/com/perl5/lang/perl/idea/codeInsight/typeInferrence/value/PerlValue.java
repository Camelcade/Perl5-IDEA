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

package com.perl5.lang.perl.idea.codeInsight.typeInferrence.value;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.Processor;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Set;

import static com.perl5.lang.perl.idea.codeInsight.typeInferrence.value.PerlValueUndef.TYPE_UNDEF;
import static com.perl5.lang.perl.idea.codeInsight.typeInferrence.value.PerlValueUnknown.UNKNOWN_VALUE;

/**
 * Parent for all perl values
 */
public abstract class PerlValue {
  @Nullable
  private final PerlValue myBless;

  protected PerlValue() {
    myBless = null;
  }

  protected PerlValue(@NotNull PerlValue bless) {
    myBless = bless;
  }

  protected PerlValue(@NotNull PerlValue original, @NotNull PerlValue bless) {
    myBless = bless;
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
   * @return result of {@code Scalar::Util::blessed} equivalent invocation
   */
  @NotNull
  public PerlValue getBless() {
    return TYPE_UNDEF;
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
   * @return true iff {@code type}is null or {@link PerlValueUnknown#UNKNOWN_VALUE}
   */
  @Contract("null->true")
  public static boolean isEmpty(@Nullable PerlValue type) {
    return type == null || type == UNKNOWN_VALUE;
  }

  /**
   * @return true iff {@code} type is not empty and not {@link PerlValueUnknown#UNKNOWN_VALUE}
   */
  @Contract("null->false")
  public static boolean isNotEmpty(@Nullable PerlValue type) {
    return !isEmpty(type);
  }
}
