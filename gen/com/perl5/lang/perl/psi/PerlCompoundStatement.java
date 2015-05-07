// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PerlCompoundStatement extends PsiElement {

  @Nullable
  PerlBlockCompound getBlockCompound();

  @Nullable
  PerlForCompound getForCompound();

  @Nullable
  PerlForeachCompound getForeachCompound();

  @Nullable
  PerlGivenCompound getGivenCompound();

  @Nullable
  PerlIfCompound getIfCompound();

  @Nullable
  PerlLabelDeclaration getLabelDeclaration();

  @Nullable
  PerlUnlessCompound getUnlessCompound();

  @Nullable
  PerlUntilCompound getUntilCompound();

  @Nullable
  PerlWhileCompound getWhileCompound();

}
