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
  @NotNull
  private final String myPackageName;

  public PerlImplicitNamespaceDefinition(@NotNull PsiManager manager,
                                         @NotNull String packageName,
                                         @Nullable PsiElement parent) {
    super(manager, parent);
    myPackageName = PerlPackageUtil.getCanonicalPackageName(packageName);
  }

  @Override
  public String toString() {
    return "Implicit namespace: " + getPresentableName();
  }

  @NotNull
  @Override
  public PerlMroType getMroType() {
    return PerlMroType.DFS;
  }

  @Override
  public String getName() {
    return getPackageName();
  }

  @NotNull
  @Override
  public String getPackageName() {
    return myPackageName;
  }

  @Nullable
  @Override
  public Icon getIcon(int flags) {
    return PerlIcons.PACKAGE_GUTTER_ICON;
  }

  @NotNull
  @Override
  public List<String> getParentNamespacesNames() {
    return Collections.emptyList();
  }

  @Nullable
  @Override
  public PerlNamespaceAnnotations getAnnotations() {
    return null;
  }

  @NotNull
  @Override
  public List<String> getEXPORT() {
    return Collections.emptyList();
  }

  @NotNull
  @Override
  public List<String> getEXPORT_OK() {
    return Collections.emptyList();
  }

  @NotNull
  @Override
  public Map<String, List<String>> getEXPORT_TAGS() {
    return Collections.emptyMap();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PerlImplicitNamespaceDefinition)) return false;
    if (!super.equals(o)) return false;

    PerlImplicitNamespaceDefinition that = (PerlImplicitNamespaceDefinition)o;

    return getPackageName() != null ? getPackageName().equals(that.getPackageName()) : that.getPackageName() == null;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (getPackageName() != null ? getPackageName().hashCode() : 0);
    return result;
  }

  @Nullable
  @Override
  public PsiElement getNameIdentifier() {
    return null;
  }

  @Override
  public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
    return this;
  }

  @Nullable
  @Override
  public PerlNamespaceElement getNamespaceElement() {
    return null;
  }
}
