// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.psi.properties.PerlStatementsContainer;

public interface PsiPerlConditionalBlock extends PerlStatementsContainer {

  @Nullable
  PsiPerlBlock getBlock();

  @NotNull
  PsiPerlConditionExpr getConditionExpr();

}
