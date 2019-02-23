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
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.perl5.lang.perl.idea.codeInsight.typeInferrence.value.PerlValueUnknown.UNKNOWN_VALUE;

public final class PerlValueOneOf extends PerlValue {
  @NotNull
  private final Set<PerlValue> myVariants;

  private PerlValueOneOf(@NotNull Set<PerlValue> variants) {
    myVariants = Collections.unmodifiableSet(new HashSet<>(variants));
  }

  private PerlValueOneOf(@NotNull PerlValueOneOf original, @NotNull PerlValue bless) {
    super(original, bless);
    myVariants = original.myVariants;
  }

  @NotNull
  @Override
  PerlValueOneOf createBlessedCopy(@NotNull PerlValue bless) {
    return new PerlValueOneOf(this, bless);
  }

  @NotNull
  @Override
  protected Set<String> getNamespaceNames(@NotNull Project project,
                                          @NotNull GlobalSearchScope searchScope,
                                          @Nullable Set<PerlValue> recursion) {
    if (myVariants.isEmpty()) {
      return Collections.emptySet();
    }

    return PerlValuesCacheService.getInstance(project).getNamespaceNames(this, () -> {
      Set<PerlValue> finalRecursion = ObjectUtils.notNull(recursion, new HashSet<>());
      Set<String> result = new HashSet<>();
      myVariants.forEach(it -> result.addAll(it.getNamespaceNames(project, searchScope, finalRecursion)));
      return result;
    });
  }

  @NotNull
  @Override
  protected Set<String> getSubNames(@NotNull Project project,
                                    @NotNull GlobalSearchScope searchScope,
                                    @Nullable Set<PerlValue> recursion) {
    if (myVariants.isEmpty()) {
      return Collections.emptySet();
    }

    return PerlValuesCacheService.getInstance(project).getSubsNames(this, () -> {
      Set<PerlValue> finalRecursion = ObjectUtils.notNull(recursion, new HashSet<>());
      Set<String> result = new HashSet<>();
      myVariants.forEach(it -> result.addAll(it.getSubNames(project, searchScope, finalRecursion)));
      return result;
    });
  }

  @Override
  public boolean canRepresentNamespace(@Nullable String namespaceName) {
    for (PerlValue variant : myVariants) {
      if (variant.canRepresentNamespace(namespaceName)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean canRepresentSubName(@Nullable String subName) {
    for (PerlValue variant : myVariants) {
      if (variant.canRepresentSubName(subName)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PerlValueOneOf of = (PerlValueOneOf)o;

    return myVariants.equals(of.myVariants);
  }

  @Override
  public int hashCode() {
    return myVariants.hashCode();
  }

  @Override
  public String toString() {
    return "OneOf: " + myVariants;
  }

  public static class Builder {
    @NotNull
    private final Set<PerlValue> myVariants = new HashSet<>();

    public void addVariant(@Nullable PerlValue variant) {
      if (variant == null || variant == UNKNOWN_VALUE) {
        return;
      }

      if (variant instanceof PerlValueOneOf) {
        ((PerlValueOneOf)variant).myVariants.forEach(this::addVariant);
      }
      else {
        myVariants.add(variant);
      }
    }

    @NotNull
    public PerlValue build() {
      return myVariants.isEmpty() ? UNKNOWN_VALUE : myVariants.size() == 1 ? myVariants.iterator().next() : new PerlValueOneOf(myVariants);
    }
  }
}
