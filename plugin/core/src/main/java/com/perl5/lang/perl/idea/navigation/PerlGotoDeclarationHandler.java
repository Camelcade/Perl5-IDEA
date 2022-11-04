/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.navigation;

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.utils.PerlResolveUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Objects;


public class PerlGotoDeclarationHandler implements GotoDeclarationHandler {
  @Override
  public @Nullable PsiElement[] getGotoDeclarationTargets(@Nullable PsiElement sourceElement, int offset, Editor editor) {
    if (sourceElement == null) {
      return null;
    }

    int offsetInElement = offset - sourceElement.getNode().getStartOffset();

    ArrayList<PsiElement> result = new ArrayList<>();
    PerlResolveUtil.processResolveTargets((element, reference) -> {
      if (reference.getRangeInElement().contains(offsetInElement)) {
        result.add(element);
      }
      return true;
    }, sourceElement);

    addShadowedVariable(sourceElement, result);
    addStringFileTargets(sourceElement, result);

    return result.isEmpty() ? null : result.toArray(PsiElement.EMPTY_ARRAY);
  }

  /**
   * Adds files references by {@link sourceElement} string content element
   */
  private void addStringFileTargets(@NotNull PsiElement sourceElement, ArrayList<PsiElement> result) {
    if (!(sourceElement instanceof PerlStringContentElement)) {
      return;
    }
    String valueText = ElementManipulators.getValueText(sourceElement);
    if (!PerlString.looksLikePath(valueText)) {
      return;
    }
    String tokenText = valueText.replace("\\", "/").replaceAll("/+", "/");
    Project project = sourceElement.getProject();

    String fileName = Objects.requireNonNull(PerlString.getContentFileName(valueText));
    var psiManager = PsiManager.getInstance(project);

    for (String file : FilenameIndex.getAllFilenames(project)) {
      if (!file.contains(fileName)) {
        continue;
      }
      for (var fileItem : FilenameIndex.getVirtualFilesByName(file, GlobalSearchScope.allScope(project))) {
        String canonicalPath = fileItem.getCanonicalPath();
        if (canonicalPath == null) {
          continue;
        }
        if (canonicalPath.contains(tokenText + ".")) {
          var psiFile = psiManager.findFile(fileItem);
          if (psiFile != null) {
            result.add(0, psiFile);
          }
        }
        else if (canonicalPath.contains(tokenText)) {
          ContainerUtil.addIfNotNull(result, psiManager.findFile(fileItem));
        }
      }
    }
  }

  /**
   * Adds a variable declaration that current one shadows
   */
  private void addShadowedVariable(@NotNull PsiElement sourceElement, ArrayList<PsiElement> result) {
    if (!(sourceElement instanceof PerlVariableNameElement)) {
      return;
    }
    PsiElement variable = sourceElement.getParent();

    if (!(variable instanceof PerlVariable)) {
      return;
    }
    PsiElement variableContainer = variable.getParent();

    if (!(variableContainer instanceof PerlVariableDeclarationElement)) {
      return;
    }
    PerlVariableDeclarationElement shadowedVariable = PerlResolveUtil.getLexicalDeclaration((PerlVariable)variable);
    if (shadowedVariable != null) {
      result.add(shadowedVariable);
    }
  }

  @Override
  public @Nullable String getActionText(@NotNull DataContext context) {
    return null;
  }
}
