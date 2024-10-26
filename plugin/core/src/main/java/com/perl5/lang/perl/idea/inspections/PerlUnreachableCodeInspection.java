/*
 * Copyright 2015-2024 Alexandr Evstigneev
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

import com.intellij.codeInsight.controlflow.ControlFlowUtil;
import com.intellij.codeInsight.controlflow.Instruction;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.codeInsight.controlFlow.PerlControlFlowBuilder;
import com.perl5.lang.perl.psi.PerlMethodModifier;
import com.perl5.lang.perl.psi.PerlSubDefinitionElement;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.psi.PsiPerlSubExpr;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.*;

public class PerlUnreachableCodeInspection extends PerlInspection {
  private final TokenSet TRANSPARENT_ELEMENTS = TokenSet.create(
    COMMA_SEQUENCE_EXPR, DO_BLOCK_EXPR, PARENTHESISED_EXPR, DEREF_EXPR, SUB_EXPR, SUB_DEFINITION, METHOD_DEFINITION, FUNC_DEFINITION,
    LP_OR_XOR_EXPR, EVAL_EXPR
  );

  private final TokenSet NOT_ANNOTATABLE_TOKENS = TokenSet.create(
    TokenType.WHITE_SPACE
  );

  @Override
  public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
    var notifiedElements = new HashSet<PsiElement>();
    var reachableRanges = new LinkedList<TextRange>();
    var unreachableElements = new HashSet<PsiElement>();
    return new PerlVisitor() {
      @Override
      public void visitPerlSubDefinitionElement(@NotNull PerlSubDefinitionElement o) {
        processElement(o);
      }

      @Override
      public void visitPerlMethodModifier(@NotNull PerlMethodModifier o) {
        processElement(o);
      }

      private void processElement(@NotNull PsiElement o) {
        PerlControlFlowBuilder.iteratePrev(o, instruction -> {
          if (instruction.allPred().isEmpty() && instruction.num() != 0) {
            processUnreachableInstruction(instruction);
          }
          else {
            registerReachableRange(instruction);
          }
          return ControlFlowUtil.Operation.NEXT;
        });
        unreachableElements.forEach(this::annotateUnreachableElement);
      }

      private void registerReachableRange(@NotNull Instruction instruction) {
        var element = instruction.getElement();
        if (element == null || element instanceof PsiFile) {
          return;
        }
        var elementRange = element.getTextRange();
        if (elementRange == null || elementRange.isEmpty()) {
          return;
        }
        var rangesIterator = reachableRanges.iterator();
        var toAdd = true;
        while (rangesIterator.hasNext()) {
          var range = rangesIterator.next();
          if (elementRange.contains(range)) {
            rangesIterator.remove();
          }
          else if (range.contains(elementRange)) {
            toAdd = false;
            break;
          }
        }
        if (toAdd) {
          reachableRanges.add(elementRange);
        }
      }

      @Override
      public void visitSubExpr(@NotNull PsiPerlSubExpr o) {
        processElement(o);
      }

      @Override
      public void visitFile(@NotNull PsiFile file) {
        processElement(file);
      }

      private void processUnreachableInstruction(@NotNull Instruction instruction) {
        PsiElement element = instruction.getElement();
        if (element == null) {
          return;
        }

        if (TRANSPARENT_ELEMENTS.contains(PsiUtilCore.getElementType(element))) {
          instruction.allSucc().forEach(it -> {
            // this is just a weak check for the next unreachable instruction
            if (it.allPred().size() < 2) {
              processUnreachableInstruction(it);
            }
          });
        }
        else {
          unreachableElements.add(element);
        }
      }

      private void annotateUnreachableElement(PsiElement element) {
        var firstChild = element.getFirstChild();
        if (firstChild == null || firstChild == element.getLastChild()) {
          doRegisterProblem(element);
          return;
        }

        var reachableKids = new ArrayList<PsiElement>();
        var unreachableKids = new ArrayList<PsiElement>();
        var elementRange = element.getTextRange();
        var reachableSubRanges = new LinkedList<>(ContainerUtil.filter(reachableRanges, elementRange::contains));
        var childElement = firstChild;
        while (childElement != null) {
          var childElementType = PsiUtilCore.getElementType(childElement);
          if (!NOT_ANNOTATABLE_TOKENS.contains(childElementType)) {
            var childElementRange = childElement.getTextRange();
            if (reachableSubRanges.removeIf(it -> it.contains(childElementRange))) {
              reachableKids.add(childElement);
            }
            else {
              unreachableKids.add(childElement);
            }
          }
          childElement = childElement.getNextSibling();
        }

        if (reachableKids.isEmpty()) {
          doRegisterProblem(element);
        }
        else {
          unreachableKids.forEach(this::doRegisterProblem);
        }
      }

      private void doRegisterProblem(@NotNull PsiElement element) {
        if (notifiedElements.add(element)) {
          registerProblem(holder, element, PerlBundle.message("perl.inspection.unreachable.code"));
        }
      }
    };
  }
}
