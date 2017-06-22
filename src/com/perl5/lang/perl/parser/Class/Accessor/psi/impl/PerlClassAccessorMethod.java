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

package com.perl5.lang.perl.parser.Class.Accessor.psi.impl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.Function;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.extensions.PerlRenameUsagesHelper;
import com.perl5.lang.perl.psi.light.PerlDelegatingLightNamedElement;
import com.perl5.lang.perl.psi.light.PerlLightMethodDefinitionElement;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;

public class PerlClassAccessorMethod extends PerlLightMethodDefinitionElement<PerlClassAccessorWrapper>
  implements PerlRenameUsagesHelper {
  public static final String GETTER_PREFIX = "get_";
  public static final String SETTER_PREFIX = "set_";

  public static final Function<String, String> SIMPLE_COMPUTATION = name -> name;
  public static final Function<String, String> GETTER_COMPUTATION = name -> GETTER_PREFIX + name;
  public static final Function<String, String> SETTER_COMPUTATION = name -> SETTER_PREFIX + name;

  public PerlClassAccessorMethod(@NotNull PerlClassAccessorWrapper delegate,
                                 @NotNull String baseName,
                                 @NotNull Function<String, String> nameComputation,
                                 @NotNull IStubElementType elementType,
                                 @NotNull PsiElement nameIdentifier,
                                 @Nullable String packageName,
                                 @Nullable PerlSubAnnotations annotations) {
    super(delegate, nameComputation.fun(baseName), elementType, nameIdentifier, packageName, Collections.emptyList(), annotations);
    withNameComputation(nameComputation);
    if (isFollowBestPractice()) {
      if (hasSetterName()) {
        setSubArguments(Arrays.asList(PerlSubArgument.self(), PerlSubArgument.mandatoryScalar("new_value")));
      }
    }
    else {
      setSubArguments(Arrays.asList(PerlSubArgument.self(), PerlSubArgument.optionalScalar("new_value")));
    }
  }

  public PerlClassAccessorMethod(@NotNull PerlClassAccessorWrapper delegate,
                                 @NotNull PerlSubDefinitionStub stub) {
    super(delegate, stub);
    if (hasGetterName()) {
      withNameComputation(GETTER_COMPUTATION);
    }
    else if (hasSetterName()) {
      withNameComputation(SETTER_COMPUTATION);
    }
    else {
      withNameComputation(SIMPLE_COMPUTATION);
    }
  }

  public boolean isFollowBestPractice() {
    return getDelegate().isFollowBestPractice();
  }

  public boolean hasGetterName() {
    return isFollowBestPractice() && getSubName().startsWith(GETTER_PREFIX);
  }

  public boolean hasSetterName() {
    return isFollowBestPractice() && getSubName().startsWith(SETTER_PREFIX);
  }

  @NotNull
  @Override
  public String getName() {
    return getBaseName();
  }

  @NotNull
  public String getBaseName() {
    String name = getSubName();
    return isFollowBestPractice() ?
           hasGetterName() ? name.substring(GETTER_PREFIX.length()) : name.substring(SETTER_PREFIX.length()) :
           name;
  }

  public String getGetterName() {
    return isFollowBestPractice() && hasSetterName() ? GETTER_PREFIX + getBaseName() : getSubName();
  }

  public String getSetterName() {
    return isFollowBestPractice() && hasGetterName() ? SETTER_PREFIX + getBaseName() : getSubName();
  }

  @Nullable
  public PerlClassAccessorMethod getPairedMethod() {
    if (!isFollowBestPractice()) {
      return null;
    }
    String baseName = getBaseName();
    for (PerlDelegatingLightNamedElement element : getDelegate().getLightElements()) {
      if (element instanceof PerlClassAccessorMethod &&
          baseName.equals(((PerlClassAccessorMethod)element).getBaseName()) &&
          !element.equals(this)
        ) {
        return (PerlClassAccessorMethod)element;
      }
    }
    return null;
  }

  @NotNull
  @Override
  public String getSubstitutedUsageName(@NotNull String newName, @NotNull PsiElement element) {
    if (isFollowBestPractice()) {
      String currentValue = element.getText();
      if (getGetterName().equals(currentValue)) {
        return GETTER_PREFIX + newName;
      }
      else if (getSetterName().equals(currentValue)) {
        return SETTER_PREFIX + newName;
      }
    }
    return newName;
  }

  @Override
  public boolean isInplaceRefactoringAllowed() {
    return !isFollowBestPractice();
  }

  @Override
  public PsiElement setName(@NotNull String newBaseName) throws IncorrectOperationException {
    return isValid() ? super.setName(newBaseName) : null;
  }
}
