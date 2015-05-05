// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PerlStatement extends PsiElement {

  @Nullable
  PerlExpr getExpr();

  @Nullable
  PerlForStatementModifier getForStatementModifier();

  @Nullable
  PerlForeachStatementModifier getForeachStatementModifier();

  @Nullable
  PerlIfStatementModifier getIfStatementModifier();

  @Nullable
  PerlNoStatement getNoStatement();

  @Nullable
  PerlSubDeclarationStatement getSubDeclarationStatement();

  @Nullable
  PerlUndefStatement getUndefStatement();

  @Nullable
  PerlUnlessStatementModifier getUnlessStatementModifier();

  @Nullable
  PerlUntilStatementModifier getUntilStatementModifier();

  @Nullable
  PerlUseStatement getUseStatement();

  @Nullable
  PerlWhenStatementModifier getWhenStatementModifier();

  @Nullable
  PerlWhileStatementModifier getWhileStatementModifier();

}
