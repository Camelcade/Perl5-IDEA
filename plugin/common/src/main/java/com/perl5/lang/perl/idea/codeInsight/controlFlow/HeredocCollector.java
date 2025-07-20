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

package com.perl5.lang.perl.idea.codeInsight.controlFlow;

import com.intellij.psi.PsiElement;
import com.intellij.util.ObjectUtils;
import com.perl5.lang.perl.psi.PerlRecursiveVisitor;
import com.perl5.lang.perl.psi.PsiPerlHeredocOpener;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import com.perl5.lang.perl.psi.impl.PerlHeredocTerminatorElementImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

// this won't handle nested heredocs.
class HeredocCollector extends PerlRecursiveVisitor {
  private final List<PsiElement> myOpeners = new ArrayList<>();
  // may contain nulls for heredocs without bodies
  private final List<PsiElement> myBodies = new ArrayList<>();
  private int myTerminatorCounter = 0;

  @Override
  public void visitHeredocOpener(@NotNull PsiPerlHeredocOpener o) {
    myOpeners.add(o);
  }

  @Override
  public void visitHeredocElement(@NotNull PerlHeredocElementImpl o) {
    myBodies.add(o);
    super.visitHeredocElement(o);
  }

  @Override
  public void visitHeredocTeminator(@NotNull PerlHeredocTerminatorElementImpl o) {
    myTerminatorCounter++;
    if (myTerminatorCounter > myBodies.size()) {
      myBodies.add(null);
    }
  }

  /**
   * @return body psi element for opener if any
   */
  public @Nullable PerlHeredocElementImpl getBody(@NotNull PsiPerlHeredocOpener opener) {
    int openerIndex = myOpeners.indexOf(opener);
    if (openerIndex == -1 || myBodies.size() <= openerIndex) {
      return null;
    }
    return ObjectUtils.tryCast(myBodies.get(openerIndex), PerlHeredocElementImpl.class);
  }
}
