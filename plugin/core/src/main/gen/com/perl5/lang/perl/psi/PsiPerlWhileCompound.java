// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PsiPerlWhileCompound extends PerlWhileUntilCompound {

  @Nullable
  PsiPerlBlock getBlock();

  @Nullable
  PsiPerlConditionExpr getConditionExpr();

  @Nullable
  PsiPerlContinueBlock getContinueBlock();

}
