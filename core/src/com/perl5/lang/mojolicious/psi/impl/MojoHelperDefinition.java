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

package com.perl5.lang.mojolicious.psi.impl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.psi.PsiPerlBlock;
import com.perl5.lang.perl.psi.light.PerlLightMethodDefinitionElement;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

public class MojoHelperDefinition extends PerlLightMethodDefinitionElement<MojoHelperWrapper> {

  public MojoHelperDefinition(@NotNull MojoHelperWrapper wrapper,
                              @NotNull String subName,
                              @NotNull IStubElementType elementType,
                              @NotNull PsiElement nameIdentifier,
                              @Nullable String packageName,
                              @NotNull List<PerlSubArgument> subArguments,
                              @Nullable PerlSubAnnotations annotations) {
    super(wrapper, subName, elementType, nameIdentifier, packageName, subArguments, annotations);
  }

  public MojoHelperDefinition(@NotNull MojoHelperWrapper wrapper,
                              @NotNull String subName,
                              @NotNull IStubElementType elementType,
                              @NotNull PsiElement nameIdentifier,
                              @Nullable String packageName,
                              @NotNull List<PerlSubArgument> subArguments,
                              @Nullable PerlSubAnnotations annotations, @Nullable PsiPerlBlock subDefinitionBody) {
    super(wrapper, subName, elementType, nameIdentifier, packageName, subArguments, annotations, subDefinitionBody);
  }

  public MojoHelperDefinition(@NotNull MojoHelperWrapper wrapper,
                              @NotNull PerlSubDefinitionStub stub) {
    super(wrapper, stub);
  }

  @Override
  public boolean isStatic() {
    return true;
  }

  @Nullable
  @Override
  public Icon getIcon(int flags) {
    return PerlIcons.MOJO_FILE;
  }
}
