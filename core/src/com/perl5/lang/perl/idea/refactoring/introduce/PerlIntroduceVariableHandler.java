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

package com.perl5.lang.perl.idea.refactoring.introduce;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pass;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.HelpID;
import com.intellij.refactoring.IntroduceTargetChooser;
import com.intellij.refactoring.RefactoringActionHandler;
import com.intellij.refactoring.RefactoringBundle;
import com.intellij.refactoring.introduce.inplace.OccurrencesChooser;
import com.intellij.refactoring.listeners.RefactoringEventData;
import com.intellij.refactoring.listeners.RefactoringEventListener;
import com.intellij.refactoring.util.CommonRefactoringUtil;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.refactoring.introduce.occurrence.PerlTargetOccurrencesCollector;
import com.perl5.lang.perl.idea.refactoring.introduce.target.PerlTargetsHandler;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PsiPerlStatementImpl;
import com.perl5.lang.perl.psi.properties.PerlCompound;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

public class PerlIntroduceVariableHandler implements RefactoringActionHandler {
  private static final Logger LOG = Logger.getInstance(PerlIntroduceVariableHandler.class);

  @Override
  public void invoke(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file, DataContext dataContext) {
    List<PerlIntroduceTarget> targets = PerlTargetsHandler.getIntroduceTargets(editor, file);
    if (targets.isEmpty()) {
      showErrorMessage(project, editor, RefactoringBundle.getCannotRefactorMessage(PerlBundle.message("perl.introduce.no.target")));
      return;
    }

    if (targets.size() > 1) {
      IntroduceTargetChooser.showIntroduceTargetChooser(
        editor,
        targets,
        new Pass<PerlIntroduceTarget>() {
          @Override
          public void pass(PerlIntroduceTarget target) {
            introduceTarget(target, editor, file, dataContext);
          }
        },
        PerlBundle.message("perl.introduce.expressions"),
        -1);
    }
    else {
      introduceTarget(targets.iterator().next(), editor, file, dataContext);
    }
  }

  /**
   * Collects occurrences of selected {@code target}, suggest to replace all or one, and going on
   */
  private void introduceTarget(@NotNull PerlIntroduceTarget target,
                               @NotNull Editor editor,
                               @NotNull PsiFile file,
                               DataContext dataContext) {
    List<PerlIntroduceTarget> allOccurrences = PerlTargetOccurrencesCollector.collect(target);
    if (allOccurrences.size() > 1) {
      new OccurrencesChooser<PerlIntroduceTarget>(editor) {
        @Override
        protected TextRange getOccurrenceRange(PerlIntroduceTarget occurrence) {
          return occurrence.getTextRange();
        }
      }.showChooser(
        target,
        allOccurrences,
        new Pass<OccurrencesChooser.ReplaceChoice>() {
          @Override
          public void pass(OccurrencesChooser.ReplaceChoice replaceChoice) {
            introduceTarget(target, allOccurrences, replaceChoice, editor, file, dataContext);
          }
        }
      );
    }
    else {
      introduceTarget(target, allOccurrences, OccurrencesChooser.ReplaceChoice.NO, editor, file, dataContext);
    }
  }

  private void introduceTarget(@NotNull PerlIntroduceTarget target,
                               @NotNull List<PerlIntroduceTarget> occurrences,
                               @NotNull OccurrencesChooser.ReplaceChoice replaceChoice,
                               @NotNull Editor editor,
                               @NotNull PsiFile file,
                               DataContext dataContext) {
    if (replaceChoice == OccurrencesChooser.ReplaceChoice.NO) {
      occurrences = Collections.singletonList(target);
    }

    if (occurrences.isEmpty()) {
      LOG.error("Occurrences happened to be empty for " + target);
      return;
    }

    List<String> suggestedNames = getSuggestedNames(target);
    if (suggestedNames.isEmpty()) {
      LOG.error("Suggested names list was empty for " + target);
      return;
    }
    String variableName = suggestedNames.get(0);

    String targetExpressionText = createTargetExpressionText(variableName, target);
    Project project = file.getProject();
    PsiElement statement = PerlElementFactory.createStatement(project, targetExpressionText);
    if (statement == null) {
      LOG.error("Unable to create a statement for " + targetExpressionText + "; target was " + target);
      return;
    }

    PsiElement enclosingScope = PerlTargetOccurrencesCollector.computeTargetScope(target);
    if (enclosingScope == null) {
      LOG.error("Unable find enclosing scope for " + target);
      return;
    }
    PsiElement commonParent = PsiTreeUtil.findCommonParent(ContainerUtil.map(occurrences, PerlIntroduceTarget::getPlace));
    if (commonParent == null) {
      LOG.error("Unable to find common parent for target's occurrences: " + target);
      return;
    }
    if (!PsiTreeUtil.isAncestor(enclosingScope, commonParent, false)) {
      LOG.error("Common parent is not inside enclosing scope: " +
                "enclosingScope: " + enclosingScope + "; " +
                "commeonParent: " + commonParent);
      return;
    }

    PsiElement anchorElement = occurrences.get(0).getPlace();
    //noinspection ConditionalBreakInInfiniteLoop
    while (true) {
      anchorElement = anchorElement.getParent();
      if (enclosingScope.equals(anchorElement)) {
        LOG.error("Unable to find anchor element (scope reached) for: " + target);
        return;
      }
      if ((anchorElement instanceof PsiPerlStatement || anchorElement instanceof PerlCompound) &&
          PsiTreeUtil.isAncestor(anchorElement.getParent(), commonParent, false)) {
        break;
      }
    }
    List<PsiElement> psiOccurrences = new ArrayList<>();
    PerlVariableDeclarationElement variableDeclaration = introduceVariable(target, statement, occurrences, anchorElement, psiOccurrences);

    if (variableDeclaration == null) {
      return;
    }

    editor.getCaretModel().moveToOffset(variableDeclaration.getTextRange().getStartOffset() + 1);
    new PerlVariableIntroducer(variableDeclaration, editor, psiOccurrences.toArray(PsiElement.EMPTY_ARRAY))
      .performInplaceRefactoring(new LinkedHashSet<>(suggestedNames));
  }

