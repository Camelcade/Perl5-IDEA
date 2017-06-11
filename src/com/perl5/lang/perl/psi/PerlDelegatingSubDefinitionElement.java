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

package com.perl5.lang.perl.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

public class PerlDelegatingSubDefinitionElement extends PerlDelegatingLightNamedElement<PerlPolyNamedElement, PerlSubDefinitionStub>
  implements PerlSubDefinitionElement {
  @Nullable
  private final String myPackageName;

  @NotNull
  private final List<PerlSubArgument> mySubArguments;

  @Nullable
  private final PerlSubAnnotations myAnnotations;

  public PerlDelegatingSubDefinitionElement(@NotNull PerlPolyNamedElement delegate,
                                            @NotNull String subName,
                                            @NotNull IStubElementType elementType,
                                            @NotNull PsiElement nameIdentifier,
                                            @Nullable String packageName,
                                            @NotNull List<PerlSubArgument> subArguments,
                                            @Nullable PerlSubAnnotations annotations) {
    super(delegate, subName, elementType, nameIdentifier);
    myPackageName = packageName;
    mySubArguments = subArguments;
    myAnnotations = annotations;
  }

  public PerlDelegatingSubDefinitionElement(@NotNull PerlSubDefinitionStub stub) {
    super(stub, stub.getSubName());
    myPackageName = stub.getPackageName();
    mySubArguments = stub.getSubArgumentsList();
    myAnnotations = stub.getAnnotations();
  }

  @Nullable
  @Override
  public String getPackageName() {
    return myPackageName;
  }

  @NotNull
  @Override
  public List<PerlSubArgument> getSubArgumentsList() {
    return mySubArguments;
  }

  @Override
  public String getSubName() {
    return getName();
  }

  @Nullable
  @Override
  public PerlSubAnnotations getAnnotations() {
    return myAnnotations;
  }

  @Nullable
  @Override
  public String getExplicitPackageName() {
    return myPackageName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PerlDelegatingSubDefinitionElement)) return false;
    if (!super.equals(o)) return false;

    PerlDelegatingSubDefinitionElement element = (PerlDelegatingSubDefinitionElement)o;

    if (getPackageName() != null ? !getPackageName().equals(element.getPackageName()) : element.getPackageName() != null) return false;
    if (!mySubArguments.equals(element.mySubArguments)) return false;
    return getAnnotations() != null ? getAnnotations().equals(element.getAnnotations()) : element.getAnnotations() == null;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (getPackageName() != null ? getPackageName().hashCode() : 0);
    result = 31 * result + mySubArguments.hashCode();
    result = 31 * result + (getAnnotations() != null ? getAnnotations().hashCode() : 0);
    return result;
  }

  @Nullable
  @Override
  public Icon getIcon(int flags) {
    if (isMethod()) {
      return PerlIcons.METHOD_GUTTER_ICON;
    }
    else {
      return PerlIcons.SUB_GUTTER_ICON;
    }
  }
}
