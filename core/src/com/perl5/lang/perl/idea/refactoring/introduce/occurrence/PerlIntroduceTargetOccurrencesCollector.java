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

package com.perl5.lang.perl.idea.refactoring.introduce.occurrence;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.idea.refactoring.introduce.PerlIntroduceTarget;
import com.perl5.lang.perl.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.perl5.lang.perl.idea.refactoring.introduce.target.PerlIntroduceTargetsHandler.isTargetableElement;

public abstract class PerlIntroduceTargetOccurrencesCollector {
  @NotNull
  private final PerlIntroduceTarget myTarget;
  @Nullable
  private List<PerlIntroduceTarget> myOccurrences;

  protected PerlIntroduceTargetOccurrencesCollector(@NotNull PerlIntroduceTarget target) {
    myTarget = target;
  }

  @NotNull
  protected final List<PerlIntroduceTarget> getOccurrences() {
    return myOccurrences == null ? Collections.singletonList(myTarget) : Collections.unmodifiableList(myOccurrences);
  }

  /**
   * @return true iff we've not yet collected occurrences or found nothing
   */
  public final boolean isEmpty() {
    return myOccurrences == null || myOccurrences.isEmpty();
  }

  protected final void addOccurrence(@Nullable PerlIntroduceTarget occurrence) {
    if (occurrence != null) {
      myOccurrences.add(occurrence);
    }
  }

  @NotNull
  protected final PerlIntroduceTarget getTarget() {
    return myTarget;
  }

  @Nullable
  protected final PsiElement getTargetElement() {
    return getTarget().getPlace();
  }

  @NotNull
  public final List<PerlIntroduceTarget> collectOccurrences() {
    PsiElement searchScope = computeTargetScope(myTarget);
    if (searchScope == null) {
      return getOccurrences();
    }
    if (myOccurrences != null) {
      throw new RuntimeException("Looks like we've already collected");
    }

    myOccurrences = new ArrayList<>();

    searchScope.accept(new PerlRecursiveVisitor() {
      @Override
      public void visitElement(@NotNull PsiElement element) {
        if (!isTargetableElement(element) || !collectOccurrences(element)) {
          super.visitElement(element);
        }
      }
    });
    return getOccurrences();
  }

  /**
   * @return psi element we should search for occurrences of the searcher's target.
   */
  @Nullable
  public static final PsiElement computeTargetScope(@NotNull PerlIntroduceTarget target) {
    PsiElement targetElement = target.getPlace();
    PsiElement scope = PsiTreeUtil.getParentOfType(targetElement, PerlSubDefinitionElement.class);
    return scope != null ? scope : PsiTreeUtil.getParentOfType(targetElement, PerlNamespaceDefinitionElement.class);
  }

  /**
   * Collecting occurrences of searcher's target from the {@code element}
   *
   * @return true iff element matches our target
   */
  protected abstract boolean collectOccurrences(@NotNull PsiElement element);

  /**
   * @return occurrences collector for the {@code target}
   */
  @NotNull
  public static PerlIntroduceTargetOccurrencesCollector create(@NotNull PerlIntroduceTarget target) {
    PsiElement targetElement = target.getPlace();
    if (targetElement instanceof PerlDerefExpression) {
      return new PerlDerefOccurrencesCollector(target);
    }
    else if (targetElement instanceof PerlStringList || targetElement instanceof PsiPerlCommaSequenceExpr) {
      return new PerlListOccurrencesCollector(target);
    }
    else if (targetElement instanceof PerlString && !target.isFullRange()) {
      return new PerlPartialStringOccurrencesCollector(target);
    }
    if (!target.isFullRange()) {
      throw new RuntimeException("Generic collector may handle only full-range targets");
    }

    return new PerlGenericOccurrencesCollector(target);
  }

  /**
   * @return all known {@code target} occurrences
   */
  @NotNull
  public static List<PerlIntroduceTarget> collect(@NotNull PerlIntroduceTarget target) {
    return create(target).collectOccurrences();
  }
}
