/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.mojolicious.psi.references;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.stubs.StubIndex;
import com.perl5.lang.mojolicious.psi.MojoliciousHelperDeclaration;
import com.perl5.lang.mojolicious.psi.stubs.MojoliciousHelpersStubIndex;
import com.perl5.lang.perl.PerlScopes;
import com.perl5.lang.perl.psi.references.PerlCachingReference;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 23.04.2016.
 */
public class MojoliciousHelperReference extends PerlCachingReference<PsiElement> {
  public MojoliciousHelperReference(@NotNull PsiElement element) {
    super(element, null);
  }

  @Override
  protected ResolveResult[] resolveInner(boolean incompleteCode) {
    PsiElement element = getElement();
    String elementText = element.getText();
    final Project project = element.getProject();
    List<ResolveResult> result = new ArrayList<ResolveResult>();

    for (MojoliciousHelperDeclaration helper : StubIndex
      .getElements(MojoliciousHelpersStubIndex.KEY, elementText, project, PerlScopes.getProjectAndLibrariesScope(project),
                   MojoliciousHelperDeclaration.class)) {
      result.add(new PsiElementResolveResult(helper));
    }

    return result.toArray(new ResolveResult[result.size()]);
  }
}
