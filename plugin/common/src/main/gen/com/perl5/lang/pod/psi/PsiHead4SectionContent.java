// This is a generated file. Not intended for manual editing.
package com.perl5.lang.pod.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.perl5.lang.pod.parser.psi.PodSectionContent;

public interface PsiHead4SectionContent extends PodSectionContent {

  @NotNull
  List<PsiCutSection> getCutSectionList();

  @NotNull
  List<PsiEncodingSection> getEncodingSectionList();

  @NotNull
  List<PsiForSection> getForSectionList();

  @NotNull
  List<PsiOverSection> getOverSectionList();

  @NotNull
  List<PsiPodFormatIndex> getPodFormatIndexList();

  @NotNull
  List<PsiPodParagraph> getPodParagraphList();

  @NotNull
  List<PsiPodSection> getPodSectionList();

  @NotNull
  List<PsiPodVerbatimParagraph> getPodVerbatimParagraphList();

  @NotNull
  List<PsiUnknownSection> getUnknownSectionList();

}
