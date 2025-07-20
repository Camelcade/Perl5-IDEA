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

package com.perl5.lang.perl.parser.moose.psi.references;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.parser.moose.psi.impl.PerlMooseOverrideStatement;
import com.perl5.lang.perl.psi.references.PerlCachingReference;
import com.perl5.lang.perl.util.PerlMroUtil;
import com.perl5.lang.perl.util.PerlPackageUtilCore;
import org.jetbrains.annotations.NotNull;


public class PerlMooseSuperReference extends PerlCachingReference<PsiElement> {

  public PerlMooseSuperReference(PsiElement psiElement) {
    super(psiElement);
  }

  @Override
  protected @NotNull ResolveResult[] resolveInner(boolean incompleteCode) {
    // fixme not really dry with simpleresolver and superresolver. Need some generics magic
    PsiElement element = getElement();

    PerlMooseOverrideStatement overrideStatement = PsiTreeUtil.getParentOfType(element, PerlMooseOverrideStatement.class);

    if (overrideStatement == null) {
      return ResolveResult.EMPTY_ARRAY;
    }
    String packageName = PerlPackageUtilCore.getContextNamespaceName(element);
    String subName = overrideStatement.getSubName();
    Project project = element.getProject();


    var targetSubs = PerlMroUtil.collectCallables(
      project, element.getResolveScope(),
      packageName,
      subName,
      true
    );
    return PsiElementResolveResult.createResults(targetSubs);
  }

  @Override
  public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
    return myElement;
  }
}
