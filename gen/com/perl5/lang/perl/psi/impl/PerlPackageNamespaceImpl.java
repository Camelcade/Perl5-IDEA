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

public class PerlPackageNamespaceImpl extends ASTWrapperPsiElement implements PerlPackageNamespace {

  public PerlPackageNamespaceImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitPackageNamespace(this);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PerlCodeLine> getCodeLineList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlCodeLine.class);
  }

  @Override
  @NotNull
  public List<PerlFunctionDefinitionAnon> getFunctionDefinitionAnonList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlFunctionDefinitionAnon.class);
  }

  @Override
  @NotNull
  public List<PerlFunctionDefinitionNamed> getFunctionDefinitionNamedList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlFunctionDefinitionNamed.class);
  }

  @Override
  @NotNull
  public List<PerlIfBlock> getIfBlockList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlIfBlock.class);
  }

  @Override
  @NotNull
  public List<PerlPackageNo> getPackageNoList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlPackageNo.class);
  }

  @Override
  @NotNull
  public List<PerlPackageNoInvalid> getPackageNoInvalidList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlPackageNoInvalid.class);
  }

  @Override
  @NotNull
  public List<PerlPackageRequire> getPackageRequireList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlPackageRequire.class);
  }

  @Override
  @NotNull
  public List<PerlPackageRequireInvalid> getPackageRequireInvalidList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlPackageRequireInvalid.class);
  }

  @Override
  @NotNull
  public List<PerlPackageUse> getPackageUseList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlPackageUse.class);
  }

  @Override
  @NotNull
  public List<PerlPackageUseInvalid> getPackageUseInvalidList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlPackageUseInvalid.class);
  }

  @Override
  @NotNull
  public List<PerlPerlBlock> getPerlBlockList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlPerlBlock.class);
  }

  @Override
  @NotNull
  public List<PerlPerlEval> getPerlEvalList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlPerlEval.class);
  }

  @Override
  @NotNull
  public List<PerlPerlEvalInvalid> getPerlEvalInvalidList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlPerlEvalInvalid.class);
  }

  @Override
  @Nullable
  public PsiElement getPerlPackageBuiltIn() {
    return findChildByType(PERL_PACKAGE_BUILT_IN);
  }

  @Override
  @Nullable
  public PsiElement getPerlPackageBuiltInDeprecated() {
    return findChildByType(PERL_PACKAGE_BUILT_IN_DEPRECATED);
  }

  @Override
  @Nullable
  public PsiElement getPerlPackageBuiltInPragma() {
    return findChildByType(PERL_PACKAGE_BUILT_IN_PRAGMA);
  }

  @Override
  @Nullable
  public PsiElement getPerlPackageUser() {
    return findChildByType(PERL_PACKAGE_USER);
  }

  @Override
  @Nullable
  public PsiElement getPerlVersion() {
    return findChildByType(PERL_VERSION);
  }

}
