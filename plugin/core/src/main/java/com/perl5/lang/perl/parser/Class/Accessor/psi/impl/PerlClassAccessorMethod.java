/*
 * Copyright 2015-2021 Alexandr Evstigneev
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
import com.perl5.lang.perl.extensions.PerlRenameUsagesHelper;
import com.perl5.lang.perl.psi.impl.PerlSubCallElement;
import com.perl5.lang.perl.psi.light.PerlDelegatingLightNamedElement;
import com.perl5.lang.perl.psi.light.PerlLightMethodDefinitionElement;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class PerlClassAccessorMethod extends PerlLightMethodDefinitionElement<PerlSubCallElement>
  implements PerlRenameUsagesHelper {
  @NonNls private static final String GETTER_PREFIX = "get_";
  @NonNls private static final String SETTER_PREFIX = "set_";

  public static final Function<String, String> SIMPLE_COMPUTATION = name -> name;
  public static final Function<String, String> GETTER_COMPUTATION = name -> GETTER_PREFIX + name;
  public static final Function<String, String> SETTER_COMPUTATION = name -> SETTER_PREFIX + name;

  public PerlClassAccessorMethod(@NotNull PerlSubCallElement delegate,
                                 @NotNull String baseName,
                                 @NotNull Function<String, String> nameComputation,
                                 @NotNull IStubElementType<?, ?> elementType,
                                 @NotNull PsiElement nameIdentifier,
                                 @Nullable String packageName,
                                 @Nullable PerlSubAnnotations annotations) {
    super(delegate,
          nameComputation.fun(baseName),
          elementType,
          nameIdentifier,
          packageName,
          Collections.emptyList(),
          annotations
    );
    setNameComputation(nameComputation);
    setSubArgumentsProvider(() -> {
      if (isFollowBestPractice()) {
        if (hasSetterName()) {
          return List.of(PerlSubArgument.self(), PerlSubArgument.mandatoryScalar(PerlSubArgument.NEW_VALUE_VALUE));
        }
      }
      else {
        return List.of(PerlSubArgument.self(), PerlSubArgument.optionalScalar(PerlSubArgument.NEW_VALUE_VALUE));
      }
      return Collections.emptyList();
    });
  }

  public PerlClassAccessorMethod(@NotNull PerlSubCallElement delegate,
                                 @NotNull PerlSubDefinitionStub stub) {
    super(delegate, stub);
    if (hasGetterName()) {
      setNameComputation(GETTER_COMPUTATION);
    }
    else if (hasSetterName()) {
      setNameComputation(SETTER_COMPUTATION);
    }
    else {
      setNameComputation(SIMPLE_COMPUTATION);
    }
  }

  public boolean isFollowBestPractice() {
    return PerlClassAccessorHandler.isFollowBestPractice(getDelegate());
  }

  public boolean hasGetterName() {
    return isFollowBestPractice() && getSubName().startsWith(GETTER_PREFIX);
  }

  public boolean hasSetterName() {
    return isFollowBestPractice() && getSubName().startsWith(SETTER_PREFIX);
  }

  @Override
  public @NotNull String getName() {
    return getBaseName();
  }

  public @NotNull String getBaseName() {
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

  public @Nullable PerlClassAccessorMethod getPairedMethod() {
    if (!isFollowBestPractice()) {
      return null;
    }
    String baseName = getBaseName();
    for (PerlDelegatingLightNamedElement<?> element : getDelegate().getLightElements()) {
      if (element instanceof PerlClassAccessorMethod &&
          baseName.equals(((PerlClassAccessorMethod)element).getBaseName()) &&
          !element.equals(this)
      ) {
        return (PerlClassAccessorMethod)element;
      }
    }
    return null;
  }

  @Override
  public @NotNull String getSubstitutedUsageName(@NotNull String newName, @NotNull PsiElement element) {
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
}
