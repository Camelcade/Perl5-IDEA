package intellilang;

import base.PerlLightCodeInsightFixtureTestCase;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import org.jetbrains.annotations.NotNull;

public abstract class PerlHeredocInjectionTestCase extends PerlLightCodeInsightFixtureTestCase {
  @NotNull
  protected PerlHeredocElementImpl getHeredocUnderCursor() {
    PsiElement leafElement = getFile().getViewProvider().findElementAt(getEditor().getCaretModel().getOffset());
    assert leafElement != null;
    PerlHeredocElementImpl heredocElement = PsiTreeUtil.getParentOfType(leafElement, PerlHeredocElementImpl.class);
    assert heredocElement != null;
    return heredocElement;
  }
}
