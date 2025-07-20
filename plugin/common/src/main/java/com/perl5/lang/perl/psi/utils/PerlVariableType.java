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

package com.perl5.lang.perl.psi.utils;

import com.perl5.PerlIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public enum PerlVariableType {
  SCALAR('$', PerlIcons.SCALAR_GUTTER_ICON),
  ARRAY('@', PerlIcons.ARRAY_GUTTER_ICON),
  HASH('%', PerlIcons.HASH_GUTTER_ICON),
  GLOB('*', PerlIcons.GLOB_GUTTER_ICON),
  CODE('&', PerlIcons.SUB_GUTTER_ICON);

  private final char mySigil;
  private final @NotNull Icon myIcon;

  PerlVariableType(char sigil, @NotNull Icon icon) {
    mySigil = sigil;
    myIcon = icon;
  }

  public char getSigil() {
    return mySigil;
  }

  public @NotNull String withSigil(@NotNull String variableName) {
    return getSigil() + variableName;
  }

  public @NotNull Icon getIcon() {
    return myIcon;
  }

  public static @NotNull PerlVariableType bySigil(char sigil) {
    PerlVariableType value = bySigilOrNull(sigil);
    if (value != null) {
      return value;
    }
    throw new RuntimeException("Uknown sigil: " + sigil);
  }

  public static @Nullable PerlVariableType bySigilOrNull(char sigil) {
    for (PerlVariableType value : PerlVariableType.values()) {
      if (value.getSigil() == sigil) {
        return value;
      }
    }
    return null;
  }
}
