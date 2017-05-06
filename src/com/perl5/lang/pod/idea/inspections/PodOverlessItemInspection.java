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

package com.perl5.lang.pod.idea.inspections;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.pod.parser.psi.PodSectionOver;
import com.perl5.lang.pod.parser.psi.PodVisitor;
import com.perl5.lang.pod.psi.PsiItemSection;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 10.04.2016.
 */
public class PodOverlessItemInspection extends LocalInspectionTool {
  @NotNull
  @Override
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
    return new PodVisitor() {
      @Override
      public void visitItemSection(@NotNull PsiItemSection o) {
        if (PsiTreeUtil.getParentOfType(o, PodSectionOver.class) == null) {
          PsiElement openTag = o.getFirstChild();
          if (openTag != null) {
            holder.registerProblem(openTag, "List item outside over block", ProblemHighlightType.GENERIC_ERROR);
          }
        }
        super.visitItemSection(o);
      }
    };
  }
}
