// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PerlForBlockArguments extends PsiElement {

  @NotNull
  List<PerlBlock> getBlockList();

  @NotNull
  List<PerlExpr> getExprList();

  @Nullable
  PerlList getList();

  @Nullable
  PerlVariableDefinition getVariableDefinition();

}
