/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.stubs.PerlPolyNamedElementStub;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for
 */
public abstract class PerlPolyNamedNestedCallElementBase<Stub extends PerlPolyNamedElementStub> extends PerlPolyNamedElementBase<Stub>
  implements PerlNestedCall {
  public PerlPolyNamedNestedCallElementBase(@NotNull Stub stub,
                                            @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  public PerlPolyNamedNestedCallElementBase(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  @NotNull
  public PsiPerlMethod getMethod() {
    return findNotNullChildByClass(PsiPerlMethod.class);
  }

  @Nullable
  protected PerlSubAnnotations computeSubAnnotations(@NotNull PsiElement nestedCallElement, @NotNull PsiElement nameIdentifier) {
    List<PsiElement> baseElements = new ArrayList<>();
    PsiPerlStatement containingStatement = PsiTreeUtil.getParentOfType(nestedCallElement, PsiPerlStatement.class);
    if (containingStatement != null) {
      baseElements.add(containingStatement);
    }
    baseElements.add(nestedCallElement);

    if (nameIdentifier instanceof PerlStringContentElement) {
      PerlStringList perlStringList = PsiTreeUtil.getParentOfType(nameIdentifier, PerlStringList.class);
      if (perlStringList != null) {
        baseElements.add(perlStringList);
      }
    }
    else {
      baseElements.add(nameIdentifier);
    }

    return PerlSubAnnotations.tryToFindAnnotations(baseElements);
  }
}
