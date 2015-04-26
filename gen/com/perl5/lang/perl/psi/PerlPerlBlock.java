// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PerlPerlBlock extends PsiElement {

  @NotNull
  List<PerlCodeLine> getCodeLineList();

  @NotNull
  List<PerlFunctionDefinitionAnon> getFunctionDefinitionAnonList();

  @NotNull
  List<PerlFunctionDefinitionNamed> getFunctionDefinitionNamedList();

  @NotNull
  List<PerlIfBlock> getIfBlockList();

  @NotNull
  List<PerlPackageDefinitionInvalid> getPackageDefinitionInvalidList();

  @NotNull
  List<PerlPackageNamespace> getPackageNamespaceList();

  @NotNull
  List<PerlPackageNo> getPackageNoList();

  @NotNull
  List<PerlPackageNoInvalid> getPackageNoInvalidList();

  @NotNull
  List<PerlPackageRequire> getPackageRequireList();

  @NotNull
  List<PerlPackageRequireInvalid> getPackageRequireInvalidList();

  @NotNull
  List<PerlPackageUse> getPackageUseList();

  @NotNull
  List<PerlPackageUseInvalid> getPackageUseInvalidList();

  @NotNull
  List<PerlPerlBlock> getPerlBlockList();

  @NotNull
  List<PerlPerlEval> getPerlEvalList();

  @NotNull
  List<PerlPerlEvalInvalid> getPerlEvalInvalidList();

}
