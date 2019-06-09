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

package com.perl5.lang.perl.idea.codeInsight.highlighting;

import com.intellij.codeInsight.highlighting.HighlightUsagesHandlerBase;
import com.intellij.codeInsight.highlighting.HighlightUsagesHandlerFactoryBase;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.idea.codeInsight.controlFlow.PerlControlFlowBuilder;
import com.perl5.lang.perl.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.RESERVED_EXIT;
import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.RESERVED_RETURN;
import static com.perl5.lang.perl.lexer.PerlTokenSets.LOOP_CONTROL_KEYWORDS;

public class PerlHighlightExitPointsHandlerFactory extends HighlightUsagesHandlerFactoryBase {
  private static final TokenSet EXIT_KEYWORDS = TokenSet.create(
    RESERVED_RETURN, RESERVED_EXIT
  );

  @Nullable
  @Override
  public HighlightUsagesHandlerBase createHighlightUsagesHandler(@NotNull Editor editor,
                                                                 @NotNull PsiFile file,
                                                                 @NotNull PsiElement target) {
    if (target instanceof PerlSubNameElement &&
        target.getParent() instanceof PsiPerlMethod &&
        target.getParent().getParent() instanceof PsiPerlSubCallExpr &&
        PerlControlFlowBuilder.DIE_SUBS.contains(target.getText()) ||
        EXIT_KEYWORDS.contains(PsiUtilCore.getElementType(target))) {
      return new PerlHighlightExitPointsHandler(editor, file, target);
    }
    else if (LOOP_CONTROL_KEYWORDS.contains(PsiUtilCore.getElementType(target)) && target.getParent() instanceof PerlFlowControlExpr) {
      PsiElement flowTarget = ((PerlFlowControlExpr)target.getParent()).getTargetScope();
      if (flowTarget instanceof PerlSubDefinition) {
        return new PerlHighlightExitPointsHandler(editor, file, target);
      }
    }
    return null;
  }
}
