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

import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.light.LightElement;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.idea.presentations.PerlItemPresentationSimple;
import com.perl5.lang.perl.psi.properties.PerlPackageMember;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PerlImplicitElement extends LightElement implements PerlPackageMember {
  @Nullable
  private final PsiElement myParent;


  public PerlImplicitElement(@NotNull PsiManager manager,
                             @Nullable PsiElement parent) {
    super(manager, PerlLanguage.INSTANCE);
    myParent = parent;
  }

  @Nullable
  public PsiElement getParent() {
    return myParent;
  }


  @Nullable
  protected String getContextPackageName() {
    return PerlPackageUtil.getContextPackageName(getParent());
  }

  @Override
  public int getTextOffset() {
    return getParent() == null ? 0 : getParent().getTextOffset();
  }

  @Override
  public PsiFile getContainingFile() {
    PsiElement parent = getParent();
    return parent == null ? null : parent.getContainingFile();
  }

  @Nullable
  @Override
  public String getPackageName() {
    return getContextPackageName();
  }

  @Nullable
  @Override
  public String getCanonicalName() {
    String packageName = getPackageName();
    if (packageName == null) {
      return null;
    }
    return packageName + PerlPackageUtil.PACKAGE_SEPARATOR + getName();
  }


  @Override
  public String getExplicitPackageName() {
    return null;
  }


  @Override
  public ItemPresentation getPresentation() {
    return new PerlItemPresentationSimple(this, getName());
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PerlImplicitElement element = (PerlImplicitElement)o;

    return myParent != null ? myParent.equals(element.myParent) : element.myParent == null;
  }

  @Override
  public int hashCode() {
    return myParent != null ? myParent.hashCode() : 0;
  }
}
