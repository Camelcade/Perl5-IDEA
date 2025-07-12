// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.perl5.lang.perl.psi.stubs.subsdeclarations.PerlSubDeclarationStub;

public interface PsiPerlSubDeclaration extends PsiPerlStatement, PerlSubDeclarationElement, StubBasedPsiElement<PerlSubDeclarationStub> {

  @Nullable
  PsiPerlAttributes getAttributes();

  @Nullable
  PsiPerlSignatureContent getSignatureContent();

}
