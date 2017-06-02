package intellilang;

import base.PerlLightCodeInsightFixtureTestCase;
import com.intellij.lang.Language;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.LiteralTextEscaper;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.idea.intellilang.PerlHeredocLanguageInjector;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

  protected String getHeredocDecodedText(@Nullable PerlHeredocElementImpl heredocElement) {
    assertNotNull(heredocElement);
    StringBuilder sb = new StringBuilder();
    LiteralTextEscaper<PerlHeredocElementImpl> escaper = heredocElement.createLiteralTextEscaper();
    // host MUST be auto-injected with our own injector
    new PerlHeredocLanguageInjector().getLanguagesToInject(new MultiHostRegistrar() {
      @NotNull
      @Override
      public MultiHostRegistrar startInjecting(@NotNull Language language) {
        return this;
      }

      @NotNull
      @Override
      public MultiHostRegistrar addPlace(@Nullable String prefix,
                                         @Nullable String suffix,
                                         @NotNull PsiLanguageInjectionHost host,
                                         @NotNull TextRange rangeInsideHost) {
        escaper.decode(rangeInsideHost, sb);
        return this;
      }

      @Override
      public void doneInjecting() {

      }
    }, heredocElement);
    return sb.toString();
  }
}
