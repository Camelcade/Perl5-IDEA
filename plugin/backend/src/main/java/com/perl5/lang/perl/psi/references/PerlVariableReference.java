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

package com.perl5.lang.perl.psi.references;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.ResolveResult;
import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.utils.PerlResolveUtilCore;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import com.perl5.lang.perl.util.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class PerlVariableReference extends PerlCachingReference<PsiElement> {

  public PerlVariableReference(@NotNull PsiElement element, TextRange textRange) {
    super(element, textRange);
  }

  @Override
  protected @NotNull ResolveResult[] resolveInner(boolean incompleteCode) {
    PsiElement elementParent = myElement.getParent();
    assert elementParent instanceof PerlVariable;
    PerlVariable perlVariable = (PerlVariable)elementParent;

    List<PsiElement> result = new ArrayList<>();

    PerlVariableDeclarationElement lexicalDeclaration = PerlResolveUtilCore.getLexicalDeclaration(perlVariable);

    if (lexicalDeclaration != null) {
      result.add(lexicalDeclaration);
      if (lexicalDeclaration.isGlobalDeclaration()) {
        addRelatedGlobs(lexicalDeclaration.getVariable(), result);
        addGlobalVariables(lexicalDeclaration.getVariable(), result);
      }
      return PsiElementResolveResult.createResults(result);
    }

    // not found explicit lexically visible declarations

    // imports
    addImportedVariables(perlVariable, result);
    addRelatedGlobs(perlVariable, result);
    addGlobalVariables(perlVariable, result);

    return PsiElementResolveResult.createResults(result);
  }

  private void addGlobalVariables(@NotNull PerlVariable variable, @NotNull List<? super PsiElement> result) {
    result.addAll(PerlVariableUtil.getGlobalDeclarations(variable));
  }

  private void addRelatedGlobs(@NotNull PerlVariable variable, @NotNull List<? super PsiElement> result) {
    result.addAll(PerlVariableUtil.getRelatedGlobs(variable));
  }

  private void addImportedVariables(@NotNull PerlVariable perlVariable, @NotNull List<? super PsiElement> result) {
    PerlVariableType actualType = perlVariable.getActualType();
    Project project = perlVariable.getProject();
    PerlNamespaceDefinitionElement namespaceContainer = PerlPackageUtil.getNamespaceContainerForElement(perlVariable);

    if (namespaceContainer != null) // not true if LPE in TemplateToolkit
    {
      String variableName = perlVariable.getName();

      if (actualType == PerlVariableType.SCALAR) {
        for (PerlExportDescriptor importEntry : namespaceContainer.getImportedScalarDescriptors()) {
          if (importEntry.getImportedName().equals(variableName)) {
            result.addAll(PerlScalarUtil.getGlobalScalarDefinitions(project, importEntry.getTargetCanonicalName()));
          }
        }
      }
      else if (actualType == PerlVariableType.ARRAY) {
        for (PerlExportDescriptor importEntry : namespaceContainer.getImportedArrayDescriptors()) {
          if (importEntry.getImportedName().equals(variableName)) {
            result.addAll(PerlArrayUtil.getGlobalArrayDefinitions(project, importEntry.getTargetCanonicalName()));
          }
        }
      }
      else if (actualType == PerlVariableType.HASH) {
        for (PerlExportDescriptor importEntry : namespaceContainer.getImportedHashDescriptors()) {
          if (importEntry.getImportedName().equals(variableName)) {
            result.addAll(PerlHashUtil.getGlobalHashDefinitions(project, importEntry.getTargetCanonicalName()));
          }
        }
      }
    }
  }
}
