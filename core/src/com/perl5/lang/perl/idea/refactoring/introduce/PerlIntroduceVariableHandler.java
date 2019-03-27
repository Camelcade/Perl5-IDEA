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

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.TransactionGuard;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.Pass;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.SmartPointerManager;
import com.intellij.psi.SmartPsiElementPointer;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.*;
import com.intellij.refactoring.introduce.inplace.OccurrencesChooser;
import com.intellij.refactoring.listeners.RefactoringEventData;
import com.intellij.refactoring.listeners.RefactoringEventListener;
import com.intellij.refactoring.rename.PsiElementRenameHandler;
import com.intellij.refactoring.util.CommonRefactoringUtil;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.refactoring.introduce.occurrence.PerlIntroduceTargetOccurrencesCollector;
import com.perl5.lang.perl.idea.refactoring.introduce.target.PerlIntroduceTargetsHandler;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import com.perl5.lang.perl.psi.impl.PsiPerlStatementImpl;
import com.perl5.lang.perl.psi.properties.PerlCompound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class PerlIntroduceVariableHandler implements RefactoringActionHandler {
  private static final Logger LOG = Logger.getInstance(PerlIntroduceVariableHandler.class);

  @Override
  public void invoke(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file, DataContext dataContext) {
    List<PerlIntroduceTarget> targets = PerlIntroduceTargetsHandler.getIntroduceTargets(editor, file);
    if (targets.isEmpty()) {
      showErrorMessage(project, editor, RefactoringBundle.getCannotRefactorMessage(PerlBundle.message("perl.introduce.no.target")));
      return;
    }

    if (targets.size() > 1) {
      if (ApplicationManager.getApplication().isUnitTestMode()) {
        selectOccurrences(targets.get(targets.size() - 1), editor, file, dataContext);
      }
      else {
        IntroduceTargetChooser.showIntroduceTargetChooser(
          editor,
          targets,
          new Pass<PerlIntroduceTarget>() {
            @Override
            public void pass(PerlIntroduceTarget target) {
              selectOccurrences(target, editor, file, dataContext);
            }
          },
          PerlBundle.message("perl.introduce.expressions"),
          -1);
      }
    }
    else {
      selectOccurrences(targets.iterator().next(), editor, file, dataContext);
    }
  }

  /**
   * Collects occurrences of selected {@code target}, suggest to replace all or one, and going on
   */
  private void selectOccurrences(@NotNull PerlIntroduceTarget target,
                                 @NotNull Editor editor,
                                 @NotNull PsiFile file,
                                 DataContext dataContext) {
    List<PerlIntroduceTarget> allOccurrences = PerlIntroduceTargetOccurrencesCollector.collect(target);
    if (allOccurrences.size() > 1) {
      if (ApplicationManager.getApplication().isUnitTestMode()) {
        performIntroduce(target, allOccurrences, OccurrencesChooser.ReplaceChoice.ALL, editor, file, dataContext);
      }
      else {
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
              performIntroduce(target, allOccurrences, replaceChoice, editor, file, dataContext);
            }
          }
        );
      }
    }
    else {
      performIntroduce(target, allOccurrences, OccurrencesChooser.ReplaceChoice.NO, editor, file, dataContext);
    }
  }

  private void performIntroduce(@NotNull PerlIntroduceTarget target,
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

    List<String> suggestedNames = PerlIntroduceTargetsHandler.getSuggestedNames(target);
    if (suggestedNames.isEmpty()) {
      LOG.error("Suggested names list was empty for " + target);
      return;
    }
    String variableName = suggestedNames.get(0);

    Project project = file.getProject();
    Pair<PsiElement, PsiElement> declaration = PerlIntroduceTargetsHandler.createTargetDeclarationStatement(project, target, variableName);
    if (declaration == null) {
      return;
    }

    PsiElement anchorElement = computeAnchor(target, occurrences);
    if (anchorElement == null) {
      return;
    }

    List<SmartPsiElementPointer<PsiElement>> psiOccurrencesPointers = new ArrayList<>();
    PerlVariableDeclarationElement variableDeclaration =
      introduceVariable(target, declaration, occurrences, anchorElement, psiOccurrencesPointers);

    if (variableDeclaration == null) {
      LOG.error("No variable declaration created");
      return;
    }

    editor.getCaretModel().moveToOffset(variableDeclaration.getTextRange().getStartOffset() + 1);

    List<PsiElement> psiOccurrences = psiOccurrencesPointers.stream()
      .map(SmartPsiElementPointer::getElement)
      .filter(Objects::nonNull)
      .collect(Collectors.toList());

    PerlVariableIntroducer inplaceIntroducer = new PerlVariableIntroducer(
      variableDeclaration, editor, psiOccurrences.toArray(PsiElement.EMPTY_ARRAY));
    if (inplaceIntroducer.performInplaceRefactoring(new LinkedHashSet<>(suggestedNames))) {
      return;
    }
    inplaceIntroducer.finish(false);
    performDialogRename(project, variableDeclaration);
  }

  private void performDialogRename(@NotNull Project project, @NotNull PerlVariableDeclarationElement variableDeclaration) {
    DataContext renamingContext = DataManager.getInstance().getDataContext();
    if (ApplicationManager.getApplication().isUnitTestMode()) {
      DataContext originalContext = renamingContext;
      renamingContext = dataId -> PsiElementRenameHandler.DEFAULT_NAME.getName().equals(dataId)
                                  ? "dialog_test_name"
                                  : originalContext.getData(dataId);
    }
    RefactoringActionHandler handler = RefactoringActionHandlerFactory.getInstance().createRenameHandler();
    DataContext effectiveContext = renamingContext;
    TransactionGuard.submitTransaction(project, () -> handler.invoke(project, new PsiElement[]{variableDeclaration}, effectiveContext));
  }

  /**
   * @return anchor statement to add a declaration statement before. Or null if anchor can't be found
   */
  @Nullable
  private PsiElement computeAnchor(@NotNull PerlIntroduceTarget target,
                                   @NotNull List<PerlIntroduceTarget> occurrences) {
    PsiElement enclosingScope = PerlIntroduceTargetOccurrencesCollector.computeTargetScope(target);
    if (enclosingScope == null) {
      LOG.error("Unable find enclosing scope for " + target);
      return null;
    }
    PsiElement commonParent = PsiTreeUtil.findCommonParent(ContainerUtil.map(occurrences, PerlIntroduceTarget::getPlace));
    if (commonParent == null) {
      LOG.error("Unable to find common parent for target's occurrences: " + target);
      return null;
    }
    if (!PsiTreeUtil.isAncestor(enclosingScope, commonParent, false)) {
      LOG.error("Common parent is not inside enclosing scope: " +
                "enclosingScope: " + enclosingScope + "; " +
                "commeonParent: " + commonParent);
      return null;
    }

    PsiElement anchorElement = Objects.requireNonNull(occurrences.get(0).getPlace());
    while (true) {
      if (anchorElement instanceof PerlHeredocElementImpl) {
        anchorElement = ((PerlHeredocElementImpl)anchorElement).getHeredocOpener();
        if (anchorElement == null) {
          LOG.error("Unable to find here-doc opener");
          return null;
        }
      }

      if (enclosingScope.equals(anchorElement)) {
        LOG.error("Unable to find anchor element (scope reached) for: " + target);
        return null;
      }
      if ((anchorElement instanceof PsiPerlStatement || anchorElement instanceof PerlCompound) &&
          PsiTreeUtil.isAncestor(anchorElement.getParent(), commonParent, false)) {
        break;
      }
      anchorElement = anchorElement.getParent();
    }
    return anchorElement;
  }

  /**
   * Performs introduction of the {@code declarationBlock} before the {@code anchor}
   *
   * @return variable introduced by the statement
   */
  @Nullable
  private PerlVariableDeclarationElement introduceVariable(@NotNull PerlIntroduceTarget target,
                                                           @NotNull Pair<PsiElement, PsiElement> declarationBlock,
                                                           @NotNull List<PerlIntroduceTarget> occurrences,
                                                           @NotNull PsiElement anchor,
                                                           @NotNull List<SmartPsiElementPointer<PsiElement>> psiOccurrences) {
    Project project = declarationBlock.first.getProject();
    return WriteCommandAction.writeCommandAction(project).compute(() -> {
      PsiFile containingFile = anchor.getContainingFile();
      final RefactoringEventData afterData = new RefactoringEventData();
      afterData.addElement(declarationBlock.first);
      project.getMessageBus().syncPublisher(RefactoringEventListener.REFACTORING_EVENT_TOPIC)
        .refactoringStarted(getRefactoringId(), afterData);

      PsiElement introducedStatement = anchor.getParent().addRangeBefore(declarationBlock.first, declarationBlock.second, anchor);

      project.getMessageBus().syncPublisher(RefactoringEventListener.REFACTORING_EVENT_TOPIC)
        .refactoringDone(getRefactoringId(), afterData);

      if (introducedStatement == null) {
        LOG.error("No statement been introduced from " + declarationBlock.first.getContainingFile().getText());
        return null;
      }

      if (target.isFullRange()) {
        PsiElement targetPlace = Objects.requireNonNull(target.getPlace());
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
      PsiElement variableDeclaration = assignment.getFirstChild();
      if (!(variableDeclaration instanceof PerlVariableDeclarationExpr)) {
        LOG.error("Unable to find declarationBlock in " + introducedStatement.getText());
        return null;
      }

      List<PsiPerlVariableDeclarationElement> declarations =
        ((PerlVariableDeclarationExpr)variableDeclaration).getVariableDeclarationElementList();

      if (declarations.size() != 1) {
        LOG.error("Single variable declarationBlock expected: " + introducedStatement.getText());
        return null;
      }

      PsiPerlVariableDeclarationElement declarationElement = declarations.get(0);
      PerlVariable declaredVariable = declarationElement.getVariable();

      Map<PsiElement, List<PerlIntroduceTarget>> occurrencesMap = new HashMap<>();
      occurrences.forEach(it -> occurrencesMap.computeIfAbsent(it.getPlace(), __ -> new ArrayList<>()).add(it));

      occurrencesMap.keySet().forEach(it -> psiOccurrences.addAll(ContainerUtil.map(
        PerlIntroduceTargetsHandler.replaceOccurences(occurrencesMap.get(it), declaredVariable),
        SmartPointerManager::createPointer))
      );

      return SmartPointerManager.createPointer(declarationElement);
    }).getElement();
  }

  @NotNull
  private String getRefactoringId() {
    return "perl.introduce.variable";
  }

  protected void showErrorMessage(@NotNull Project project, Editor editor, @NotNull String message) {
    CommonRefactoringUtil
      .showErrorHint(project, editor, message, RefactoringBundle.message("introduce.variable.title"), HelpID.INTRODUCE_VARIABLE);
  }

  @Override
  public void invoke(@NotNull Project project, @NotNull PsiElement[] elements, DataContext dataContext) {

  }
}
