// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.perl5.lang.perl.psi.stubs.calls.PerlSubCallElementStub;

public interface PsiPerlSubCall extends PsiPerlExpr, PerlMethodContainer, StubBasedPsiElement<PerlSubCallElementStub> {

  @Nullable
  PsiPerlCallArguments getCallArguments();

  @Nullable
  PsiPerlExpr getExpr();

  @Nullable
  PsiPerlMethod getMethod();

  @Nullable
  PsiPerlParenthesisedCallArguments getParenthesisedCallArguments();

}
