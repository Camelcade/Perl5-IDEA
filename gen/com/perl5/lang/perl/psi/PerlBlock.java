// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PerlBlock extends PsiElement {

  @NotNull
  List<PerlExpr> getExprList();

  @Nullable
  PerlLabel getLabel();

  @NotNull
  List<PerlNoStatement> getNoStatementList();

  @NotNull
  List<PerlPackageNamespace> getPackageNamespaceList();

  @NotNull
  List<PerlUseStatement> getUseStatementList();

}
