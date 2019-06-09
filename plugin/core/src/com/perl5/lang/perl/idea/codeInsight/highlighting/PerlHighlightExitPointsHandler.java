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

import com.intellij.codeInsight.controlflow.ControlFlowUtil;
import com.intellij.codeInsight.controlflow.Instruction;
import com.intellij.codeInsight.highlighting.HighlightUsagesHandlerBase;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Consumer;
import com.perl5.lang.perl.idea.codeInsight.controlFlow.PerlControlFlowBuilder;
import com.perl5.lang.perl.psi.properties.PerlReturnScope;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

class PerlHighlightExitPointsHandler extends HighlightUsagesHandlerBase<PsiElement> {
  @NotNull
  private final PsiElement myTarget;

  public PerlHighlightExitPointsHandler(@NotNull Editor editor,
                                        @NotNull PsiFile file,
                                        @NotNull PsiElement target) {
    super(editor, file);
    myTarget = target;
  }

  @Override
  public List<PsiElement> getTargets() {
    return Collections.emptyList();
  }

  @Override
  protected void selectTargets(List<PsiElement> targets, Consumer<List<PsiElement>> selectionConsumer) {
    selectionConsumer.consume(targets);
  }

  @Override
  public void computeUsages(List<PsiElement> targets) {
    PerlReturnScope returnScope = PsiTreeUtil.getParentOfType(myTarget, PerlReturnScope.class);
    if (returnScope == null) {
      return;
    }
    Instruction[] instructions = PerlControlFlowBuilder.getFor(returnScope).getInstructions();
    Instruction exitInstruction = instructions[instructions.length - 1];
    PerlControlFlowBuilder.iteratePrev(instructions, it -> {
      if (it == exitInstruction || it.num() == 0) {
        return ControlFlowUtil.Operation.NEXT;
      }
      PsiElement element = it.getElement();
      if (element == null) {
        return ControlFlowUtil.Operation.NEXT;
      }
      myReadUsages.add(element.getTextRange());

      return ControlFlowUtil.Operation.CONTINUE;
    });
  }
}
