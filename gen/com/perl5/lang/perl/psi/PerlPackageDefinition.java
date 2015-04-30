// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PerlPackageDefinition extends PsiElement {

  @Nullable
  PerlBlock getBlock();

  @NotNull
  List<PerlBlockBlock> getBlockBlockList();

  @NotNull
  List<PerlCodeLine> getCodeLineList();

  @NotNull
  List<PerlEval> getEvalList();

  @NotNull
  List<PerlForBlock> getForBlockList();

  @NotNull
  List<PerlGivenBlock> getGivenBlockList();

  @NotNull
  List<PerlIfBlock> getIfBlockList();

  @NotNull
  PerlPackageBare getPackageBare();

  @NotNull
  List<PerlPackageNo> getPackageNoList();

  @NotNull
  List<PerlPackageRequire> getPackageRequireList();

  @NotNull
  List<PerlPackageUse> getPackageUseList();

  @NotNull
  List<PerlSubBlockNamed> getSubBlockNamedList();

  @NotNull
  List<PerlWhileBlock> getWhileBlockList();

}
