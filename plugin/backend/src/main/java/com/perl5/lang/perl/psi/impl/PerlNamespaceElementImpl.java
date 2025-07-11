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
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionWithIdentifier;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.psi.references.PerlNamespaceFileReference;
import com.perl5.lang.perl.psi.references.PerlNamespaceReference;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
    return isTag() ? PerlPackageUtil.getContextNamespaceName(this) : this.getText();
  }

  @Override
  public String getCanonicalName() {
    return PerlPackageUtil.getCanonicalNamespaceName(getName());
  }

  @Override
  public boolean isTag() {return getNode().getElementType() == TAG_PACKAGE;}

  @Override
  public boolean isPragma() {
    return CORE_PACKAGES_PRAGMAS.contains(getCanonicalName());
  }

  @Override
  public boolean isSUPER() {
    return PerlPackageUtil.isSUPER(getCanonicalName());
  }

  @Override
  public boolean isMain() {
    return PerlPackageUtil.isMain(getCanonicalName());
  }

  @Override
  public boolean isCORE() {
    return PerlPackageUtil.isCORE(getCanonicalName());
  }

  @Override
  public boolean isUNIVERSAL() {
    return PerlPackageUtil.isUNIVERSAL(getCanonicalName());
  }

  @Override
  public boolean isDeprecated() {
    PsiElement parent = getParent();
    if (parent instanceof PerlNamespaceDefinitionWithIdentifier) {
      return ((PerlNamespaceDefinitionElement)parent).isDeprecated();
    }
    return PerlPackageUtil.isDeprecated(getProject(), GlobalSearchScope.allScope(getProject()), getCanonicalName());
  }

  @Override
  public List<PerlNamespaceDefinitionElement> getNamespaceDefinitions() {
    List<PerlNamespaceDefinitionElement> namespaceDefinitions = new ArrayList<>();

    PsiReference[] references = getReferences();

    for (PsiReference reference : references) {
      if (reference instanceof PerlNamespaceReference namespaceReference) {
        ResolveResult[] results = namespaceReference.multiResolve(false);

        for (ResolveResult result : results) {
          PsiElement targetElement = result.getElement();
          assert targetElement != null;
          assert targetElement instanceof PerlNamespaceDefinitionElement;

          namespaceDefinitions.add((PerlNamespaceDefinitionElement)targetElement);
        }
      }
    }
    return namespaceDefinitions;
  }

  @Override
  public List<PerlFileImpl> getNamespaceFiles() {
    List<PerlFileImpl> namespaceFiles = new ArrayList<>();

    PsiReference[] references = getReferences();

    for (PsiReference reference : references) {
      if (reference instanceof PerlNamespaceFileReference namespaceFileReference) {
        ResolveResult[] results = namespaceFileReference.multiResolve(false);

        for (ResolveResult result : results) {
          PsiElement targetElement = result.getElement();
          assert targetElement != null;

          if (targetElement instanceof PerlFileImpl perlFile) {
            namespaceFiles.add(perlFile);
          }
        }
      }
    }
    return namespaceFiles;
  }
}
