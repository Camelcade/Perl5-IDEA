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

package com.perl5.lang.perl.idea.inspections;

import com.intellij.codeInsight.controlflow.ControlFlow;
import com.intellij.codeInsight.controlflow.ControlFlowUtil;
import com.intellij.codeInsight.controlflow.Instruction;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.codeInsight.controlFlow.PerlControlFlowBuilder;
import com.perl5.lang.perl.psi.PerlSubDefinitionElement;
import com.perl5.lang.perl.psi.PerlVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PerlUnreachableCodeInspection extends PerlInspection {
  @NotNull
  @Override
  public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
    return new PerlVisitor() {
      @Override
      public void visitPerlSubDefinitionElement(@NotNull PerlSubDefinitionElement o) {
        ControlFlow flow = PerlControlFlowBuilder.getFor(o);
        final Instruction[] instructions = flow.getInstructions();
        final List<PsiElement> unreachable = new ArrayList<>();
        if (instructions.length > 0) {
          ControlFlowUtil.iteratePrev(instructions.length - 1, instructions, instruction -> {
            if (instruction.allPred().isEmpty() && !isFirstInstruction(instruction)) {
              unreachable.add(instruction.getElement());
            }
            return ControlFlowUtil.Operation.NEXT;
          });
        }
        for (PsiElement e : unreachable) {
          registerProblem(holder, e, PerlBundle.message("perl.inspection.unreachable.code"));
        }
      }
    };
  }

  private static boolean isFirstInstruction(Instruction instruction) {
    return instruction.num() == 0;
  }
}
