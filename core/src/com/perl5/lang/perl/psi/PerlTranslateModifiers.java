/*
 * Copyright 2015-2018 Alexandr Evstigneev
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
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.lexer.PerlElementTypesGenerated;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.BitSet;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.REGEX_MODIFIER;
import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.TR_MODIFIERS;

/**
 * Represents modifiers for translitiration expression
 * {@code tr/SEARCHLIST/REPLACEMENTLIST/cdsr}
 */
public class PerlTranslateModifiers {
  private static final int C_MODIFIER = 0;
  private static final int D_MODIFIER = 1;
  private static final int S_MODIFIER = 2;
  private static final int R_MODIFIER = 3;

  @NotNull
  private final BitSet myModifiers = new BitSet();

  private PerlTranslateModifiers() {
  }

  /**
   * c - Complement the SEARCHLIST.
   */
  private void setComplement() {
    myModifiers.set(C_MODIFIER);
  }

  /**
   * d - Delete found but unreplaced characters.
   */
  private void setDeleteUnreplaced() {
    myModifiers.set(D_MODIFIER);
  }

  /**
   * s - Squash duplicate replaced characters.
   */
  private void setSquash() {
    myModifiers.set(S_MODIFIER);
  }

  /**
   * r - Return the modified string and leave the original string untouched.
   */
  private void setPreserveOriginal() {
    myModifiers.set(R_MODIFIER);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PerlTranslateModifiers modifiers = (PerlTranslateModifiers)o;

    return myModifiers.equals(modifiers.myModifiers);
  }

  @Override
  public int hashCode() {
    return myModifiers.hashCode();
  }

  /**
   * @return a model of transliterate modifiers from {@code element} or null if {@code element} is not a {@link PerlElementTypesGenerated#TR_MODIFIERS}
   */
  @Nullable
  @Contract("null -> null")
  public PerlTranslateModifiers create(@Nullable PsiElement element) {
    if (element == null || PsiUtilCore.getElementType(element) != TR_MODIFIERS) {
      return null;
    }
    PerlTranslateModifiers result = new PerlTranslateModifiers();
    PsiElement run = element.getFirstChild();
    while (run != null) {
      if (PsiUtilCore.getElementType(run) == REGEX_MODIFIER) {
        switch (run.getText()) {
          case "c":
            result.setComplement();
            break;
          case "d":
            result.setDeleteUnreplaced();
            break;
          case "s":
            result.setSquash();
            break;
          case "r":
            result.setPreserveOriginal();
            break;
        }
      }
      run = run.getNextSibling();
    }

    return result;
  }
}
