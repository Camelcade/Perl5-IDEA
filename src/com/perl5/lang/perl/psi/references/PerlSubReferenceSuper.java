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
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveResult;
import com.perl5.lang.perl.psi.mro.PerlMroDfs;
import com.perl5.lang.perl.util.PerlPackageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 25.01.2016.
 */
public class PerlSubReferenceSuper extends PerlSubReferenceSimple {
  public PerlSubReferenceSuper(PsiElement psiElement) {
    super(psiElement);
  }


  @Override
  protected ResolveResult[] resolveInner(boolean incompleteCode) {
    // fixme not dry with simple resolver, need some generics fix
    PsiElement myElement = getElement();
    List<PsiElement> relatedItems = new ArrayList<>();

    String packageName = PerlPackageUtil.getContextPackageName(myElement);
    String subName = myElement.getNode().getText();
    Project project = myElement.getProject();

    relatedItems.addAll(PerlMroDfs.resolveSub(
      project,
      packageName,
      subName,
      true
    ));

    List<ResolveResult> result = getResolveResults(relatedItems);

    return result.toArray(new ResolveResult[result.size()]);
  }
}
