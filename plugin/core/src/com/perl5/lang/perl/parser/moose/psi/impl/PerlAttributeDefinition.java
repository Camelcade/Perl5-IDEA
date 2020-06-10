/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package com.perl5.lang.perl.parser.moose.psi.impl;

import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.ElementManipulator;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.Function;
import com.perl5.lang.perl.parser.PerlIdentifierRangeProvider;
import com.perl5.lang.perl.psi.impl.PerlSubCallElement;
import com.perl5.lang.perl.psi.light.PerlLightMethodDefinitionElement;
import com.perl5.lang.perl.psi.properties.PerlPodAwareElement;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PerlAttributeDefinition extends PerlLightMethodDefinitionElement<PerlSubCallElement>
  implements PerlIdentifierRangeProvider, PerlPodAwareElement {
  public static final Function<String, String> DEFAULT_NAME_COMPUTATION =
    name -> StringUtil.startsWith(name, "+") ? name.substring(1) : name;

  public PerlAttributeDefinition(@NotNull PerlSubCallElement wrapper,
                                 @NotNull String name,
                                 @NotNull IStubElementType<?, ?> elementType,
                                 @Nullable PsiElement nameIdentifier,
                                 @Nullable String packageName,
                                 @NotNull List<PerlSubArgument> subArguments,
                                 @Nullable PerlSubAnnotations annotations) {
    super(wrapper, name, elementType, nameIdentifier, packageName, subArguments, annotations);
  }

  public PerlAttributeDefinition(@NotNull PerlSubCallElement wrapper,
                                 @NotNull PerlSubDefinitionStub stub) {
    super(wrapper, stub);
  }

  @NotNull
  @Override
  public Function<String, String> getNameComputation() {
    return DEFAULT_NAME_COMPUTATION;
  }

  @NotNull
  @Override
  public TextRange getRangeInIdentifier() {
    PsiElement nameIdentifier = getNameIdentifier();
    if (nameIdentifier == null) {
      return TextRange.EMPTY_RANGE;
    }
    ElementManipulator<PsiElement> manipulator = ElementManipulators.getNotNullManipulator(nameIdentifier);
    TextRange defaultRange = manipulator.getRangeInElement(nameIdentifier);
    return StringUtil.startsWith(defaultRange.subSequence(nameIdentifier.getNode().getChars()), "+")
           ? TextRange.create(defaultRange.getStartOffset() + 1, defaultRange.getEndOffset())
           : defaultRange;
  }

  @NotNull
  @Override
  public PsiElement getPodAnchor() {
    return getDelegate();
  }
}
