// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.*;
import com.perl5.lang.perl.psi.mixins.PerlGlobVariableMixin;
import com.perl5.lang.perl.psi.*;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.perl.psi.stubs.globs.PerlGlobStubImpl;

public class PsiPerlGlobVariableImpl extends PerlGlobVariableMixin implements PsiPerlGlobVariable {

  public PsiPerlGlobVariableImpl(ASTNode node) {
    super(node);
  }

  public PsiPerlGlobVariableImpl(PerlGlobStubImpl stub, IStubElementType stubType) {
    super(stub, stubType);
  }

  public void accept(@NotNull PsiPerlVisitor visitor) {
    visitor.visitGlobVariable(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiPerlVisitor) accept((PsiPerlVisitor)visitor);
    else super.accept(visitor);
  }

}
