// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.perl5.lang.perl.lexer.PerlElementTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.perl5.lang.perl.psi.*;

public class PerlArrayElementImpl extends ASTWrapperPsiElement implements PerlArrayElement {

  public PerlArrayElementImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitArrayElement(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PerlArray getArray() {
    return findChildByClass(PerlArray.class);
  }

  @Override
  @Nullable
  public PerlArrayDereference getArrayDereference() {
    return findChildByClass(PerlArrayDereference.class);
  }

  @Override
  @Nullable
  public PerlArraySlice getArraySlice() {
    return findChildByClass(PerlArraySlice.class);
  }

  @Override
  @Nullable
  public PerlHashSlice getHashSlice() {
    return findChildByClass(PerlHashSlice.class);
  }

  @Override
  @Nullable
  public PerlHashValue getHashValue() {
    return findChildByClass(PerlHashValue.class);
  }

  @Override
  @Nullable
  public PerlScalarValue getScalarValue() {
    return findChildByClass(PerlScalarValue.class);
  }

}
