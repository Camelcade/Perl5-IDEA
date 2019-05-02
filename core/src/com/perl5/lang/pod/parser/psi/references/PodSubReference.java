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

package com.perl5.lang.pod.parser.psi.references;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiFile;
import com.intellij.psi.ResolveResult;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.psi.PerlSubDeclarationElement;
import com.perl5.lang.perl.psi.PerlSubDefinitionElement;
import com.perl5.lang.perl.psi.references.PerlCachingReference;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlSubUtil;
import com.perl5.lang.pod.parser.psi.impl.PodIdentifierImpl;
import com.perl5.lang.pod.parser.psi.util.PodFileUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PodSubReference extends PerlCachingReference<PodIdentifierImpl> {
  public PodSubReference(PodIdentifierImpl element) {
    super(element, new TextRange(0, element.getTextLength()), true);
  }

  @Override
  public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
    return (PsiElement)myElement.replaceWithText(newElementName);
  }

  @NotNull
  @Override
  protected ResolveResult[] resolveInner(boolean incompleteCode) {
    PsiElement element = getElement();
    final Project project = element.getProject();
    String subName = element.getText();
    if (!StringUtil.isNotEmpty(subName)) {
      return ResolveResult.EMPTY_ARRAY;
    }
    final PsiFile containingFile = element.getContainingFile();
    // this is a bit lame. we could try to detect from perl context. But unsure
    String packageName = PodFileUtil.getPackageName(containingFile);

    List<ResolveResult> results = new ArrayList<>();

    if (StringUtil.isNotEmpty(packageName)) {
      String canonicalName = packageName + PerlPackageUtil.PACKAGE_SEPARATOR + subName;
      for (PerlSubDefinitionElement target : PerlSubUtil.getSubDefinitions(project, canonicalName)) {
        results.add(new PsiElementResolveResult(target));
      }
      for (PerlSubDeclarationElement target : PerlSubUtil.getSubDeclarations(project, canonicalName)) {
        results.add(new PsiElementResolveResult(target));
      }
    }

    if (!results.isEmpty()) {
      return results.toArray(ResolveResult.EMPTY_ARRAY);
    }

    PsiFile targetPerlFile = PodFileUtil.getTargetPerlFile(containingFile);
    if (targetPerlFile == null) {
      return ResolveResult.EMPTY_ARRAY;
    }

    PerlPsiUtil.processSubElements(targetPerlFile, it -> {
      String subBaseName = it.getName();
      if (subBaseName != null && StringUtil.equals(subBaseName, subName)) {
        results.add(new PsiElementResolveResult(it));
      }
      return true;
    });

    return results.toArray(ResolveResult.EMPTY_ARRAY);
  }
}
