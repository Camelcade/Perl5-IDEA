// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PerlPackageNamespace extends PsiElement {

  @NotNull
  List<PerlCodeLineValid> getCodeLineValidList();

  @NotNull
  List<PerlFunctionDefinition> getFunctionDefinitionList();

  @NotNull
  List<PerlIfBlock> getIfBlockList();

  @NotNull
  PerlPackageDefinition getPackageDefinition();

  @NotNull
  List<PerlPerlBlock> getPerlBlockList();

}
