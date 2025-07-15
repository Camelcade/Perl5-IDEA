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
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlScalarValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.psi.PerlSelfHinterElement;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PerlPackageUtilCore {
  public static final String MAIN_NAMESPACE_NAME = "main";
  public static final String NAMESPACE_SEPARATOR = "::";

  private PerlPackageUtilCore() {
  }

  /**
   * @return the expected value of the {@code $self} passed to the method. This is either context value or value from the self hinter
   */
  public static @NotNull PerlValue getExpectedSelfValue(@NotNull PsiElement psiElement) {
    PsiElement run = psiElement;
    while (true) {
      PerlSelfHinterElement selfHinter = PsiTreeUtil.getParentOfType(run, PerlSelfHinterElement.class);
      if (selfHinter == null) {
        break;
      }
      PerlValue hintedType = selfHinter.getSelfType();
      if (!hintedType.isUnknown()) {
        return hintedType;
      }
      run = selfHinter;
    }
    return PerlScalarValue.create(PerlPackageUtilCore.getContextNamespaceName(psiElement));
  }

  /**
   * Searching of namespace element is in. If no explicit namespaces defined, main is returned
   *
   * @param element psi element to find definition for
   * @return canonical package name
   */
  @Contract("null->null;!null->!null")
  public static String getContextNamespaceName(@Nullable PsiElement element) {
    if (element == null) {
      return null;
    }
    PerlNamespaceDefinitionElement namespaceDefinition = getContainingNamespace(element);

    if (namespaceDefinition != null &&
        namespaceDefinition.getNamespaceName() != null) // checking that definition is valid and got namespace
    {
      String name = namespaceDefinition.getNamespaceName();
      assert name != null;
      return name;
    }

    // default value
    PsiFile file = element.getContainingFile();
    if (file instanceof PerlFileImpl perlFile) {
      PsiElement contextParent = file.getContext();
      PsiElement realParent = file.getParent();

      if (contextParent != null && !contextParent.equals(realParent)) {
        return getContextNamespaceName(contextParent);
      }

      return perlFile.getNamespaceName();
    }
    else {
      return MAIN_NAMESPACE_NAME;
    }
  }

  public static PerlNamespaceDefinitionElement getContainingNamespace(PsiElement element) {
    return PsiTreeUtil.getStubOrPsiParentOfType(element, PerlNamespaceDefinitionElement.class);
  }

  public static @NotNull String join(@NotNull String... chunks) {
    return StringUtil.join(chunks, NAMESPACE_SEPARATOR);
  }
}
