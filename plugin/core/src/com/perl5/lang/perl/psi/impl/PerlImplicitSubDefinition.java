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

package com.perl5.lang.perl.psi.impl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.ObjectUtils;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues;
import com.perl5.lang.perl.psi.PerlSubDefinitionElement;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

public class PerlImplicitSubDefinition extends PerlImplicitElement implements PerlSubDefinitionElement {
  @NotNull
  private final String mySubName;
  @NotNull
  private final String myNamespaceName;
  @NotNull
  private final List<PerlSubArgument> mySubArguments;
  @NotNull
  private final PerlValue myReturnValue;

  final boolean myIsAnonymous;

  public PerlImplicitSubDefinition(@NotNull PsiManager manager,
                                   @NotNull String subName,
                                   @NotNull String namespaceName,
                                   @NotNull List<PerlSubArgument> argumentList) {
    this(manager, subName, namespaceName, argumentList, null, null, false);
  }

  public PerlImplicitSubDefinition(@NotNull PsiManager manager,
                                   @NotNull String subName,
                                   @NotNull String namespaceName,
                                   @NotNull List<PerlSubArgument> argumentList,
                                   @Nullable PerlValue returnValue,
                                   boolean isAnonymous) {
    this(manager, subName, namespaceName, argumentList, null, returnValue, isAnonymous);
  }

  public PerlImplicitSubDefinition(@NotNull PsiManager manager,
                                   @NotNull String subName,
                                   @NotNull String namespaceName,
                                   @NotNull List<PerlSubArgument> argumentList,
                                   @Nullable PsiElement parent,
                                   @Nullable PerlValue returnValue,
                                   boolean isAnonymous) {
    super(manager, parent);
    mySubName = subName;
    myNamespaceName = namespaceName;
    mySubArguments = argumentList;
    myReturnValue = ObjectUtils.notNull(returnValue, PerlValues.UNKNOWN_VALUE);
    myIsAnonymous = isAnonymous;
  }

  public boolean isAnonymous() {
    return myIsAnonymous;
  }

  @NotNull
  @Override
  public PerlValue getReturnValueFromCode() {
    return myReturnValue;
  }

  @Override
  public String toString() {
    return "Implicit sub: " + getPresentableName();
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

  @NotNull
  @Override
  public String getExplicitNamespaceName() {
    return myNamespaceName;
  }

  @Override
  @NotNull
  public String getNamespaceName() {
    return getExplicitNamespaceName();
  }

  @NotNull
  @Override
  public List<PerlSubArgument> getSubArgumentsList() {
    return mySubArguments;
  }

  @NotNull
  @Override
  public String getSubName() {
    return myIsAnonymous ? "ANON-" + mySubName : mySubName;
  }

  @Override
  public String getName() {
    return getSubName();
  }

  @Nullable
  @Override
  public PerlSubAnnotations getAnnotations() {
    return null;
  }

  @Nullable
  @Override
  public Icon getIcon(int flags) {
    return PerlIcons.SUB_GUTTER_ICON;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    PerlImplicitSubDefinition that = (PerlImplicitSubDefinition)o;

    if (myIsAnonymous != that.myIsAnonymous) {
      return false;
    }
    if (!mySubName.equals(that.mySubName)) {
      return false;
    }
    if (!myNamespaceName.equals(that.myNamespaceName)) {
      return false;
    }
    if (!mySubArguments.equals(that.mySubArguments)) {
      return false;
    }
    return myReturnValue.equals(that.myReturnValue);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + mySubName.hashCode();
    result = 31 * result + myNamespaceName.hashCode();
    result = 31 * result + mySubArguments.hashCode();
    result = 31 * result + myReturnValue.hashCode();
    result = 31 * result + (myIsAnonymous ? 1 : 0);
    return result;
  }

  @Nullable
  @Override
  public PsiElement getControlFlowElement() {
    return null;
  }
}
