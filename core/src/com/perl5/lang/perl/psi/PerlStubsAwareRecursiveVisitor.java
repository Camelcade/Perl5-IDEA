/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package com.perl5.lang.perl.psi;

import com.intellij.openapi.progress.ProgressManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.StubElement;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Visitor that attempts to use stubs tree if available
 */
public class PerlStubsAwareRecursiveVisitor extends PerlRecursiveVisitor {
  @Override
  public void visitElement(@NotNull PsiElement element) {
    if (!tryUseStubs(element, this)) {
      super.visitElement(element);
    }
  }

  public static boolean tryUseStubs(@NotNull PsiElement element, @NotNull PsiElementVisitor visitor) {
    StubElement<?> stubElement = PerlPsiUtil.getStubFromElement(element);

    if (stubElement == null) {
      return false;
    }
    ProgressManager.checkCanceled();
    for (StubElement childrenStub : stubElement.getChildrenStubs()) {
      PsiElement psi = childrenStub.getPsi();
      if (psi != null) {
        psi.accept(visitor);
      }
    }
    return true;
  }
}
