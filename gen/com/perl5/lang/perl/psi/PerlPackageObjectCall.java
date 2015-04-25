// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PerlPackageObjectCall extends PsiElement {

  @NotNull
  PerlFunctionCall getFunctionCall();

  @Nullable
  PsiElement getPerlPackageBuiltIn();

  @Nullable
  PsiElement getPerlPackageBuiltInDeprecated();

  @Nullable
  PsiElement getPerlPackageBuiltInPragma();

  @Nullable
  PsiElement getPerlPackageUser();

}
