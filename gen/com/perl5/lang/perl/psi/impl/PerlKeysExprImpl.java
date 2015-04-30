// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.perl5.lang.perl.lexer.PerlElementTypes.*;
import com.perl5.lang.perl.psi.*;

public class PerlKeysExprImpl extends PerlExprImpl implements PerlKeysExpr {

  public PerlKeysExprImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitKeysExpr(this);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PerlKeysArgs getKeysArgs() {
    return findNotNullChildByClass(PerlKeysArgs.class);
  }

}
