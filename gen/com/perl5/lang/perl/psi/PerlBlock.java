// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PerlBlock extends PsiElement {

  @NotNull
  List<PerlBlockCompound> getBlockCompoundList();

  @NotNull
  List<PerlForCompound> getForCompoundList();

  @NotNull
  List<PerlForeachCompound> getForeachCompoundList();

  @NotNull
  List<PerlGivenCompound> getGivenCompoundList();

  @NotNull
  List<PerlIfCompound> getIfCompoundList();

  @Nullable
  PerlLabelDeclaration getLabelDeclaration();

  @NotNull
  List<PerlPackageNamespace> getPackageNamespaceList();

  @NotNull
  List<PerlStatement> getStatementList();

  @NotNull
  List<PerlSubDefinition> getSubDefinitionList();

  @NotNull
  List<PerlUnlessCompound> getUnlessCompoundList();

  @NotNull
  List<PerlUntilCompound> getUntilCompoundList();

  @NotNull
  List<PerlWhileCompound> getWhileCompoundList();

}
