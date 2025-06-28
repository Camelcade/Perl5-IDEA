// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PsiPerlRegexReplacement extends PerlRegexReplacement {

  @NotNull
  List<PsiPerlDerefExpr> getDerefExprList();

  @NotNull
  List<PsiPerlEscChar> getEscCharList();

  @NotNull
  List<PsiPerlHexChar> getHexCharList();

  @NotNull
  List<PsiPerlOctChar> getOctCharList();

  @NotNull
  List<PsiPerlUnicodeChar> getUnicodeCharList();

}
