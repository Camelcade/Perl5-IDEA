/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.ObjectUtils;
import com.perl5.lang.perl.idea.inspections.PerlLoopControlInspection;
import com.perl5.lang.perl.psi.impl.PsiPerlStatementImpl;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.SUB_EXPR;
import static com.perl5.lang.perl.psi.PerlBlock.LOOPS_CONTAINERS;
import static com.perl5.lang.tt2.lexer.TemplateToolkitElementTypesGenerated.NAMED_BLOCK;


/**
 * Represents flow control expressions with possible label: next/last/redo
 * Goto is a special case because it may go anywhere
 * We suppose the proper control flow of next/last/redo
 */
public interface PerlFlowControlExpr extends PsiPerlExpr {
  @Nullable
  default PsiPerlLabelExpr getLabelExpr() {
    return ObjectUtils.tryCast(getExpr(), PsiPerlLabelExpr.class);
  }

  /**
   * @return target scope for this control flow: loop, statement with for modifier, invalid block container or null if not found, e.g. statement in file.
   * @implNote partially duplicates logic in {@link PerlLoopControlInspection#buildVisitor(com.intellij.codeInspection.ProblemsHolder, boolean)}
   * but this logic more like real life
   * We also have labels reference resolve logic, which duplicates too. Need to unite.
   */
  @Nullable
  default PsiElement getTargetScope() {
    PsiPerlLabelExpr labelExpr = getLabelExpr();
    String labelName = labelExpr == null ? null : labelExpr.getText();

    PsiPerlStatementImpl containingStatementWithForModifier =
      getExpr() != null ? null : PsiTreeUtil.getParentOfType(this, PsiPerlStatementImpl.class);
    if (containingStatementWithForModifier != null &&
        ObjectUtils.tryCast(containingStatementWithForModifier.getModifier(), PsiPerlForStatementModifier.class) == null) {
      containingStatementWithForModifier = null;
    }

    PsiElement closestBlockContainer = this;
    while (true) {
      PerlBlock closestBlock = PerlBlock.getClosestParentFor(closestBlockContainer);
      if (closestBlock == null) {
        return containingStatementWithForModifier;
      }

      closestBlockContainer = closestBlock.getContainer();
      IElementType blockContainerType = PsiUtilCore.getElementType(closestBlockContainer);


      // we can move out of sort
      // we are falling through grep and map


      if (LOOPS_CONTAINERS.contains(blockContainerType)) {
        if (labelExpr == null) {
          return PerlPsiUtil.getClosest(closestBlockContainer, containingStatementWithForModifier);
        }
        else {
          PsiElement potentialLabel = PerlPsiUtil.getPrevSignificantSibling(closestBlockContainer);
          if (potentialLabel instanceof PerlLabelDeclaration &&
              StringUtils.equals(labelName, ((PerlLabelDeclaration)potentialLabel).getName())) {
            return PerlPsiUtil.getClosest(closestBlockContainer, containingStatementWithForModifier);
          }
        }
      }
      else if (closestBlockContainer instanceof PerlSubDefinition ||
               blockContainerType == NAMED_BLOCK ||
               blockContainerType == SUB_EXPR) {
        return PerlPsiUtil.getClosest(closestBlockContainer, containingStatementWithForModifier);
      }
    }
  }

  @Nullable
  PsiPerlExpr getExpr();
}
