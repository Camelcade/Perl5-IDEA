// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PerlFileItem extends PsiElement {

  @Nullable
  PerlBlockBlock getBlockBlock();

  @Nullable
  PerlCodeLine getCodeLine();

  @Nullable
  PerlEval getEval();

  @Nullable
  PerlExpr getExpr();

  @Nullable
  PerlForBlock getForBlock();

  @Nullable
  PerlGivenBlock getGivenBlock();

  @Nullable
  PerlIfBlock getIfBlock();

  @Nullable
  PerlIfPostfix getIfPostfix();

  @Nullable
  PerlPackageDefinition getPackageDefinition();

  @Nullable
  PerlPackageNo getPackageNo();

  @Nullable
  PerlPackageRequire getPackageRequire();

  @Nullable
  PerlPackageUse getPackageUse();

  @Nullable
  PerlSubBlockNamed getSubBlockNamed();

  @Nullable
  PerlWhileBlock getWhileBlock();

}
