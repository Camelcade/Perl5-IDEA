// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.*;
import com.perl5.lang.perl.psi.*;

public class PsiPerlCommentAnnotationImpl extends PerlCompositeElementImpl implements PsiPerlCommentAnnotation {

  public PsiPerlCommentAnnotationImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiPerlVisitor visitor) {
    visitor.visitCommentAnnotation(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiPerlVisitor) accept((PsiPerlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiPerlAnnotationAbstract getAnnotationAbstract() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlAnnotationAbstract.class);
  }

  @Override
  @Nullable
  public PsiPerlAnnotationDeprecated getAnnotationDeprecated() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlAnnotationDeprecated.class);
  }

  @Override
  @Nullable
  public PsiPerlAnnotationInject getAnnotationInject() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlAnnotationInject.class);
  }

  @Override
  @Nullable
  public PsiPerlAnnotationMethod getAnnotationMethod() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlAnnotationMethod.class);
  }

  @Override
  @Nullable
  public PsiPerlAnnotationNoInject getAnnotationNoInject() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlAnnotationNoInject.class);
  }

  @Override
  @Nullable
  public PsiPerlAnnotationNoinspection getAnnotationNoinspection() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlAnnotationNoinspection.class);
  }

  @Override
  @Nullable
  public PsiPerlAnnotationOverride getAnnotationOverride() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlAnnotationOverride.class);
  }

  @Override
  @Nullable
  public PsiPerlAnnotationReturns getAnnotationReturns() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlAnnotationReturns.class);
  }

  @Override
  @Nullable
  public PsiPerlAnnotationType getAnnotationType() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlAnnotationType.class);
  }

}
