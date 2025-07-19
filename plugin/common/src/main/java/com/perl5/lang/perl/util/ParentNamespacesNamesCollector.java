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

package com.perl5.lang.perl.util;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Processor;
import com.intellij.util.SmartList;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageParentsProvider;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageProcessor;
import com.perl5.lang.perl.extensions.parser.PerlRuntimeParentsProvider;
import com.perl5.lang.perl.extensions.parser.PerlRuntimeParentsProviderFromArray;
import com.perl5.lang.perl.idea.PerlElementPatterns;
import com.perl5.lang.perl.psi.PsiPerlArrayVariable;
import com.perl5.lang.perl.psi.PsiPerlAssignExpr;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ParentNamespacesNamesCollector implements Processor<PsiElement> {
  private final @NotNull List<String> myParentNamespaces = new SmartList<>();
  private final @NotNull List<PerlRuntimeParentsProvider> runtimeModifiers = new SmartList<>();
  private final @NotNull String myNamespaceName;

  public ParentNamespacesNamesCollector(@NotNull String namespaceName) {
    myNamespaceName = namespaceName;
  }

  @Override
  public boolean process(PsiElement element) {
    if (element instanceof PerlUseStatementElement useStatementElement) {
      PerlPackageProcessor processor = useStatementElement.getPackageProcessor();
      if (processor instanceof PerlPackageParentsProvider packageParentsProvider) {
        packageParentsProvider.changeParentsList(useStatementElement, myParentNamespaces);
      }
    }
    else if (element instanceof PerlRuntimeParentsProvider runtimeParentsProvider) {
      runtimeModifiers.add(runtimeParentsProvider);
    }
    else if (element.getFirstChild() instanceof PerlRuntimeParentsProvider runtimeParentsProvider) {
      runtimeModifiers.add(runtimeParentsProvider);
    }
    else if (PerlElementPatterns.ISA_ASSIGN_STATEMENT.accepts(element)) {
      PsiElement assignExpr = element.getFirstChild();
      if (assignExpr instanceof PsiPerlAssignExpr) {
        PsiPerlArrayVariable variable = PsiTreeUtil.findChildOfType(element, PsiPerlArrayVariable.class);

        if (variable != null && StringUtil.equals("ISA", variable.getName())) {
          PsiElement rightSide = assignExpr.getLastChild();
          if (rightSide != null) {
            String explicitPackageName = variable.getExplicitNamespaceName();
            if (explicitPackageName == null || StringUtil.equals(explicitPackageName, myNamespaceName)) {
              runtimeModifiers.add(new PerlRuntimeParentsProviderFromArray(assignExpr.getLastChild()));
            }
          }
        }
      }
    }

    return true;
  }

  public void applyRunTimeModifiers() {
    for (PerlRuntimeParentsProvider provider : runtimeModifiers) {
      provider.changeParentsList(myParentNamespaces);
    }
  }

  public @NotNull List<String> getParentNamespaces() {
    return myParentNamespaces;
  }
}
