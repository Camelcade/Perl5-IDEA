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

package com.perl5.lang.perl.parser.constant.psi.light;

import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement;
import com.perl5.lang.perl.psi.light.PerlLightSubDefinitionElement;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

public class PerlLightConstantDefinitionElement extends PerlLightSubDefinitionElement<PerlUseStatementElement> {
  public PerlLightConstantDefinitionElement(@NotNull PerlUseStatementElement wrapper,
                                            @NotNull String subName,
                                            @NotNull IStubElementType<?, ?> elementType,
                                            @NotNull PsiElement nameIdentifier,
                                            @Nullable String packageName,
                                            @NotNull List<PerlSubArgument> subArguments,
                                            @Nullable PerlSubAnnotations annotations,
                                            @NotNull AtomicNotNullLazyValue<? extends PerlValue> returnValueFromCodeProvider) {
    super(wrapper, subName, elementType, nameIdentifier, packageName, subArguments, annotations, returnValueFromCodeProvider, null);
  }

  public PerlLightConstantDefinitionElement(@NotNull PerlUseStatementElement delegate, @NotNull PerlSubDefinitionStub stub) {
    super(delegate, stub);
  }

  @Override
  public @Nullable Icon getIcon(int flags) {
    return PerlIcons.CONSTANT_GUTTER_ICON;
  }

  @Override
  public boolean isMethod() {
    return true;
  }

  @Override
  public boolean isStatic() {
    return true;
  }
}
