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

package com.perl5.lang.pod.parser.psi;

import com.intellij.openapi.progress.ProgressIndicatorProvider;
import com.intellij.psi.PsiElement;
import com.perl5.lang.pod.psi.*;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 27.03.2016.
 */
public class PodVisitor extends PsiPodVisitorGenerated {
  @Override
  public void visitElement(@NotNull PsiElement o) {
    // fixme no idea why generated visitor has recursion
    ProgressIndicatorProvider.checkCanceled();
  }

  @Override
  public void visitItemSection(@NotNull PsiItemSection o) {
    visitTargetableSection(o);
  }

  public void visitTargetableSection(PodTitledSection o) {
    visitElement(o);
  }

  @Override
  public void visitHead1Section(@NotNull PsiHead1Section o) {
    visitTargetableSection(o);
  }

  @Override
  public void visitHead2Section(@NotNull PsiHead2Section o) {
    visitTargetableSection(o);
  }

  @Override
  public void visitHead3Section(@NotNull PsiHead3Section o) {
    visitTargetableSection(o);
  }

  @Override
  public void visitHead4Section(@NotNull PsiHead4Section o) {
    visitTargetableSection(o);
  }
}
