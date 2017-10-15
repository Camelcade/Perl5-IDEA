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

package com.perl5.lang.perl.psi.references;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.ResolveResult;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.impl.PerlBuiltInNamespaceDefinition;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 28.05.2015.
 */
public class PerlNamespaceReference extends PerlCachingReference<PsiElement> {
  public PerlNamespaceReference(PsiElement psiElement) {
    super(psiElement);
  }

  public PerlNamespaceReference(@NotNull PsiElement element, TextRange textRange) {
    super(element, textRange);
  }

  @NotNull
  private String getPackageName() {
    if (myElement instanceof PerlNamespaceElement) {
      return ((PerlNamespaceElement)myElement).getCanonicalName();
    }
    return getRangeInElement().substring(myElement.getText());
  }

  @Override
  protected ResolveResult[] resolveInner(boolean incompleteCode) {
    String packageName = getPackageName();
    if (packageName.isEmpty()) {
      packageName = PerlPackageUtil.MAIN_PACKAGE;
    }

    Project project = myElement.getProject();
    PerlBuiltInNamespaceDefinition builtInNamespaceDefinition =
      PerlBuiltInNamespacesService.getInstance(project).getNamespaceDefinition(packageName);
    if (builtInNamespaceDefinition != null) {
      return PsiElementResolveResult.createResults(builtInNamespaceDefinition);
    }

    List<PsiElement> result = new ArrayList<>();
    result.addAll(PerlPackageUtil.getNamespaceDefinitions(project, PerlPackageUtil.getCanonicalPackageName(packageName)));

    return PsiElementResolveResult.createResults(result);
  }

  @Override
  public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
    if (myElement instanceof PerlNamespaceElement && ((PerlNamespaceElement)myElement).isTag()) {
      return myElement;
    }
    return super.handleElementRename(newElementName);
  }
}
