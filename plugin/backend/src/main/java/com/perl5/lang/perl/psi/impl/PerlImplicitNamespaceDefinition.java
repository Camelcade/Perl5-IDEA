/*
 * Copyright 2015-2024 Alexandr Evstigneev
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

package com.perl5.lang.perl.psi.impl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.util.IncorrectOperationException;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionWithIdentifier;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.mro.PerlMroType;
import com.perl5.lang.perl.psi.utils.PerlNamespaceAnnotations;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PerlImplicitNamespaceDefinition extends PerlImplicitElement implements PerlNamespaceDefinitionWithIdentifier {
  private final @NotNull String myNamespaceName;

  public PerlImplicitNamespaceDefinition(@NotNull PsiManager manager,
                                         @NotNull String namespaceName,
                                         @Nullable PsiElement parent) {
    super(manager, parent);
    myNamespaceName = PerlPackageUtil.getCanonicalNamespaceName(namespaceName);
  }

  @Override
  public String toString() {
    return "Implicit namespace: " + getPresentableName();
  }

  @Override
  public @NotNull PerlMroType getMroType() {
    return PerlMroType.DFS;
  }

  @Override
  public String getName() {
    return getNamespaceName();
  }

  @Override
  public @NotNull String getNamespaceName() {
    return myNamespaceName;
  }

  @Override
  public @Nullable Icon getIcon(int flags) {
    return PerlIcons.PACKAGE_GUTTER_ICON;
  }

  @Override
  public @NotNull List<String> getParentNamespacesNames() {
    return Collections.emptyList();
  }

  @Override
  public @Nullable PerlNamespaceAnnotations getAnnotations() {
    return null;
  }

  @Override
  public @NotNull List<String> getEXPORT() {
    return Collections.emptyList();
  }

  @Override
  public @NotNull List<String> getEXPORT_OK() {
    return Collections.emptyList();
  }

  @Override
  public @NotNull Map<String, List<String>> getEXPORT_TAGS() {
    return Collections.emptyMap();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PerlImplicitNamespaceDefinition that)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    return getNamespaceName().equals(that.getNamespaceName());
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + getNamespaceName().hashCode();
    return result;
  }

  @Override
  public @Nullable PsiElement getNameIdentifier() {
    return null;
  }

  @Override
  public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
    return this;
  }

  @Override
  public @Nullable PerlNamespaceElement getNamespaceElement() {
    return null;
  }
}
