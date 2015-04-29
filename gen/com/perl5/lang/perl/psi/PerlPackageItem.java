// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PerlPackageItem extends PsiElement {

  @Nullable
  PerlBlock getBlock();

  @Nullable
  PerlEval getEval();

  @Nullable
  PerlExpr getExpr();

  @Nullable
  PerlFunctionDefinition getFunctionDefinition();

  @Nullable
  PerlIfBlock getIfBlock();

  @Nullable
  PerlPackageNo getPackageNo();

  @Nullable
  PerlPackageRequire getPackageRequire();

  @Nullable
  PerlPackageUse getPackageUse();

}
