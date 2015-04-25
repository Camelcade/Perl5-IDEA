// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PerlPerlBlock extends PsiElement {

  @NotNull
  List<PerlCodeLineValid> getCodeLineValidList();

  @NotNull
  List<PerlFunctionDefinition> getFunctionDefinitionList();

  @NotNull
  List<PerlIfBlock> getIfBlockList();

  @NotNull
  List<PerlPackageDefinitionInvalid> getPackageDefinitionInvalidList();

  @NotNull
  List<PerlPackageNamespace> getPackageNamespaceList();

  @NotNull
  List<PerlPackageNoInvalid> getPackageNoInvalidList();

  @NotNull
  List<PerlPackageRequireInvalid> getPackageRequireInvalidList();

  @NotNull
  List<PerlPackageUseInvalid> getPackageUseInvalidList();

  @NotNull
  List<PerlPerlBlock> getPerlBlockList();

  @NotNull
  List<PerlPerlEvalInvalid> getPerlEvalInvalidList();

}
