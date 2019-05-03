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

package com.perl5.lang.pod.idea.inspections;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.pod.parser.psi.PodVisitor;
import com.perl5.lang.pod.parser.psi.mixin.PodFormatterL;
import com.perl5.lang.pod.parser.psi.util.PodRenderUtil;
import com.perl5.lang.pod.psi.PsiLinkSection;
import com.perl5.lang.pod.psi.PsiPodFormatLink;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.pod.lexer.PodElementTypesGenerated.POD_DIV;
import static com.perl5.lang.pod.lexer.PodElementTypesGenerated.POD_QUOTE_DOUBLE;

public class PodLegacySectionLinkInspection extends LocalInspectionTool {
  @NotNull
  @Override
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
    return new PodVisitor() {

      @Override
      public void visitPodFormatLink(@NotNull PsiPodFormatLink o) {
        assert o instanceof PodFormatterL;
        PodFormatterL link = (PodFormatterL)o;
        PsiLinkSection sectionelement = link.getLinkSectionElement();
        if (link.getLinkNameElement() == null && isSectionLegacy(sectionelement)) {
          holder.registerProblem(sectionelement,
                                 "Section \"" + PodRenderUtil.renderPsiElementAsText(sectionelement) + "\" should have a slash before it",
                                 ProblemHighlightType.LIKE_DEPRECATED);
        }
        super.visitPodFormatLink(o);
      }

      @Contract("null -> false")
      private boolean isSectionLegacy(@Nullable PsiLinkSection linkSection) {
        if (linkSection == null) {
          return false;
        }

        PsiElement run = linkSection.getPrevSibling();
        IElementType elementType = PsiUtilCore.getElementType(run);
        return elementType != POD_DIV &&
               !(elementType == POD_QUOTE_DOUBLE && PsiUtilCore.getElementType(run.getPrevSibling()) == POD_DIV);
      }
    };
  }
}
