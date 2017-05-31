package intellilang;

import base.PerlLightCodeInsightFixtureTestCase;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import org.jetbrains.annotations.NotNull;

public abstract class PerlHeredocInjectionTestCase extends PerlLightCodeInsightFixtureTestCase {
  @NotNull
  protected PerlHeredocElementImpl getHeredocUnderCursor() {
    PsiElement sourceElement = getFile().getViewProvider().findElementAt(getEditor().getCaretModel().getOffset());
    assertNotNull(sourceElement);
    PsiFile leafFile = sourceElement.getContainingFile();
    if (InjectedLanguageManager.getInstance(getProject()).isInjectedFragment(leafFile)) {
      sourceElement = leafFile.getContext();
      assertNotNull(sourceElement);
    }

    if (sourceElement instanceof PerlHeredocElementImpl) {
      return (PerlHeredocElementImpl)sourceElement;
    }
    PerlHeredocElementImpl heredocElement = PsiTreeUtil.getParentOfType(sourceElement, PerlHeredocElementImpl.class);
    assertNotNull(heredocElement);
    return heredocElement;
  }
}
