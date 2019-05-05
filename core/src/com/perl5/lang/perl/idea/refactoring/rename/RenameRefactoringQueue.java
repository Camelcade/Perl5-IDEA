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

package com.perl5.lang.perl.idea.refactoring.rename;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.refactoring.RefactoringFactory;
import com.intellij.refactoring.RenameRefactoring;


public class RenameRefactoringQueue implements Runnable {
  private final RenameRefactoring[] myRefactoring = {null};
  private Project myProject;

  public RenameRefactoringQueue(Project project) {
    myProject = project;
  }

  public void addElement(PsiElement element, String newName) {
    if (element instanceof PsiNamedElement) {
      if (myRefactoring[0] == null) {
        myRefactoring[0] = RefactoringFactory.getInstance(myProject).createRename(element, newName);
      }
      else {
        myRefactoring[0].addElement(element, newName);
      }
    }
  }

  public void run() {
    if (myRefactoring[0] != null) {
      ApplicationManager.getApplication().invokeLater(myRefactoring[0]::run);
    }
  }

  public Project getProject() {
    return myProject;
  }
}
