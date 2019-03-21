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
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pass;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.refactoring.HelpID;
import com.intellij.refactoring.IntroduceTargetChooser;
import com.intellij.refactoring.RefactoringActionHandler;
import com.intellij.refactoring.RefactoringBundle;
import com.intellij.refactoring.introduce.inplace.OccurrencesChooser;
import com.intellij.refactoring.util.CommonRefactoringUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.refactoring.introduce.occurrence.PerlTargetOccurrencesCollector;
import com.perl5.lang.perl.idea.refactoring.introduce.target.PerlTargetsCollector;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PerlIntroduceVariableHandler implements RefactoringActionHandler {
  private static final Logger LOG = Logger.getInstance(PerlIntroduceVariableHandler.class);

  @Override
  public void invoke(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file, DataContext dataContext) {
    List<PerlIntroduceTarget> targets = PerlTargetsCollector.getIntroduceTargets(editor, file);
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
    introduceTarget(target, allOccurrences, OccurrencesChooser.ReplaceChoice.NO, editor, file, dataContext);
  }

  private void introduceTarget(@NotNull PerlIntroduceTarget target,
                               @NotNull List<PerlIntroduceTarget> allTargets,
                               @NotNull OccurrencesChooser.ReplaceChoice replaceChoice,
                               @NotNull Editor editor,
                               @NotNull PsiFile file,
                               DataContext dataContext) {
    LOG.warn("Introducing " + target.render());
  }


  protected void showErrorMessage(@NotNull Project project, Editor editor, @NotNull String message) {
    CommonRefactoringUtil
      .showErrorHint(project, editor, message, RefactoringBundle.message("introduce.variable.title"), HelpID.INTRODUCE_VARIABLE);
  }

  @Override
  public void invoke(@NotNull Project project, @NotNull PsiElement[] elements, DataContext dataContext) {

  }
}
