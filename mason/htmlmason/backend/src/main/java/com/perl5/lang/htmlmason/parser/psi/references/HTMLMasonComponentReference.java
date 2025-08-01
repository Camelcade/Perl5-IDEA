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

package com.perl5.lang.htmlmason.parser.psi.references;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.htmlmason.HTMLMasonUtil;
import com.perl5.lang.htmlmason.idea.configuration.HTMLMasonSettings;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonCompositeElement;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonNamedElement;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonSubcomponentDefitnition;
import com.perl5.lang.htmlmason.parser.psi.impl.HTMLMasonFileImpl;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.psi.PerlString;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class HTMLMasonComponentReference extends HTMLMasonStringReference {
  public HTMLMasonComponentReference(@NotNull PerlString element, TextRange textRange) {
    super(element, textRange);
  }

  @Override
  public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
    ResolveResult[] results = multiResolve(false);
    String currentContent = ElementManipulators.getValueText(myElement);

    if (results.length == 1 && results[0].getElement() instanceof HTMLMasonFileImpl htmlMasonFile) {
      return handleFilePathChange(htmlMasonFile, currentContent, newElementName);
    }
    else if (HTMLMasonNamedElement.HTML_MASON_IDENTIFIER_PATTERN.matcher(newElementName).matches()) {
      String newContent = newElementName + currentContent.substring(getRangeInElement().getLength());
      return ElementManipulators.handleContentChange(myElement, newContent);
    }
    return myElement;
  }

  private PsiElement handleFilePathChange(HTMLMasonFileImpl target, String currentContent, String newFileName) {
    VirtualFile componentFileDir = HTMLMasonUtil.getComponentVirtualFile(target).getParent();
    VirtualFile componentRoot = null;
    String absPrefix = "";

    if (StringUtil.startsWith(currentContent, "/")) // abs path
    {
      absPrefix = "/";
      componentRoot = HTMLMasonUtil.getComponentRoot(target);
    }
    else // relative path
    {
      PsiFile psiFile = myElement.getContainingFile();

      if (psiFile instanceof HTMLMasonFileImpl htmlMasonFile) {
        VirtualFile virtualFile = HTMLMasonUtil.getComponentVirtualFile(htmlMasonFile);

        if (virtualFile != null) {
          componentRoot = virtualFile.getParent();
        }
      }
    }

    if (componentFileDir != null && componentRoot != null) {
      String newContent = null;

      if (componentRoot.equals(componentFileDir)) {
        newContent = absPrefix + newFileName;
      }
      else {
        String relativePath = VfsUtilCore.getRelativePath(componentFileDir, componentRoot);

        if (relativePath != null) {
          newContent = absPrefix + relativePath + '/' + newFileName;
        }
      }

      if (newContent != null) {
        ElementManipulators.handleContentChange(myElement, newContent + currentContent.substring(getRangeInElement().getLength()));
      }
    }
    return myElement;
  }


  @Override
  public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
    if (element instanceof HTMLMasonFileImpl htmlMasonFile) {
      handleFilePathChange(
        htmlMasonFile,
        ElementManipulators.getValueText(myElement),
        htmlMasonFile.getName()
      );
    }
    return myElement;
  }

  @Override
  protected @NotNull ResolveResult[] resolveInner(boolean incompleteCode) {
    List<ResolveResult> result = null;

    // looking subcomponents
    String nameOrPath = getRangeInElement().substring(getElement().getText());
    final PsiFile file = getElement().getContainingFile();

    if (file instanceof HTMLMasonFileImpl htmlMasonFile) {
      for (HTMLMasonCompositeElement subcontinentDefinition : htmlMasonFile.getSubComponentsDefinitions()) {
        assert subcontinentDefinition instanceof HTMLMasonSubcomponentDefitnition;
        if (StringUtil.equals(((HTMLMasonSubcomponentDefitnition)subcontinentDefinition).getName(), nameOrPath)) {
          if (result == null) {
            result = new ArrayList<>();
          }
          result.add(new PsiElementResolveResult(subcontinentDefinition));
        }
      }
    }

    // looking components
    if (result == null) {
      final Project project = file.getProject();
      VirtualFile componentVirtualFile = null;

      if (StringUtil.startsWith(nameOrPath, "/")) {
        HTMLMasonSettings settings = HTMLMasonSettings.getInstance(project);
        for (VirtualFile componentRoot : PerlProjectManager.getInstance(settings.getProject())
          .getModulesRootsOfType(settings.getSourceRootType())) {
          componentVirtualFile = componentRoot.findFileByRelativePath(nameOrPath);

          if (componentVirtualFile != null) {
            break;
          }
        }
      }
      else // possible relative path
      {
        VirtualFile containingFile = file.getVirtualFile();
        if (containingFile != null) {
          VirtualFile containingDir = containingFile.getParent();
          componentVirtualFile = containingDir.findFileByRelativePath(nameOrPath);
        }
      }

      if (componentVirtualFile != null) {
        PsiFile componentFile = PsiManager.getInstance(project).findFile(componentVirtualFile);
        if (componentFile instanceof HTMLMasonFileImpl) {
          result = new ArrayList<>();
          result.add(new PsiElementResolveResult(componentFile));
        }
      }
    }

    return result == null ? ResolveResult.EMPTY_ARRAY : result.toArray(ResolveResult.EMPTY_ARRAY);
  }
}
