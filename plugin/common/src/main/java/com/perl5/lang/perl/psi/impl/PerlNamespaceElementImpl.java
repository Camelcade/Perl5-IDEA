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

package com.perl5.lang.perl.psi.impl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionWithIdentifier;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.util.PerlPackageService;
import com.perl5.lang.perl.util.PerlPackageUtilCore;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.TAG_PACKAGE;
import static com.perl5.lang.perl.util.PerlCorePackages.CORE_PACKAGES_PRAGMAS;


public class PerlNamespaceElementImpl extends PerlLeafPsiElementWithReferences implements PerlNamespaceElement {
  public PerlNamespaceElementImpl(@NotNull IElementType type, CharSequence text) {
    super(type, text);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor perlVisitor) {
      perlVisitor.visitNamespaceElement(this);
    }
    else {
      super.accept(visitor);
    }
  }

  @Override
  public @NotNull String getName() {
    return isTag() ? PerlPackageUtilCore.getContextNamespaceName(this) : this.getText();
  }

  @Override
  public String getCanonicalName() {
    return PerlPackageUtilCore.getCanonicalNamespaceName(getName());
  }

  @Override
  public boolean isTag() {return getNode().getElementType() == TAG_PACKAGE;}

  @Override
  public boolean isPragma() {
    return CORE_PACKAGES_PRAGMAS.contains(getCanonicalName());
  }

  @Override
  public boolean isSUPER() {
    return PerlPackageUtilCore.isSUPER(getCanonicalName());
  }

  @Override
  public boolean isMain() {
    return PerlPackageUtilCore.isMain(getCanonicalName());
  }

  @Override
  public boolean isCORE() {
    return PerlPackageUtilCore.isCORE(getCanonicalName());
  }

  @Override
  public boolean isUNIVERSAL() {
    return PerlPackageUtilCore.isUNIVERSAL(getCanonicalName());
  }

  @Override
  public boolean isDeprecated() {
    PsiElement parent = getParent();
    if (parent instanceof PerlNamespaceDefinitionWithIdentifier) {
      return ((PerlNamespaceDefinitionElement)parent).isDeprecated();
    }
    return PerlPackageService.getInstance().isDeprecated(getProject(), GlobalSearchScope.allScope(getProject()), getCanonicalName());
  }
}
