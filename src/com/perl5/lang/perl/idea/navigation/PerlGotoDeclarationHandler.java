/*
 * Copyright 2015-2017 Alexandr Evstigneev
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
import com.intellij.psi.*;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.utils.PerlResolveUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * Created by hurricup on 05.06.2015.
 */
public class PerlGotoDeclarationHandler implements GotoDeclarationHandler {
  @Nullable
  @Override
  public PsiElement[] getGotoDeclarationTargets(@Nullable PsiElement sourceElement, int offset, Editor editor) {
    if (sourceElement == null) {
      return null;
    }

    int offsetInElement = offset - sourceElement.getNode().getStartOffset();

    ArrayList<PsiElement> result = new ArrayList<>();
    for (PsiReference reference : sourceElement.getReferences()) {
      if (reference.getRangeInElement().contains(offsetInElement)) {
        if (reference instanceof PsiPolyVariantReference) {
          for (ResolveResult resolveResult : ((PsiPolyVariantReference)reference).multiResolve(false)) {
            PsiElement element = resolveResult.getElement();
            if (element != null) {
              result.add(element);
            }
          }
        }
        else {
          PsiElement element = reference.resolve();
          if (element != null) {
            result.add(element);
          }
        }
      }
    }

    // add shadowed variables declaration
    if (sourceElement instanceof PerlVariableNameElement) {
      PsiElement variable = sourceElement.getParent();

      if (variable instanceof PerlVariable) {
        PsiElement variableContainer = variable.getParent();

        if (variableContainer instanceof PerlVariableDeclarationElement) {

          PerlVariableDeclarationElement shadowedVariable = PerlResolveUtil.getLexicalDeclaration((PerlVariable)variable);
          if (shadowedVariable != null && !result.contains(shadowedVariable)) {
            result.add(shadowedVariable);
          }
        }
      }
    }
    // additional procesing for subname
    else if (sourceElement instanceof PerlSubNameElement) {
      PsiElement elementParent = sourceElement.getParent();

      // suppress declaration if there is a definition and declaration
      if (result.size() == 2 &&
          !(elementParent instanceof PerlSubDefinitionElement || elementParent instanceof PerlSubDeclarationElement)) {
        if (result.get(0).getOriginalElement() instanceof PerlSubDeclarationElement &&
            result.get(1).getOriginalElement() instanceof PerlSubDefinitionElement) {
          result.remove(0);
        }
      }
    }
    // string content to file jump fixme change to string
    else if (sourceElement instanceof PerlStringContentElement && ((PerlStringContentElement)sourceElement).looksLikePath()) {
      String tokenText = ((PerlStringContentElement)sourceElement).getContinuosText().replaceAll("\\\\", "/").replaceAll("/+", "/");
      Project project = sourceElement.getProject();

      String fileName = ((PerlStringContentElement)sourceElement).getContentFileName();


      for (String file : FilenameIndex.getAllFilenames(project)) {
        if (file != null && file.contains(fileName)) {
          // fixme somehow if includeDirectories is true - no files found
          for (PsiFileSystemItem fileItem : FilenameIndex.getFilesByName(project, file, GlobalSearchScope.allScope(project))) {
            String canonicalPath = fileItem.getVirtualFile().getCanonicalPath();
            if (canonicalPath != null) {
              if (canonicalPath.contains(tokenText + "."))    // higer priority
              {
                result.add(0, fileItem);
              }
              else if (canonicalPath.contains(tokenText)) {
                result.add(fileItem);
              }
            }
          }
          for (PsiFileSystemItem fileItem : FilenameIndex.getFilesByName(project, file, GlobalSearchScope.allScope(project), true)) {
            String canonicalPath = fileItem.getVirtualFile().getCanonicalPath();
            if (canonicalPath != null) {
              if (canonicalPath.contains(tokenText)) {
                result.add(fileItem);
              }
            }
          }
        }
      }
    }

    return result.isEmpty() ? null : result.toArray(new PsiElement[result.size()]);
  }

  @Nullable
  @Override
  public String getActionText(DataContext context) {
    return null;
  }
}
