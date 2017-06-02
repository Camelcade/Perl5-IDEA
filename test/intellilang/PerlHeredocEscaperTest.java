package intellilang;

import com.intellij.lang.Language;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.LiteralTextEscaper;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.testFramework.UsefulTestCase;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.idea.intellilang.PerlHeredocLanguageInjector;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerlHeredocEscaperTest extends PerlHeredocInjectionTestCase {
  private boolean myInjectWithInterpolation;

  @Override
  protected String getTestDataPath() {
    return "testData/intellilang/perl/escaper";
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    myInjectWithInterpolation = PerlSharedSettings.getInstance(getProject()).ALLOW_INJECTIONS_WITH_INTERPOLATION;
    PerlSharedSettings.getInstance(getProject()).ALLOW_INJECTIONS_WITH_INTERPOLATION = true;
  }

  @Override
  protected void tearDown() throws Exception {
    PerlSharedSettings.getInstance(getProject()).ALLOW_INJECTIONS_WITH_INTERPOLATION = myInjectWithInterpolation;
    super.tearDown();
  }

  public void testUnindentableEmpty() { doTest();}

  public void testUnindentableOneliner() { doTest();}

  public void testUnindentableMultiliner() { doTest();}

  public void testUnindentableWithInterpolation() { doTest();}

  public void testUnindentedEmpty() { doTest();}

  public void testUnindentedOneliner() { doTest();}

  public void testUnindentedMultiliner() { doTest();}

  public void testUnindentedWithInterpolation() { doTest();}

  public void testProperlyIndentedEmpty() { doTest();}

  public void testProperlyIndentedOneliner() { doTest();}

  public void testProperlyIndentedMultiliner() { doTest();}

  public void testProperlyIndentedMultilinerWithNewLines() { doTest();}

  public void testProperlyIndentedWithInterpolation() { doTest();}

  public void testImproperlyIndentedOneliner() { doTest();}

  public void testImproperlyIndentedMultiliner() { doTest();}

  public void testImproperlyIndentedMultilinerWithNewLines() { doTest();}

  public void testImproperlyIndentedWithInterpolation() { doTest();}

  private void doTest() {
    initWithFileSmartWithoutErrors();
    assertInjected();
    PerlHeredocElementImpl heredocUnderCursor = getHeredocUnderCursor();
    assertNotNull(heredocUnderCursor);
    StringBuilder sb = new StringBuilder();
    LiteralTextEscaper<PerlHeredocElementImpl> escaper = heredocUnderCursor.createLiteralTextEscaper();
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
    }, heredocUnderCursor);

    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), sb.toString());
  }
}
