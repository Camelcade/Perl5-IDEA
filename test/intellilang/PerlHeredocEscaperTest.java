package intellilang;

import com.intellij.testFramework.UsefulTestCase;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;

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
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), getHeredocDecodedText(getHeredocUnderCursor()));
  }
}
