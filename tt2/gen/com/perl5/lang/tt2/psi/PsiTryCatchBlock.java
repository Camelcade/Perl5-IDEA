// This is a generated file. Not intended for manual editing.
package com.perl5.lang.tt2.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PsiTryCatchBlock extends PsiElement {

  @NotNull
  List<PsiCatchBranch> getCatchBranchList();

  @NotNull
  PsiEndDirective getEndDirective();

  @Nullable
  PsiFinalBranch getFinalBranch();

  @NotNull
  PsiTryBranch getTryBranch();

}
