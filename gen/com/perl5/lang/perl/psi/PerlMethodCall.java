// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PerlMethodCall extends PsiElement {

  @Nullable
  PerlObjectMethodCall getObjectMethodCall();

  @Nullable
  PerlPackageFunctionCall getPackageFunctionCall();

  @Nullable
  PerlPackageMethodCall getPackageMethodCall();

}
