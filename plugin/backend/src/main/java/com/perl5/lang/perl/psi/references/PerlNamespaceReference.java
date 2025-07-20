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

package com.perl5.lang.perl.psi.references;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.ResolveResult;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.impl.PerlBuiltInNamespaceDefinition;
import com.perl5.lang.perl.util.PerlNamespaceUtil;
import com.perl5.lang.perl.util.PerlPackageUtilCore;
import org.jetbrains.annotations.NotNull;

public class PerlNamespaceReference extends PerlCachingReference<PsiElement> {
  public PerlNamespaceReference(PsiElement psiElement) {
    super(psiElement);
  }

  public PerlNamespaceReference(@NotNull PsiElement element, TextRange textRange) {
    super(element, textRange);
  }

  private @NotNull String getNamspaceName() {
    if (myElement instanceof PerlNamespaceElement namespaceElement) {
      return namespaceElement.getCanonicalName();
    }
    return getRangeInElement().substring(myElement.getText());
  }

  @Override
  protected @NotNull ResolveResult[] resolveInner(boolean incompleteCode) {
    String namespaceName = getNamspaceName();
    if (namespaceName.isEmpty()) {
      namespaceName = PerlPackageUtilCore.MAIN_NAMESPACE_NAME;
    }

    Project project = myElement.getProject();
    PerlBuiltInNamespaceDefinition builtInNamespaceDefinition =
      PerlBuiltInNamespacesService.getInstance(project).getNamespaceDefinition(namespaceName);
    if (builtInNamespaceDefinition != null) {
      return PsiElementResolveResult.createResults(builtInNamespaceDefinition);
    }

    return PsiElementResolveResult.createResults(
      PerlNamespaceUtil.getNamespaceDefinitions(project, myElement.getResolveScope(),
                                                PerlPackageUtilCore.getCanonicalNamespaceName(namespaceName)));
  }

  @Override
  public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
    if (myElement instanceof PerlNamespaceElement namespaceElement && namespaceElement.isTag()) {
      return myElement;
    }
    return super.handleElementRename(newElementName);
  }
}
