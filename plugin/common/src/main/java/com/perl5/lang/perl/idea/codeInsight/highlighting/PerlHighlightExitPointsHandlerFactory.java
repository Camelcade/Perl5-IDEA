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

package com.perl5.lang.perl.idea.codeInsight.highlighting;

import com.intellij.codeInsight.highlighting.HighlightUsagesHandlerBase;
import com.intellij.codeInsight.highlighting.HighlightUsagesHandlerFactoryBase;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.idea.codeInsight.controlFlow.PerlControlFlowBuilder;
import com.perl5.lang.perl.psi.PerlFlowControlExpr;
import com.perl5.lang.perl.psi.PerlSubDefinition;
import com.perl5.lang.perl.psi.PerlSubNameElement;
import com.perl5.lang.perl.psi.PsiPerlMethod;
import com.perl5.lang.perl.psi.impl.PerlSubCallElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.lexer.PerlTokenSets.LOOP_CONTROL_KEYWORDS;
import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.RESERVED_EXIT;
import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.RESERVED_RETURN;

public class PerlHighlightExitPointsHandlerFactory extends HighlightUsagesHandlerFactoryBase {
  private static final TokenSet EXIT_KEYWORDS = TokenSet.create(
    RESERVED_RETURN, RESERVED_EXIT
  );

  @Override
  public @Nullable HighlightUsagesHandlerBase<?> createHighlightUsagesHandler(@NotNull Editor editor,
                                                                              @NotNull PsiFile file,
                                                                              @NotNull PsiElement target) {
    if( !target.isValid()){
      return null;
    }
    if (target instanceof PerlSubNameElement &&
        target.getParent() instanceof PsiPerlMethod &&
        target.getParent().getParent() instanceof PerlSubCallElement &&
        PerlControlFlowBuilder.DIE_SUBS.contains(target.getText()) ||
        EXIT_KEYWORDS.contains(PsiUtilCore.getElementType(target))) {
      return new PerlHighlightExitPointsHandler(editor, file, target);
    }
    else if (LOOP_CONTROL_KEYWORDS.contains(PsiUtilCore.getElementType(target)) &&
             target.getParent() instanceof PerlFlowControlExpr perlFlowControlExpr) {
      PsiElement flowTarget = perlFlowControlExpr.getTargetScope();
      if (flowTarget instanceof PerlSubDefinition) {
        return new PerlHighlightExitPointsHandler(editor, file, target);
      }
    }
    return null;
  }
}
