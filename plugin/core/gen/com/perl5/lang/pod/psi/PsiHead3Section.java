// This is a generated file. Not intended for manual editing.
package com.perl5.lang.pod.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.perl5.lang.pod.parser.psi.stubs.PodSectionStub;

public interface PsiHead3Section extends PsiElement, StubBasedPsiElement<PodSectionStub> {

  @Nullable
  PsiHead3SectionContent getHead3SectionContent();

  @Nullable
  PsiSectionTitle getSectionTitle();

}
