// This is a generated file. Not intended for manual editing.
package com.perl5.lang.tt2.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PsiIfBlock extends PsiElement {

  @Nullable
  PsiElseBranch getElseBranch();

  @NotNull
  List<PsiElsifBranch> getElsifBranchList();

  @NotNull
  PsiEndDirective getEndDirective();

  @NotNull
  PsiIfBranch getIfBranch();

}
