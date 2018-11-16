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

package com.perl5.lang.perl.psi.references;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.ResolveResult;
import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor;
import com.perl5.lang.perl.psi.PerlGlobVariable;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.impl.PerlImplicitVariableDeclaration;
import com.perl5.lang.perl.psi.utils.PerlResolveUtil;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import com.perl5.lang.perl.util.PerlArrayUtil;
import com.perl5.lang.perl.util.PerlHashUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlScalarUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 27.05.2015.
 */
public class PerlVariableReference extends PerlCachingReference<PsiElement> {

  public PerlVariableReference(@NotNull PsiElement element, TextRange textRange) {
    super(element, textRange);
  }

  @NotNull
  @Override
  protected ResolveResult[] resolveInner(boolean incompleteCode) {
    PsiElement elementParent = myElement.getParent();
    assert elementParent instanceof PerlVariable;
    PerlVariable perlVariable = (PerlVariable)elementParent;

    List<ResolveResult> result = new ArrayList<>();

    PerlVariableDeclarationElement lexicalDeclaration = PerlResolveUtil.getLexicalDeclaration(perlVariable);

    if (lexicalDeclaration == null ||
        lexicalDeclaration.isGlobalDeclaration() && !(lexicalDeclaration instanceof PerlImplicitVariableDeclaration)) {
      // not found explicit lexically visible declarations

      // imports
      PerlVariableType actualType = perlVariable.getActualType();
      Project project = perlVariable.getProject();
      PerlNamespaceDefinitionElement namespaceContainer = PerlPackageUtil.getNamespaceContainerForElement(perlVariable);

      if (namespaceContainer != null) // not true if LPE in TemplateToolkit
      {
        String variableName = perlVariable.getName();

        if (actualType == PerlVariableType.SCALAR) {
          for (PerlExportDescriptor importEntry : namespaceContainer.getImportedScalarDescriptors()) {
            if (importEntry.getImportedName().equals(variableName)) {
              for (PerlVariableDeclarationElement targetVariable : PerlScalarUtil
                .getGlobalScalarDefinitions(project, importEntry.getTargetCanonicalName())) {
                result.add(new PsiElementResolveResult(targetVariable));
              }
            }
          }
        }
        else if (actualType == PerlVariableType.ARRAY) {
          for (PerlExportDescriptor importEntry : namespaceContainer.getImportedArrayDescriptors()) {
            if (importEntry.getImportedName().equals(variableName)) {
              for (PerlVariableDeclarationElement targetVariable : PerlArrayUtil
                .getGlobalArrayDefinitions(project, importEntry.getTargetCanonicalName())) {
                result.add(new PsiElementResolveResult(targetVariable));
              }
            }
          }
        }
        else if (actualType == PerlVariableType.HASH) {
          for (PerlExportDescriptor importEntry : namespaceContainer.getImportedHashDescriptors()) {
            if (importEntry.getImportedName().equals(variableName)) {
              for (PerlVariableDeclarationElement targetVariable : PerlHashUtil
                .getGlobalHashDefinitions(project, importEntry.getTargetCanonicalName())) {
                result.add(new PsiElementResolveResult(targetVariable));
              }
            }
          }
        }
      }

      // our variable declaration
      for (PerlGlobVariable glob : perlVariable.getRelatedGlobs()) {
        result.add(new PsiElementResolveResult(glob));
      }

      // globs
      for (PerlVariableDeclarationElement globalDeclaration : perlVariable.getGlobalDeclarations()) {
        result.add(new PsiElementResolveResult(globalDeclaration));
      }
    }
    else {
      result.add(new PsiElementResolveResult(lexicalDeclaration));
    }

    return result.toArray(new ResolveResult[result.size()]);
  }
}
