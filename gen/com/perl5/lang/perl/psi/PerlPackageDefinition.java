// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PerlPackageDefinition extends PsiElement {

  @Nullable
  PerlBlock getBlock();

  @NotNull
  PerlPackageBare getPackageBare();

  @NotNull
  List<PerlPackageItem> getPackageItemList();

}
