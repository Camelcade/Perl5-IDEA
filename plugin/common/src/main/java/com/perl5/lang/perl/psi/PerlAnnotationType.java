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

package com.perl5.lang.perl.psi;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiModificationTracker;
import com.perl5.lang.perl.psi.utils.PerlVariableAnnotations;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public interface PerlAnnotationType extends PerlAnnotationWithValue {

  @SuppressWarnings("override")
  @Nullable
  PsiPerlAnnotationVariable getAnnotationVariable();

  /**
   * @return true iff annotation has no variable name and may be applied to any variable in context.
   */
  default boolean isWildCard() {
    return getAnnotationVariable() == null;
  }

  /**
   * @return true iff this {@code #@type} annotation may describe the {@code perlVariable}. Have same type and name.
   */
  @Contract("null->false")
  default boolean accept(@Nullable PerlVariable perlVariable) {
    if (perlVariable == null) {
      return false;
    }
    var annotationVariable = getAnnotationVariable();
    if (annotationVariable == null) {
      return true;
    }
    var annotationVariableName = annotationVariable.getNode().getChars();
    return perlVariable.getActualType().getSigil() == annotationVariableName.charAt(0) &&
           StringUtil.equal(perlVariable.getName(), annotationVariableName.subSequence(1, annotationVariableName.length()), true);
  }

  /**
   * @return collection of declarations this annotation applies to
   */
  default @NotNull List<PerlVariableDeclarationElement> getTargets() {
    return CachedValuesManager.getCachedValue(
      this,
      () -> CachedValueProvider.Result.create(computeTargets(this), PsiModificationTracker.MODIFICATION_COUNT));
  }

  /**
   * @return wrapping comment {@link PsiElement}
   */
  default @NotNull PerlAnnotationContainer getAnnotationContainer() {
    var parent = getParent();
    if (parent instanceof PerlAnnotationContainer annotationContainer) {
      return annotationContainer;
    }
    throw new IllegalStateException(
      "Type annotation is expected to be inside " + PerlAnnotationContainer.class.getSimpleName() + ", not " + parent);
  }

  /**
   * @return list of variable declarations {@code typeAnnotation} applies to
   */
  static @NotNull List<PerlVariableDeclarationElement> computeTargets(@NotNull PerlAnnotationType typeAnnotation) {
    var result = new ArrayList<PerlVariableDeclarationElement>();
    PerlVariableAnnotations.processPotentialTargets(typeAnnotation, it -> PerlVariableAnnotations.processAnnotations(it,
                                                                                                                     new PerlVariableAnnotations.VariableAnnotationProcessor() {
                                                                                                                       @Override
                                                                                                                       public boolean processType(
                                                                                                                         @NotNull PerlAnnotationType annotationType) {
                                                                                                                         if (annotationType.equals(
                                                                                                                           typeAnnotation)) {
                                                                                                                           result.add(it);
                                                                                                                         }
                                                                                                                         return true;
                                                                                                                       }
                                                                                                                     }));
    return result;
  }
}
