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

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.PERL_REGEX_MODIFIERS;
import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.REGEX_MODIFIER;

/**
 * Represents possible regular expression modifiers
 * {@code m/PATTERN/msixpodualngc}
 * {@code qr/STRING/msixpodualn}
 * {@code s/PATTERN/REPLACEMENT/msixpodualngcer}
 */
public class PerlRegexpModifiers {
  public static final int M_MODIFIER = 0;
  public static final int S_MODIFIER = 1;
  public static final int I_MODIFIER = 2;
  public static final int X_MODIFIER = 3;
  public static final int P_MODIFIER = 4;
  public static final int O_MODIFIER = 5;
  public static final int D_MODIFIER = 6;
  public static final int U_MODIFIER = 7;
  public static final int A_MODIFIER = 8;
  public static final int L_MODIFIER = 9;
  public static final int N_MODIFIER = 10;
  public static final int G_MODIFIER = 11;
  public static final int C_MODIFIER = 12;
  public static final int E_MODIFIER = 13;
  public static final int EE_MODIFIER = 14;
  public static final int R_MODIFIER = 15;
  public static final int XX_MODIFIER = 16;
  public static final int AA_MODIFIER = 17;

  @NotNull
  private final BitSet myModifiers = new BitSet();

  private PerlRegexpModifiers() {
  }

  /**
   * m - Treat string as multiple lines.
   */
  private void setMultiline() {
    myModifiers.set(M_MODIFIER);
  }

  /**
   * s - Treat string as single line. (Make . match a newline)
   */
  private void setSingleLine() {
    myModifiers.set(S_MODIFIER);
  }

  /**
   * i - Do case-insensitive pattern matching.
   */
  private void setCaseInsensitive() {
    myModifiers.set(I_MODIFIER);
  }

  /**
   * x - Use extended regular expressions;
   */
  private void setExtended() {
    myModifiers.set(X_MODIFIER);
  }

  /**
   * xx - Specifying two x's means \t and the SPACE character are
   * ignored within square-bracketed character classes
   */
  private void setSuperExtended() {
    myModifiers.set(XX_MODIFIER);
  }

  /**
   * p - When matching preserve a copy of the matched string so
   * that ${^PREMATCH}, ${^MATCH}, ${^POSTMATCH} will be
   * defined (ignored starting in v5.20) as these are always
   * defined starting in that release
   */
  private void setPreserveCopy() {
    myModifiers.set(P_MODIFIER);
  }

  /**
   * o - Compile pattern only once.
   */
  private void setCompileOnce() {
    myModifiers.set(O_MODIFIER);
  }

  /**
   * a - ASCII-restrict: Use ASCII for \d, \s, \w and [[:posix:]]
   * character classes
   */
  private void setAsciiRestricted() {
    myModifiers.set(A_MODIFIER);
  }

  /**
   * aa - specifying two a's adds the further
   * restriction that no ASCII character will match a
   * non-ASCII one under /i.
   */
  private void setSuperAsciiRestricted() {
    myModifiers.set(AA_MODIFIER);
  }

  /**
   * l - Use the current run-time locale's rules.
   */
  private void setUseRuntimeLocale() {
    myModifiers.set(L_MODIFIER);
  }

  /**
   * u - Use Unicode rules.
   */
  private void setUseUnicode() {
    myModifiers.set(U_MODIFIER);
  }

  /**
   * d - Use Unicode or native charset, as in 5.12 and earlier.
   */
  private void setUseUnicodeOrNativeCharset() {
    myModifiers.set(D_MODIFIER);
  }

  /**
   * n - Non-capture mode. Don't let () fill in $1, $2, etc...
   */
  private void setNonCaptureMode() {
    myModifiers.set(N_MODIFIER);
  }

  /**
   * g - Match globally, i.e., find all occurrences.
   */
  private void setGlobalMatch() {
    myModifiers.set(G_MODIFIER);
  }

  /**
   * c - Do not reset search position on a failed match when /g is in effect.
   */
  private void setContinuationSearch() {
    myModifiers.set(C_MODIFIER);
  }

  /**
   * e - Evaluate the right side as an expression.
   */
  private void setEval() {
    myModifiers.set(E_MODIFIER);
  }

  /**
   * ee - Evaluate the right side as a string then eval the result.
   */
  private void setEvalTwice() {
    myModifiers.set(EE_MODIFIER);
  }

  /**
   * r - Return substitution and leave the original string untouched.
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

    PerlRegexpModifiers modifiers = (PerlRegexpModifiers)o;

    return myModifiers.equals(modifiers.myModifiers);
  }

  @Override
  public int hashCode() {
    return myModifiers.hashCode();
  }

  /**
   * @return a model of regexp modifiers from {@code element} or null if {@code element} is not a {@link PerlElementTypesGenerated#PERL_REGEX_MODIFIERS}
   */
  @Nullable
  @Contract("null -> null")
  public PerlRegexpModifiers create(@Nullable PsiElement element) {
    if (element == null || PsiUtilCore.getElementType(element) != PERL_REGEX_MODIFIERS) {
      return null;
    }
    PerlRegexpModifiers result = new PerlRegexpModifiers();
    PsiElement run = element.getFirstChild();
    while (run != null) {
      if (PsiUtilCore.getElementType(run) == REGEX_MODIFIER) {
        switch (run.getText()) {
          case "m":
            result.setMultiline();
            break;
          case "s":
            result.setSingleLine();
            break;
          case "i":
            result.setCaseInsensitive();
            break;
          case "x":
            result.setExtended();
            break;
          case "p":
            result.setPreserveCopy();
            break;
          case "o":
            result.setCompileOnce();
            break;
          case "d":
            result.setUseUnicodeOrNativeCharset();
            break;
          case "u":
            result.setUseUnicode();
            break;
          case "a":
            result.setAsciiRestricted();
            break;
          case "l":
            result.setUseRuntimeLocale();
            break;
          case "n":
            result.setNonCaptureMode();
            break;
          case "g":
            result.setGlobalMatch();
            break;
          case "c":
            result.setContinuationSearch();
            break;
          case "e":
            result.setEval();
            break;
          case "r":
            result.setPreserveOriginal();
            break;
          case "aa":
            result.setSuperAsciiRestricted();
            break;
          case "ee":
            result.setEvalTwice();
            break;
          case "xx":
            result.setSuperExtended();
            break;
        }
      }
      run = run.getNextSibling();
    }

    return result;
  }
}