  /**
   * Performs introduction of the {@code statement} before the {@code anchor}
   *
   * @return variable introduced by the statement
   */
  @Nullable
  private PerlVariableDeclarationElement introduceVariable(@NotNull PerlIntroduceTarget target,
                                                           @NotNull PsiElement statement,
                                                           @NotNull List<PerlIntroduceTarget> occurrences,
                                                           @NotNull PsiElement anchor,
                                                           @NotNull List<PsiElement> psiOccurrences) {
    Project project = statement.getProject();
    return WriteCommandAction.writeCommandAction(project).compute(() -> {
      final RefactoringEventData afterData = new RefactoringEventData();
      afterData.addElement(statement);
      project.getMessageBus().syncPublisher(RefactoringEventListener.REFACTORING_EVENT_TOPIC)
        .refactoringStarted(getRefactoringId(), afterData);

      PsiElement introducedStatement = anchor.getParent().addBefore(statement, anchor);

      project.getMessageBus().syncPublisher(RefactoringEventListener.REFACTORING_EVENT_TOPIC)
        .refactoringDone(getRefactoringId(), afterData);

      if (introducedStatement == null) {
        LOG.error("No statement been introduced from " + statement.getText());
        return null;
      }

      if (target.isFullRange()) {
        PsiElement targetPlace = target.getPlace();
        PsiElement targetPlaceParent = targetPlace.getParent();
        if (targetPlaceParent.equals(anchor) && targetPlaceParent instanceof PsiPerlStatementImpl
            && !((PsiPerlStatementImpl)targetPlaceParent).hasModifier()) {
          targetPlaceParent.delete();
        }
      }

      PsiElement assignment = introducedStatement.getFirstChild();
      if (!(assignment instanceof PsiPerlAssignExpr)) {
        LOG.error("Unable to find assignment in " + introducedStatement.getText());
        return null;
      }
      PsiElement declaration = assignment.getFirstChild();
      if (!(declaration instanceof PerlVariableDeclarationExpr)) {
        LOG.error("Unable to find declaration in " + introducedStatement.getText());
        return null;
      }

      List<PsiPerlVariableDeclarationElement> declarations =
        ((PerlVariableDeclarationExpr)declaration).getVariableDeclarationElementList();

      if (declarations.size() != 1) {
        LOG.error("Single variable declaration expected: " + introducedStatement.getText());
      }

      PsiPerlVariableDeclarationElement declarationElement = declarations.get(0);
      PerlVariable declaredVariable = declarationElement.getVariable();

      occurrences.forEach(it -> {
        PsiElement occurrenceElement = it.getPlace();
        if (occurrenceElement != null && occurrenceElement.isValid()) {
          psiOccurrences.add(occurrenceElement.replace(declaredVariable));
        }
      });

      return declarationElement;
    });
  }

  @NotNull
  private String getRefactoringId() {
    return "perl.introduce.variable";
  }

  /**
   * Generates a text for decaration of variable with {@code variableName} expression representing by {@code target}
   */
  @NotNull
  private String createTargetExpressionText(@NotNull String variableName, @NotNull PerlIntroduceTarget target) {
    return "my $" + variableName + " = " + target.getPlace().getText();
  }

  /**
   * @return collection of names suggested for variable representing a {@code target}
   */
  @NotNull
  private List<String> getSuggestedNames(@NotNull PerlIntroduceTarget target) {
    return Collections.singletonList("mysupervariable");
  }

  protected void showErrorMessage(@NotNull Project project, Editor editor, @NotNull String message) {
    CommonRefactoringUtil
      .showErrorHint(project, editor, message, RefactoringBundle.message("introduce.variable.title"), HelpID.INTRODUCE_VARIABLE);
  }

  @Override
  public void invoke(@NotNull Project project, @NotNull PsiElement[] elements, DataContext dataContext) {

  }
}
