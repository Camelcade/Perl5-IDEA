// This is a generated file. Not intended for manual editing.
package com.perl5.lang.pod.psi.impl;

import java.util.List;
import com.intellij.psi.tree.IElementType;import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.perl5.lang.pod.parser.PodElementTypesGenerated.*;
import com.perl5.lang.pod.parser.psi.mixin.PodSectionParagraph;
import com.perl5.lang.pod.psi.*;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.pod.parser.psi.stubs.PodSectionStub;

public class PsiPodParagraphImpl extends PodSectionParagraph implements PsiPodParagraph {

  public PsiPodParagraphImpl(ASTNode node) {
    super(node);
  }

  public PsiPodParagraphImpl(PodSectionStub stub, IElementType stubType) {
    super(stub, stubType);
  }

  public void accept(@NotNull PsiPodVisitorGenerated visitor) {
    visitor.visitPodParagraph(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiPodVisitorGenerated) accept((PsiPodVisitorGenerated)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PsiPodFormatBold> getPodFormatBoldList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPodFormatBold.class);
  }

  @Override
  @NotNull
  public List<PsiPodFormatCode> getPodFormatCodeList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPodFormatCode.class);
  }

  @Override
  @NotNull
  public List<PsiPodFormatEscape> getPodFormatEscapeList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPodFormatEscape.class);
  }

  @Override
  @NotNull
  public List<PsiPodFormatFile> getPodFormatFileList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPodFormatFile.class);
  }

  @Override
  @NotNull
  public List<PsiPodFormatIndex> getPodFormatIndexList() {
    return PsiTreeUtil.getStubChildrenOfTypeAsList(this, PsiPodFormatIndex.class);
  }

  @Override
  @NotNull
  public List<PsiPodFormatItalic> getPodFormatItalicList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPodFormatItalic.class);
  }

  @Override
  @NotNull
  public List<PsiPodFormatLink> getPodFormatLinkList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPodFormatLink.class);
  }

  @Override
  @NotNull
  public List<PsiPodFormatNbsp> getPodFormatNbspList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPodFormatNbsp.class);
  }

  @Override
  @NotNull
  public List<PsiPodFormatNull> getPodFormatNullList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPodFormatNull.class);
  }

}
